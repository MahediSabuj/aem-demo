package com.aem.demo.core.models.authentication.impl;

import com.aem.demo.core.models.authentication.TokenModel;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class TokenModelImpl implements TokenModel {
    @Getter
    @JsonProperty("access_token")
    private String accessToken;
}
