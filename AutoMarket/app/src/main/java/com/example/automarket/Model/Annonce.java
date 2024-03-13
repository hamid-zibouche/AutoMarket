package com.example.automarket.Model;

import java.io.Serializable;
import java.util.Date;

public class Annonce implements Serializable {
    private int id;
    private int userId; // ID de l'utilisateur propriétaire de l'annonce
    private String marque;
    private String modele;
    private String boite;
    private String energie;
    private String moteur;
    private int kilometrage;
    private String couleur;
    private int annee;
    private double prix;
    private String photoUrl;
    private String commentaire;
    private String adresse; // Adresse de l'annonce

    private int nbrVue;

    private String dateCreation; // Heure de création de l'annonce
    public Annonce() {
    }

    public Annonce(String marque, String modele, String boite, String energie, String moteur,
                   int kilometrage, String couleur, int annee, double prix, String photoUrl, String commentaire,
                   String adresse, String dateCreation) {

        this.marque = marque;
        this.modele = modele;
        this.boite = boite;
        this.energie = energie;
        this.moteur = moteur;
        this.kilometrage = kilometrage;
        this.couleur = couleur;
        this.annee = annee;
        this.prix = prix;
        this.photoUrl = photoUrl;
        this.commentaire = commentaire;
        this.adresse = adresse;
        this.dateCreation = dateCreation;
    }

    // Getters and Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getMarque() {
        return marque;
    }

    public void setMarque(String marque) {
        this.marque = marque;
    }

    public String getModele() {
        return modele;
    }

    public void setModele(String modele) {
        this.modele = modele;
    }

    public String getBoite() {
        return boite;
    }

    public void setBoite(String boite) {
        this.boite = boite;
    }

    public String getEnergie() {
        return energie;
    }

    public void setEnergie(String energie) {
        this.energie = energie;
    }

    public String getMoteur() {
        return moteur;
    }

    public void setMoteur(String moteur) {
        this.moteur = moteur;
    }

    public int getKilometrage() {
        return kilometrage;
    }

    public void setKilometrage(int kilometrage) {
        this.kilometrage = kilometrage;
    }

    public String getCouleur() {
        return couleur;
    }

    public void setCouleur(String couleur) {
        this.couleur = couleur;
    }

    public int getAnnee() {
        return annee;
    }

    public void setAnnee(int annee) {
        this.annee = annee;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(String dateCreation) {
        this.dateCreation = dateCreation;
    }

    public int getNbrVue() {
        return nbrVue;
    }

    public void setNbrVue(int nbrVue) {
        this.nbrVue = nbrVue;
    }

    @Override
    public String toString() {
        return "Annonce{" +
                "id=" + id +
                ", userId=" + userId +
                ", marque='" + marque + '\'' +
                ", modele='" + modele + '\'' +
                ", boite='" + boite + '\'' +
                ", energie='" + energie + '\'' +
                ", moteur='" + moteur + '\'' +
                ", kilometrage=" + kilometrage +
                ", couleur='" + couleur + '\'' +
                ", annee=" + annee +
                ", prix=" + prix +
                ", photoUrl='" + photoUrl + '\'' +
                ", commentaire='" + commentaire + '\'' +
                ", adresse='" + adresse + '\'' +
                ", dateCreation ='" + dateCreation + '\'' +
                '}';
    }
}
