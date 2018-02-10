package com.carelynk.profile.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class DashboardModel {

    @SerializedName("result")
    @Expose
    private Result result;

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public class Result {

        @SerializedName("QuestionArray")
        @Expose
        private List<QuestionArray> questionArray = null;
        @SerializedName("ArticleArray")
        @Expose
        private List<ArticleArray> articleArray = null;
        @SerializedName("AnswerArray")
        @Expose
        private List<AnswerArray> answerArray = null;
        @SerializedName("FollowingArray")
        @Expose
        private List<FollowingArray> followingArray = null;
        @SerializedName("FollowerArray")
        @Expose
        private List<FollowerArray> followerArray = null;
        @SerializedName("DashboardDetail")
        @Expose
        private DashboardDetail dashboardDetail;

        @SerializedName("profileDetail")
        @Expose
        private profileDetail profileDetail;

        public List<QuestionArray> getQuestionArray() {
            return questionArray;
        }

        public void setQuestionArray(List<QuestionArray> questionArray) {
            this.questionArray = questionArray;
        }

        public List<ArticleArray> getArticleArray() {
            return articleArray;
        }

        public void setArticleArray(List<ArticleArray> articleArray) {
            this.articleArray = articleArray;
        }

        public List<AnswerArray> getAnswerArray() {
            return answerArray;
        }

        public void setAnswerArray(List<AnswerArray> answerArray) {
            this.answerArray = answerArray;
        }

        public List<FollowingArray> getFollowingArray() {
            return followingArray;
        }

        public void setFollowingArray(List<FollowingArray> followingArray) {
            this.followingArray = followingArray;
        }

        public List<FollowerArray> getFollowerArray() {
            return followerArray;
        }

        public void setFollowerArray(List<FollowerArray> followerArray) {
            this.followerArray = followerArray;
        }

        public DashboardDetail getDashboardDetail() {
            return dashboardDetail;
        }

        public void setDashboardDetail(DashboardDetail dashboardDetail) {
            this.dashboardDetail = dashboardDetail;
        }

        public profileDetail getProfileDetail() {
            return profileDetail;
        }

        public void setProfileDetail(profileDetail profileDetail) {
            this.profileDetail = profileDetail;
        }
    }

    public class DashboardDetail {

        @SerializedName("Name")
        @Expose
        private String name;
        @SerializedName("UserId")
        @Expose
        private String userId;
        @SerializedName("ProfilePicUrl")
        @Expose
        private String profilePicUrl;

        public String getRequestSent() {
            return RequestSent;
        }

        public void setRequestSent(String requestSent) {
            RequestSent = requestSent;
        }

        public String getFollowings() {
            return Followings;
        }

        public void setFollowings(String followings) {
            Followings = followings;
        }

        public String getOwnUserId() {
            return OwnUserId;
        }

        public void setOwnUserId(String ownUserId) {
            OwnUserId = ownUserId;
        }

        @SerializedName("AboutMeText")
        @Expose
        private String aboutMeText;
        @SerializedName("Education")
        @Expose
        private String education;
        @SerializedName("Certification")
        @Expose
        private String certification;
        @SerializedName("Expertise")
        @Expose
        private String expertise;
        @SerializedName("RequestSent")
        @Expose
        private String RequestSent;
        @SerializedName("Followings")
        @Expose
        private String Followings;
        @SerializedName("OwnUserId")
        @Expose
        private String OwnUserId;


        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getProfilePicUrl() {
            return profilePicUrl;
        }

        public void setProfilePicUrl(String profilePicUrl) {
            this.profilePicUrl = profilePicUrl;
        }

        public String getAboutMeText() {
            return aboutMeText;
        }

        public void setAboutMeText(String aboutMeText) {
            this.aboutMeText = aboutMeText;
        }

        public String getEducation() {
            return education;
        }

        public void setEducation(String education) {
            this.education = education;
        }

        public String getCertification() {
            return certification;
        }

        public void setCertification(String certification) {
            this.certification = certification;
        }

        public String getExpertise() {
            return expertise;
        }

        public void setExpertise(String expertise) {
            this.expertise = expertise;
        }

    }

    public class profileDetail {

        public String getAboutMe() {
            return AboutMe;
        }

        public void setAboutMe(String aboutMe) {
            AboutMe = aboutMe;
        }

        public String getAboutMeText() {
            return AboutMeText;
        }

        public void setAboutMeText(String aboutMeText) {
            AboutMeText = aboutMeText;
        }

        public String getCertification() {
            return Certification;
        }

        public void setCertification(String certification) {
            Certification = certification;
        }

        public String getCity() {
            return City;
        }

        public void setCity(String city) {
            City = city;
        }

        public String getEducation() {
            return Education;
        }

        public void setEducation(String education) {
            Education = education;
        }

        public String getExpertise() {
            return Expertise;
        }

        public void setExpertise(String expertise) {
            Expertise = expertise;
        }

        public String getFirstName() {
            return FirstName;
        }

        public void setFirstName(String firstName) {
            FirstName = firstName;
        }

        public String getLastName() {
            return LastName;
        }

        public void setLastName(String lastName) {
            LastName = lastName;
        }

        public String getState() {
            return State;
        }

        public void setState(String state) {
            State = state;
        }

        @SerializedName("AboutMe")
        @Expose
        private String AboutMe;
        @SerializedName("AboutMeText")
        @Expose
        private String AboutMeText;
        @SerializedName("Certification")
        @Expose
        private String Certification;
        @SerializedName("City")
        @Expose
        private String City;
        @SerializedName("Education")
        @Expose
        private String Education;
        @SerializedName("Expertise")
        @Expose
        private String Expertise;
        @SerializedName("FirstName")
        @Expose
        private String FirstName;
        @SerializedName("LastName")
        @Expose
        private String LastName;
        @SerializedName("State")
        @Expose
        private String State;

    }

    public class FollowerArray {

        @SerializedName("Name")
        @Expose
        private String name;
        @SerializedName("userid")
        @Expose
        private String userid;
        @SerializedName("ProfilePicUrl")
        @Expose
        private String profilePicUrl;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public String getProfilePicUrl() {
            return profilePicUrl;
        }

        public void setProfilePicUrl(String profilePicUrl) {
            this.profilePicUrl = profilePicUrl;
        }

    }

    public class FollowingArray {

        @SerializedName("Name")
        @Expose
        private String name;
        @SerializedName("userid")
        @Expose
        private String userid;
        @SerializedName("ProfilePicUrl")
        @Expose
        private String profilePicUrl;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public String getProfilePicUrl() {
            return profilePicUrl;
        }

        public void setProfilePicUrl(String profilePicUrl) {
            this.profilePicUrl = profilePicUrl;
        }

    }

    public class QuestionArray {

        @SerializedName("GoalId")
        @Expose
        private String goalId;
        @SerializedName("Question")
        @Expose
        private String question;

        public String getGoalId() {
            return goalId;
        }

        public void setGoalId(String goalId) {
            this.goalId = goalId;
        }

        public String getQuestion() {
            return question;
        }

        public void setQuestion(String question) {
            this.question = question;
        }

    }

    public class ArticleArray {

        @SerializedName("GoalId")
        @Expose
        private String goalId;
        @SerializedName("Article")
        @Expose
        private String article;

        public String getGoalId() {
            return goalId;
        }

        public void setGoalId(String goalId) {
            this.goalId = goalId;
        }

        public String getArticle() {
            return article;
        }

        public void setArticle(String article) {
            this.article = article;
        }
    }

    public class AnswerArray {

        @SerializedName("GoalId")
        @Expose
        private String goalId;
        @SerializedName("GoalName")
        @Expose
        private String goalName;
        @SerializedName("Desc")
        @Expose
        private String desc;

        public String getGoalId() {
            return goalId;
        }

        public void setGoalId(String goalId) {
            this.goalId = goalId;
        }

        public String getGoalName() {
            return goalName;
        }

        public void setGoalName(String goalName) {
            this.goalName = goalName;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

    }
}







