package com.polytech.model;

public class ParagrapheSecret {
    private String texte;
    private boolean isSecret;

    public ParagrapheSecret(String texte, boolean isSecret) {
        this.texte = texte;
        this.isSecret = isSecret;
    }

    public String getTexte() {
        return texte;
    }

    public void setTexte(String texte) {
        this.texte = texte;
    }

    public boolean isSecret() {
        return isSecret;
    }

    public void setSecret(boolean secret) {
        isSecret = secret;
    }
}
