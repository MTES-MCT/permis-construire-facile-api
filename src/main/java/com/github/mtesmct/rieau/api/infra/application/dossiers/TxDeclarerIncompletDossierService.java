package com.github.mtesmct.rieau.api.infra.application.dossiers;

import com.github.mtesmct.rieau.api.application.auth.AuthRequiredException;
import com.github.mtesmct.rieau.api.application.auth.UserForbiddenException;
import com.github.mtesmct.rieau.api.application.auth.UserInfoServiceException;
import com.github.mtesmct.rieau.api.application.dossiers.AppDeclarerIncompletDossierService;
import com.github.mtesmct.rieau.api.application.dossiers.DeclarerIncompletDossierService;
import com.github.mtesmct.rieau.api.application.dossiers.DossierNotFoundException;
import com.github.mtesmct.rieau.api.domain.entities.dossiers.*;
import com.github.mtesmct.rieau.api.domain.repositories.SaveDossierException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class TxDeclarerIncompletDossierService implements DeclarerIncompletDossierService {

    @Autowired
    private AppDeclarerIncompletDossierService applicationIncompletDossierService;

    @Override
    public Optional<Dossier> execute(DossierId id, String message) throws DossierNotFoundException,
            InstructeurForbiddenException, AuthRequiredException, UserForbiddenException, UserInfoServiceException,
            TypeStatutNotFoundException, StatutForbiddenException, SaveDossierException {
        return this.applicationIncompletDossierService.execute(id, message);
    }
}