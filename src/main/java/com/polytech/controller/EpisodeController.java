package com.polytech.controller;

import com.polytech.model.*;
import com.polytech.repository.PersonnageRepository;
import com.polytech.repository.PartieRepository;

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
        if (episode.getStatut() != Episode.Status.BROUILLON) {
            throw new IllegalStateException("Cannot modify non-draft episode");
        }
        // Modify content, but since simple, perhaps replace
    }

    public void validerParJoueur(String nomPersonnage, String dateRelative) {
        Personnage personnage = personnageRepo.findByNom(nomPersonnage)
                .orElseThrow(() -> new IllegalArgumentException("Personnage not found"));
        Episode episode = personnage.getBiographie().getEpisodes().stream()
                .filter(e -> e.getDateRelative().equals(dateRelative)).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Episode not found"));
        if (episode.getStatut() == Episode.Status.BROUILLON) {
            episode.setStatut(Episode.Status.EN_ATTENTE);
        } else if (episode.getStatut() == Episode.Status.EN_ATTENTE) {
            episode.setStatut(Episode.Status.VALIDE);
        }
    }

    public void validerParMJ(String nomPersonnage, String dateRelative) {
        // Similar to above
        validerParJoueur(nomPersonnage, dateRelative);
    }

    public void supprimerEpisode(String nomPersonnage, String dateRelative) {
        Personnage personnage = personnageRepo.findByNom(nomPersonnage)
                .orElseThrow(() -> new IllegalArgumentException("Personnage not found"));
        Episode episode = personnage.getBiographie().getEpisodes().stream()
                .filter(e -> e.getDateRelative().equals(dateRelative)).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Episode not found"));
        if (episode.getStatut() == Episode.Status.VALIDE) {
            throw new IllegalStateException("Cannot delete validated episode");
        }
        personnage.getBiographie().getEpisodes().remove(episode);
    }

    public void revelerParagrapheSecret(String nomPersonnage, String dateRelative, int index) {
        Personnage personnage = personnageRepo.findByNom(nomPersonnage)
                .orElseThrow(() -> new IllegalArgumentException("Personnage not found"));
        Episode episode = personnage.getBiographie().getEpisodes().stream()
                .filter(e -> e.getDateRelative().equals(dateRelative)).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Episode not found"));
        if (index < episode.getParagraphesSecrets().size()) {
            episode.getParagraphesSecrets().get(index).setRevele(true);
        }
    }
}
