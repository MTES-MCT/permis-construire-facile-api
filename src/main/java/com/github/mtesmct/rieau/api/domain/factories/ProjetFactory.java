package com.github.mtesmct.rieau.api.domain.factories;

import com.github.mtesmct.rieau.api.domain.entities.Factory;
import com.github.mtesmct.rieau.api.domain.entities.dossiers.*;
import com.github.mtesmct.rieau.api.domain.services.CommuneNotFoundException;
import com.github.mtesmct.rieau.api.domain.services.CommuneService;

import java.util.Optional;

@Factory
public class ProjetFactory {

    private CommuneService communeService;

    public ProjetFactory(CommuneService communeService){
        if (communeService == null)
            throw new NullPointerException("Le service des communes ne peut pas être nul.");
        this.communeService = communeService;
    }
    public Projet creer(String numero, String voie, String lieuDit, String codePostal, String bp, String cedex, ParcelleCadastrale parcelleCadastrale, boolean nouvelleConstruction, boolean lotissement) throws CommuneNotFoundException {
        Optional<Commune> commune = this.communeService.findByCodeCodePostal(codePostal);
        if (commune.isEmpty())
            throw new CommuneNotFoundException(codePostal);
        return new Projet(new Localisation(new Adresse(numero, voie, lieuDit, commune.get(), bp, cedex), parcelleCadastrale, lotissement), new Nature(nouvelleConstruction));
    }
}