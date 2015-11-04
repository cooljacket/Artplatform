package com.gexin.artplatform.bean;

import java.util.List;

public class Answer {

	private String _id;
	private String userId;
	private String problemId;
	private String avatarUrl;
	private String userName;
	private List<AnswerContent> content;
	private long createTime;
	private long updateTime;
	private int uType;

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getProblemId() {
		return problemId;
	}

	public void setProblemId(String problemId) {
		this.problemId = problemId;
	}

	public String getAvatarUrl() {
		return avatarUrl;
	}

	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}

	public List<AnswerContent> getContent() {
		return content;
	}

	public void setContent(List<AnswerContent> content) {
		this.content = content;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}

	public int getuType() {
		return uType;
	}

	public void setuType(int uType) {
		this.uType = uType;
	}

	@Override
	public String toString() {
		return "Answer [_id=" + _id + ", userId=" + userId + ", problemId="
				+ problemId + ", avatarUrl=" + avatarUrl + ", userName="
				+ userName + ", content=" + content + ", createTime="
				+ createTime + ", updateTime=" + updateTime + ", uType="
				+ uType + "]";
	}

}
