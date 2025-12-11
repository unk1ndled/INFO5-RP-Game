package com.polytech.model;

public class Utilisateur {
    private String pseudo;
    private String email;

    public Utilisateur(String pseudo, String email) {
        this.pseudo = pseudo;
        this.email = email;
    }

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || !(obj instanceof Utilisateur)) return false;
        Utilisateur that = (Utilisateur) obj;
        return pseudo.equals(that.pseudo);
    }

    @Override
    public int hashCode() {
        return pseudo.hashCode();
    }
}
