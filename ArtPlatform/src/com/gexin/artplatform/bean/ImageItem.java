package com.gexin.artplatform.bean;

import java.util.List;

public class ImageItem {

	private String url;
	private String authorId;
	private List<String> tags;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getAuthorId() {
		return authorId;
	}

	public void setAuthorId(String authorId) {
		this.authorId = authorId;
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}

	@Override
	public String toString() {
		return "ImageItem [url=" + url + ", authorId=" + authorId + ", tags="
				+ tags + "]";
	}

}
