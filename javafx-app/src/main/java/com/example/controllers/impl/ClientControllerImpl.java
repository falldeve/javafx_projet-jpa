package com.example.controllers.impl;

import java.util.List;

import javax.persistence.EntityManagerFactory;

import com.example.controllers.ClientController;
import com.example.entites.Client;
import com.example.entites.Utilisateur;
import com.example.services.ClientServiceI;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ClientControllerImpl implements ClientController {
    
     private ClientServiceI clientService;
         private EntityManagerFactory entityManagerFactory; 


    public ClientControllerImpl(ClientServiceI clientService) {
        this.clientService = clientService;

    }

    @Override
    public void ajouterClient(String surname, String telephone, String adresse, Utilisateur utilisateur) {
        clientService.ajouterClient(surname, telephone, adresse, utilisateur);
    }

    @Override
    public Client rechercherClientParTelephone(String telephone) {
        return clientService.rechercherClientParTelephone(telephone);
    }
    @Override
      public ObservableList<Client> listerClients(boolean avecCompte) {
        // Récupérer les clients de la base de données
        List<Client> clients = clientService.listerClients(avecCompte);
      
        return FXCollections.observableArrayList(clients); 
    }

    public void closeEntityManagerFactory() {
        if (entityManagerFactory != null && entityManagerFactory.isOpen()) {
            entityManagerFactory.close();
        }
    }

   
}
