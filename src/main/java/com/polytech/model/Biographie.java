package com.polytech.model;

import java.util.ArrayList;
import java.util.List;

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
}
