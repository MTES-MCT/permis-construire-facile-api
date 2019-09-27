package com.github.mtesmct.rieau.api.application.dossiers;

public class FichierNotFoundException extends Exception {
    private static final long serialVersionUID = 1L;

    public FichierNotFoundException(String id) {
        super("Le fichier avec l'id {" + id + "} est introuvable");
    }

    public FichierNotFoundException(String id, Throwable cause) {
        super("Le fichier avec l'id {" + id + "} est introuvable", cause);
    }
}