package com.polytech.controller;

import com.polytech.model.*;
import com.polytech.repository.PartieRepository;
import com.polytech.repository.PersonnageRepository;
import com.polytech.repository.UniversRepository;
import com.polytech.repository.UtilisateurRepository;

import java.util.List;
import java.util.stream.Collectors;

public class PartieController {
    private PartieRepository partieRepo = PartieRepository.getInstance();
    private PersonnageRepository personnageRepo = PersonnageRepository.getInstance();
    private UniversRepository universRepo = UniversRepository.getInstance();
    private UtilisateurRepository userRepo = UtilisateurRepository.getInstance();

    public Partie proposerPartie(String titre, String resumeInitial, String date, String lieu,
                                String universNom, String meneurPseudo) {
        Univers univers = universRepo.findByNom(universNom)
                .orElseThrow(() -> new IllegalArgumentException("Univers not found"));
        MeneurDeJeu meneurDeJeu = (MeneurDeJeu) userRepo.findByPseudo(meneurPseudo)
                .orElseThrow(() -> new IllegalArgumentException("Meneur de jeu not found"));

        Partie partie = new Partie(titre, resumeInitial, date, lieu, univers, meneurDeJeu);
        partieRepo.ajouterPartie(partie);
        return partie;
    }

    public List<Partie> getParties() {
        return partieRepo.getParties();
    }

    public Partie findByTitre(String titre) {
        return partieRepo.findByTitre(titre).orElse(null);
    }

    public List<Partie> getPartiesByMeneur(String meneurPseudo) {
        MeneurDeJeu mj = (MeneurDeJeu) userRepo.findByPseudo(meneurPseudo)
                .orElseThrow(() -> new IllegalArgumentException("Meneur de jeu not found"));
        return partieRepo.findByMeneurDeJeu(mj);
    }

    public void ajouterParticipant(String titrePartie, String nomPersonnage, String meneurPseudo) {
        Partie partie = partieRepo.findByTitre(titrePartie)
                .orElseThrow(() -> new IllegalArgumentException("Partie not found"));
        Personnage personnage = personnageRepo.findByNom(nomPersonnage)
                .orElseThrow(() -> new IllegalArgumentException("Personnage not found"));

        partie.checkModifiable();

        if (!partie.getMeneurDeJeu().getPseudo().equals(meneurPseudo)) {
            throw new IllegalArgumentException("Only the MJ can manage participants");
        }

        if (!personnage.getUnivers().equals(partie.getUnivers())) {
            throw new IllegalArgumentException("Character must belong to the party's universe");
        }

        if (personnage.getMeneurDeJeu() == null || !personnage.getMeneurDeJeu().getPseudo().equals(meneurPseudo)) {
            throw new IllegalArgumentException("MJ must be assigned to this character");
        }

        if (partie.getParticipants().contains(personnage)) {
            throw new IllegalArgumentException("Character is already a participant");
        }

        partie.ajouterParticipant(personnage);
    }

    public void retirerParticipant(String titrePartie, String nomPersonnage, String meneurPseudo) {
        Partie partie = partieRepo.findByTitre(titrePartie)
                .orElseThrow(() -> new IllegalArgumentException("Partie not found"));
        Personnage personnage = personnageRepo.findByNom(nomPersonnage)
                .orElseThrow(() -> new IllegalArgumentException("Personnage not found"));

        partie.checkModifiable();

        if (!partie.getMeneurDeJeu().getPseudo().equals(meneurPseudo)) {
            throw new IllegalArgumentException("Only the MJ can manage participants");
        }

        partie.retirerParticipant(personnage);
    }

    public void finirPartie(String titrePartie, String resumeEvenements, String meneurPseudo) {
        Partie partie = partieRepo.findByTitre(titrePartie)
                .orElseThrow(() -> new IllegalArgumentException("Partie not found"));

        if (!partie.getMeneurDeJeu().getPseudo().equals(meneurPseudo)) {
            throw new IllegalArgumentException("Only the MJ can finish the party");
        }

        if (partie.getStatut() != Partie.Status.PROPOSITION) {
            throw new IllegalArgumentException("Party is already finished");
        }

        partie.finirPartie(resumeEvenements);

        // Add to participant biographies
        for (Personnage participant : partie.getParticipants()) {
            Episode episodeAventure = new Episode(partie.getDate());
            ParagrapheSecret paraAventure = new ParagrapheSecret(resumeEvenements, false);
            episodeAventure.ajouterParagrapheSecret(paraAventure);
            participant.getBiographie().ajouterEpisode(episodeAventure);
        }
    }

    public void supprimerProposition(String titrePartie, String meneurPseudo) {
        Partie partie = partieRepo.findByTitre(titrePartie)
                .orElseThrow(() -> new IllegalArgumentException("Partie not found"));

        partie.checkModifiable();

        if (!partie.getMeneurDeJeu().getPseudo().equals(meneurPseudo)) {
            throw new IllegalArgumentException("Only the MJ can delete the proposal");
        }

        partieRepo.getParties().remove(partie);
    }

    public List<Personnage> getPersonnagesDisponibles(String titrePartie, String meneurPseudo) {
        Partie partie = partieRepo.findByTitre(titrePartie)
                .orElseThrow(() -> new IllegalArgumentException("Partie not found"));

        if (!partie.getMeneurDeJeu().getPseudo().equals(meneurPseudo)) {
            throw new IllegalArgumentException("Unauthorized access");
        }

        return personnageRepo.getPersonnages().stream()
                .filter(p -> p.getUnivers().equals(partie.getUnivers()))
                .filter(p -> p.getMeneurDeJeu() != null && p.getMeneurDeJeu().getPseudo().equals(meneurPseudo))
                .filter(p -> !partie.getParticipants().contains(p))
                .collect(Collectors.toList());
    }
}
