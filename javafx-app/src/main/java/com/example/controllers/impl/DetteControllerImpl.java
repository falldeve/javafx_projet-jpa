package com.example.controllers.impl;

import java.util.Date;
import java.util.List;

import com.example.controllers.DetteController;
import com.example.entites.Dette;
import com.example.services.impl.DetteServiceImpl;

public class DetteControllerImpl implements DetteController{
    private DetteServiceImpl detteService;

    public DetteControllerImpl(DetteServiceImpl detteService) {
        this.detteService = detteService;
    }

    @Override
    public void ajouterDette(Dette dette) {
        detteService.ajouterDette(dette);
    }

    @Override
    public void enregistrerPaiement(Long detteId, double montant, Date date) {
        detteService.enregistrerPaiement(detteId, montant, date);
    }

    @Override
    public List<Dette> listerDettes() {
        return detteService.listerDettes(); 
    }
}