package com.sosokan.android.models;

import java.util.Map;

/**
 * Created by AnhZin on 9/24/2016.
 */
public class Conversation {
    public String id;
    public String conversationId;
    public String createdBy;
    public String receivedBy;
    public String postId;
    public long createdAt;
    public long updatedAt;
    public String postName;
    public long postCreatedAt;
    public String postHeaderImageURL;
    public String lastMessageId;
    public String lastMessageContent;
    public String lastMessageSentBy;
    public long lastMessageTimestamp;
    public Map<String, Object> users;
    public Map<String, Object> unreadLastMessageUsers;
    public Map<String, Object> tempDeleteUsers;

    public Map<String, Object> getWaitingUsers() {
        return waitingUsers;
    }

    public void setWaitingUsers(Map<String, Object> waitingUsers) {
        this.waitingUsers = waitingUsers;
    }

    public Map<String, Object> waitingUsers;


    public String postOwnerId;

    public String getPostCategoryId() {
        return postCategoryId;
    }

    public void setPostCategoryId(String postCategoryId) {
        this.postCategoryId = postCategoryId;
    }

    public String getPostOwnerId() {
        return postOwnerId;
    }

    public void setPostOwnerId(String postOwnerId) {
        this.postOwnerId = postOwnerId;
    }

    public String postCategoryId;
    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }


    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getReceivedBy() {
        return receivedBy;
    }

    public void setReceivedBy(String receivedBy) {
        this.receivedBy = receivedBy;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }


    public String getPostName() {
        return postName;
    }

    public void setPostName(String postName) {
        this.postName = postName;
    }

    public long getPostCreatedAt() {
        return postCreatedAt;
    }

    public void setPostCreatedAt(long postCreatedAt) {
        this.postCreatedAt = postCreatedAt;
    }

    public String getPostHeaderImageURL() {
        return postHeaderImageURL;
    }

    public void setPostHeaderImageURL(String postHeaderImageURL) {
        this.postHeaderImageURL = postHeaderImageURL;
    }


    public String getLastMessageId() {
        return lastMessageId;
    }

    public void setLastMessageId(String lastMessageId) {
        this.lastMessageId = lastMessageId;
    }

    public String getLastMessageContent() {
        return lastMessageContent;
    }

    public void setLastMessageContent(String lastMessageContent) {
        this.lastMessageContent = lastMessageContent;
    }

    public String getLastMessageSentBy() {
        return lastMessageSentBy;
    }

    public void setLastMessageSentBy(String lastMessageSentBy) {
        this.lastMessageSentBy = lastMessageSentBy;
    }

    public long getLastMessageTimestamp() {
        return lastMessageTimestamp;
    }

    public void setLastMessageTimestamp(long lastMessageTimestamp) {
        this.lastMessageTimestamp = lastMessageTimestamp;
    }

    public Map<String, Object> getUsers() {
        return users;
    }

    public void setUsers(Map<String, Object> users) {
        this.users = users;
    }


    public Map<String, Object> getUnreadLastMessageUsers() {
        return unreadLastMessageUsers;
    }

    public void setUnreadLastMessageUsers(Map<String, Object> unreadLastMessageUsers) {
        this.unreadLastMessageUsers = unreadLastMessageUsers;
    }

    public Map<String, Object> getTempDeleteUsers() {
        return tempDeleteUsers;
    }

    public void setTempDeleteUsers(Map<String, Object> tempDeleteUsers) {
        this.tempDeleteUsers = tempDeleteUsers;
    }

    public Conversation() {
    }


}
