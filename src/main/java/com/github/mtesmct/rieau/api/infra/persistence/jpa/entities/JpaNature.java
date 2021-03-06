package com.github.mtesmct.rieau.api.infra.persistence.jpa.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Embeddable
@Getter
@Setter
public class JpaNature {
    @Column(nullable = false)
    @NotNull
    private boolean constructionNouvelle;

    public JpaNature(@NotNull boolean constructionNouvelle) {
        this.constructionNouvelle = constructionNouvelle;
    }

    public JpaNature() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
            JpaNature jpaNature = (JpaNature) o;
        return Objects.equals(this.constructionNouvelle, jpaNature.constructionNouvelle);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.constructionNouvelle);
    }

    @Override
    public String toString() {
        return "JpaNature [constructionNouvelle=" + constructionNouvelle + "]";
    }
}