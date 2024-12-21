package com.example.services.impl;

import java.util.Date;
import java.util.List;

import com.example.entites.Dette;
import com.example.repository.DetteRepository;
import com.example.services.DetteService;

public class DetteServiceImpl implements DetteService{
    private DetteRepository detteRepository;

    public DetteServiceImpl(DetteRepository detteRepository) {
        this.detteRepository = detteRepository;
    }

    @Override
    public void ajouterDette(Dette dette) {
        detteRepository.save(dette);
    }

    @Override
    public void enregistrerPaiement(Long detteId, double montant, Date date) {
        Dette dette = detteRepository.findById(detteId);
        if (dette == null) {
            throw new RuntimeException("Dette non trouvée");
        }
        dette.enregistrerPaiement(montant, date);
        detteRepository.save(dette);
    }

    @Override
    public List<Dette> listerDettes() {
        return detteRepository.findAll(); 
    }

}