package com.satpra.WhatsAppmeta.api.dao;

import java.util.Arrays;

public class Entry {
	
	String id;
	Change[] changes;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Change[] getChanges() {
		return changes;
	}
	public void setChanges(Change[] changes) {
		this.changes = changes;
	}
	@Override
	public String toString() {
		return "Entry [id=" + id + ", changes=" + Arrays.toString(changes) + ", getId()=" + getId() + ", getChanges()="
				+ Arrays.toString(getChanges()) + ", getClass()=" + getClass() + ", hashCode()=" + hashCode()
				+ ", toString()=" + super.toString() + "]";
	}
	

}
