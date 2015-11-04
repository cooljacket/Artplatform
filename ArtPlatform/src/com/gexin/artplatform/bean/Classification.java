package com.gexin.artplatform.bean;

import java.util.List;

public class Classification {
	private String name;
	private String icon;
	private String _id;
	private String superior;
	private long updateTime;
	private int type;
	private List<ImageItem> image;
	private List<Classification> subclass;

	@Override
	public String toString() {
		return "Classification [name=" + name + ", icon=" + icon + ", _id="
				+ _id + ", superior=" + superior + ", updateTime=" + updateTime
				+ ", type=" + type + ", image=" + image + ", subclass="
				+ subclass + "]";
	}

	public List<Classification> getSubclass() {
		return subclass;
	}

	public void setSubclass(List<Classification> subclass) {
		this.subclass = subclass;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public String getSuperior() {
		return superior;
	}

	public void setSuperior(String superior) {
		this.superior = superior;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public List<ImageItem> getImage() {
		return image;
	}

	public void setImage(List<ImageItem> image) {
		this.image = image;
	}

	public long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}

}
