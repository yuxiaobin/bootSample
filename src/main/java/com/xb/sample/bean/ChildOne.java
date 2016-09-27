package com.xb.sample.bean;

import org.springframework.stereotype.Component;

@Component(value="com.xb.sample.bean.ChildOne")
public class ChildOne extends Parent {

	@Override
	public String sayHello() {
		String str = "ChildOne say hello";
		System.out.println(str);
		return str;
	}

}
