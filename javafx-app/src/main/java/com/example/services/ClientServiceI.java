package com.example.services;

import java.util.List;

import com.example.entites.Client;
import com.example.entites.Utilisateur;

// import com.example.repository.ClientRepository;

public interface ClientServiceI {
    
        // private ClientRepository clientRepository;
        void ajouterClient(String surname, String telephone, String adresse, Utilisateur utilisateur);
        List<Client> listerClients(boolean avecCompte);
        Client rechercherClientParTelephone(String telephone);
        

}
