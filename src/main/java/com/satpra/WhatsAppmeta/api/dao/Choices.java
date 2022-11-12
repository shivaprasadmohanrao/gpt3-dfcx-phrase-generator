package com.satpra.WhatsAppmeta.api.dao;

public class Choices {
	
	private String text;
	private int index;
	private String finish_reason;
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public String getFinish_reason() {
		return finish_reason;
	}
	public void setFinish_reason(String finish_reason) {
		this.finish_reason = finish_reason;
	}
	
	

}
