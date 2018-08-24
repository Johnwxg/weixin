package com.weixin.bean;

public class MenuButton {
	private String type;
	private String name;
	private MenuButton[] sub_button;
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public MenuButton[] getSub_button() {
		return sub_button;
	}
	public void setSub_button(MenuButton[] sub_button) {
		this.sub_button = sub_button;
	}
	
}
