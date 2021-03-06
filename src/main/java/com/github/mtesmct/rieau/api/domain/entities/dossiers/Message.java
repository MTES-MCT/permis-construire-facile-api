package com.github.mtesmct.rieau.api.domain.entities.dossiers;

import java.time.LocalDateTime;
import java.util.Objects;

import com.github.mtesmct.rieau.api.domain.entities.ValueObject;
import com.github.mtesmct.rieau.api.domain.entities.personnes.User;

public class Message implements ValueObject<Message> {

    private User auteur;
    private LocalDateTime date;
    private String contenu;

    public User auteur() {
        return this.auteur;
    }

    public LocalDateTime date() {
        return this.date;
    }

    public String contenu() {
        return this.contenu;
    }

    @Override
    public boolean hasSameValuesAs(Message other) {
        return other != null && Objects.equals(this.auteur, other.auteur) && Objects.equals(this.date, other.date)
                && Objects.equals(this.contenu, other.contenu);
    }

    public Message(User auteur, LocalDateTime date, String contenu) {
        if (auteur == null)
            throw new NullPointerException("L'auteur du message ne peut pas être nul.");
        this.auteur = auteur;
        if (date == null)
            throw new NullPointerException("La date du message ne peut pas être nulle.");
        this.date = date;
        if (contenu == null)
            throw new NullPointerException("Le contenu du message ne peut pas être nul.");
        this.contenu = contenu;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.auteur, this.date, this.contenu);
    }

    @Override
    public boolean equals(final Object object) {
        if (this == object)
            return true;
        if (object == null || getClass() != object.getClass())
            return false;
        final Message other = (Message) object;
        return this.hasSameValuesAs(other);
    }

    @Override
    public String toString() {
        return "Message={ auteur={" + Objects.toString(this.auteur) + "}, date={" + Objects.toString(this.date)
                + "}, contenu={" + Objects.toString(this.contenu) + "} }";
    }

}