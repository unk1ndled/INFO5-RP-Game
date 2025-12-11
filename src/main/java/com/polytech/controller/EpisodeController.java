package com.polytech.controller;

import com.polytech.model.*;
import com.polytech.repository.PersonnageRepository;
import com.polytech.repository.PartieRepository;
import com.polytech.repository.UtilisateurRepository;

public class EpisodeController {
    private PersonnageRepository personnageRepo = PersonnageRepository.getInstance();
    private PartieRepository partieRepo = PartieRepository.getInstance();

    public Episode creerEpisode(String nomPersonnage, String dateRelative, String contenu) {
        Personnage personnage = personnageRepo.findByNom(nomPersonnage)
                .orElseThrow(() -> new IllegalArgumentException("Personnage not found"));
        Episode episode = new Episode(dateRelative);
        // Add content as paragraphe, assume public for simplicity, or handle later
        personnage.getBiographie().ajouterEpisode(episode);
        return episode;
    }

    public void modifierEpisode(String nomPersonnage, String dateRelative, String nouveauContenu) {
        Personnage personnage = personnageRepo.findByNom(nomPersonnage)
                .orElseThrow(() -> new IllegalArgumentException("Personnage not found"));
        Episode episode = personnage.getBiographie().getEpisodes().stream()
                .filter(e -> e.getDateRelative().equals(dateRelative)).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Episode not found"));
        if (episode.getStatut() != Episode.Status.DRAFT) {
            throw new IllegalStateException("Cannot modify non-draft episode");
        }
        // Modify content, but since simple, perhaps replace
    }

    public void validerEpisode(String nomPersonnage, String dateRelative, String acteurPseudo) {
        Personnage personnage = personnageRepo.findByNom(nomPersonnage)
                .orElseThrow(() -> new IllegalArgumentException("Personnage not found"));
        Episode episode = personnage.getBiographie().getEpisodes().stream()
                .filter(e -> e.getDateRelative().equals(dateRelative)).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Episode not found"));

        Utilisateur acteur = UtilisateurRepository.getInstance().findByPseudo(acteurPseudo)
                .orElseThrow(() -> new IllegalArgumentException("Acteur not found"));

        episode.validate(acteur, personnage.getJoueur(), personnage.getMeneurDeJeu());
    }

    public void supprimerEpisode(String nomPersonnage, String dateRelative) {
        Personnage personnage = personnageRepo.findByNom(nomPersonnage)
                .orElseThrow(() -> new IllegalArgumentException("Personnage not found"));
        Episode episode = personnage.getBiographie().getEpisodes().stream()
                .filter(e -> e.getDateRelative().equals(dateRelative)).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Episode not found"));
        if (episode.getStatut() == Episode.Status.VALIDATED) {
            throw new IllegalStateException("Cannot delete validated episode");
        }
        personnage.getBiographie().getEpisodes().remove(episode);
    }


}
