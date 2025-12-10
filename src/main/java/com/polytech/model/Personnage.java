package com.polytech.model;

public class Personnage {
    private String nom;
    private String dateNaissance;
    private String profession;
    private String portrait; // perhaps URL or path
    private Biographie biographie;
    private Univers univers;
    private Joueur joueur; // owner
    private MeneurDeJeu meneurDeJeu; // assigned MJ

    public Personnage(String nom, String dateNaissance, String profession, String portrait, Univers univers, Joueur joueur) {
        this.nom = nom;
        this.dateNaissance = dateNaissance;
        this.profession = profession;
        this.portrait = portrait;
        this.univers = univers;
        this.joueur = joueur;
        this.biographie = new Biographie();
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(String dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public Biographie getBiographie() {
        return biographie;
    }

    public void setBiographie(Biographie biographie) {
        this.biographie = biographie;
    }

    public Univers getUnivers() {
        return univers;
    }

    public void setUnivers(Univers univers) {
        this.univers = univers;
    }

    public Joueur getJoueur() {
        return joueur;
    }

    public void setJoueur(Joueur joueur) {
        this.joueur = joueur;
    }

    public MeneurDeJeu getMeneurDeJeu() {
        return meneurDeJeu;
    }

    public void setMeneurDeJeu(MeneurDeJeu meneurDeJeu) {
        this.meneurDeJeu = meneurDeJeu;
    }
}
