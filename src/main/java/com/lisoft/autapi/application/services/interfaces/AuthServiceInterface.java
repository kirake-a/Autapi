package com.lisoft.autapi.application.services.interfaces;

import com.lisoft.autapi.application.dtos.*;

public interface AuthServiceInterface {
    UserLoginServiceDto logIn(UserLogInDto user);

    SuccessfulRegistrationDto signUp(UserSignUpDto user);

    SuccessfulPasswordResetDto passwordReset(UserResetPassword data);
}
