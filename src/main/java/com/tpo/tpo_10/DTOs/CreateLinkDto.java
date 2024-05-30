package com.tpo.tpo_10.DTOs;


import com.tpo.tpo_10.ValidationUtils.PasswordConstraints;
import com.tpo.tpo_10.ValidationUtils.UniqueURL;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;

import javax.annotation.Nullable;

public class CreateLinkDto {
    @NotNull
    @Size(min = 5, max = 20, message = "{error.name.length}")
    private String name;
    @Nullable
    @PasswordConstraints
    private String password;
    @URL(protocol = "https", message = "{error.url.invalid}")
    @UniqueURL
    private String targetUrl;

    public CreateLinkDto() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Nullable
    public String getPassword() {
        return password;
    }

    public void setPassword(@Nullable String password) {
        this.password = password;
    }

    public String getTargetUrl() {
        return targetUrl;
    }

    public void setTargetUrl(String targetUrl) {
        this.targetUrl = targetUrl;
    }
}
