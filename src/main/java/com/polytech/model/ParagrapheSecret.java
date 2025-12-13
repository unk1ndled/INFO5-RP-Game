package com.polytech.model;

public class ParagrapheSecret {
    private String texte;
    private boolean isSecret;
    private boolean isRevele;

    public ParagrapheSecret(String texte, boolean isSecret) {
        this.texte = texte;
        this.isSecret = isSecret;
        this.isRevele = false;
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

    public boolean isRevele() {
        return isRevele;
    }

    public void reveler() {
        if (!isSecret) {
            throw new IllegalStateException("Not a secret");
        }
        if (isRevele) {
            throw new IllegalStateException("Already revealed");
        }
        isSecret = false;
        isRevele = true;
    }
}
