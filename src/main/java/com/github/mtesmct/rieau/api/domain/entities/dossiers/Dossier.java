package com.github.mtesmct.rieau.api.domain.entities.dossiers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import com.github.mtesmct.rieau.api.domain.entities.Entity;
import com.github.mtesmct.rieau.api.domain.entities.personnes.Personne;

public class Dossier implements Entity<Dossier, DossierId> {
    private DossierId id;
    public StatutDossier statut;
    public TypeDossier type;
    private Date dateDepot;
    private Personne deposant;
    private Petitionnaires petitionnaires;
    private Projet projet;
    private PieceJointe cerfa;
    private List<PieceJointe> piecesJointes;
    private PiecesAJoindre piecesAJoindre;

    public Date dateDepot() {
        return this.dateDepot;
    }

    public Projet projet() {
        return this.projet;
    }

    public StatutDossier statut() {
        return this.statut;
    }

    public Petitionnaires petitionnaires() {
        return this.petitionnaires;
    }

    public Personne deposant() {
        return this.deposant;
    }

    public PieceJointe cerfa() {
        return this.cerfa;
    }

    public TypeDossier type() {
        return this.type;
    }

    public List<PieceJointe> pieceJointes() {
        return this.piecesJointes;
    }

    public PieceJointe ajouterCerfa(FichierId fichierId) throws PieceNonAJoindreException {
        PieceJointe pieceJointe = new PieceJointe(this, new CodePieceJointe(this.type.type(), "0"), fichierId);
        this.cerfa = pieceJointe;
        return pieceJointe;
    }

    public Optional<PieceJointe> ajouter(String numero, FichierId fichierId) throws AjouterPieceJointeException {
        Optional<PieceJointe> pieceJointe = Optional.empty();
        try {
            if (this.type == null)
                throw new AjouterPieceJointeException(new NullPointerException("Le type du dossier est nul"));
            if (numero.equals("0"))
                throw new AjouterPieceJointeException(new NumeroPieceJointeException());
            pieceJointe = Optional
                    .ofNullable(new PieceJointe(this, new CodePieceJointe(this.type.type(), numero), fichierId));
            if (!pieceJointe.orElseThrow().isAJoindre())
                throw new AjouterPieceJointeException(new PieceNonAJoindreException(pieceJointe.orElseThrow().code()));
            this.piecesJointes.add(pieceJointe.orElseThrow());
        } catch (IllegalArgumentException | NullPointerException | UnsupportedOperationException | ClassCastException
                | NoSuchElementException e) {
            throw new AjouterPieceJointeException("Ajout de la pièce jointe impossible", e);
        }
        return pieceJointe;
    }

    public PiecesAJoindre piecesAJoindre() {
        return this.piecesAJoindre;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean equals(final Object object) {
        if (this == object)
            return true;
        if (object == null || getClass() != object.getClass())
            return false;
        final Dossier other = (Dossier) object;
        return this.hasSameIdentityAs(other);
    }

    @Override
    public String toString() {
        return "Dossier={ id={" + this.id.toString() + "}, deposant={" + this.deposant.toString() + "}, statut={"
                + this.statut.toString() + "}, dateDepot={" + this.dateDepot.toString() + "}, type={"
                + this.type.toString() + "} }";
    }

    @Override
    public DossierId identity() {
        return this.id;
    }

    @Override
    public boolean hasSameIdentityAs(Dossier other) {
        return other != null && this.id.hasSameValuesAs(other.id);
    }

    public Dossier(DossierId id, Personne deposant, StatutDossier statut, Date dateDepot, TypeDossier type) {
        if (id == null)
            throw new NullPointerException("L'id du dépôt ne peut pas être nul");
        this.id = id;
        if (deposant == null)
            throw new NullPointerException("Le deposant ne peut pas être nul");
        this.deposant = deposant;
        if (statut == null)
            throw new NullPointerException("Le statut du dossier ne peut pas être nul");
        this.statut = statut;
        if (dateDepot == null)
            throw new NullPointerException("La date du dépôt ne peut pas être nulle");
        this.dateDepot = dateDepot;
        if (type == null)
            throw new NullPointerException("Le type du dossier ne peut pas être nul");
        this.type = type;
        this.piecesAJoindre = new PiecesAJoindre(this);
        this.piecesJointes = new ArrayList<PieceJointe>();
    }

}