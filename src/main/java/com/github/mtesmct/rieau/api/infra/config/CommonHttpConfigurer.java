package com.github.mtesmct.rieau.api.infra.config;

import com.github.mtesmct.rieau.api.application.auth.Roles;
import com.github.mtesmct.rieau.api.infra.http.dossiers.DossiersController;
import com.github.mtesmct.rieau.api.infra.http.fichiers.FichiersController;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;

public class CommonHttpConfigurer extends AbstractHttpConfigurer<CommonHttpConfigurer, HttpSecurity> {

    @Override
    public void init(HttpSecurity http) throws Exception {
        http.csrf().disable().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests().antMatchers(HttpMethod.OPTIONS).permitAll()
                .and().authorizeRequests()
                .antMatchers(HttpMethod.GET, DossiersController.ROOT_URI + "/**")
                .hasAnyRole(Roles.DEPOSANT, Roles.MAIRIE, Roles.INSTRUCTEUR)
                .antMatchers(HttpMethod.POST, DossiersController.ROOT_URI)
                .hasRole(Roles.DEPOSANT)
                .antMatchers(HttpMethod.DELETE, DossiersController.ROOT_URI + "/**")
                .hasRole(Roles.DEPOSANT)
                .antMatchers(HttpMethod.POST, DossiersController.ROOT_URI + "/*" + DossiersController.PIECES_JOINTES_URI + "/*")
                .hasRole(Roles.DEPOSANT)
                .antMatchers(HttpMethod.POST, DossiersController.ROOT_URI + "/*" + DossiersController.QUALIFIER_URI)
                .hasRole(Roles.MAIRIE)
                .antMatchers(HttpMethod.POST, DossiersController.ROOT_URI + "/*" + DossiersController.DECLARER_INCOMPLET_URI)
                .hasRole(Roles.INSTRUCTEUR)
                .antMatchers(HttpMethod.POST, DossiersController.ROOT_URI + "/*" + DossiersController.DECLARER_COMPLET_URI)
                .hasRole(Roles.INSTRUCTEUR)
                .antMatchers(HttpMethod.POST, DossiersController.ROOT_URI + "/*" + DossiersController.PRENDRE_DECISION_URI)
                .hasRole(Roles.MAIRIE)
                .antMatchers(HttpMethod.POST, DossiersController.ROOT_URI + "/*" + DossiersController.MESSAGES_URI)
                .hasAnyRole(Roles.INSTRUCTEUR, Roles.DEPOSANT)
                .antMatchers(HttpMethod.GET, FichiersController.ROOT_URI + "/**")
                .hasAnyRole(Roles.DEPOSANT, Roles.MAIRIE, Roles.INSTRUCTEUR)
                .anyRequest().denyAll();
    }
}