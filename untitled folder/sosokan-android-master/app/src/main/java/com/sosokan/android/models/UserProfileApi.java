package com.sosokan.android.models;

/**
 * Created by macintosh on 2/3/17.
 */

public class UserProfileApi {

    private int id;
    private String url;
    private String address;
    private boolean callAble;
    private String city;
    private String companyName;
    private String credit;
    private boolean emailAble;
    private String faxNumber;
    private String legacy_id;
    private int myAdvertiseCount;
    private String note;
    private String phoneNumber;
    private String role;
    private String state;
    private String zip;
    private String user;
    private String image_url;
    private String display_name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDisplay_name() {
        return display_name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isCallAble() {
        return callAble;
    }

    public void setCallAble(boolean callAble) {
        this.callAble = callAble;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCredit() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }

    public boolean isEmailAble() {
        return emailAble;
    }

    public void setEmailAble(boolean emailAble) {
        this.emailAble = emailAble;
    }

    public String getFaxNumber() {
        return faxNumber;
    }

    public void setFaxNumber(String faxNumber) {
        this.faxNumber = faxNumber;
    }

    public String getLegacy_id() {
        return legacy_id;
    }

    public void setLegacy_id(String legacy_id) {
        this.legacy_id = legacy_id;
    }

    public int getMyAdvertiseCount() {
        return myAdvertiseCount;
    }

    public void setMyAdvertiseCount(int myAdvertiseCount) {
        this.myAdvertiseCount = myAdvertiseCount;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public UserProfileApi() {

    }
}
