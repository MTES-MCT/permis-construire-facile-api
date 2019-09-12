package com.github.mtesmct.rieau.api.application.auth;

public interface AuthenticationService {
    public boolean isAuthenticaed();
    public boolean isDeposant();
    public boolean isInstructeur();
    public boolean isBeta();
    public String userId();
}