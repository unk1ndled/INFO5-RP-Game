package com.polytech.controller;

import com.polytech.model.*;
import com.polytech.repository.PersonnageRepository;
import com.polytech.repository.UniversRepository;
import com.polytech.repository.UtilisateurRepository;

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

    // Other methods as needed
}
