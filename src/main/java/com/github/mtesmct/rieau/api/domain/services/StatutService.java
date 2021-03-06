package com.github.mtesmct.rieau.api.domain.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.github.mtesmct.rieau.api.domain.entities.dossiers.Dossier;
import com.github.mtesmct.rieau.api.domain.entities.dossiers.EnumStatuts;
import com.github.mtesmct.rieau.api.domain.entities.dossiers.Message;
import com.github.mtesmct.rieau.api.domain.entities.dossiers.Statut;
import com.github.mtesmct.rieau.api.domain.entities.dossiers.StatutForbiddenException;
import com.github.mtesmct.rieau.api.domain.entities.dossiers.TypeStatut;
import com.github.mtesmct.rieau.api.domain.entities.dossiers.TypeStatutNotFoundException;
import com.github.mtesmct.rieau.api.domain.entities.personnes.User;
import com.github.mtesmct.rieau.api.domain.repositories.TypeStatutDossierRepository;


@DomainService
public class StatutService {
    private TypeStatutDossierRepository statutDossierRepository;
    private DateService dateService;

    public StatutService(TypeStatutDossierRepository statutDossierRepository, DateService dateService) {
        if (statutDossierRepository == null)
            throw new NullPointerException("Le repository des statuts de dossier ne peut pas être nul.");
        this.statutDossierRepository = statutDossierRepository;
        if (dateService == null)
            throw new NullPointerException("Le service des dates ne peut pas être nul.");
        this.dateService = dateService;
    }

    private TypeStatut type(EnumStatuts id) throws TypeStatutNotFoundException {
        Optional<TypeStatut> typeStatut = this.statutDossierRepository.findById(id);
        if (typeStatut.isEmpty())
            throw new TypeStatutNotFoundException(id);
        return typeStatut.get();
    }

    public void deposer(Dossier dossier) throws StatutForbiddenException, TypeStatutNotFoundException {
        dossier.ajouterStatut(this.dateService.now(), type(EnumStatuts.DEPOSE));
    }

    public void qualifier(Dossier dossier) throws StatutForbiddenException, TypeStatutNotFoundException {
        dossier.ajouterStatut(this.dateService.now(), type(EnumStatuts.QUALIFIE));
    }

    public void declarerIncomplet(Dossier dossier, User auteur, String contenu) throws StatutForbiddenException, TypeStatutNotFoundException {
        dossier.ajouterStatut(this.dateService.now(), type(EnumStatuts.INCOMPLET));
        Message message = new Message(auteur, this.dateService.now(), contenu);
        dossier.ajouterMessage(message);
    }

    public void declarerComplet(Dossier dossier) throws StatutForbiddenException, TypeStatutNotFoundException {
        dossier.ajouterStatut(this.dateService.now(), type(EnumStatuts.COMPLET));
    }

    public void prononcerDecision(Dossier dossier) throws StatutForbiddenException, TypeStatutNotFoundException {
        dossier.ajouterStatut(this.dateService.now(), type(EnumStatuts.DECISION));
    }

    public List<TypeStatut> statutsRestants(Dossier dossier){
        List<TypeStatut> types = new ArrayList<TypeStatut>();
        if (dossier.statutActuel().isPresent()){
            types = this.statutDossierRepository.findAllGreaterThan(dossier.statutActuel().get().type());            
        }
        return types;
    }

    public Integer joursRestants(Statut statut){
        Integer joursDepuisDateDebut = this.dateService.daysUntilNow(statut.dateDebut());
        return statut.type().joursDelais() - joursDepuisDateDebut;
    }
}