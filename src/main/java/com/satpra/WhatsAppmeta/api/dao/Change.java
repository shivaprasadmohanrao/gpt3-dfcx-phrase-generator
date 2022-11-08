package com.satpra.WhatsAppmeta.api.dao;

public class Change {
	Value value;
	String field;
	public Value getValue() {
		return value;
	}
	public void setValue(Value value) {
		this.value = value;
	}
	public String getField() {
		return field;
	}
	public void setField(String field) {
		this.field = field;
	}
	@Override
	public String toString() {
		return "Change [value=" + value + ", field=" + field + ", getValue()=" + getValue() + ", getField()="
				+ getField() + ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()="
				+ super.toString() + "]";
	}
	
	

}
