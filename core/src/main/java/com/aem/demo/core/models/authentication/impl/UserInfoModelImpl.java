package com.aem.demo.core.models.authentication.impl;

import com.aem.demo.core.models.authentication.UserInfoModel;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UserInfoModelImpl implements UserInfoModel {
    @JsonProperty("given_name")
    private String firstName;

    @JsonProperty("family_name")
    private String lastName;

    private String email;

    @JsonProperty("preferred_username")
    private String username;

    @Override
    public String getFirstName() {
        return firstName;
    }

    @Override
    public String getLastName() {
        return lastName;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getCountry() {
        return null;
    }
}
