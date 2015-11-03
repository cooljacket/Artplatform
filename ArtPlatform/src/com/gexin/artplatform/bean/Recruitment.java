package com.gexin.artplatform.bean;

public class Recruitment {

	private String description;
	private String recruitmentId;
	private String salary;
	private String title;
	private long updateTime;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getRecruitmentId() {
		return recruitmentId;
	}

	public void setRecruitmentId(String recruitmentId) {
		this.recruitmentId = recruitmentId;
	}

	public String getSalary() {
		return salary;
	}

	public void setSalary(String salary) {
		this.salary = salary;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}

	@Override
	public String toString() {
		return "Recruitment [description=" + description + ", recruitmentId="
				+ recruitmentId + ", salary=" + salary + ", title=" + title
				+ ", updateTime=" + updateTime + "]";
	}

}
