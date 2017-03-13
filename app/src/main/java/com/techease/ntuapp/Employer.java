package com.techease.ntuapp;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kaxhiftaj on 3/9/17.
 */

public class Employer { String name, logo, bio, subject, apply  ;

    public Employer() {

    }

    public Employer(String logo,  String name, String bio , String subject , String apply ) {
        this.logo = logo;
        this.name = name;
        this.bio = bio;
        this.subject = subject;
        this.apply = apply ;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }


    public String getApply() {
        return apply;
    }

    public void setApply(String apply) {
        this.apply = apply;
    }
}
