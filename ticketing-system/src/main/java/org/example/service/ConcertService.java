package org.example.service;

import org.example.dao.ConcertDAO;
import org.example.model.Concert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConcertService {

    @Autowired
    private ConcertDAO concertDAO;

    public List<Concert> getAllConcerts() {
        return concertDAO.findAll();
    }
}
