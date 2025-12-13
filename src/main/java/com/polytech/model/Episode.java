package com.polytech.model;

import java.util.ArrayList;
import java.util.List;

public class Episode {
    private String dateRelative;
    private Status statut;
    private List<ParagrapheSecret> paragraphesSecrets = new ArrayList<>();
    private Partie partieLiee; // optional

    // Double validation flags
    private boolean isValidatedByPlayer;
    private boolean isValidatedByMJ;

    public enum Status {
        DRAFT, VALIDATED
    }

    public Episode(String dateRelative) {
        this.dateRelative = dateRelative;
        this.statut = Status.DRAFT;
        this.paragraphesSecrets = new ArrayList<>();
        this.isValidatedByPlayer = false;
        this.isValidatedByMJ = false;
    }

    public void ajouterParagrapheSecret(ParagrapheSecret paragraphe) {
        paragraphesSecrets.add(paragraphe);
    }

    // Business logic: validate method
    public void validate(Utilisateur actor, Joueur owner, MeneurDeJeu mj) {
        if (statut == Status.VALIDATED) {
            throw new IllegalStateException("Cannot validate an already validated episode");
        }

        boolean isOwner = actor.equals(owner);
        boolean isMJ = actor.equals(mj);

        if (isOwner) {
            isValidatedByPlayer = true;
        }
        if (isMJ) {
            isValidatedByMJ = true;
        }
        if (!isOwner && !isMJ) {
            throw new IllegalArgumentException("Actor must be either the owner or the MJ");
        }

        // Transition to VALIDATED based on validation requirements
        boolean ownerIsMJ = owner.equals(mj);
        if (ownerIsMJ) {
            // If owner is MJ, only one validation is needed
            if (isValidatedByPlayer || isValidatedByMJ) {
                statut = Status.VALIDATED;
            }
        } else {
            // If owner is not MJ, both validations are needed
            if (isValidatedByPlayer && isValidatedByMJ) {
                statut = Status.VALIDATED;
            }
        }
    }

    // Immutability: prevent modifications if validated
    private void checkImmutability() {
        if (statut == Status.VALIDATED) {
            throw new IllegalStateException("Cannot modify a validated episode");
        }
    }

    public String getDateRelative() {
        return dateRelative;
    }

    public void setDateRelative(String dateRelative) {
        checkImmutability();
        this.dateRelative = dateRelative;
    }

    public Status getStatut() {
        return statut;
    }

    public List<ParagrapheSecret> getParagraphesSecrets() {
        return paragraphesSecrets;
    }

    public void setParagraphesSecrets(List<ParagrapheSecret> paragraphesSecrets) {
        checkImmutability();
        this.paragraphesSecrets = paragraphesSecrets;
    }

    public Partie getPartieLiee() {
        return partieLiee;
    }

    public void setPartieLiee(Partie partieLiee) {
        checkImmutability();
        this.partieLiee = partieLiee;
    }

    // Getters for validation flags (for testing)
    public boolean isValidatedByPlayer() {
        return isValidatedByPlayer;
    }

    public boolean isValidatedByMJ() {
        return isValidatedByMJ;
    }

    public boolean isVisiblePublic() {
        return statut == Status.VALIDATED;
    }
}
