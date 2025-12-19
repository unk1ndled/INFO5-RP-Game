package com.polytech.model;

import java.util.ArrayList;
import java.util.List;

public class Partie {
    private String titre;
    private String resumeInitial;
    private String date;
    private String lieu;
    private Univers univers;
    private Status statut;
    private MeneurDeJeu meneurDeJeu;
    private List<Personnage> participants = new ArrayList<>();
    private String resumeEvenements; 
    private boolean terminee = false;

    public enum Status {
        PROPOSITION, AVENTURE
    }

    public Partie(String titre, String resumeInitial, String date, String lieu, Univers univers, MeneurDeJeu meneurDeJeu) {
        this.titre = titre;
        this.resumeInitial = resumeInitial;
        this.date = date;
        this.lieu = lieu;
        this.univers = univers;
        this.statut = Status.PROPOSITION;
        this.meneurDeJeu = meneurDeJeu;
        this.participants = new ArrayList<>();
    }

    public void ajouterParticipant(Personnage personnage) {
        participants.add(personnage);
    }

    public void retirerParticipant(Personnage personnage) {
        participants.remove(personnage);
    }

    public void finirPartie(String resumeEvenements) {
        this.resumeEvenements = resumeEvenements;
        this.statut = Status.AVENTURE;
        this.terminee = true;
    }

    public void checkModifiable() {
        if (terminee) {
            throw new IllegalStateException("Party finished");
        }
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getResumeInitial() {
        return resumeInitial;
    }

    public void setResumeInitial(String resumeInitial) {
        this.resumeInitial = resumeInitial;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLieu() {
        return lieu;
    }

    public void setLieu(String lieu) {
        this.lieu = lieu;
    }

    public Univers getUnivers() {
        return univers;
    }

    public void setUnivers(Univers univers) {
        this.univers = univers;
    }

    public Status getStatut() {
        return statut;
    }

    public void setStatut(Status statut) {
        this.statut = statut;
    }

    public MeneurDeJeu getMeneurDeJeu() {
        return meneurDeJeu;
    }

    public void setMeneurDeJeu(MeneurDeJeu meneurDeJeu) {
        this.meneurDeJeu = meneurDeJeu;
    }

    public List<Personnage> getParticipants() {
        return participants;
    }

    public void setParticipants(List<Personnage> participants) {
        this.participants = participants;
    }

    public String getResumeEvenements() {
        return resumeEvenements;
    }

    public void setResumeEvenements(String resumeEvenements) {
        this.resumeEvenements = resumeEvenements;
    }

    public boolean isTerminee() {
        return terminee;
    }

    public void setTerminee(boolean terminee) {
        this.terminee = terminee;
    }

    public boolean aParticipe(Personnage personnage) {
        return participants.contains(personnage);
    }
}
