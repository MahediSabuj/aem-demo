package com.aem.demo.core.models.authentication.impl;

import com.aem.demo.core.models.authentication.UserInfoModel;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class UserInfoModelImpl implements UserInfoModel {
    @Getter
    @JsonProperty("given_name")
    private String firstName;

    @Getter
    @JsonProperty("family_name")
    private String lastName;

    @Getter
    private String email;

    @Getter
    @JsonProperty("preferred_username")
    private String username;
}
