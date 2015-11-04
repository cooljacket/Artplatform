package com.gexin.artplatform.bean;

public class Studio {

	private String studioId;
	private String avatarUrl;
	private String description;
	private String name;
	private String phone;
	private long createTime;
	private int register;
	private int answerNum;
	private int articleNum;
	private int fanNum;
	private int followNum;
	private int teacherNum;
	private int videoNum;
	private int workNum;
	private int isWatched;

	public String getStudioId() {
		return studioId;
	}

	public void setStudioId(String studioId) {
		this.studioId = studioId;
	}

	public String getAvatarUrl() {
		return avatarUrl;
	}

	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public int getRegister() {
		return register;
	}

	public void setRegister(int register) {
		this.register = register;
	}

	public int getAnswerNum() {
		return answerNum;
	}

	public void setAnswerNum(int answerNum) {
		this.answerNum = answerNum;
	}

	public int getArticleNum() {
		return articleNum;
	}

	public void setArticleNum(int articleNum) {
		this.articleNum = articleNum;
	}

	public int getFanNum() {
		return fanNum;
	}

	public void setFanNum(int fanNum) {
		this.fanNum = fanNum;
	}

	public int getFollowNum() {
		return followNum;
	}

	public void setFollowNum(int followNum) {
		this.followNum = followNum;
	}

	public int getTeacherNum() {
		return teacherNum;
	}

	public void setTeacherNum(int teacherNum) {
		this.teacherNum = teacherNum;
	}

	public int getVideoNum() {
		return videoNum;
	}

	public void setVideoNum(int videoNum) {
		this.videoNum = videoNum;
	}

	public int getWorkNum() {
		return workNum;
	}

	public void setWorkNum(int workNum) {
		this.workNum = workNum;
	}

	public int getIsWatched() {
		return isWatched;
	}

	public void setIsWatched(int isWatched) {
		this.isWatched = isWatched;
	}

	@Override
	public String toString() {
		return "Studio [studioId=" + studioId + ", avatarUrl=" + avatarUrl
				+ ", description=" + description + ", name=" + name
				+ ", phone=" + phone + ", createTime=" + createTime
				+ ", register=" + register + ", answerNum=" + answerNum
				+ ", articleNum=" + articleNum + ", fanNum=" + fanNum
				+ ", followNum=" + followNum + ", teacherNum=" + teacherNum
				+ ", videoNum=" + videoNum + ", workNum=" + workNum
				+ ", isWatched=" + isWatched + "]";
	}

}
