package com.aem.demo.core.models.authentication.impl;

import com.aem.demo.core.models.authentication.AuthorizeModel;

public class AuthorizeModelImpl implements AuthorizeModel {
    private String code;

    @Override
    public String getCode() {
        return code;
    }
}
