package com.github.mtesmct.rieau.api.infra.application.dossiers;

import java.util.Optional;

import com.github.mtesmct.rieau.api.application.dossiers.ApplicationImporterCerfaService;
import com.github.mtesmct.rieau.api.application.dossiers.DossierImportException;
import com.github.mtesmct.rieau.api.application.dossiers.ImporterCerfaService;
import com.github.mtesmct.rieau.api.domain.entities.dossiers.Dossier;
import com.github.mtesmct.rieau.api.domain.entities.dossiers.Fichier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SpringImporterCerfaService implements ImporterCerfaService {

    @Autowired
    private ApplicationImporterCerfaService applicationImporterCerfaService;

    @Override
    public Optional<Dossier> execute(Fichier fichier) throws DossierImportException {
        return this.applicationImporterCerfaService.execute(fichier);
    }


    
}