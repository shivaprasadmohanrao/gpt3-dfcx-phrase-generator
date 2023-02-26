package com.technocouple.promptGenerator.api.dao;

public class Messages {
	
	
	String from;
	String id;
	Text text;
	String timestamp;
	String type;
	
	public Messages() {}
	
	public Messages(String from, String id, Text text, String timestamp, String type) {
		super();
		this.from = from;
		this.id = id;
		this.text = text;
		this.timestamp = timestamp;
		this.type = type;
	}
	
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Text getText() {
		return text;
	}
	public void setText(Text text) {
		this.text = text;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	@Override
	public String toString() {
		return "Messages [from=" + from + ", id=" + id + ", text=" + text + ", timestamp=" + timestamp + ", type="
				+ type + "]";
	}
	
	

}
