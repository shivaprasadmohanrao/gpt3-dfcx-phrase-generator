package com.satpra.WhatsAppmeta.api.dao;

import java.util.Arrays;

import org.springframework.stereotype.Component;

@Component
public class MessagesRes {
	
	String object;
	Entry[] entry;
	
	public String getObject() {
		return object;
	}
	public void setObject(String object) {
		this.object = object;
	}
	
	public Entry[] getEntry() {
		return entry;
	}
	public void setEntry(Entry[] entry) {
		this.entry = entry;
	}
	@Override
	public String toString() {
		return "MessagesRes [object=" + object + ", entry=" + Arrays.toString(entry) + ", getObject()=" + getObject()
				+ ", getEntry()=" + Arrays.toString(getEntry()) + ", getClass()=" + getClass() + ", hashCode()="
				+ hashCode() + ", toString()=" + super.toString() + "]";
	}
	
	

}
