package com.evolution.quicktrack.response.userlist;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class Data{

	@SerializedName("recent")
	private List<UserDataItem> recent;

	@SerializedName("friends")
	private List<UserDataItem> friends;

	public void setRecent(List<UserDataItem> recent){
		this.recent = recent;
	}

	public List<UserDataItem> getRecent(){
		return recent;
	}

	public void setFriends(List<UserDataItem> friends){
		this.friends = friends;
	}

	public List<UserDataItem> getFriends(){
		return friends;
	}

	@Override
 	public String toString(){
		return 
			"UserChatData{" +
			"recent = '" + recent + '\'' + 
			",friends = '" + friends + '\'' + 
			"}";
		}
}