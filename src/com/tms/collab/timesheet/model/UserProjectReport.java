package com.tms.collab.timesheet.model;

import java.io.Serializable;
import java.util.Collection;

import kacang.services.security.User;

import com.tms.collab.project.Project;

/**
 * Created by IntelliJ IDEA.
 * User: oilai
 * Date: Mar 2, 2006
 * Time: 3:24:25 PM
 * To change this template use File | Settings | File Templates.
 */
public class UserProjectReport implements Serializable {
        public Project[] projects;
        public User user;
        public Collection totalDetails;
        public double[] daily;
        public double[] dailyTotal;
        public double[] projectTotal;

        public UserProjectReport() {
        }

    public Project[] getProjects() {
        return projects;
    }

    public void setProjects(Project[] projects) {
        this.projects = projects;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Collection getTotalDetails() {
        return totalDetails;
    }

    public void setTotalDetails(Collection totalDetails) {
        this.totalDetails = totalDetails;
    }

    public double[] getDaily() {
        return daily;
    }

    public void setDaily(double[] daily) {
        this.daily = daily;
    }

    public double[] getDailyTotal() {
        return dailyTotal;
    }

    public void setDailyTotal(double[] dailyTotal) {
        this.dailyTotal = dailyTotal;
    }

    public double[] getProjectTotal() {
        return projectTotal;
    }

    public void setProjectTotal(double[] projectTotal) {
        this.projectTotal = projectTotal;
    }

    public String getProjectTotalList() {
        if (projects!=null && projects.length>0)
            return projects.length+"";
        else
            return "0";
    }

}
