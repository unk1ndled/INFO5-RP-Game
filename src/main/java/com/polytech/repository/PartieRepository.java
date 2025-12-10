package com.polytech.repository;

import com.polytech.model.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PartieRepository {
    private static PartieRepository instance;
    private List<Partie> parties = new ArrayList<>();

    private PartieRepository() {}

    public static PartieRepository getInstance() {
        if (instance == null) {
            instance = new PartieRepository();
        }
        return instance;
    }

    public void ajouterPartie(Partie partie) {
        parties.add(partie);
    }

    public List<Partie> getParties() {
        return parties;
    }

    public Optional<Partie> findByTitre(String titre) {
        return parties.stream().filter(p -> p.getTitre().equals(titre)).findFirst();
    }

    public List<Partie> findByMeneurDeJeu(MeneurDeJeu mj) {
        return parties.stream().filter(p -> p.getMeneurDeJeu().equals(mj)).toList();
    }
}
