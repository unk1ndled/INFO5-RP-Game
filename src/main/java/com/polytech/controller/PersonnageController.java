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
        personnage.setStatut(Personnage.StatutPersonnage.PROPOSE);
        personnageRepo.ajouterPersonnage(personnage);
        return personnage;
    }

    public void accepterPersonnage(String nomPersonnage, String mjPseudo) {
        Personnage personnage = personnageRepo.findByNom(nomPersonnage)
                .orElseThrow(() -> new IllegalArgumentException("Personnage not found"));
        MeneurDeJeu mj = (MeneurDeJeu) userRepo.findByPseudo(mjPseudo)
                .orElseThrow(() -> new IllegalArgumentException("MJ not found"));

        if (personnage.getStatut() != Personnage.StatutPersonnage.PROPOSE) {
            throw new IllegalStateException("Character already accepted");
        }

        personnage.setMeneurDeJeu(mj);
        personnage.setStatut(Personnage.StatutPersonnage.ACCEPTE);
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

    public Biographie getBiographiePrivee(String nomPersonnage, String pseudoUtilisateur) {
        Personnage personnage = personnageRepo.findByNom(nomPersonnage)
                .orElseThrow(() -> new IllegalArgumentException("Personnage not found"));
        Utilisateur utilisateur = userRepo.findByPseudo(pseudoUtilisateur)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur not found"));

        if (!utilisateur.equals(personnage.getJoueur()) && !utilisateur.equals(personnage.getMeneurDeJeu())) {
            throw new IllegalArgumentException("Unauthorized access to private biography");
        }

        return personnage.getBiographie();
    }

    public void transfererJoueur(String nomPersonnage, String nouveauJoueurPseudo) {
        Personnage personnage = personnageRepo.findByNom(nomPersonnage)
                .orElseThrow(() -> new IllegalArgumentException("Personnage not found"));

        if (personnage.participeAPartieNonTerminee()) {
            throw new IllegalStateException("Cannot transfer character in unfinished party");
        }

        Joueur nouveauJoueur = (Joueur) userRepo.findByPseudo(nouveauJoueurPseudo)
                .orElseThrow(() -> new IllegalArgumentException("Nouveau joueur not found"));

        personnage.setJoueur(nouveauJoueur);
    }

    public void demanderChangementMJ(String nomPersonnage, String nouveauMjPseudo) {
        Personnage personnage = personnageRepo.findByNom(nomPersonnage)
                .orElseThrow(() -> new IllegalArgumentException("Personnage not found"));

        MeneurDeJeu nouveauMj = (MeneurDeJeu) userRepo.findByPseudo(nouveauMjPseudo)
                .orElseThrow(() -> new IllegalArgumentException("Nouveau MJ not found"));

        personnage.setMjEnAttente(nouveauMj);
    }

    public void accepterChangementMJ(String nomPersonnage, String mjPseudo) {
        Personnage personnage = personnageRepo.findByNom(nomPersonnage)
                .orElseThrow(() -> new IllegalArgumentException("Personnage not found"));

        MeneurDeJeu mj = (MeneurDeJeu) userRepo.findByPseudo(mjPseudo)
                .orElseThrow(() -> new IllegalArgumentException("MJ not found"));

        if (!mj.equals(personnage.getMjEnAttente())) {
            throw new IllegalArgumentException("Not the pending MJ");
        }

        personnage.setMeneurDeJeu(mj);
        personnage.setMjEnAttente(null);
    }

}
