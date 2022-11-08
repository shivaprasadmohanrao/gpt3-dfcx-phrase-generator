package com.satpra.WhatsAppmeta.api.dao;

public class Metadata {
	
	String display_phone_number;
	String phone_number_id;
	public String getDisplay_phone_number() {
		return display_phone_number;
	}
	public void setDisplay_phone_number(String display_phone_number) {
		this.display_phone_number = display_phone_number;
	}
	public String getPhone_number_id() {
		return phone_number_id;
	}
	public void setPhone_number_id(String phone_number_id) {
		this.phone_number_id = phone_number_id;
	}
	@Override
	public String toString() {
		return "Metadata [display_phone_number=" + display_phone_number + ", phone_number_id=" + phone_number_id
				+ ", getDisplay_phone_number()=" + getDisplay_phone_number() + ", getPhone_number_id()="
				+ getPhone_number_id() + ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()="
				+ super.toString() + "]";
	}
	
	

}
