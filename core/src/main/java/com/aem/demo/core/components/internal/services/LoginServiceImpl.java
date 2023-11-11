package com.aem.demo.core.components.internal.services;

import com.aem.demo.core.components.services.LoginService;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;

@Component(service = { LoginService.class },
  property = {
    Constants.SERVICE_DESCRIPTION + "=Login Service"
  }
)
public class LoginServiceImpl implements LoginService {
  @Override
  public boolean loginUser(String username) {
    return false;
  }
}
