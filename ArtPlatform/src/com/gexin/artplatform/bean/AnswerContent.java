package com.gexin.artplatform.bean;

public class AnswerContent {

	private String content;
	private int aType;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getaType() {
		return aType;
	}

	public void setaType(int aType) {
		this.aType = aType;
	}

	@Override
	public String toString() {
		return "AnswerContent [content=" + content + ", aType=" + aType + "]";
	}

}
