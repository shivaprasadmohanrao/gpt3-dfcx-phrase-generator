package com.technocouple.promptGenerator.api.dao;

import java.util.List;

public class TrainingPhrases {
	
	private List<Parts> parts;
	private String repeatCount;
	public TrainingPhrases() {}
	public TrainingPhrases(List<Parts>  parts, String repeatCount) {
		super();
		this.parts = parts;
		this.repeatCount = repeatCount;
	}
	public List<Parts>  getParts() {
		return parts;
	}
	public void setParts(List<Parts>  parts) {
		this.parts = parts;
	}
	public String getRepeatCount() {
		return repeatCount;
	}
	public void setRepeatCount(String repeatCount) {
		this.repeatCount = repeatCount;
	}
	@Override
	public String toString() {
		return "TrainingPhrases [parts=" + parts + ", repeatCount=" + repeatCount + "]";
	}
	
	

}
