package com.github.mtesmct.rieau.api.infra.application.dossiers;

import java.util.Optional;

import com.github.mtesmct.rieau.api.application.dossiers.AjouterPieceJointeService;
import com.github.mtesmct.rieau.api.application.dossiers.ApplicationAJouterPieceJointeService;
import com.github.mtesmct.rieau.api.domain.entities.dossiers.AjouterPieceJointeException;
import com.github.mtesmct.rieau.api.domain.entities.dossiers.Dossier;
import com.github.mtesmct.rieau.api.domain.entities.dossiers.Fichier;
import com.github.mtesmct.rieau.api.domain.entities.dossiers.PieceJointe;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SpringAjouterPieceJointeService implements AjouterPieceJointeService {

    @Autowired
    private ApplicationAJouterPieceJointeService applicationAjouterPieceJointeService;

    @Override
    public Optional<PieceJointe> execute(Dossier dossier, String numero, Fichier fichier)
            throws AjouterPieceJointeException {
        return this.applicationAjouterPieceJointeService.execute(dossier, numero, fichier);
    }

}