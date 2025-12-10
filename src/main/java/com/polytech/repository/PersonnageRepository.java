package com.polytech.repository;

import com.polytech.model.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PersonnageRepository {
    private static PersonnageRepository instance;
    private List<Personnage> personnages = new ArrayList<>();

    private PersonnageRepository() {}

    public static PersonnageRepository getInstance() {
        if (instance == null) {
            instance = new PersonnageRepository();
        }
        return instance;
    }

    public void ajouterPersonnage(Personnage personnage) {
        personnages.add(personnage);
    }

    public List<Personnage> getPersonnages() {
        return personnages;
    }

    public Optional<Personnage> findByNom(String nom) {
        return personnages.stream().filter(p -> p.getNom().equals(nom)).findFirst();
    }

    public List<Personnage> findByJoueur(Joueur joueur) {
        return personnages.stream().filter(p -> p.getJoueur().equals(joueur)).toList();
    }

    public List<Personnage> findByUnivers(Univers univers) {
        return personnages.stream().filter(p -> p.getUnivers().equals(univers)).toList();
    }
}
