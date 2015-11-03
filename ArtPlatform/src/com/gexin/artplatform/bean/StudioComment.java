package com.gexin.artplatform.bean;

public class StudioComment {

	private String _id;
	private String fromUserId;
	private String fromUserName;
	private String fromUserAvatarUrl;
	private String content;
	private String reply;
	private String studioId;
	private long timestamp;

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public String getFromUserId() {
		return fromUserId;
	}

	public void setFromUserId(String fromUserId) {
		this.fromUserId = fromUserId;
	}

	public String getFromUserName() {
		return fromUserName;
	}

	public void setFromUserName(String fromUserName) {
		this.fromUserName = fromUserName;
	}

	public String getFromUserAvatarUrl() {
		return fromUserAvatarUrl;
	}

	public void setFromUserAvatarUrl(String fromUserAvatarUrl) {
		this.fromUserAvatarUrl = fromUserAvatarUrl;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getReply() {
		return reply;
	}

	public void setReply(String reply) {
		this.reply = reply;
	}

	public String getStudioId() {
		return studioId;
	}

	public void setStudioId(String studioId) {
		this.studioId = studioId;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	@Override
	public String toString() {
		return "StudioComment [_id=" + _id + ", fromUserId=" + fromUserId
				+ ", fromUserName=" + fromUserName + ", fromUserAvatarUrl="
				+ fromUserAvatarUrl + ", content=" + content + ", reply="
				+ reply + ", studioId=" + studioId + ", timestamp=" + timestamp
				+ "]";
	}

}
