package com.aem.demo.core.models.authentication.impl;

import com.aem.demo.core.models.authentication.AuthorizeModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class AuthorizeModelImpl implements AuthorizeModel {
    @Getter
    private String code;
}
