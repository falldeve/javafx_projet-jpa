package com.example;

import com.example.controllers.impl.ClientControllerImpl;
import com.example.controllers.impl.DetteControllerImpl;
import com.example.entites.Client;
import com.example.entites.Dette;
import com.example.entites.Utilisateur;
import com.example.services.impl.ClientServiceImpl;
import com.example.services.impl.DetteServiceImpl;
import com.example.repository.ClientRepository;
import com.example.repository.DetteRepository;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.sql.Date;
import java.util.Arrays;
import java.util.List;

public class AppView extends Application {
    private ClientControllerImpl clientController;
    private DetteControllerImpl detteController;
    private TableView<Client> tableView;
    private Stage primaryStage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("monPU");
        if (entityManagerFactory == null) {
            throw new IllegalStateException("EntityManagerFactory is not initialized");
        }

        // Initialisation des repositories et services
        ClientRepository clientRepository = new ClientRepository(entityManagerFactory);
        ClientServiceImpl clientService = new ClientServiceImpl(clientRepository, entityManagerFactory);
        clientController = new ClientControllerImpl(clientService);

        DetteRepository detteRepository = new DetteRepository(entityManagerFactory);
        DetteServiceImpl detteService = new DetteServiceImpl(detteRepository);
        detteController = new DetteControllerImpl(detteService);

        // Créer la barre de menu
        MenuBar menuBar = new MenuBar();
        Menu menuFile = new Menu("Fichier");
        MenuItem addClientMenuItem = new MenuItem("Ajouter Client");
        MenuItem viewClientsMenuItem = new MenuItem("Voir Clients");
        MenuItem addDetteMenuItem = new MenuItem("Ajouter Dette");
        MenuItem enregistrerPaiementMenuItem = new MenuItem("Enregistrer Paiement");

        // Actions pour les éléments du menu
        addClientMenuItem.setOnAction(e -> primaryStage.setScene(createAddClientScene()));
        viewClientsMenuItem.setOnAction(e -> primaryStage.setScene(createViewClientsScene()));
        addDetteMenuItem.setOnAction(e -> primaryStage.setScene(createAddDetteScene()));
        enregistrerPaiementMenuItem.setOnAction(e -> primaryStage.setScene(createEnregistrerPaiementScene()));

        menuFile.getItems().addAll(addClientMenuItem, viewClientsMenuItem, addDetteMenuItem, enregistrerPaiementMenuItem);
        menuBar.getMenus().add(menuFile);

        // Créer la scène principale
        VBox mainLayout = new VBox(menuBar, createAddClientLayout());
        Scene mainScene = new Scene(mainLayout, 600, 600);
        
