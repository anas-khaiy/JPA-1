package model;

import javax.persistence.*;
import java.util.List;

@Entity
public class Categorie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nom;

    // Relation OneToMany inverse (optionnelle)
    @OneToMany(mappedBy = "categorie")
    private List<Produit> produits;

    public Categorie() {}

    public Categorie(String nom) {
        this.nom = nom;
    }

    public Categorie(String nom, List<Produit> produits) {
        this.nom = nom;
        this.produits = produits;
    }

    // Getters / Setters
    public Long getId() { return id; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    @Override
    public String toString() {
        return "Categorie{id=" + id + ", nom='" + nom + " "+produits.toString()+"'}";
    }
}