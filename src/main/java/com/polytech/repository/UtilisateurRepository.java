package com.polytech.repository;

import com.polytech.model.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UtilisateurRepository {
    private static UtilisateurRepository instance;
    private List<Utilisateur> utilisateurs = new ArrayList<>();

    private UtilisateurRepository() {}

    public static UtilisateurRepository getInstance() {
        if (instance == null) {
            instance = new UtilisateurRepository();
        }
        return instance;
    }

    public void ajouterUtilisateur(Utilisateur utilisateur) {
        utilisateurs.add(utilisateur);
    }

    public List<Utilisateur> getUtilisateurs() {
        return utilisateurs;
    }

    public Optional<Utilisateur> findByPseudo(String pseudo) {
        return utilisateurs.stream().filter(u -> u.getPseudo().equals(pseudo)).findFirst();
    }
}
