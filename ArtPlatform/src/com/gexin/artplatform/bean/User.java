package com.gexin.artplatform.bean;

import com.gexin.artplatform.utils.SPUtil;

import android.content.Context;

public class User {

	private String userId;
	private String name;
	private String avatarUrl;
	private String school;
	private String email;
	private String studioName;
	private int collectionNum;
	private int fanNum;
	private int followNum;
	private int gender;
	private int subscriptionNum;
	private int workNum;
	private int askNum;
	private int commentNum;
	private int askMeNum;
	private int answerNum;
	private int isTeacher;
	private long phone;
	private Place place;
	private Weibo weibo;
	private int grade;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAvatarUrl() {
		return avatarUrl;
	}

	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}

	public String getSchool() {
		return school;
	}

	public void setSchool(String school) {
		this.school = school;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getCollectionNum() {
		return collectionNum;
	}

	public void setCollectionNum(int collectionNum) {
		this.collectionNum = collectionNum;
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

	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	public int getSubscriptionNum() {
		return subscriptionNum;
	}

	public void setSubscriptionNum(int subscriptionNum) {
		this.subscriptionNum = subscriptionNum;
	}

	public int getWorkNum() {
		return workNum;
	}

	public void setWorkNum(int workNum) {
		this.workNum = workNum;
	}

	public int getAskNum() {
		return askNum;
	}

	public void setAskNum(int askNum) {
		this.askNum = askNum;
	}

	public int getCommentNum() {
		return commentNum;
	}

	public void setCommentNum(int commentNum) {
		this.commentNum = commentNum;
	}

	public int getAskMeNum() {
		return askMeNum;
	}

	public void setAskMeNum(int askMeNum) {
		this.askMeNum = askMeNum;
	}

	public int getAnswerNum() {
		return answerNum;
	}

	public void setAnswerNum(int answerNum) {
		this.answerNum = answerNum;
	}

	public long getPhone() {
		return phone;
	}

	public void setPhone(long phone) {
		this.phone = phone;
	}

	public Place getPlace() {
		return place;
	}

	public void setPlace(Place place) {
		this.place = place;
	}

	public int getIsTeacher() {
		return isTeacher;
	}

	public void setIsTeacher(int isTeacher) {
		this.isTeacher = isTeacher;
	}

	public Weibo getWeibo() {
		return weibo;
	}

	public void setWeibo(Weibo weibo) {
		this.weibo = weibo;
	}

	public int getGrade() {
		return grade;
	}

	public void setGrade(int grade) {
		this.grade = grade;
	}

	public String getStudioName() {
		return studioName;
	}

	public void setStudioName(String studioName) {
		this.studioName = studioName;
	}

	@Override
	public String toString() {
		return "User [userId=" + userId + ", name=" + name + ", avatarUrl="
				+ avatarUrl + ", school=" + school + ", email=" + email
				+ ", studioName=" + studioName + ", collectionNum="
				+ collectionNum + ", fanNum=" + fanNum + ", followNum="
				+ followNum + ", gender=" + gender + ", subscriptionNum="
				+ subscriptionNum + ", workNum=" + workNum + ", askNum="
				+ askNum + ", commentNum=" + commentNum + ", askMeNum="
				+ askMeNum + ", answerNum=" + answerNum + ", isTeacher="
				+ isTeacher + ", phone=" + phone + ", place=" + place
				+ ", weibo=" + weibo + ", grade=" + grade + "]";
	}

	public void putToSP(Context context) {
		SPUtil.put(context, "userId", this.userId);
		SPUtil.put(context, "name", this.name);
		SPUtil.put(context, "avatarUrl", this.avatarUrl);
		SPUtil.put(context, "school", this.school);
		SPUtil.put(context, "collectionNum", this.collectionNum);
		SPUtil.put(context, "fanNum", this.fanNum);
		SPUtil.put(context, "followNum", this.followNum);
		SPUtil.put(context, "gender", this.gender);
		SPUtil.put(context, "subscriptionNum", this.subscriptionNum);
		SPUtil.put(context, "workNum", this.workNum);
		SPUtil.put(context, "askNum", this.askNum);
		SPUtil.put(context, "commentNum", this.commentNum);
		SPUtil.put(context, "askMeNum", this.askMeNum);
		SPUtil.put(context, "answerNum", this.answerNum);
		SPUtil.put(context, "phone", this.phone);
		SPUtil.put(context, "grade", this.grade);
		if(this.studioName!=null){
			SPUtil.put(context, "studioName", this.studioName);
		}
		if (this.place.getProvince() != null) {
			SPUtil.put(context, "province", this.place.getProvince());
		}
		if (this.place.getCity() != null) {
			SPUtil.put(context, "city", this.place.getCity());
		}
		if (this.email != null) {
			SPUtil.put(context, "email", this.email);
		}
		if (this.weibo != null) {
			if (this.weibo.pToken != null) {
				SPUtil.put(context, "pToken", this.weibo.pToken);
			}
			if (this.weibo.pId != null) {
				SPUtil.put(context, "pID", this.weibo.pId);
			}
		}
		if (isTeacher == 0) {
			SPUtil.put(context, "LOGIN", "STUDENT");
		} else {
			SPUtil.put(context, "LOGIN", "TEACHER");
		}
	}

	private static class Weibo {
		public String pToken;
		public String pId;

		@Override
		public String toString() {
			return "Place [pToken=" + pToken + ", pId=" + pId + "]";
		}
	}
}
