package com.polytech.controller;

import com.polytech.model.*;
import com.polytech.repository.PersonnageRepository;
import com.polytech.repository.UniversRepository;
import com.polytech.repository.UtilisateurRepository;

import java.util.List;

public class PersonnageController {
    private PersonnageRepository personnageRepo = PersonnageRepository.getInstance();
    private UtilisateurRepository userRepo = UtilisateurRepository.getInstance();

    public Personnage creerPersonnage(String nom, String dateNaissance, String profession, String portrait,
            String universNom, String joueurPseudo) {
        Univers univers = UniversRepository.getInstance().findByNom(universNom)
                .orElseThrow(() -> new IllegalArgumentException("Univers not found"));
        Joueur joueur = (Joueur) userRepo.findByPseudo(joueurPseudo)
                .orElseThrow(() -> new IllegalArgumentException("Joueur not found"));
        Personnage personnage = new Personnage(nom, dateNaissance, profession, portrait, univers, joueur);
        personnageRepo.ajouterPersonnage(personnage);
        return personnage;
    }

    public void accepterPersonnage(String nomPersonnage, String mjPseudo) {
        Personnage personnage = personnageRepo.findByNom(nomPersonnage)
                .orElseThrow(() -> new IllegalArgumentException("Personnage not found"));
        MeneurDeJeu mj = (MeneurDeJeu) userRepo.findByPseudo(mjPseudo)
                .orElseThrow(() -> new IllegalArgumentException("MJ not found"));
        personnage.setMeneurDeJeu(mj);
    }

    public void refuserPersonnage(String nomPersonnage) {
        Personnage personnage = personnageRepo.findByNom(nomPersonnage)
                .orElseThrow(() -> new IllegalArgumentException("Personnage not found"));
        personnageRepo.getPersonnages().remove(personnage);
    }

    public List<Personnage> getPersonnages() {
        return personnageRepo.getPersonnages();
    }

    public Personnage findByNom(String nom) {
        return personnageRepo.findByNom(nom).orElse(null);
    }

    public void ajouterEpisodeBiographieInitiale(String nomPersonnage, String contenu, String dateNaissance) {
        Personnage personnage = personnageRepo.findByNom(nomPersonnage)
                .orElseThrow(() -> new IllegalArgumentException("Personnage not found"));
        Episode episodeInitial = new Episode(dateNaissance);
        ParagrapheSecret paraBio = new ParagrapheSecret(contenu, false);
        episodeInitial.ajouterParagrapheSecret(paraBio);
        personnage.getBiographie().ajouterEpisode(episodeInitial);
    }

    // Other methods as needed
}
