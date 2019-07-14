package com.github.mtesmct.rieau.api.depositaire.domain.repositories;

import java.util.List;
import java.util.Optional;

import com.github.mtesmct.rieau.api.depositaire.domain.entities.Demande;

public interface DemandeRepository {
    public List<Demande> findAll();
    public Optional<Demande> findById(String id);
    public Demande save(Demande demande);
}