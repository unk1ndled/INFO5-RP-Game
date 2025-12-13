package com.polytech.controller;

import com.polytech.model.*;
import com.polytech.repository.PersonnageRepository;
import com.polytech.repository.PartieRepository;
import com.polytech.repository.UtilisateurRepository;

public class EpisodeController {
    private PersonnageRepository personnageRepo = PersonnageRepository.getInstance();
    private PartieRepository partieRepo = PartieRepository.getInstance();

    public Episode creerEpisode(String nomPersonnage, String dateRelative, String contenu, String acteurPseudo) {
        Personnage personnage = personnageRepo.findByNom(nomPersonnage)
                .orElseThrow(() -> new IllegalArgumentException("Personnage not found"));
        Utilisateur acteur = UtilisateurRepository.getInstance().findByPseudo(acteurPseudo)
                .orElseThrow(() -> new IllegalArgumentException("Acteur not found"));

        if (!acteur.equals(personnage.getJoueur()) && !acteur.equals(personnage.getMeneurDeJeu())) {
            throw new IllegalArgumentException("Unauthorized to create episode");
        }

        Episode episode = new Episode(dateRelative);

        ParagrapheSecret paragraphe = new ParagrapheSecret(contenu, false);
        episode.ajouterParagrapheSecret(paragraphe);
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

    public void revelerSecret(String nomPersonnage, String dateRelative, int paragrapheIndex) {
        Personnage personnage = personnageRepo.findByNom(nomPersonnage)
                .orElseThrow(() -> new IllegalArgumentException("Personnage not found"));
        Episode episode = personnage.getBiographie().getEpisodes().stream()
                .filter(e -> e.getDateRelative().equals(dateRelative)).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Episode not found"));

        if (paragrapheIndex < 0 || paragrapheIndex >= episode.getParagraphesSecrets().size()) {
            throw new IllegalArgumentException("Invalid paragraph index");
        }

        ParagrapheSecret para = episode.getParagraphesSecrets().get(paragrapheIndex);
        para.reveler();
    }

    public Episode creerEpisodeDraft(String nomPersonnage, String dateRelative) {
        Personnage personnage = personnageRepo.findByNom(nomPersonnage)
                .orElseThrow(() -> new IllegalArgumentException("Personnage not found"));
        Episode episode = new Episode(dateRelative);
        personnage.getBiographie().ajouterEpisode(episode);
        return episode;
    }

    public void ajouterParagrapheAEpisode(String nomPersonnage, String dateRelative, String contenu, boolean isSecret) {
        Personnage personnage = personnageRepo.findByNom(nomPersonnage)
                .orElseThrow(() -> new IllegalArgumentException("Personnage not found"));
        Episode episode = personnage.getBiographie().getEpisodes().stream()
                .filter(e -> e.getDateRelative().equals(dateRelative)).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Episode not found"));
        if (episode.getStatut() != Episode.Status.DRAFT) {
            throw new IllegalStateException("Cannot add paragraphs to non-draft episode");
        }
        ParagrapheSecret paragraphe = new ParagrapheSecret(contenu, isSecret);
        episode.ajouterParagrapheSecret(paragraphe);
    }

    public void lierEpisodeAventure(String nomPersonnage, String dateRelative, String titrePartie) {
        Personnage personnage = personnageRepo.findByNom(nomPersonnage)
                .orElseThrow(() -> new IllegalArgumentException("Personnage not found"));
        Episode episode = personnage.getBiographie().getEpisodes().stream()
                .filter(e -> e.getDateRelative().equals(dateRelative)).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Episode not found"));

        Partie partie = partieRepo.findByTitre(titrePartie)
                .orElseThrow(() -> new IllegalArgumentException("Partie not found"));

        if (!partie.isTerminee()) {
            throw new IllegalStateException("Party not finished");
        }

        if (!partie.aParticipe(personnage)) {
            throw new IllegalArgumentException("Character did not participate in this adventure");
        }

        episode.setPartieLiee(partie);
    }


}
