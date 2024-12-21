package com.example.entites;
import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "client")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String surname;
    private String telephone;
    private String adresse;
    private double montantDu;

    @OneToOne(cascade = CascadeType.ALL) // Association optionnelle avec l'utilisateur
    private Utilisateur utilisateur;
   public Client(String surname,String telephone,String adresse){
    this.surname=surname;
    this.telephone=telephone;
    this.adresse=adresse;

   }

}
