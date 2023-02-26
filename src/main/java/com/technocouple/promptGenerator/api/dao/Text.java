package com.technocouple.promptGenerator.api.dao;

public class Text {
	
	String body;
	Boolean preview_url;

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public Boolean getPreview_url() {
		return preview_url;
	}

	public void setPreview_url(Boolean preview_url) {
		this.preview_url = preview_url;
	}

	@Override
	public String toString() {
		return "Text [body=" + body + ", getBody()=" + getBody() + ", getClass()=" + getClass() + ", hashCode()="
				+ hashCode() + ", toString()=" + super.toString() + "]";
	}
	

}
