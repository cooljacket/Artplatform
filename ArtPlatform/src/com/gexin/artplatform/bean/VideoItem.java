package com.gexin.artplatform.bean;

public class VideoItem {

	private String _id;
	private String title;
	private String description;
	private String imageUrl;
	private long createTime;
	private long updateTime;
	private String userId;
	private String studioId;
	private String videoUrl;
	private String verified;
	private String name;

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
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

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getStudioId() {
		return studioId;
	}

	public void setStudioId(String studioId) {
		this.studioId = studioId;
	}

	public String getVideoUrl() {
		return videoUrl;
	}

	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
	}

	public String getVerified() {
		return verified;
	}

	public void setVerified(String verified) {
		this.verified = verified;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "VideoItem [_id=" + _id + ", title=" + title + ", description="
				+ description + ", imageUrl=" + imageUrl + ", createTime="
				+ createTime + ", updateTime=" + updateTime + ", userId="
				+ userId + ", studioId=" + studioId + ", videoUrl=" + videoUrl
				+ ", verified=" + verified + ", name=" + name + "]";
	}

}
