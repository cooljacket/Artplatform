package com.gexin.artplatform.bean;

import java.util.List;

public class Problem {

	private String _id;
	private int answerNum;
	private int commentNum;
	private String avatarUrl;
	private String content;
	private String image;
	private String name;
	private long timestamp;
	private int viewNum;
	private int zan;
	private int isZan;
	private String userId;
	private List<Comment> commentList;
	private List<Answer> answerList;
	private String askTo;
	private List<String> tag;
	private int askToType;
	private String askToAvatarUrl;
	private String askToName;

	public int getAnswerNum() {
		return answerNum;
	}

	public void setAnswerNum(int answerNum) {
		this.answerNum = answerNum;
	}

	public String getAvatarUrl() {
		return avatarUrl;
	}

	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public int getViewNum() {
		return viewNum;
	}

	public void setViewNum(int viewNum) {
		this.viewNum = viewNum;
	}

	public int getZan() {
		return zan;
	}

	public void setZan(int zan) {
		this.zan = zan;
	}

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

	public List<Comment> getCommentList() {
		return commentList;
	}

	public void setCommentList(List<Comment> commentList) {
		this.commentList = commentList;
	}

	public String getAskTo() {
		return askTo;
	}

	public void setAskTo(String askTo) {
		this.askTo = askTo;
	}

	public List<String> getTag() {
		return tag;
	}

	public void setTag(List<String> tag) {
		this.tag = tag;
	}

	public int getIsZan() {
		return isZan;
	}

	public void setIsZan(int isZan) {
		this.isZan = isZan;
	}

	public List<Answer> getAnswerList() {
		return answerList;
	}

	public void setAnswerList(List<Answer> answerList) {
		this.answerList = answerList;
	}

	public int getAskToType() {
		return askToType;
	}

	public void setAskToType(int askToType) {
		this.askToType = askToType;
	}

	public String getAskToAvatarUrl() {
		return askToAvatarUrl;
	}

	public void setAskToAvatarUrl(String askToAvatarUrl) {
		this.askToAvatarUrl = askToAvatarUrl;
	}

	public String getAskToName() {
		return askToName;
	}

	public void setAskToName(String askToName) {
		this.askToName = askToName;
	}

	public int getCommentNum() {
		return commentNum;
	}

	public void setCommentNum(int commentNum) {
		this.commentNum = commentNum;
	}

	@Override
	public String toString() {
		return "Problem [_id=" + _id + ", answerNum=" + answerNum
				+ ", commentNum=" + commentNum + ", avatarUrl=" + avatarUrl
				+ ", content=" + content + ", image=" + image + ", name="
				+ name + ", timestamp=" + timestamp + ", viewNum=" + viewNum
				+ ", zan=" + zan + ", isZan=" + isZan + ", userId=" + userId
				+ ", commentList=" + commentList + ", answerList=" + answerList
				+ ", askTo=" + askTo + ", tag=" + tag + ", askToType="
				+ askToType + ", askToAvatarUrl=" + askToAvatarUrl
				+ ", askToName=" + askToName + "]";
	}

}
