package com.polytech;

import com.polytech.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class BiographieTest {

    private Biographie biographie;
    private Joueur owner;
    private MeneurDeJeu mj;
    private Utilisateur visitor;
    private Episode episode;

    @BeforeEach
    void setUp() {
        biographie = new Biographie();
        owner = new Joueur("owner", "owner@test.com");
        mj = new MeneurDeJeu("mj", "mj@test.com");
        visitor = new Utilisateur("visitor", "visitor@test.com");

        // Create an episode with some paragraphs
        episode = new Episode("Test Date");
        ParagrapheSecret publicPara = new ParagrapheSecret("Public content", false);
        ParagrapheSecret secretPara = new ParagrapheSecret("Secret content", true);
        episode.getParagraphesSecrets().add(publicPara);
        episode.getParagraphesSecrets().add(secretPara);

        biographie.ajouterEpisode(episode);
    }

    @Test
    void testVisitorReceivesFilteredListSecretsRemoved() {
        List<ParagrapheSecret> visibleParagraphs = biographie.getVisibleParagraphs(visitor, owner, mj);

        // Should only contain the public paragraph
        assertEquals(1, visibleParagraphs.size());
        assertEquals("Public content", visibleParagraphs.get(0).getTexte());
        assertFalse(visibleParagraphs.get(0).isSecret());
    }

    @Test
    void testMJReceivesFullList() {
        List<ParagrapheSecret> visibleParagraphs = biographie.getVisibleParagraphs(mj, owner, mj);

        // Should contain both public and secret paragraphs
        assertEquals(2, visibleParagraphs.size());

        // Check that both types are present
        boolean hasPublic = visibleParagraphs.stream().anyMatch(p -> !p.isSecret());
        boolean hasSecret = visibleParagraphs.stream().anyMatch(p -> p.isSecret());

        assertTrue(hasPublic, "Should contain public paragraph");
        assertTrue(hasSecret, "Should contain secret paragraph");
    }

    @Test
    void testOwnerReceivesFullList() {
        List<ParagrapheSecret> visibleParagraphs = biographie.getVisibleParagraphs(owner, owner, mj);

        // Should contain both public and secret paragraphs
        assertEquals(2, visibleParagraphs.size());

        // Check that both types are present
        boolean hasPublic = visibleParagraphs.stream().anyMatch(p -> !p.isSecret());
        boolean hasSecret = visibleParagraphs.stream().anyMatch(p -> p.isSecret());

        assertTrue(hasPublic, "Should contain public paragraph");
        assertTrue(hasSecret, "Should contain secret paragraph");
    }

    @Test
    void testEmptyBiographieReturnsEmptyList() {
        Biographie emptyBio = new Biographie();
        List<ParagrapheSecret> visibleParagraphs = emptyBio.getVisibleParagraphs(visitor, owner, mj);
        assertTrue(visibleParagraphs.isEmpty());
    }
}
