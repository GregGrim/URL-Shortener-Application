package com.tpo.tpo_10.DTOs;

import com.tpo.tpo_10.ValidationUtils.UniqueURL;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;

public class LinkDto {
    private String id;
    @NotNull
    @Size(min = 5, max = 20, message = "{error.name.length}")
    private String name;
    @URL(protocol = "https", message = "{error.url.invalid}")
    @UniqueURL
    private String targetUrl;
    @URL(protocol = "https", message = "{error.url.invalid}")
    @UniqueURL
    private String redirectUrl;
    private int visits;

    public LinkDto () {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTargetUrl() {
        return targetUrl;
    }

    public void setTargetUrl(String targetUrl) {
        this.targetUrl = targetUrl;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public int getVisits() {
        return visits;
    }

    public void setVisits(int visits) {
        this.visits = visits;
    }

}

