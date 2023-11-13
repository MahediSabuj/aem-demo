package com.aem.demo.core.models.authentication.impl;

import com.aem.demo.core.models.authentication.TokenModel;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TokenModelImpl implements TokenModel {
    @JsonProperty("access_token")
    private String accessToken;

    @Override
    public String getAccessToken() {
        return accessToken;
    }
}
