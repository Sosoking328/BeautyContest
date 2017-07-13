package com.sosokan.android.models;


import java.util.Map;

/**
 * Created by AnhZin on 8/27/2016.
 */
public class User {
    public User() {
    }

    public User(String id, String phoneNumber,String phoneCode, String email, String password, String address, boolean isVerify, String providerId, Image avatar, String userName) {
        this.id = id;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.password = password;
        this.address = address;
        this.isVerify = isVerify;
        this.providerId = providerId;
        this.avatar = avatar;
        this.userName = userName;
        this.phoneCode = phoneCode;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }

    public Integer getTotalClassifieds() {
        return totalClassifieds;
    }

    public void setTotalClassifieds(Integer totalClassifieds) {
        this.totalClassifieds = totalClassifieds;
    }

    public Boolean getCallAble() {
        return callAble;
    }

    public void setCallAble(Boolean callAble) {
        this.callAble = callAble;
    }

    public Boolean getEmailAble() {
        return emailAble;
    }

    public void setEmailAble(Boolean emailAble) {
        this.emailAble = emailAble;
    }

    public ObjectAuthData getAuthData() {
        return authData;
    }

    public void setAuthData(ObjectAuthData authData) {
        this.authData = authData;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Double getCredit() {
        return credit;
    }

    public void setCredit(Double credit) {
        this.credit = credit;
    }

    public Double credit;
    public String firstName;
    public String lastName;
    public ObjectAuthData authData;
    public Boolean callAble;
    public Boolean emailAble;
    public Integer totalClassifieds;
    public String sessionToken;
    public String role;
    public long createdAt;
    public long updatedAt;
    private  String id;
    private  String phoneNumber;


    public Map<String, Object> getMessages() {
        return messages;
    }

    public void setMessages(Map<String, Object> messages) {
        this.messages = messages;
    }

    public Map<String,Object>   messages;

    public String getPhoneCode() {
        return phoneCode;
    }

    public void setPhoneCode(String phoneCode) {
        this.phoneCode = phoneCode;
    }

    private  String  phoneCode;
    private  String email;
    private  String password;
    private  String companyName;
    private  String address;
    private  String city;
    private  String state;
    private  String zipCode;
    private  String countryCode;
    private  String faxCode;
    private  String faxNumber;
    private  String note;
    private  String subscriptionId;
    private  boolean isVerify;
    private  String providerId;
    private  Image avatar;
    private  String userName;

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    private  String zip;
    public int getNumberOfMessages() {
        return numberOfMessages;
    }

    public void setNumberOfMessages(int numberOfMessages) {
        this.numberOfMessages = numberOfMessages;
    }

    private  int numberOfMessages;
    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    private  String  website;
    public Map<String, Object> getConversations() {
        return conversations;
    }

    public void setConversations(Map<String, Object> conversations) {
        this.conversations = conversations;
    }

    public Map<String, Object> conversations;
    public String getBcryptPassword() {
        return bcryptPassword;
    }

    public void setBcryptPassword(String bcryptPassword) {
        this.bcryptPassword = bcryptPassword;
    }

    public String bcryptPassword;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getFaxCode() {
        return faxCode;
    }

    public void setFaxCode(String faxCode) {
        this.faxCode = faxCode;
    }

    public String getFaxNumber() {
        return faxNumber;
    }

    public void setFaxNumber(String faxNumber) {
        this.faxNumber = faxNumber;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(String subscriptionId) {
        this.subscriptionId = subscriptionId;
    }



    public boolean isVerify() {
        return isVerify;
    }

    public void setVerify(boolean verify) {
        isVerify = verify;
    }

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public Image getAvatar() {
        return avatar;
    }

    public void setAvatar(Image avatar) {
        this.avatar = avatar;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }

    public float getStar() {
        return star;
    }

    public void setStar(float star) {
        this.star = star;
    }



    public Map<String, Object> getImages() {
        return images;
    }

    public void setImages(Map<String, Object> images) {
        this.images = images;
    }

    public Map<String, Object> getVideos() {
        return videos;
    }

    public void setVideos(Map<String, Object> videos) {
        this.videos = videos;
    }



    public Map<String, Object> getAdvertises() {
        return advertises;
    }

    public void setAdvertises(Map<String, Object> advertises) {
        this.advertises = advertises;
    }

    public Map<String, Object> getFavorites() {
        return favorites;
    }

    public void setFavorites(Map<String, Object> favorites) {
        this.favorites = favorites;
    }

    private  int like;
    private float star;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String userId;
    public int getMyAdvertiseCount() {
        return myAdvertiseCount;
    }

    public void setMyAdvertiseCount(int myAdvertiseCount) {
        this.myAdvertiseCount = myAdvertiseCount;
    }

    private  int myAdvertiseCount;
    public Map<String,Object> images;
    public Map<String,Object> videos;


   /* public Map<String,Object> getSubscriptions() {
        return subscriptions;
    }

    public void setSubscriptions(Map<String,Object> subscriptions) {
        this.subscriptions = subscriptions;
    }*/

    public Map<String, Object> getUsersSubscribingMe() {
        return usersSubscribingMe;
    }

    public void setUsersSubscribingMe(Map<String, Object> usersSubscribingMe) {
        this.usersSubscribingMe = usersSubscribingMe;
    }

    public Map<String, Object> getFollowingUsers() {
        return followingUsers;
    }

    public void setFollowingUsers(Map<String, Object> followingUsers) {
        this.followingUsers = followingUsers;
    }


    public Map<String,Object> followingUsers;
    public Map<String,Object> usersSubscribingMe;
    public Map<String,Object> advertises;
    public Map<String,Object> favorites;

    public Map<String, Object> getRatings() {
        return ratings;
    }

    public void setRatings(Map<String, Object> ratings) {
        this.ratings = ratings;
    }

    public Map<String,Object> ratings;


    public Map<String, Object> getRateds() {
        return rateds;
    }

    public void setRateds(Map<String, Object> rateds) {
        this.rateds = rateds;
    }

    public Map<String,Object> rateds;

    public Double getDescendingTime() {
        return descendingTime;
    }

    public void setDescendingTime(Double descendingTime) {
        this.descendingTime = descendingTime;
    }

    public Double descendingTime;
}
