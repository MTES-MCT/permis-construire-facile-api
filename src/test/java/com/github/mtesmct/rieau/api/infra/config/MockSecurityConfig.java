package com.github.mtesmct.rieau.api.infra.config;

import java.util.Optional;

import com.github.mtesmct.rieau.api.application.auth.Role;
import com.github.mtesmct.rieau.api.application.auth.UserService;
import com.github.mtesmct.rieau.api.domain.entities.personnes.Personne;
import com.github.mtesmct.rieau.api.infra.application.auth.WithDeposantAndBetaDetails;
import com.github.mtesmct.rieau.api.infra.application.auth.WithInstructeurNonBetaDetails;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@TestConfiguration
public class MockSecurityConfig {

    @Autowired
    private UserService userService;

    @Bean
    @Qualifier("deposantBeta")
    public Personne deposantBeta(){
        Optional<Personne> deposantBeta = this.userService.findUserById((WithDeposantAndBetaDetails.ID));
        if (deposantBeta.isEmpty())
            throw new NullPointerException("Le bean de l'utilisateur déposant beta ne peut être instantié car le user service ne le trouve pas.");
        return deposantBeta.get();
    }

    @Bean
    @Qualifier("instructeurNonBeta")
    public Personne instructeurNonBeta(){
        Optional<Personne> instructeurNonBeta = this.userService.findUserById(WithInstructeurNonBetaDetails.ID);
        if (instructeurNonBeta.isEmpty())
            throw new NullPointerException("Le bean de l'utilisateur instructeur non beta ne peut être instantié car le user service ne le trouve pas.");
        return instructeurNonBeta.get();
    }

    @Bean
    public UserDetailsService userDetailsService() throws Exception {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        manager.createUser(User.withUsername(WithDeposantAndBetaDetails.ID).password(encoder.encode(WithDeposantAndBetaDetails.ID)).roles(Role.DEPOSANT.toString(), Role.BETA.toString()).build());
        manager.createUser(User.withUsername(WithInstructeurNonBetaDetails.ID).password(encoder.encode(WithInstructeurNonBetaDetails.ID)).roles(Role.INSTRUCTEUR.toString()).build());
        return manager;
    }

}