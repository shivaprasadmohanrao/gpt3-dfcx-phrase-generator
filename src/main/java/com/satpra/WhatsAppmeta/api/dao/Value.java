package com.satpra.WhatsAppmeta.api.dao;

import java.util.Arrays;

public class Value {
	
	String messaging_product;
	Metadata metadata;
	Contacts[] contacts;
	Messages[] messages;
	
	public String getMessaging_product() {
		return messaging_product;
	}
	public void setMessaging_product(String messaging_product) {
		this.messaging_product = messaging_product;
	}
	public Metadata getMetadata() {
		return metadata;
	}
	public void setMetadata(Metadata metadata) {
		this.metadata = metadata;
	}
	public Contacts[] getContacts() {
		return contacts;
	}
	public void setContacts(Contacts[] contacts) {
		this.contacts = contacts;
	}
	public Messages[] getMessages() {
		return messages;
	}
	public void setMessages(Messages[] messages) {
		this.messages = messages;
	}
	@Override
	public String toString() {
		return "Value [messaging_product=" + messaging_product + ", metadata=" + metadata + ", contacts="
				+ Arrays.toString(contacts) + ", messages=" + Arrays.toString(messages) + ", getMessaging_product()="
				+ getMessaging_product() + ", getMetadata()=" + getMetadata() + ", getContacts()="
				+ Arrays.toString(getContacts()) + ", getMessages()=" + Arrays.toString(getMessages()) + ", getClass()="
				+ getClass() + ", hashCode()=" + hashCode() + ", toString()=" + super.toString() + "]";
	}
	

}
