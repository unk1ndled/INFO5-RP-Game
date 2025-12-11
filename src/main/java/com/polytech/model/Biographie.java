package com.polytech.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Biographie {
    private List<Episode> episodes = new ArrayList<>();

    public Biographie() {
        this.episodes = new ArrayList<>();
    }

    public void ajouterEpisode(Episode episode) {
        episodes.add(episode);
    }

    public List<Episode> getEpisodes() {
        return episodes;
    }

    public void setEpisodes(List<Episode> episodes) {
        this.episodes = episodes;
    }

    // Visibility access control method
    public List<ParagrapheSecret> getVisibleParagraphs(Utilisateur viewer, Joueur owner, MeneurDeJeu mj) {
        // Collect all paragraphs from all episodes
        List<ParagrapheSecret> allParagraphs = new ArrayList<>();
        for (Episode episode : episodes) {
            allParagraphs.addAll(episode.getParagraphesSecrets());
        }

        // Filter based on viewer permissions
        if (viewer.equals(owner) || viewer.equals(mj)) {
            // Owner or MJ can see all paragraphs
            return allParagraphs;
        } else {
            // Visitor can only see non-secret paragraphs
            return allParagraphs.stream()
                    .filter(p -> !p.isSecret())
                    .collect(Collectors.toList());
        }
    }
}
