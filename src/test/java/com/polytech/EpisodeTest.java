package com.polytech;

import com.polytech.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class EpisodeTest {

    private Episode episode;
    private Joueur owner;
    private MeneurDeJeu mj;
    private Utilisateur visitor;

    @BeforeEach
    void setUp() {
        episode = new Episode("Test Date");
        owner = new Joueur("owner", "owner@test.com");
        mj = new MeneurDeJeu("mj", "mj@test.com");
        visitor = new Utilisateur("visitor", "visitor@test.com");
    }

    @Test
    void testEpisodeStartsAsDraft() {
        assertEquals(Episode.Status.DRAFT, episode.getStatut());
        assertFalse(episode.isValidatedByPlayer());
        assertFalse(episode.isValidatedByMJ());
    }

    @Test
    void testValidateByOwnerSetsPlayerFlag() {
        episode.validate(owner, owner, mj);
        assertTrue(episode.isValidatedByPlayer());
        assertFalse(episode.isValidatedByMJ());
        assertEquals(Episode.Status.DRAFT, episode.getStatut()); // Not yet validated
    }

    @Test
    void testValidateByMJSetsMJFlag() {
        episode.validate(mj, owner, mj);
        assertFalse(episode.isValidatedByPlayer());
        assertTrue(episode.isValidatedByMJ());
        assertEquals(Episode.Status.DRAFT, episode.getStatut()); // Not yet validated
    }

    @Test
    void testStatusDoesNotChangeToValidatedIfOnlyPlayerValidates() {
        episode.validate(owner, owner, mj);
        assertEquals(Episode.Status.DRAFT, episode.getStatut());
    }

    @Test
    void testStatusChangesToValidatedOnlyWhenBothValidate() {
        MeneurDeJeu differentMJ = new MeneurDeJeu("differentMJ", "differentMJ@test.com");
        episode.validate(owner, owner, differentMJ); // Player validates
        episode.validate(differentMJ, owner, differentMJ);   // MJ validates
        assertEquals(Episode.Status.VALIDATED, episode.getStatut());
    }

    @Test
    void testStatusChangesToValidatedWhenOwnerIsMJAndValidatesOnce() {
        MeneurDeJeu ownerAsMJ = new MeneurDeJeu(owner.getPseudo(), owner.getEmail()); // Same pseudo as owner
        episode.validate(owner, owner, ownerAsMJ); // Owner validates as player, then MJ validates
        assertEquals(Episode.Status.VALIDATED, episode.getStatut());
    }

    @Test
    void testCannotValidateAlreadyValidatedEpisode() {
        episode.validate(owner, owner, mj);
        episode.validate(mj, owner, mj);
        assertEquals(Episode.Status.VALIDATED, episode.getStatut());

        assertThrows(IllegalStateException.class, () -> {
            episode.validate(owner, owner, mj);
        });
    }

    @Test
    void testIllegalStateExceptionWhenTryingToEditValidatedEpisode() {
        episode.validate(owner, owner, mj);
        episode.validate(mj, owner, mj);
        assertEquals(Episode.Status.VALIDATED, episode.getStatut());

        assertThrows(IllegalStateException.class, () -> {
            episode.setDateRelative("New Date");
        });

        assertThrows(IllegalStateException.class, () -> {
            episode.setParagraphesSecrets(null);
        });
    }

    @Test
    void testValidateWithInvalidActorThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            episode.validate(visitor, owner, mj);
        });
    }
}
