package com.github.mtesmct.rieau.api.infra.application.dossiers;

import java.util.Optional;

import com.github.mtesmct.rieau.api.application.auth.AuthRequiredException;
import com.github.mtesmct.rieau.api.application.auth.UserForbiddenException;
import com.github.mtesmct.rieau.api.application.auth.UserInfoServiceException;
import com.github.mtesmct.rieau.api.application.dossiers.AppInstruireDossierService;
import com.github.mtesmct.rieau.api.application.dossiers.DossierNotFoundException;
import com.github.mtesmct.rieau.api.application.dossiers.InstruireDossierService;
import com.github.mtesmct.rieau.api.domain.entities.dossiers.Dossier;
import com.github.mtesmct.rieau.api.domain.entities.dossiers.DossierId;
import com.github.mtesmct.rieau.api.domain.entities.dossiers.InstructeurForbiddenException;
import com.github.mtesmct.rieau.api.domain.entities.dossiers.StatutForbiddenException;
import com.github.mtesmct.rieau.api.domain.entities.dossiers.TypeStatutNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TxInstruireDossierService implements InstruireDossierService {

    @Autowired
    private AppInstruireDossierService appInstruireDossierService;

    @Override
    public Optional<Dossier> execute(DossierId id)
            throws DossierNotFoundException, InstructeurForbiddenException, AuthRequiredException, UserForbiddenException,
            UserInfoServiceException, TypeStatutNotFoundException, StatutForbiddenException {
        return this.appInstruireDossierService.execute(id);
    }
}