package com.gexin.artplatform.bean;

import com.gexin.artplatform.utils.PinYin;

public class SimpleStudio implements Comparable<SimpleStudio> {

	private String studioId;
	private String avatarUrl;
	private String name;
	private String fullPinyin;
	private String simplePinyin;
	private String sortLetter;

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		try {
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.fullPinyin = PinYin.getPinYin(name);
		this.simplePinyin = PinYin.getSimplePinYin(name);
		this.sortLetter = fullPinyin.substring(0, 1);
		this.name = name;
	}

	public String getFullPinyin() {
		return fullPinyin;
	}

	public String getSimplePinyin() {
		return simplePinyin;
	}

	public String getSortLetter() {
		return sortLetter;
	}

	@Override
	public String toString() {
		return "SimpleStudio [studioId=" + studioId + ", avatarUrl="
				+ avatarUrl + ", name=" + name + ", fullPinyin=" + fullPinyin
				+ ", simplePinyin=" + simplePinyin + ", sortLetter="
				+ sortLetter + "]";
	}

	@Override
	public int compareTo(SimpleStudio arg0) {
		try {
			if (fullPinyin == null || fullPinyin.isEmpty()) {
				this.fullPinyin = PinYin.getPinYin(name);
				this.simplePinyin = PinYin.getSimplePinYin(name);
				this.sortLetter = fullPinyin.substring(0, 1);
			}
			if (arg0.fullPinyin == null || arg0.fullPinyin.isEmpty()) {
				arg0.fullPinyin = PinYin.getPinYin(arg0.name);
				arg0.simplePinyin = PinYin.getSimplePinYin(arg0.name);
				arg0.sortLetter = arg0.fullPinyin.substring(0, 1);
			}
			return this.fullPinyin.compareTo(arg0.fullPinyin);
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
}
