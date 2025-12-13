package com.polytech;

import com.polytech.controller.PartieController;
import com.polytech.model.*;
import com.polytech.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class PartieTest {

    private PartieController partieController;
    private UtilisateurRepository userRepo;
    private UniversRepository universRepo;
    private PersonnageRepository personnageRepo;

    private MeneurDeJeu mj1, mj2;
    private Joueur joueur1, joueur2;
    private Univers univers1, univers2;
    private Personnage perso1, perso2, perso3, perso4;

    @BeforeEach
    void setUp() {
        userRepo = UtilisateurRepository.getInstance();
        universRepo = UniversRepository.getInstance();
        personnageRepo = PersonnageRepository.getInstance();

        userRepo.getUtilisateurs().clear();
        universRepo.getUnivers().clear();
        personnageRepo.getPersonnages().clear();
        PartieRepository.getInstance().getParties().clear();

        mj1 = new MeneurDeJeu("mj1", "mj1@test.com");
        mj2 = new MeneurDeJeu("mj2", "mj2@test.com");
        joueur1 = new Joueur("joueur1", "joueur1@test.com");
        joueur2 = new Joueur("joueur2", "joueur2@test.com");

        userRepo.ajouterUtilisateur(mj1);
        userRepo.ajouterUtilisateur(mj2);
        userRepo.ajouterUtilisateur(joueur1);
        userRepo.ajouterUtilisateur(joueur2);

        univers1 = new Univers("Univers1");
        univers2 = new Univers("Univers2");
        universRepo.ajouterUnivers(univers1);
        universRepo.ajouterUnivers(univers2);

        perso1 = new Personnage("Perso1", "01/01/2000", "Guerrier", "portrait1", univers1, joueur1);
        perso2 = new Personnage("Perso2", "02/02/2000", "Mage", "portrait2", univers1, joueur2);
        perso3 = new Personnage("Perso3", "03/03/2000", "Voleur", "portrait3", univers2, joueur1);
        perso4 = new Personnage("Perso4", "04/04/2000", "Archer", "portrait4", univers1, joueur2);

        perso1.setMeneurDeJeu(mj1);
        perso2.setMeneurDeJeu(mj1);
        perso3.setMeneurDeJeu(mj2);

        personnageRepo.ajouterPersonnage(perso1);
        personnageRepo.ajouterPersonnage(perso2);
        personnageRepo.ajouterPersonnage(perso3);
        personnageRepo.ajouterPersonnage(perso4);

        partieController = new PartieController();
    }

    @Test
    void testProposerPartieSuccess() {
        Partie partie = partieController.proposerPartie("Test Party", "Initial summary", "15/12/2025", "Paris", "Univers1", "mj1");

        assertNotNull(partie);
        assertEquals("Test Party", partie.getTitre());
        assertEquals("Initial summary", partie.getResumeInitial());
        assertEquals("15/12/2025", partie.getDate());
        assertEquals("Paris", partie.getLieu());
        assertEquals(univers1, partie.getUnivers());
        assertEquals(mj1, partie.getMeneurDeJeu());
        assertEquals(Partie.Status.PROPOSITION, partie.getStatut());
        assertTrue(partie.getParticipants().isEmpty());
    }

    @Test
    void testProposerPartieUniversNotFound() {
        assertThrows(IllegalArgumentException.class, () ->
            partieController.proposerPartie("Test Party", "Summary", "15/12/2025", "Paris", "NonExistent", "mj1"));
    }

    @Test
    void testProposerPartieMJNotFound() {
        assertThrows(IllegalArgumentException.class, () ->
            partieController.proposerPartie("Test Party", "Summary", "15/12/2025", "Paris", "Univers1", "nonexistent"));
    }

    @Test
    void testAjouterParticipantSuccess() {
        Partie partie = partieController.proposerPartie("Test Party", "Summary", "15/12/2025", "Paris", "Univers1", "mj1");

        partieController.ajouterParticipant("Test Party", "Perso1", "mj1");

        assertEquals(1, partie.getParticipants().size());
        assertTrue(partie.getParticipants().contains(perso1));
    }

    @Test
    void testAjouterParticipantWrongUniverse() {
        Partie partie = partieController.proposerPartie("Test Party", "Summary", "15/12/2025", "Paris", "Univers1", "mj1");

        assertThrows(IllegalArgumentException.class, () ->
            partieController.ajouterParticipant("Test Party", "Perso3", "mj1"));
    }

    @Test
    void testAjouterParticipantNotAssignedToMJ() {
        Partie partie = partieController.proposerPartie("Test Party", "Summary", "15/12/2025", "Paris", "Univers1", "mj1");

        assertThrows(IllegalArgumentException.class, () ->
            partieController.ajouterParticipant("Test Party", "Perso4", "mj1"));
    }

    @Test
    void testAjouterParticipantUnauthorizedMJ() {
        Partie partie = partieController.proposerPartie("Test Party", "Summary", "15/12/2025", "Paris", "Univers1", "mj1");

        assertThrows(IllegalArgumentException.class, () ->
            partieController.ajouterParticipant("Test Party", "Perso1", "mj2"));
    }

    @Test
    void testAjouterParticipantAlreadyAdded() {
        Partie partie = partieController.proposerPartie("Test Party", "Summary", "15/12/2025", "Paris", "Univers1", "mj1");
        partieController.ajouterParticipant("Test Party", "Perso1", "mj1");

        assertThrows(IllegalArgumentException.class, () ->
            partieController.ajouterParticipant("Test Party", "Perso1", "mj1"));
    }

    @Test
    void testAjouterParticipantFinishedParty() {
        Partie partie = partieController.proposerPartie("Test Party", "Summary", "15/12/2025", "Paris", "Univers1", "mj1");
        partieController.ajouterParticipant("Test Party", "Perso1", "mj1");
        partieController.finirPartie("Test Party", "Great adventure!", "mj1");

        assertThrows(IllegalArgumentException.class, () ->
            partieController.ajouterParticipant("Test Party", "Perso2", "mj1"));
    }

    @Test
    void testRetirerParticipantSuccess() {
        Partie partie = partieController.proposerPartie("Test Party", "Summary", "15/12/2025", "Paris", "Univers1", "mj1");
        partieController.ajouterParticipant("Test Party", "Perso1", "mj1");

        partieController.retirerParticipant("Test Party", "Perso1", "mj1");

        assertTrue(partie.getParticipants().isEmpty());
    }

    @Test
    void testRetirerParticipantFinishedParty() {
        Partie partie = partieController.proposerPartie("Test Party", "Summary", "15/12/2025", "Paris", "Univers1", "mj1");
        partieController.ajouterParticipant("Test Party", "Perso1", "mj1");
        partieController.finirPartie("Test Party", "Great adventure!", "mj1");

        assertThrows(IllegalArgumentException.class, () ->
            partieController.retirerParticipant("Test Party", "Perso1", "mj1"));
    }

    @Test
    void testFinirPartieSuccess() {
        Partie partie = partieController.proposerPartie("Test Party", "Summary", "15/12/2025", "Paris", "Univers1", "mj1");
        partieController.ajouterParticipant("Test Party", "Perso1", "mj1");
        partieController.ajouterParticipant("Test Party", "Perso2", "mj1");

        partieController.finirPartie("Test Party", "Epic adventure with dragons!", "mj1");

        assertEquals(Partie.Status.AVENTURE, partie.getStatut());
        assertEquals("Epic adventure with dragons!", partie.getResumeEvenements());

        assertEquals(1, perso1.getBiographie().getEpisodes().size());
        assertEquals(1, perso2.getBiographie().getEpisodes().size());

        Episode adventure1 = perso1.getBiographie().getEpisodes().get(0);
        Episode adventure2 = perso2.getBiographie().getEpisodes().get(0);

        assertEquals("15/12/2025", adventure1.getDateRelative());
        assertEquals("15/12/2025", adventure2.getDateRelative());
        assertEquals("Epic adventure with dragons!", adventure1.getParagraphesSecrets().get(0).getTexte());
        assertEquals("Epic adventure with dragons!", adventure2.getParagraphesSecrets().get(0).getTexte());
    }

    @Test
    void testFinirPartieAlreadyFinished() {
        Partie partie = partieController.proposerPartie("Test Party", "Summary", "15/12/2025", "Paris", "Univers1", "mj1");
        partieController.finirPartie("Test Party", "Adventure!", "mj1");

        assertThrows(IllegalArgumentException.class, () ->
            partieController.finirPartie("Test Party", "Another summary", "mj1"));
    }

    @Test
    void testSupprimerPropositionSuccess() {
        Partie partie = partieController.proposerPartie("Test Party", "Summary", "15/12/2025", "Paris", "Univers1", "mj1");

        partieController.supprimerProposition("Test Party", "mj1");

        assertNull(partieController.findByTitre("Test Party"));
    }

    @Test
    void testSupprimerPropositionFinishedParty() {
        Partie partie = partieController.proposerPartie("Test Party", "Summary", "15/12/2025", "Paris", "Univers1", "mj1");
        partieController.finirPartie("Test Party", "Adventure!", "mj1");

        assertThrows(IllegalArgumentException.class, () ->
            partieController.supprimerProposition("Test Party", "mj1"));
    }

    @Test
    void testGetPersonnagesDisponibles() {
        Partie partie = partieController.proposerPartie("Test Party", "Summary", "15/12/2025", "Paris", "Univers1", "mj1");

        List<Personnage> disponibles = partieController.getPersonnagesDisponibles("Test Party", "mj1");

        assertEquals(2, disponibles.size());
        assertTrue(disponibles.contains(perso1));
        assertTrue(disponibles.contains(perso2));
        assertFalse(disponibles.contains(perso3));
        assertFalse(disponibles.contains(perso4));

        partieController.ajouterParticipant("Test Party", "Perso1", "mj1");

        disponibles = partieController.getPersonnagesDisponibles("Test Party", "mj1");
        assertEquals(1, disponibles.size());
        assertTrue(disponibles.contains(perso2));
    }

    @Test
    void testGetParties() {
        partieController.proposerPartie("Party1", "Summary1", "15/12/2025", "Paris", "Univers1", "mj1");
        partieController.proposerPartie("Party2", "Summary2", "16/12/2025", "Lyon", "Univers2", "mj2");

        List<Partie> parties = partieController.getParties();
        assertEquals(2, parties.size());
    }

    @Test
    void testGetPartiesByMeneur() {
        partieController.proposerPartie("Party1", "Summary1", "15/12/2025", "Paris", "Univers1", "mj1");
        partieController.proposerPartie("Party2", "Summary2", "16/12/2025", "Lyon", "Univers1", "mj1");
        partieController.proposerPartie("Party3", "Summary3", "17/12/2025", "Marseille", "Univers2", "mj2");

        List<Partie> partiesMj1 = partieController.getPartiesByMeneur("mj1");
        List<Partie> partiesMj2 = partieController.getPartiesByMeneur("mj2");

        assertEquals(2, partiesMj1.size());
        assertEquals(1, partiesMj2.size());
    }

    @Test
    void testFindByTitre() {
        partieController.proposerPartie("Unique Party", "Summary", "15/12/2025", "Paris", "Univers1", "mj1");

        Partie found = partieController.findByTitre("Unique Party");
        assertNotNull(found);
        assertEquals("Unique Party", found.getTitre());

        Partie notFound = partieController.findByTitre("NonExistent");
        assertNull(notFound);
    }
}
