package com.github.mtesmct.rieau.api.application.auth;

import java.util.Optional;

import com.github.mtesmct.rieau.api.domain.entities.personnes.User;

public interface AuthenticationService {
    public boolean isAuthenticaed();
    public boolean isDeposant();
    public boolean isInstructeur();
    public boolean isMairie();
    public boolean isBeta();
    public Optional<User> user() throws UserInfoServiceException;
}