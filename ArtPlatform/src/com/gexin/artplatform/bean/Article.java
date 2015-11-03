package com.gexin.artplatform.bean;

import java.util.List;

public class Article {

	private String articleId;
	private String content;
	private long createTime;
	private List<String> images;
	private String studioAvatarUrl;
	private String studioId;
	private String studioName;
	private String title;
	private int viewNum;

	public String getArticleId() {
		return articleId;
	}

	public void setArticleId(String articleId) {
		this.articleId = articleId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public List<String> getImages() {
		return images;
	}

	public void setImages(List<String> images) {
		this.images = images;
	}

	public String getStudioAvatarUrl() {
		return studioAvatarUrl;
	}

	public void setStudioAvatarUrl(String studioAvatarUrl) {
		this.studioAvatarUrl = studioAvatarUrl;
	}

	public String getStudioId() {
		return studioId;
	}

	public void setStudioId(String studioId) {
		this.studioId = studioId;
	}

	public String getStudioName() {
		return studioName;
	}

	public void setStudioName(String studioName) {
		this.studioName = studioName;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getViewNum() {
		return viewNum;
	}

	public void setViewNum(int viewNum) {
		this.viewNum = viewNum;
	}

	@Override
	public String toString() {
		return "Article [articleId=" + articleId + ", content=" + content
				+ ", createTime=" + createTime + ", images=" + images
				+ ", studioAvatarUrl=" + studioAvatarUrl + ", studioId="
				+ studioId + ", studioName=" + studioName + ", title=" + title
				+ ", viewNum=" + viewNum + "]";
	}

}
