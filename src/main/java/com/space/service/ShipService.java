package com.space.service;


import com.space.controller.ShipOrder;
import com.space.model.Ship;
import com.space.model.ShipType;
import com.space.repository.ShipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class ShipService {

    @Autowired
    private ShipRepository shipRepository;

    public List<Ship> getShips(String name, String planet, ShipType shipType, Long after, Long before, Boolean isUsed,
                               Double minSpeed, Double maxSpeed, Double minCrewSize, Double maxCrewSize, Double minRating,
                               Double maxRating, int pageNumber, int pageSize, ShipOrder order) {

        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(order.getFieldName()).ascending());
        String type = shipType != null ? shipType.toString() : null;
        Integer afterYear = null;
        Integer beforeYear =  null;
        if (after!=null) {
            Calendar calafter = Calendar.getInstance();
            calafter.setTime(new Date(after));
            afterYear = calafter.get(Calendar.YEAR);
        }

        if (before!=null) {
            Calendar calbefore = Calendar.getInstance();
            calbefore.setTime(new Date(before));
            beforeYear = calbefore.get(Calendar.YEAR);
        }

        return shipRepository.getShips(name, planet, type, afterYear, beforeYear, isUsed, minSpeed, maxSpeed,
                minCrewSize, maxCrewSize, minRating, maxRating, pageable);
    }

    public Ship getShip(long id) {
        if (shipRepository.findById(id).isPresent()) {
            return shipRepository.findById(id).get();
        }
        return null;
    }

    public Ship deleteShip(long id) {
       shipRepository.deleteById(id);
       return null;
    }

    public Ship updateShip(Ship ship) {

        return shipRepository.save(ship);
    }
}
