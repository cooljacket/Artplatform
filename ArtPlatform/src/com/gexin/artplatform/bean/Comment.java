package com.gexin.artplatform.bean;

public class Comment {

	private String _id;
	private String problemId;
	private String content;
	private String toUserId;
	private String fromUser;
	private long timestamp;
	private String fromUserAvatarUrl;
	private String fromUserName;
	private String toUserName;
	private Problem problem;

	public Problem get_problem() {
		return problem;
	}

	public void set_problem(Problem problem) {
		this.problem = problem;
	}

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public String getProblemId() {
		return problemId;
	}

	public void setProblemId(String problemId) {
		this.problemId = problemId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getFromUser() {
		return fromUser;
	}

	public void setFromUser(String fromUser) {
		this.fromUser = fromUser;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public String getToUserId() {
		return toUserId;
	}

	public void setToUserId(String toUserId) {
		this.toUserId = toUserId;
	}

	public String getFromUserAvatarUrl() {
		return fromUserAvatarUrl;
	}

	public void setFromUserAvatarUrl(String fromUserAvatarUrl) {
		this.fromUserAvatarUrl = fromUserAvatarUrl;
	}

	public String getFromUserName() {
		return fromUserName;
	}

	public void setFromUserName(String fromUserName) {
		this.fromUserName = fromUserName;
	}

	public String getToUserName() {
		return toUserName;
	}

	public void setToUserName(String toUserName) {
		this.toUserName = toUserName;
	}

	@Override
	public String toString() {
		return "Comment [_id=" + _id + ", problemId=" + problemId
				+ ", content=" + content + ", toUserId=" + toUserId
				+ ", fromUser=" + fromUser + ", timestamp=" + timestamp
				+ ", fromUserAvatarUrl=" + fromUserAvatarUrl
				+ ", fromUserName=" + fromUserName + ", toUserName="
				+ toUserName + "]";
	}

}
