package com.github.mtesmct.rieau.api.infra.http.dossiers;

import com.github.mtesmct.rieau.api.domain.entities.dossiers.Statut;
import com.github.mtesmct.rieau.api.domain.entities.dossiers.TypeStatut;
import com.github.mtesmct.rieau.api.infra.date.DateConverter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class JsonStatutFactory {
    @Autowired
    @Qualifier("dateTimeConverter")
    private DateConverter dateTimeConverter;

    public JsonStatut toJson(Statut statut) {
        JsonStatut jsonStatut = null;
        if (statut != null) {
            jsonStatut = new JsonStatut(statut.type().identity().toString(), statut.type().ordre(),statut.type().libelle(), statut.type().joursDelais(), this.dateTimeConverter.format(statut.dateDebut()));
        }
        return jsonStatut;
    }
    public JsonStatut toJson(TypeStatut type) {
        JsonStatut jsonStatut = null;
        if (type != null) {
            jsonStatut = new JsonStatut(type.identity().toString(), type.ordre(),type.libelle(), type.joursDelais(), "");
        }
        return jsonStatut;
    }
}