package com.gexin.artplatform.bean;

public class Fans {
	private String _id;
	private String userId;
	private String name;
	private String avatarUrl;
	private int relation;
	private int uType;

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAvatarUrl() {
		return avatarUrl;
	}

	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}

	public int getRelation() {
		return relation;
	}

	public void setRelation(int relation) {
		this.relation = relation;
	}

	public int getuType() {
		return uType;
	}

	public void setuType(int uType) {
		this.uType = uType;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Override
	public String toString() {
		return "Fans [_id=" + _id + ", userId=" + userId + ", name=" + name
				+ ", avatarUrl=" + avatarUrl + ", relation=" + relation
				+ ", uType=" + uType + "]";
	}

}
