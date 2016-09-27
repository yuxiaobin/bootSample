package com.xb.sample.bean;

public abstract class Parent {

	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public abstract String sayHello();
}
