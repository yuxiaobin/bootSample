package com.xb.sample;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.concurrent.FailureCallback;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.SuccessCallback;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONObject;
import com.xb.sample.conf.Conf;


@RestController
@EnableAutoConfiguration
@Import(value = {Conf.class})
public class SpringRestTemplateApp {
	
	@Autowired
	RestTemplate restTemplate;
	@Autowired
	AsyncRestTemplate asyncRestTemplate;

	@RequestMapping("/")
	public String hello(){
		return hello2();
	}
	@RequestMapping("")
	public String hello2(){
		String url = "http://localhost:8080/json";
		JSONObject json = restTemplate.getForEntity(url, JSONObject.class).getBody();
		return json.toJSONString();
	}
	@RequestMapping("/async")
	public String asyncReq(){
		String url = "http://localhost:8080/jsonAsync";
		ListenableFuture<ResponseEntity<JSONObject>> future = asyncRestTemplate.getForEntity(url, JSONObject.class);
		future.addCallback(new SuccessCallback<ResponseEntity<JSONObject>>() {
			public void onSuccess(ResponseEntity<JSONObject> result) {
				System.out.println(result.getBody().toJSONString());
			}
		}, new FailureCallback() {
			public void onFailure(Throwable ex) {
				System.out.println("onFailure:"+ex);
			}
		});
		return "this is async sample";
	}
	
	@RequestMapping("/json")
	public Object genJson(){
		JSONObject json = new JSONObject();
		json.put("descp", "this is spring rest template sample");
		return json;
	}
	@RequestMapping("/jsonAsync")
	public Object genJsonAsync(){
		JSONObject json = new JSONObject();
		json.put("descp", "this is spring rest template sample");
		try {
			//For testing only!!!!!!
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return json;
	}
	
	@RequestMapping("/postApi")
	public Object iAmPostApi(@RequestBody JSONObject parm){
		System.out.println(parm.toJSONString());
		parm.put("result", "hello post");
		return parm;
	}
	
	@RequestMapping("/post")
	public Object testPost(){
		String url = "http://localhost:8080/postApi";
		JSONObject postData = new JSONObject();
		postData.put("descp", "request for post");
		JSONObject json = restTemplate.postForEntity(url, postData, JSONObject.class).getBody();
		return json.toJSONString();
	}
	
	@RequestMapping("/headerApi")
	public JSONObject withHeader(@RequestBody JSONObject parm, HttpServletRequest req){
		System.out.println("headerApi====="+parm.toJSONString());
		Enumeration<String> headers = req.getHeaderNames();
		JSONObject result = new JSONObject();
		while(headers.hasMoreElements()){
			String name = headers.nextElement();
			System.out.println("["+name+"]="+req.getHeader(name));
			result.put(name, req.getHeader(name));
		}
		result.put("descp", "this is from header");
		return result;
	}
	@RequestMapping("/header")
	public Object postWithHeader(){
		HttpHeaders headers = new HttpHeaders();
		headers.set("auth_token", "asdfgh");
		headers.set("Other-Header", "othervalue");
		headers.setContentType(MediaType.APPLICATION_JSON);
		
		JSONObject parm = new JSONObject();
		parm.put("parm", "1234");
		HttpEntity<JSONObject> entity = new HttpEntity<JSONObject>(parm, headers);
		HttpEntity<String> response = restTemplate.exchange(
				"http://localhost:8080/headerApi", HttpMethod.POST, entity, String.class);//这里放JSONObject, String 都可以。因为JSONObject返回的时候其实也就是string
		return response.getBody();
	}
	
	
	public static void main(String[] args) throws Exception {
		SpringApplication.run(SpringRestTemplateApp.class, args);
	}
}
