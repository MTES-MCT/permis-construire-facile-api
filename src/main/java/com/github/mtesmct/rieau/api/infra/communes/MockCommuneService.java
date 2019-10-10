package com.github.mtesmct.rieau.api.infra.communes;

import java.util.Optional;

import com.github.mtesmct.rieau.api.domain.entities.dossiers.Commune;
import com.github.mtesmct.rieau.api.domain.services.CommuneService;

import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnExpression("T(org.springframework.util.StringUtils).isEmpty('${app.communes-url:}')")
public class MockCommuneService implements CommuneService {

    @Override
    public Optional<Commune> findByCodeCodePostal(String codePostal) {
        return Optional.ofNullable(new Commune(codePostal, codePostal, codePostal.substring(0, 1)));
    }

}