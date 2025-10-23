package com.lisoft.autapi.application.services.interfaces;

import com.lisoft.autapi.application.dtos.SuccessfulRegistrationDto;
import com.lisoft.autapi.application.dtos.UserLogInDto;
import com.lisoft.autapi.application.dtos.UserLoginServiceDto;
import com.lisoft.autapi.application.dtos.UserSignUpDto;

public interface AuthServiceInterface {
    UserLoginServiceDto logIn(UserLogInDto user);

    SuccessfulRegistrationDto signUp(UserSignUpDto user);
}
