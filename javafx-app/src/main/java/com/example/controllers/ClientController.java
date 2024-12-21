package com.example.controllers;

import com.example.entites.Client;
import com.example.entites.Utilisateur;

import javafx.collections.ObservableList;

public interface ClientController {
    void ajouterClient(String surname, String telephone, String adresse, Utilisateur utilisateur);
     ObservableList<Client> listerClients(boolean avecCompte);
     Client rechercherClientParTelephone(String telephone);
     
    
}
