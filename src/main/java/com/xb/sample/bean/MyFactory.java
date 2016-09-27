package com.xb.sample.bean;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import javax.annotation.PostConstruct;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Component
public class MyFactory implements BeanFactoryAware{
	
	private BeanFactory beanFactory;
	private static final Map<String,Parent> map = new HashMap<String,Parent>();

	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
		
	}
	
	@PostConstruct
	public void init() {
		Properties prop = new Properties();
		map.clear();
		try (InputStream is = new ClassPathResource("bean-def.properties").getInputStream();) {
			prop.load(is);
			Iterator<String> it = prop.stringPropertyNames().iterator();
			while (it.hasNext()) {
				String entry = it.next();
				map.put(entry, (Parent) beanFactory.getBean(prop.getProperty(entry)));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Parent getBean(String beanName){
		return map.get(beanName);
	}
	
}
