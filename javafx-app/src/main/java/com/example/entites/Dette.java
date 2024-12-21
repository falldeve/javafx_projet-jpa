package com.example.entites;

import java.util.Date;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "dette")
public class Dette {
      @Id // Annotation pour indiquer que c'est la clé primaire
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Génération automatique de l'ID
    private Long id; // Ajoutez un identifiant pour l'entité

    private Date date;
    private double montant;
    private double montantVerser;
    private double montantRestant;
     @ManyToOne // Relation ManyToOne avec Client
    @JoinColumn(name = "client_id") // Colonne de jointure
  
    private Client client; 

    @ElementCollection // Pour stocker une liste de chaînes
    private List<String> articles;  
      private boolean paiementEffectue; // Indique si un paiement a été effectué

    // Constructeur
    public Dette(Date date, double montant, double montantVerser, double montantRestant, List<String> articles,Client client) {
        this.date = date;
        this.montant = montant;
        this.montantVerser = montantVerser;
        this.montantRestant = montantRestant;
        this.articles = articles;
        this.paiementEffectue = false;
        this.client = client;
    }

    public void enregistrerPaiement(double montant, Date date) {
      this.montantVerser += montant;
      this.montantRestant -= montant;
      // Vous pouvez également enregistrer la date du paiement si nécessaire
  }
}
