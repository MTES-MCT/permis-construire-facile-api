package com.github.mtesmct.rieau.api.infra.application.dossiers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Optional;

import com.github.mtesmct.rieau.api.application.auth.AuthRequiredException;
import com.github.mtesmct.rieau.api.application.auth.UserForbiddenException;
import com.github.mtesmct.rieau.api.application.auth.UserInfoServiceException;
import com.github.mtesmct.rieau.api.application.dossiers.DossierImportException;
import com.github.mtesmct.rieau.api.domain.entities.dossiers.CodePieceJointe;
import com.github.mtesmct.rieau.api.domain.entities.dossiers.Dossier;
import com.github.mtesmct.rieau.api.domain.entities.dossiers.EnumStatuts;
import com.github.mtesmct.rieau.api.domain.entities.dossiers.EnumTypes;
import com.github.mtesmct.rieau.api.domain.entities.dossiers.PieceNonAJoindreException;
import com.github.mtesmct.rieau.api.domain.entities.dossiers.StatutForbiddenException;
import com.github.mtesmct.rieau.api.domain.entities.dossiers.TypeDossierNotFoundException;
import com.github.mtesmct.rieau.api.domain.entities.dossiers.TypeStatutNotFoundException;
import com.github.mtesmct.rieau.api.domain.entities.personnes.User;
import com.github.mtesmct.rieau.api.domain.repositories.SaveDossierException;
import com.github.mtesmct.rieau.api.domain.services.DateService;
import com.github.mtesmct.rieau.api.infra.application.auth.WithDeposantBetaDetails;
import com.github.mtesmct.rieau.api.infra.application.auth.WithMairieBetaDetails;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@WithDeposantBetaDetails
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class TxImporterCerfaServiceTests {
        @Autowired
        private TxImporterCerfaService importerCerfaService;

        @Autowired
        private DateService dateService;
        @Autowired
        @Qualifier("deposantBeta")
        private User deposantBeta;

        @Test
        @WithDeposantBetaDetails
        public void executeDPTest()
                        throws IOException, DossierImportException, AuthRequiredException, UserForbiddenException,
                        UserInfoServiceException, StatutForbiddenException, TypeStatutNotFoundException,
                        PieceNonAJoindreException, TypeDossierNotFoundException, SaveDossierException {
        File file = new File("src/test/fixtures/cerfa_13703_DPMI.pdf");
        Optional<Dossier> dossier = this.importerCerfaService.execute(new FileInputStream(file), file.getName(),
                "application/pdf", file.length());
        assertTrue(dossier.isPresent());
        assertTrue(dossier.get().identity().toString().startsWith(
                EnumTypes.DPMI + "-" + dossier.get().projet().localisation().adresse().commune().departement() + "-"
                        + dossier.get().projet().localisation().adresse().commune().codePostal() + "-" + this.dateService.year() + "-"));
        assertEquals(EnumTypes.DPMI, dossier.get().type().type());
        assertTrue(dossier.get().statutActuel().isPresent());
        assertEquals(EnumStatuts.DEPOSE, dossier.get().statutActuel().get().type().identity());
        assertNotNull(dossier.get().piecesAJoindre());
        assertEquals(this.deposantBeta.identity().toString(), dossier.get().deposant().identity().toString());
        assertNotNull(dossier.get().cerfa());
        assertNotNull(dossier.get().cerfa().fichierId());
        assertEquals(new CodePieceJointe(EnumTypes.DPMI, "0"), dossier.get().cerfa().code());
        assertFalse(dossier.get().projet().nature().nouvelleConstruction());
        assertEquals("1", dossier.get().projet().localisation().adresse().numero());
        assertEquals("Route de Kerrivaud", dossier.get().projet().localisation().adresse().voie());
        assertEquals("", dossier.get().projet().localisation().adresse().lieuDit());
        assertEquals("44500", dossier.get().projet().localisation().adresse().commune().codePostal());
        assertEquals("", dossier.get().projet().localisation().adresse().bp());
        assertFalse(dossier.get().projet().localisation().lotissement());
        assertEquals(1, dossier.get().projet().localisation().parcellesCadastrales().size());
        assertEquals("000-CT-0099", dossier.get().projet().localisation().parcellesCadastrales().get(0).toFlatString());
    }

    @Test
    @WithDeposantBetaDetails
    public void executePCMITest() throws IOException, DossierImportException, AuthRequiredException,
            UserForbiddenException, UserInfoServiceException, StatutForbiddenException,
                        TypeStatutNotFoundException, PieceNonAJoindreException, TypeDossierNotFoundException,
                        SaveDossierException {
        File file = new File("src/test/fixtures/cerfa_13406_PCMI.pdf");
        Optional<Dossier> dossier = this.importerCerfaService.execute(new FileInputStream(file), file.getName(),
                "application/pdf", file.length());
        assertTrue(dossier.isPresent());
        assertTrue(dossier.get().identity().toString().startsWith(
                EnumTypes.PCMI + "-" + dossier.get().projet().localisation().adresse().commune().departement() + "-"
                        + dossier.get().projet().localisation().adresse().commune().codePostal() + "-" + this.dateService.year() + "-"));
        assertEquals(EnumTypes.PCMI, dossier.get().type().type());
        assertTrue(dossier.get().statutActuel().isPresent());
        assertEquals(EnumStatuts.DEPOSE, dossier.get().statutActuel().get().type().identity());
        assertNotNull(dossier.get().piecesAJoindre());
        assertEquals(this.deposantBeta.identity().toString(), dossier.get().deposant().identity().toString());
        assertNotNull(dossier.get().cerfa());
        assertNotNull(dossier.get().cerfa().fichierId());
        assertEquals(new CodePieceJointe(EnumTypes.PCMI, "0"), dossier.get().cerfa().code());
        assertTrue(dossier.get().projet().nature().nouvelleConstruction());
        assertEquals("1", dossier.get().projet().localisation().adresse().numero());
        assertEquals("Rue des Dervallières", dossier.get().projet().localisation().adresse().voie());
        assertEquals("", dossier.get().projet().localisation().adresse().lieuDit());
        assertEquals("44100", dossier.get().projet().localisation().adresse().commune().codePostal());
        assertEquals("", dossier.get().projet().localisation().adresse().bp());
        assertTrue(dossier.get().projet().localisation().lotissement());
        assertEquals(1, dossier.get().projet().localisation().parcellesCadastrales().size());
        assertEquals("000-LV-0040", dossier.get().projet().localisation().parcellesCadastrales().get(0).toFlatString());
    }

    @Test
    @WithMairieBetaDetails
    public void executeMairieInterditTest() throws IOException, DossierImportException, AuthRequiredException,
            UserForbiddenException, UserInfoServiceException {
        File file = new File("src/test/fixtures/dummy.pdf");
        assertThrows(UserForbiddenException.class, () -> this.importerCerfaService.execute(new FileInputStream(file), file.getName(),
                "application/pdf", file.length()));

    }
}