package com.github.mtesmct.rieau.api.infra.application.dossiers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Optional;

import com.github.mtesmct.rieau.api.domain.entities.dossiers.Dossier;
import com.github.mtesmct.rieau.api.domain.entities.dossiers.Fichier;
import com.github.mtesmct.rieau.api.domain.entities.dossiers.FichierId;
import com.github.mtesmct.rieau.api.domain.entities.dossiers.FichierIdService;
import com.github.mtesmct.rieau.api.domain.entities.dossiers.PieceJointe;
import com.github.mtesmct.rieau.api.domain.entities.dossiers.StatutDossier;
import com.github.mtesmct.rieau.api.domain.entities.dossiers.TypesDossier;
import com.github.mtesmct.rieau.api.domain.entities.personnes.Personne;
import com.github.mtesmct.rieau.api.domain.factories.DossierFactory;
import com.github.mtesmct.rieau.api.domain.repositories.DossierRepository;
import com.github.mtesmct.rieau.api.domain.services.FichierService;
import com.github.mtesmct.rieau.api.infra.application.auth.WithDeposantAndBetaDetails;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@WithDeposantAndBetaDetails
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class SpringAjouterPieceJointeServiceTests {
    @Autowired
    private SpringAjouterPieceJointeService ajouterPieceJointe;

    @Autowired
    @Qualifier("deposantBeta")
    private Personne deposantBeta;
    @Autowired
    private DossierFactory dossierFactory;
    @Autowired
    private DossierRepository dossierRepository;
    @Autowired
    private FichierService fichierService;
    @Autowired
    private FichierIdService fichierIdService;

    @Test
    @WithDeposantAndBetaDetails
    public void executeDP1Test() throws FileNotFoundException {
        Dossier dp = this.dossierFactory.creer(this.deposantBeta, TypesDossier.DP);
        dp = this.dossierRepository.save(dp);
        assertEquals(dp.statut(), StatutDossier.DEPOSE);
        Fichier fichier = new Fichier("dummy.pdf", "application/pdf", new FileInputStream(new File("src/test/fixtures/dummy.pdf")));
        FichierId fichierId = this.fichierIdService.creer();
        this.fichierService.save(fichierId, fichier);
        Optional<PieceJointe> pieceJointe = this.ajouterPieceJointe.execute(dp, "1", fichier);
        assertTrue(pieceJointe.isPresent());
        assertEquals(pieceJointe.get().code().type(), TypesDossier.DP);
    }

    @Test
    @WithDeposantAndBetaDetails
    public void executePCMI1Test() throws FileNotFoundException {
        Dossier pcmi = this.dossierFactory.creer(this.deposantBeta, TypesDossier.PCMI);
        pcmi = this.dossierRepository.save(pcmi);
        Optional<Dossier> optionalDossier = this.dossierRepository.findById(pcmi.identity().toString());
        assertTrue(optionalDossier.isPresent());
        pcmi = optionalDossier.get();
        assertEquals(pcmi.statut(), StatutDossier.DEPOSE);
        Fichier fichier = new Fichier("dummy.pdf", "application/pdf", new FileInputStream(new File("src/test/fixtures/dummy.pdf")));
        FichierId fichierId = this.fichierIdService.creer();
        this.fichierService.save(fichierId, fichier);
        Optional<PieceJointe> pieceJointe = this.ajouterPieceJointe.execute(pcmi, "1", fichier);
        assertTrue(pieceJointe.isPresent());
        assertEquals(pieceJointe.get().code().type(), TypesDossier.PCMI);
    }
}