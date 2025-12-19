package com.polytech;

import com.polytech.model.*;
import com.polytech.repository.*;

public class DataInitializer {
    public static void initializeDefaultData() {
        Univers univers = new Univers("Univers des Pirates");
        UniversRepository.getInstance().ajouterUnivers(univers);

        Joueur alice = new Joueur("Alice", "alice@test.com");
        Joueur abdelRaouf = new Joueur("Abdel Raouf", "abdel.raouf@test.com");
        MeneurDeJeu bob = new MeneurDeJeu("Bob", "bob@test.com");
        Utilisateur visitor = new Utilisateur("Visiteur", "visitor@test.com");

        UtilisateurRepository.getInstance().ajouterUtilisateur(alice);
        UtilisateurRepository.getInstance().ajouterUtilisateur(abdelRaouf);
        UtilisateurRepository.getInstance().ajouterUtilisateur(bob);
        UtilisateurRepository.getInstance().ajouterUtilisateur(visitor);
    }
}
