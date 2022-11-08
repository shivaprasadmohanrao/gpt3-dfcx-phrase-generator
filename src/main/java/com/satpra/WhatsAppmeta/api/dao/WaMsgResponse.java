package com.satpra.WhatsAppmeta.api.dao;

public class WaMsgResponse {
	
	String messaging_product;
	ContactRes[] contacts; 
	Messages[] messages;
	public String getMessaging_product() {
		return messaging_product;
	}
	public void setMessaging_product(String messaging_product) {
		this.messaging_product = messaging_product;
	}
	public ContactRes[] getContacts() {
		return contacts;
	}
	public void setContacts(ContactRes[] contacts) {
		this.contacts = contacts;
	}
	public Messages[] getMessages() {
		return messages;
	}
	public void setMessages(Messages[] messages) {
		this.messages = messages;
	}
	
	

}
