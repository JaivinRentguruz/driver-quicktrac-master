package com.evolution.quicktrack.response.chatlist;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class UserChatData {

	@SerializedName("name")
	private String name;

	@SerializedName("messages")
	private List<MessagesItem> messages;

	@SerializedName("id")
	private String id;

	@SerializedName("lastupdate")
	private String lastupdate;

	@SerializedName("picture")
	private String picture;

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setMessages(List<MessagesItem> messages){
		this.messages = messages;
	}

	public List<MessagesItem> getMessages(){
		return messages;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setLastupdate(String lastupdate){
		this.lastupdate = lastupdate;
	}

	public String getLastupdate(){
		return lastupdate;
	}

	public void setPicture(String picture){
		this.picture = picture;
	}

	public String getPicture(){
		return picture;
	}

	@Override
 	public String toString(){
		return 
			"UserChatData{" +
			"name = '" + name + '\'' + 
			",messages = '" + messages + '\'' + 
			",id = '" + id + '\'' + 
			",lastupdate = '" + lastupdate + '\'' + 
			",picture = '" + picture + '\'' + 
			"}";
		}
}