package com.github.mtesmct.rieau.api.infra.http;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Optional;

import com.github.mtesmct.rieau.api.domain.entities.dossiers.Dossier;
import com.github.mtesmct.rieau.api.domain.entities.dossiers.Fichier;
import com.github.mtesmct.rieau.api.domain.entities.dossiers.FichierId;
import com.github.mtesmct.rieau.api.domain.entities.dossiers.FichierIdService;
import com.github.mtesmct.rieau.api.domain.entities.dossiers.TypesDossier;
import com.github.mtesmct.rieau.api.domain.entities.personnes.Personne;
import com.github.mtesmct.rieau.api.domain.factories.DossierFactory;
import com.github.mtesmct.rieau.api.domain.repositories.DossierRepository;
import com.github.mtesmct.rieau.api.domain.services.FichierService;
import com.github.mtesmct.rieau.api.infra.application.auth.WithDeposantAndBetaDetails;
import com.github.mtesmct.rieau.api.infra.application.auth.WithInstructeurNonBetaDetails;
import com.github.mtesmct.rieau.api.infra.application.dossiers.SpringImporterCerfaService;
import com.github.mtesmct.rieau.api.infra.date.DateConverter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@WithDeposantAndBetaDetails
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class DossiersControllerTests {

	@Autowired
	private MockMvc mvc;
	@Autowired
	private DossierRepository dossierRepository;
	@Autowired
	@Qualifier("dateTimeConverter")
	private DateConverter dateConverter;
	@Autowired
	private DossierFactory dossierFactory;
	@Autowired
	private FichierService fichierService;
	@MockBean
	private SpringImporterCerfaService mockImporterCerfaService;
	@Autowired
	private FichierIdService fichierIdService;

	private Dossier dossier;

	private String uri;

	@Autowired
	@Qualifier("deposantBeta")
	private Personne deposantBeta;

	@BeforeEach
	public void setup() throws FileNotFoundException {
		this.uri = DossiersController.ROOT_URL;
        Fichier fichier = new Fichier("cerfa_13703_DPMI.pdf", "application/pdf", new FileInputStream(new File("src/test/fixtures/cerfa_13703_DPMI.pdf")));
        FichierId fichierId = this.fichierIdService.creer();
        this.fichierService.save(fichierId, fichier);
		this.dossier = this.dossierFactory.creer(this.deposantBeta, TypesDossier.DP);
        dossier.ajouterCerfa(fichierId);
        this.dossier = this.dossierRepository.save(this.dossier);
		assertNotNull(this.dossier.identity());
		assertNotNull(this.dossier.deposant());
		assertEquals(this.dossier.deposant().identity(), this.deposantBeta.identity());
	}

	@Test
	public void listerTest() throws Exception {
		this.mvc.perform(get(this.uri).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(jsonPath("$").isArray())
				.andExpect(jsonPath("$").isNotEmpty()).andExpect(jsonPath("$", hasSize(1)))
				.andExpect(jsonPath("$[0].id", equalTo(this.dossier.identity().toString())))
				.andExpect(jsonPath("$[0].type", equalTo(this.dossier.type().toString())))
				.andExpect(jsonPath("$[0].statut", equalTo(this.dossier.statut().toString())))
				.andExpect(jsonPath("$[0].date", equalTo(this.dateConverter.format((this.dossier.dateDepot())))));
	}

	@Test
	@WithMockUser
	public void listerInterditTest() throws Exception {
		this.mvc.perform(get(this.uri).accept(MediaType.APPLICATION_JSON)).andExpect(status().isForbidden());
	}

	@Test
	@WithAnonymousUser
	public void listerRedirigeConnexionTest() throws Exception {
		this.mvc.perform(get(this.uri).accept(MediaType.APPLICATION_JSON)).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/sso/login"));
	}
	
	@Test
	public void consulterTest() throws Exception {
		this.mvc.perform(get(this.uri+"/"+this.dossier.identity().toString()).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(jsonPath("$").isNotEmpty())
				.andExpect(jsonPath("$.id", equalTo(this.dossier.identity().toString())))
				.andExpect(jsonPath("$.type", equalTo(this.dossier.type().toString())))
				.andExpect(jsonPath("$.statut", equalTo(this.dossier.statut().toString())))
				.andExpect(jsonPath("$.date", equalTo(this.dateConverter.format(this.dossier.dateDepot()))));
	}

	@Test
	public void ajouterCerfaAutoriseTest() throws Exception {
		MockMultipartFile multipartFile = new MockMultipartFile("file", "test.pdf",
		"application/pdf", "Spring Framework".getBytes());
		Mockito.when(this.mockImporterCerfaService.execute(any())).thenReturn(Optional.ofNullable(this.dossier));
		this.mvc.perform(multipart(this.uri).file(multipartFile).with(csrf().asHeader())).andExpect(status().isOk());
	}

	@Test
	public void ajouterCerfaSansCsrfInterditTest() throws Exception {
		MockMultipartFile multipartFile = new MockMultipartFile("file", "test.pdf",
		"application/pdf", "Spring Framework".getBytes());
		this.mvc.perform(multipart(this.uri).file(multipartFile)).andExpect(status().isForbidden());
	}

	@Test
	@WithInstructeurNonBetaDetails
	public void ajouterCerfaInterditTest() throws Exception {
		MockMultipartFile multipartFile = new MockMultipartFile("file", "test.pdf",
		"application/pdf", "Spring Framework".getBytes());
		this.mvc.perform(multipart(this.uri).file(multipartFile).with(csrf().asHeader())).andExpect(status().isForbidden());
	}


}