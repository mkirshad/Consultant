package com.kashifirshad.softwareprojects;

import android.content.Context;

import java.util.List;

public class Projects {
    User user;
    List<Project> projects;
    String LastUpdatedProject;
    String LastUpdatedUser;

    public Projects(Context ct){
        user = MainActivity.user;
        DatabaseHelper dh = new DatabaseHelper(ct);
        projects = dh.getAllProjects(user.getId(), 0, 0);
        LastUpdatedProject = dh.getLasUpdatedtProject();
        LastUpdatedUser = dh.getLastUpdatedUser();
        dh.closeDB();
    }
}
