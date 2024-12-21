package com.example.services.impl;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import com.example.entites.Client;
import com.example.entites.Utilisateur;
import com.example.repository.ClientRepository;
import com.example.services.ClientServiceI;

public class ClientServiceImpl implements ClientServiceI{
      private ClientRepository clientRepository;

    public ClientServiceImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    // ... code existant ...
    private EntityManagerFactory entityManagerFactory; // Ajout de la déclaration

    private EntityManager entityManager;
    public ClientServiceImpl(ClientRepository clientRepository, EntityManagerFactory entityManagerFactory) { 
        this.clientRepository = clientRepository;
        this.entityManagerFactory = entityManagerFactory; 
    }
    public ClientServiceImpl(EntityManagerFactory entityManagerFactory){
        this.entityManagerFactory=entityManagerFactory;
    }
// ... code existant ...
    @Override
    public void ajouterClient(String surname, String telephone, String adresse, Utilisateur utilisateur) {
        Client client = new Client(surname, telephone, adresse);
        client.setUtilisateur(utilisateur); // Associer l'utilisateur si fourni
        clientRepository.save(client);
    }
    @Override
    public Client rechercherClientParTelephone(String telephone) {
        return clientRepository.findByTelephone(telephone); // Assurez-vous que cette méthode existe dans votre ClientRepository
    }

    @Override
      // ... code existant ...
      public ObservableList<Client> listerClients(boolean avecCompte) {
        if (entityManagerFactory == null) {
            throw new IllegalStateException("EntityManagerFactory is not initialized");
        }
    
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            List<Client> clients;
            if (avecCompte) {
                // Si vous souhaitez filtrer par compte, vous pouvez utiliser une requête personnalisée
                String queryStr = "SELECT c FROM Client c WHERE c.utilisateur IS NOT NULL";
                clients = em.createQuery(queryStr, Client.class).getResultList();
            } else {
                // Récupérer tous les clients
                clients = clientRepository.findAll(); // Utilisation de findAll
            }
            
            System.out.println("Nombre de clients récupérés : " + clients.size()); // Debug
            return FXCollections.observableArrayList(clients);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la récupération des clients", e);
        } finally {
            em.close();
        }
    }
    }

    

