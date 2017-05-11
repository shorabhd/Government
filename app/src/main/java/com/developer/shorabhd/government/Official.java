package com.developer.shorabhd.government;

import java.io.Serializable;

/**
 * Created by shorabhd on 3/31/17.
 */

public class Official implements Serializable {
    String office;
    String name;
    String party;
    String address;
    String phone;
    String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Official(String name, String office, String party, String add,
                    String phone, String website, String photo, String gplus,
                    String fb, String twitter, String youtube) {
        this.name = name;
        this.office = office;
        this.party = party;
        this.address = add;
        this.phone = phone;
        this.website = website;
        this.photo = photo;
        this.googlePlus = gplus;
        this.fb = fb;
        this.twitter = twitter;
        this.youtube = youtube;

    }

    public String getParty() {
        return party;
    }

    public void setParty(String party) {
        this.party = party;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getGooglePlus() {
        return googlePlus;
    }

    public void setGooglePlus(String googlePlus) {
        this.googlePlus = googlePlus;
    }

    public String getFb() {
        return fb;
    }

    public void setFb(String fb) {
        this.fb = fb;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public String getYoutube() {
        return youtube;
    }

    public void setYoutube(String youtube) {
        this.youtube = youtube;
    }

    String website;
    String photo;
    String googlePlus;
    String fb;
    String twitter;
    String youtube;

    public String getOffice() {
        return office;
    }

    public void setOffice(String role) {
        this.office = office;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
