package com.polytech.repository;

import com.polytech.model.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UniversRepository {
    private static UniversRepository instance;
    private List<Univers> univers = new ArrayList<>();

    private UniversRepository() {}

    public static UniversRepository getInstance() {
        if (instance == null) {
            instance = new UniversRepository();
        }
        return instance;
    }

    public void ajouterUnivers(Univers u) {
        univers.add(u);
    }

    public List<Univers> getUnivers() {
        return univers;
    }

    public Optional<Univers> findByNom(String nom) {
        return univers.stream().filter(u -> u.getNom().equals(nom)).findFirst();
    }
}
