package com.kashifirshad.softwareprojects;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by biome on 3/17/2018.
 */

public class Project {

    long Id;
    String Story;
    String FilePaths;
    String EstimatedHrs;
    String EstimateCost;
    String DeliveryDate;
    String CreatedAt;
    String UpdatedAt;
    Long UserId;
    int IsSynched;
    int IsRead;
    long ParentId;
    long ServerId;
    User User;
    List<Project> ChildProjects;

    public Project(){

    }

    public Project(int id, String story, String filePaths, String estimatedHrs, String estimateCost, String deliveryDate, int isSynched, int isRead, long parentId, int serverId, User user) {
        Id = id;
        FilePaths = filePaths;
        Story = story;
        EstimatedHrs = estimatedHrs;
        EstimateCost = estimateCost;
        DeliveryDate = deliveryDate;
//        CreatedAt = getDateTime();
//        UpdatedAt = getDateTime();
        UserId = user.getId();
        IsSynched = isSynched;
        IsRead = isRead;
        ParentId = parentId;
        ServerId = serverId;
        User = user;
    }

    public Project(String story, String filePaths, String estimatedHrs, String estimateCost, String deliveryDate, int isSynched, int isRead, long parentId, int serverId, User user) {


        Story = story;
        FilePaths = filePaths;
        EstimatedHrs = estimatedHrs;
        EstimateCost = estimateCost;
        DeliveryDate = deliveryDate;
//        CreatedAt = getDateTime();
//        UpdatedAt = getDateTime();
        UserId = user.getId();
        IsSynched = isSynched;
        IsRead = isRead;
        ParentId = parentId;
        ServerId = serverId;
        User = user;
    }


    public long getId() {
        return Id;
    }

    public void setChildProjects(List<Project> childProjects) {
        this.ChildProjects = childProjects;
    }

    public List<Project> getChildProjects() {
        return this.ChildProjects;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getStory() {
        return Story;
    }

    public void setStory(String story) {
        Story = story;
    }

    public String getFilePaths() {
        return FilePaths;
    }

    public void setFilePaths(String filePaths) {
        FilePaths = filePaths;
    }

    public String getEstimatedHrs() {
        return EstimatedHrs;
    }

    public void setEstimatedHrs(String estimatedHrs) {
        EstimatedHrs = estimatedHrs;
    }

    public String getEstimateCost() {
        return EstimateCost;
    }

    public void setEstimateCost(String estimateCost) {
        EstimateCost = estimateCost;
    }

    public String getDeliveryDate() {
        return DeliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        DeliveryDate = deliveryDate;
    }

    public String getCreatedAt() {
        return CreatedAt;
    }

    public void setCreatedAt(String createdAt) {
        CreatedAt = createdAt;
    }

    public String getUpdatedAt() {
        return UpdatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        UpdatedAt = updatedAt;
    }

    public long getUserId() {
        return UserId;
    }

    public void setUserId(long userId) {
        UserId = userId;
    }

    public int getIsSynched() {
        return IsSynched;
    }

    public void setIsRead(int isRead) {
        IsRead = isRead;
    }
    public int getIsRead() {
        return IsRead;
    }

    public void setIsSynched(int synched) {
        IsSynched = synched;
    }

    public long getParentId() {
        return ParentId;
    }

    public void setParentId(long parentId) {
        ParentId = parentId;
    }

    public long getServerId() {
        return ServerId;
    }

    public void setServerId(long serverId) {
        ServerId = serverId;
    }

    public User getUser() {
        return User;
    }

    public void setUser(User user) {
        this.User = user;
        this.setUserId(user.getId());
    }

    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }
}
