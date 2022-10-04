package com.evolution.quicktrack.response.chatlist;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MessagesItem implements Serializable {

	@SerializedName("to_user_role")
	private String toUserRole;

	@SerializedName("read")
	private String read;

	@SerializedName("to_user_id")
	private String toUserId;

	@SerializedName("read_timestamp")
	private String readTimestamp;

	@SerializedName("id")
	private String id;

	@SerializedName("message")
	private String message;

	@SerializedName("date_timestamp")
	private String dateTimestamp;

	@SerializedName("from_user_id")
	private String fromUserId;

	@SerializedName("from_user_role")
	private String fromUserRole;

	@SerializedName("image")
	private String image;

	@SerializedName("from_user_naame")
	private String name;

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public void setToUserRole(String toUserRole){
		this.toUserRole = toUserRole;
	}

	public String getToUserRole(){
		return toUserRole;
	}

	public void setRead(String read){
		this.read = read;
	}

	public String getRead(){
		return read;
	}

	public void setToUserId(String toUserId){
		this.toUserId = toUserId;
	}

	public String getToUserId(){
		return toUserId;
	}

	public void setReadTimestamp(String readTimestamp){
		this.readTimestamp = readTimestamp;
	}

	public String getReadTimestamp(){
		return readTimestamp;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}

	public void setDateTimestamp(String dateTimestamp){
		this.dateTimestamp = dateTimestamp;
	}

	public String getDateTimestamp(){
		return dateTimestamp;
	}

	public void setFromUserId(String fromUserId){
		this.fromUserId = fromUserId;
	}

	public String getFromUserId(){
		return fromUserId;
	}

	public void setFromUserRole(String fromUserRole){
		this.fromUserRole = fromUserRole;
	}

	public String getFromUserRole(){
		return fromUserRole;
	}

	@Override
 	public String toString(){
		return 
			"MessagesItem{" + 
			"to_user_role = '" + toUserRole + '\'' + 
			",read = '" + read + '\'' + 
			",to_user_id = '" + toUserId + '\'' + 
			",read_timestamp = '" + readTimestamp + '\'' + 
			",id = '" + id + '\'' +
			",image = '" + image + '\'' +
			",message = '" + message + '\'' +
			",date_timestamp = '" + dateTimestamp + '\'' + 
			",from_user_id = '" + fromUserId + '\'' + 
			",from_user_role = '" + fromUserRole + '\'' + 
			"}";
		}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}