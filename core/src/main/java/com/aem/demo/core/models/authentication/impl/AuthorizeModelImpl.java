package com.aem.demo.core.models.authentication.impl;

import com.aem.demo.core.models.authentication.AuthorizeModel;

public class AuthorizeModelImpl implements AuthorizeModel {
    private String code;

    public AuthorizeModelImpl() {
    }

    public AuthorizeModelImpl(String code){
        this.code = code;
    }

    @Override
    public String getCode() {
        return code;
    }
}
