package org.example.service;

import org.example.dao.VenueDAO;
import org.example.model.Venue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VenueService {
    @Autowired
    private VenueDAO venueDAO;

    public Venue getVenue(Long venueId) {
        return venueDAO.findById(venueId);
    }
}
