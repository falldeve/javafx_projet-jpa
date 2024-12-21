package com.example.services;

import java.util.Date;
import java.util.List;

import com.example.entites.Dette;

public interface DetteService {
    void ajouterDette(Dette dette);
    void enregistrerPaiement(Long detteId, double montant, Date date);
    List<Dette> listerDettes();
}
