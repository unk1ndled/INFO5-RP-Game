package com.polytech.model;

public class ParagrapheSecret {
    private String texte;
    private boolean revele;

    public ParagrapheSecret(String texte) {
        this.texte = texte;
        this.revele = false;
    }

    public String getTexte() {
        return texte;
    }

    public void setTexte(String texte) {
        this.texte = texte;
    }

    public boolean isRevele() {
        return revele;
    }

    public void setRevele(boolean revele) {
        this.revele = revele;
    }
}
