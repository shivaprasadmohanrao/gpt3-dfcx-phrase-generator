package com.technocouple.promptGenerator.api.dao;

public class UserInput {
	
	private String input;
	
	public UserInput() {}

	public UserInput(String userinput) {
		super();
		this.input = userinput;
	}

	
	public String getInput() {
		return input;
	}

	public void setInput(String input) {
		this.input = input;
	}

	@Override
	public String toString() {
		return "UserInput [userinput=" + input + "]";
	}
	
	

}
