package com.xb.sample;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xb.sample.bean.MyFactory;
import com.xb.sample.bean.Parent;
import com.xb.sample.conf.Conf;

@RestController
@EnableAutoConfiguration
@Import(value = {Conf.class})
@ComponentScan(basePackages={"com.xb.sample.bean"})
public class SimpleApp {

	@Autowired
	MyFactory factory;
	
	@RequestMapping("/factory")
	public Object index(){
		return "pls try /factory/1 or 2";
	}
	
	@RequestMapping("/factory/{index}")
	public Object test(@PathVariable Integer index){
		if(index==null){
			index = 1;
		}
		Parent obj = null; 
		if(index==1){
			obj = factory.getBean("childOne");
		}else{
			obj = factory.getBean("childTwo");
		}
		return obj+":"+obj.sayHello();
	}
	
	public static void main(String[] args) throws Exception {
		SpringApplication.run(SimpleApp.class, args);
	}
}
