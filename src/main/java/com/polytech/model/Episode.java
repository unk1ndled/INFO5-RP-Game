package com.polytech.model;

import java.util.ArrayList;
import java.util.List;

public class Episode {
    private String dateRelative;
    private Status statut;
    private List<ParagrapheSecret> paragraphesSecrets = new ArrayList<>();
    private Partie partieLiee; // optional

    public enum Status {
        BROUILLON, EN_ATTENTE, VALIDE
    }

    public Episode(String dateRelative) {
        this.dateRelative = dateRelative;
        this.statut = Status.BROUILLON;
        this.paragraphesSecrets = new ArrayList<>();
    }

    public void ajouterParagrapheSecret(ParagrapheSecret paragraphe) {
        paragraphesSecrets.add(paragraphe);
    }

    public String getDateRelative() {
        return dateRelative;
    }

    public void setDateRelative(String dateRelative) {
        this.dateRelative = dateRelative;
    }

    public Status getStatut() {
        return statut;
    }

    public void setStatut(Status statut) {
        this.statut = statut;
    }

    public List<ParagrapheSecret> getParagraphesSecrets() {
        return paragraphesSecrets;
    }

    public void setParagraphesSecrets(List<ParagrapheSecret> paragraphesSecrets) {
        this.paragraphesSecrets = paragraphesSecrets;
    }

    public Partie getPartieLiee() {
        return partieLiee;
    }

    public void setPartieLiee(Partie partieLiee) {
        this.partieLiee = partieLiee;
    }
}
