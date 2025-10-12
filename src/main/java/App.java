import model.Categorie;
import model.Produit;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class App {
    public static void main(String[] args) {
        // Création de l'EntityManagerFactory
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hibernate-demo");

        // Insertion de produits
        insererProduits(emf);

        // Lecture des produits
        lireProduits(emf);




        // Mise à jour du prix
        mettreAJourPrix(emf, 2L, new BigDecimal("1099.99"));

        //supprimerProduit(emf,2L);

        rechercherProduitsParPrix(emf,new BigDecimal("10"),new BigDecimal("10000"));


        insererCategoris( emf);

        lireCategories( emf);
        // Fermeture de l'EntityManagerFactory
        emf.close();
    }

    private static void insererProduits(EntityManagerFactory emf) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            // Création de quelques produits
            Produit p1 = new Produit("Laptop", new BigDecimal("999.99"));
            Produit p2 = new Produit("Smartphone", new BigDecimal("499.99"));
            Produit p3 = new Produit("Tablette", new BigDecimal("299.99"));

            // Persistance des produits
            em.persist(p1);
            em.persist(p2);
            em.persist(p3);

            em.getTransaction().commit();
            System.out.println("Produits insérés avec succès !");
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }
    }



    private static void insererCategoris(EntityManagerFactory emf) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();


            List<Produit> produits=new ArrayList<>();
            List<Produit> produits2=new ArrayList<>();

            produits.add(getProduit(emf,1L));
            produits.add(getProduit(emf,2L));
            produits2.add(getProduit(emf,3L));
            // Création de quelques produits
            Categorie p1 = new Categorie("Cat1",produits);
            Categorie p2 = new Categorie("Cat2",produits);
            Categorie p3 = new Categorie("Cat3",produits2);


            // Persistance des produits
            em.persist(p1);
            em.persist(p2);
            em.persist(p3);

            em.getTransaction().commit();
            System.out.println("Produits insérés avec succès !");
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }
    }



    private static void lireCategories(EntityManagerFactory emf) {
        EntityManager em = emf.createEntityManager();
        try {
            // Requête JPQL pour récupérer tous les produits
            List<Categorie> produits = em.createQuery("SELECT p FROM Categorie p", Categorie.class)
                    .getResultList();

            System.out.println("\nListe des Categories :");
            for (Categorie produit : produits) {
                System.out.println(produit);
            }


        } finally {
            em.close();
        }
    }

    private static void lireProduits(EntityManagerFactory emf) {
        EntityManager em = emf.createEntityManager();
        try {
            // Requête JPQL pour récupérer tous les produits
            List<Produit> produits = em.createQuery("SELECT p FROM Produit p", Produit.class)
                    .getResultList();

            System.out.println("\nListe des produits :");
            for (Produit produit : produits) {
                System.out.println(produit);
            }

            // Recherche d'un produit par ID
            System.out.println("\nRecherche du produit avec ID=2 :");
            Produit produit = em.find(Produit.class, 2L);
            if (produit != null) {
                System.out.println(produit);
            } else {
                System.out.println("Produit non trouvé");
            }
        } finally {
            em.close();
        }
    }


    private static void mettreAJourPrix(EntityManagerFactory emf, Long produitId, BigDecimal nouveauPrix) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Produit produit = em.find(Produit.class, produitId);
            if (produit != null) {
                produit.setPrix(nouveauPrix); // Hibernate détecte automatiquement la modification
                System.out.println("Prix mis à jour : " + produit);
            } else {
                System.out.println("Produit non trouvé pour l'ID : " + produitId);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }
    }


    private static Produit getProduit(EntityManagerFactory emf,Long id)
    {
        Produit cat=null;
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Produit categorie = em.find(Produit.class, id);
            if (categorie != null) {
                cat=categorie;
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }
        return cat;
    }

    private static void supprimerProduit(EntityManagerFactory emf, Long produitId) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Produit produit = em.find(Produit.class, produitId);
            if (produit != null) {
                em.remove(produit);
                System.out.println("Produit supprimé : " + produit);
            } else {
                System.out.println("Produit non trouvé pour l'ID : " + produitId);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }
    }


    private static void rechercherProduitsParPrix(EntityManagerFactory emf, BigDecimal min, BigDecimal max) {
        EntityManager em = emf.createEntityManager();
        try {
            List<Produit> produits = em.createQuery(
                            "SELECT p FROM Produit p WHERE p.prix BETWEEN :min AND :max", Produit.class)
                    .setParameter("min", min)
                    .setParameter("max", max)
                    .getResultList();

            System.out.println("\nProduits entre " + min + " et " + max + " :");
            for (Produit p : produits) {
                System.out.println(p);
            }
        } finally {
            em.close();
        }
    }



}