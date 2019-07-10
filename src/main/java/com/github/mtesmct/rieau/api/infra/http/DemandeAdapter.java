package com.github.mtesmct.rieau.api.infra.http;

import com.github.mtesmct.rieau.api.domain.entities.Demande;
import com.github.mtesmct.rieau.api.infra.date.DateConverter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class DemandeAdapter {
    @Autowired
    @Qualifier("dateTimeConverter")
    private DateConverter dateTimeConverter;
    public JsonDemande toJson(Demande demande){
        return JsonDemande.builder().id(demande.getId()).type(demande.getType()).etat(demande.getEtat()).date(this.dateTimeConverter.format(demande.getDate())).build();
    }
}