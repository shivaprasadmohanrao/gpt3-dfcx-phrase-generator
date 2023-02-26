package com.technocouple.promptGenerator.api.dao;

public class Parts {
	
	private String parameterId;
	private String text;
	
	public Parts() {}
	public Parts(String parameterId, String text) {
		super();
		this.parameterId = parameterId;
		this.text = text;
	}
	public String getParameterId() {
		return parameterId;
	}
	public void setParameterId(String parameterId) {
		this.parameterId = parameterId;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	@Override
	public String toString() {
		return "Parts [parameterId=" + parameterId + ", text=" + text + "]";
	}
	
	

}