        primaryStage.setTitle("Application JavaFX - Gestion des Clients et Dettes");
        primaryStage.setScene(mainScene);
        primaryStage.show();
    }

    private VBox createAddClientLayout() {
        Label labelSurname = new Label("Nom :");
        TextField textFieldSurname = new TextField();
        
        Label labelTelephone = new Label("Téléphone :");
        TextField textFieldTelephone = new TextField();
        
        Label labelAdresse = new Label("Adresse :");
        TextField textFieldAdresse = new TextField();
        
        Label labelEmail = new Label("Email (optionnel) :");
        TextField textFieldEmail = new TextField();
        
        Label labelLogin = new Label("Login (optionnel) :");
        TextField textFieldLogin = new TextField();
        
        Label labelPassword = new Label("Mot de passe (optionnel) :");
        TextField textFieldPassword = new TextField();
        
        Button button = new Button("Ajouter Client");

        button.setOnAction(e -> {
            String surname = textFieldSurname.getText();
            String telephone = textFieldTelephone.getText();
            String adresse = textFieldAdresse.getText();
            Utilisateur utilisateur = null;

            // Créer un utilisateur seulement si les champs sont remplis
            if (!textFieldEmail.getText().isEmpty() && !textFieldLogin.getText().isEmpty() && !textFieldPassword.getText().isEmpty()) {
                utilisateur = new Utilisateur(textFieldEmail.getText(), textFieldLogin.getText(), textFieldPassword.getText());
            }

            // Ajout du client
            try {
                clientController.ajouterClient(surname, telephone, adresse, utilisateur);
                // Nettoyer les champs après ajout
                textFieldSurname.clear();
                textFieldTelephone.clear();
                textFieldAdresse.clear();
                textFieldEmail.clear();
                textFieldLogin.clear();
                textFieldPassword.clear();
            } catch (Exception ex) {
                System.out.println("Erreur lors de l'ajout du client : " + ex.getMessage());
            }
        });

        return new VBox(10, labelSurname, textFieldSurname, labelTelephone, textFieldTelephone, 
                        labelAdresse, textFieldAdresse, labelEmail, textFieldEmail, 
                        labelLogin, textFieldLogin, labelPassword, textFieldPassword, 
                        button);
    }

    private Scene createAddClientScene() {
        VBox layout = createAddClientLayout();
        MenuBar menuBar = createMenuBar(); // Créez le MenuBar à chaque fois
        VBox mainLayout = new VBox(menuBar, layout);
        return new Scene(mainLayout, 600, 600);
    }

    private Scene createViewClientsScene() {
        VBox layout = createViewClientsLayout();
        MenuBar menuBar = createMenuBar(); // Créez le MenuBar à chaque fois
        VBox mainLayout = new VBox(menuBar, layout);
        return new Scene(mainLayout, 600, 600);
    }

    private VBox createViewClientsLayout() {
        Label label = new Label("Liste des Clients");
        tableView = new TableView<>(); // Initialisation de tableView
    
        // Configurez les colonnes de la table
        TableColumn<Client, String> surnameColumn = new TableColumn<>("Nom");
        surnameColumn.setCellValueFactory(new PropertyValueFactory<>("surname"));
    
        TableColumn<Client, String> telephoneColumn = new TableColumn<>("Téléphone");
        telephoneColumn.setCellValueFactory(new PropertyValueFactory<>("telephone"));
    
        TableColumn<Client, String> adresseColumn = new TableColumn<>("Adresse");
        adresseColumn.setCellValueFactory(new PropertyValueFactory<>("adresse"));
    
        // Ajoutez les colonnes à la table
        tableView.getColumns().addAll(surnameColumn, telephoneColumn, adresseColumn);
    
        // Remplissez la table avec des données (exemple)
        updateClientList(false); // Appel pour remplir la table

        // Ajoutez un champ de recherche
        Label labelSearchTelephone = new Label("Rechercher par téléphone :");
        TextField textFieldSearchTelephone = new TextField();
        Button buttonSearch = new Button("Rechercher");

        buttonSearch.setOnAction(e -> {
            String telephone = textFieldSearchTelephone.getText();
            System.out.println("Recherche du client avec le téléphone : " + telephone); // Debug
            Client client = clientController.rechercherClientParTelephone(telephone);
            if (client != null) {
                // Affichez les informations du client (par exemple, dans une alerte ou un label)
                System.out.println("Client trouvé : " + client.getSurname() + ", " + client.getTelephone());
                // Vous pouvez également mettre à jour la TableView pour afficher le client trouvé
                tableView.getItems().clear(); // Effacer les éléments existants
                tableView.getItems().add(client); // Ajouter le client trouvé
            } else {
                System.out.println("Aucun client trouvé avec ce numéro de téléphone.");
                // Vous pouvez afficher un message d'erreur ou une alerte ici
            }
        });

        return new VBox(10, label, tableView, labelSearchTelephone, textFieldSearchTelephone, buttonSearch);
    }

    private Scene createAddDetteScene() {
        VBox layout = createAddDetteLayout();
        MenuBar menuBar = createMenuBar(); // Créez le MenuBar à chaque fois
        VBox mainLayout = new VBox(menuBar, layout);
        return new Scene(mainLayout, 600, 600);
    }

    private VBox createAddDetteLayout() {
        Label labelClient = new Label("Sélectionner un Client :");
        ComboBox<Client> comboBoxClients = new ComboBox<>();
        comboBoxClients.getItems().addAll(clientController.listerClients(false)); // Remplir la ComboBox avec les clients
    
        // Configurer le ComboBox pour afficher uniquement le nom du client
        comboBoxClients.setCellFactory(new Callback<ListView<Client>, ListCell<Client>>() {
            @Override
            public ListCell<Client> call(ListView<Client> param) {
                return new ListCell<Client>() {
                    @Override
                    protected void updateItem(Client item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setText(null);
                        } else {
                            setText(item.getSurname()); // Afficher uniquement le nom du client
                        }
                    }
                };
            }
        });
    
        // Configurer le ComboBox pour afficher le nom du client sélectionné
        comboBoxClients.setButtonCell(new ListCell<Client>() {
            @Override
            protected void updateItem(Client item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getSurname()); // Afficher uniquement le nom du client
                }
            }
        });
    
        Label labelDate = new Label("Date :");
        DatePicker datePicker = new DatePicker();
    
        Label labelMontant = new Label("Montant :");
        TextField textFieldMontant = new TextField();
    
        Label labelMontantVerser = new Label("Montant Versé :");
        TextField textFieldMontantVerser = new TextField();
    
        Label labelMontantRestant = new Label("Montant Restant :");
        TextField textFieldMontantRestant = new TextField();
    
        Label labelArticles = new Label("Articles (séparés par des virgules) :");
        TextField textFieldArticles = new TextField();
    
        Button buttonAjouter = new Button("Ajouter Dette");
    
        buttonAjouter.setOnAction(e -> {
            Date date = java.sql.Date.valueOf(datePicker.getValue());
            double montant = Double.parseDouble(textFieldMontant.getText());
            double montantVerser = Double.parseDouble(textFieldMontantVerser.getText());
            double montantRestant = Double.parseDouble(textFieldMontantRestant.getText());
            List<String> articles = Arrays.asList(textFieldArticles.getText().split(","));
            Client selectedClient = comboBoxClients.getValue(); // Récupérer le client sélectionné
    
            Dette dette = new Dette(date, montant, montantVerser, montantRestant, articles, selectedClient);
            detteController.ajouterDette(dette); // Appel au contrôleur pour ajouter la dette
    
            // Réinitialiser les champs
            datePicker.setValue(null);
            textFieldMontant.clear();
            textFieldMontantVerser.clear();
            textFieldMontantRestant.clear();
            textFieldArticles.clear();
        });
    
        return new VBox(10, labelClient, comboBoxClients, labelDate, datePicker, labelMontant, textFieldMontant,
                        labelMontantVerser, textFieldMontantVerser, labelMontantRestant,
                        textFieldMontantRestant, labelArticles, textFieldArticles, buttonAjouter);
    }

    private Scene createEnregistrerPaiementScene() {
        VBox layout = createEnregistrerPaiementLayout();
        MenuBar menuBar = createMenuBar(); // Créez le MenuBar à chaque fois
        VBox mainLayout = new VBox(menuBar, layout);
        return new Scene(mainLayout, 600, 600);
    }

    private VBox createEnregistrerPaiementLayout() {
        Label labelDette = new Label("Sélectionner une Dette :");
        ComboBox<Dette> comboBoxDettes = new ComboBox<>();
        comboBoxDettes.getItems().addAll(detteController.listerDettes()); // Remplir la ComboBox avec les dettes

        Label labelMontant = new Label("Montant du Paiement :");
        TextField textFieldMontant = new TextField();

        Label labelDate = new Label("Date du Paiement :");
        DatePicker datePicker = new DatePicker();

        Button buttonEnregistrer = new Button("Enregistrer Paiement");

        buttonEnregistrer.setOnAction(e -> {
            Dette selectedDette = comboBoxDettes.getValue();
            double montant = Double.parseDouble(textFieldMontant.getText());
            Date date = java.sql.Date.valueOf(datePicker.getValue());

            if (selectedDette != null) {
                // Utiliser le contrôleur pour enregistrer le paiement
                detteController.enregistrerPaiement(selectedDette.getId(), montant, date);
                // Réinitialiser les champs
                comboBoxDettes.getSelectionModel().clearSelection();
                textFieldMontant.clear();
                datePicker.setValue(null);
            } else {
                System.out.println("Veuillez sélectionner une dette.");
            }
        });

        return new VBox(10, labelDette, comboBoxDettes, labelMontant, textFieldMontant, labelDate, datePicker, buttonEnregistrer);
    }

    // Méthode pour créer le MenuBar
    private MenuBar createMenuBar() {
        MenuBar menuBar = new MenuBar();
        Menu menuFile = new Menu("Fichier");
        MenuItem addClientMenuItem = new MenuItem("Ajouter Client");
        MenuItem viewClientsMenuItem = new MenuItem("Voir Clients");
        MenuItem addDetteMenuItem = new MenuItem("Ajouter Dette");
        MenuItem enregistrerPaiementMenuItem = new MenuItem("Enregistrer Paiement");

        // Actions pour les éléments du menu
        addClientMenuItem.setOnAction(e -> primaryStage.setScene(createAddClientScene()));
        viewClientsMenuItem.setOnAction(e -> primaryStage.setScene(createViewClientsScene()));
        addDetteMenuItem.setOnAction(e -> primaryStage.setScene(createAddDetteScene()));
        enregistrerPaiementMenuItem.setOnAction(e -> primaryStage.setScene(createEnregistrerPaiementScene())); // Nouvelle action

        menuFile.getItems().addAll(addClientMenuItem, viewClientsMenuItem, addDetteMenuItem, enregistrerPaiementMenuItem);
        menuBar.getMenus().add(menuFile);
        return menuBar;
    }

    private void updateClientList(boolean avecCompte) {
        tableView.setItems(clientController.listerClients(avecCompte)); // Mettre à jour la table avec les clients filtrés
    }

    @Override
    public void stop() throws Exception {
        // Fermer l'EntityManagerFactory si nécessaire
        if (clientController != null) {
            clientController.closeEntityManagerFactory();
        }
        super.stop();
    }
}