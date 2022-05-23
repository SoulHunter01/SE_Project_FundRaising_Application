package com.zaidbinihtesham.seproject;

public class Contribution {

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getContribution() {
        return Contribution;
    }

    public void setContribution(String contribution) {
        Contribution = contribution;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public Contribution(String title, String contribution, String username) {
        Title = title;
        Contribution = contribution;
        Username = username;
    }

    String Title;
    String Contribution;
    String Username;


}