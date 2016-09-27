package com.xb.sample.bean;

import org.springframework.stereotype.Component;

@Component(value="com.xb.sample.bean.ChildTwo")
public class ChildTwo extends Parent {

	@Override
	public String sayHello() {
		String str = "ChildTwo say hello";
		System.out.println(str);
		return str;
	}

}
