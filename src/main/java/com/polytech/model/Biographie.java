package com.polytech.model;

import java.util.ArrayList;
import java.util.Comparator;
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
        return episodes.stream()
                .sorted(Comparator.comparing(Episode::getDateRelative))
                .collect(Collectors.toList());
    }

    public List<Episode> getEpisodesPublics() {
        return episodes.stream()
                .filter(Episode::isVisiblePublic)
                .sorted(Comparator.comparing(Episode::getDateRelative))
                .collect(Collectors.toList());
    }

    public List<Episode> getEpisodesPrives() {
        return getEpisodes(); 
    }

    public void setEpisodes(List<Episode> episodes) {
        this.episodes = episodes;
    }

    public List<ParagrapheSecret> getVisibleParagraphs(Utilisateur viewer, Joueur owner, MeneurDeJeu mj) {
        List<ParagrapheSecret> allParagraphs = new ArrayList<>();
        for (Episode episode : episodes) {
            allParagraphs.addAll(episode.getParagraphesSecrets());
        }

        if (viewer.equals(owner) || viewer.equals(mj)) {
            return allParagraphs;
        } else {
            return allParagraphs.stream()
                    .filter(p -> !p.isSecret())
                    .collect(Collectors.toList());
        }
    }
}
