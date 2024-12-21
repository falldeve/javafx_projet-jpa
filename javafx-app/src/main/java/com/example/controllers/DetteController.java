package com.example.controllers;

import java.util.Date;
import java.util.List;

import com.example.entites.Dette;

public interface DetteController {
    void ajouterDette(Dette dette);
    void enregistrerPaiement(Long detteId, double montant, Date date);
    List<Dette> listerDettes();
    
}
