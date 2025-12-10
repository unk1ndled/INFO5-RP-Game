package com.polytech;

import com.polytech.model.*;
import com.polytech.repository.*;
import com.polytech.controller.*;

public class App {
    public static void main(String[] args) {
        System.out.println("Bienvenue dans l'application de gestion de rôles !");

        // Initialize dummy data
        Univers univers = new Univers("Univers des Pirates");
        UniversRepository.getInstance().ajouterUnivers(univers);

        MeneurDeJeu mj = new MeneurDeJeu("MJ1", "mj@example.com");
        UtilisateurRepository.getInstance().ajouterUtilisateur(mj);

        Joueur joueur = new Joueur("Joueur1", "joueur@example.com");
        UtilisateurRepository.getInstance().ajouterUtilisateur(joueur);

        PersonnageController personnageCtrl = new PersonnageController();
        EpisodeController episodeCtrl = new EpisodeController();

        // Scenario: Create a character
        System.out.println("Création d'un personnage...");
        Personnage personnage = personnageCtrl.creerPersonnage("Capitaine Jack", "An 1", "Pirate", "jack.jpg", "Univers des Pirates", "Joueur1");
        System.out.println("Personnage créé: " + personnage.getNom());

        // MJ validates
        System.out.println("MJ valide le personnage...");
        personnageCtrl.accepterPersonnage("Capitaine Jack", "MJ1");
        System.out.println("Personnage validé par MJ: " + personnage.getMeneurDeJeu().getPseudo());

        // Add an episode
        System.out.println("Ajout d'un épisode...");
        Episode episode = episodeCtrl.creerEpisode("Capitaine Jack", "An 2", "Le capitaine découvre un trésor.");
        System.out.println("Épisode créé: statut " + episode.getStatut());

        // Validate by player
        System.out.println("Validation par le joueur...");
        episodeCtrl.validerParJoueur("Capitaine Jack", "An 2");
        System.out.println("Épisode après validation joueur: " + episode.getStatut());

        // Validate by MJ
        System.out.println("Validation par le MJ...");
        episodeCtrl.validerParMJ("Capitaine Jack", "An 2");
        System.out.println("Épisode final: " + episode.getStatut());

        System.out.println("Scénario terminé !");
    }
}
