package com.lisoft.autapi.application.services.interfaces;

import com.lisoft.autapi.application.dtos.AuthenticationDto;
import com.lisoft.autapi.application.dtos.SuccessfulRegistrationDto;
import com.lisoft.autapi.application.dtos.UserLogInDto;
import com.lisoft.autapi.application.dtos.UserSignUpDto;

public interface AuthServiceInterface {
    AuthenticationDto logIn(UserLogInDto user);

    SuccessfulRegistrationDto signUp(UserSignUpDto user);
}
