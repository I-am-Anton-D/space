package com.space.controller;

import com.space.model.Ship;
import com.space.model.ShipType;
import com.space.service.ShipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.util.Calendar;
import java.util.List;

@RestController
public class ShipController {

    @Autowired
    private ShipService shipService;

    @GetMapping("/rest/ships")
    public ResponseEntity<List<Ship>> getShipsWithFilter(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "planet", required = false) String planet,
            @RequestParam(value = "shipType", required = false) ShipType shipType,
            @RequestParam(value = "after", required = false) Long after,
            @RequestParam(value = "before", required = false) Long before,
            @RequestParam(value = "isUsed", required = false) Boolean isUsed,
            @RequestParam(value = "minSpeed", required = false) Double minSpeed,
            @RequestParam(value = "maxSpeed", required = false) Double maxSpeed,
            @RequestParam(value = "minCrewSize", required = false) Double minCrewSize,
            @RequestParam(value = "maxCrewSize", required = false) Double maxCrewSize,
            @RequestParam(value = "minRating", required = false) Double minRating,
            @RequestParam(value = "maxRating", required = false) Double maxRating,
            @RequestParam(value = "pageNumber", required = false, defaultValue = "0") int pageNumber,
            @RequestParam(value = "pageSize", required = false, defaultValue = "3") int pageSize,
            @RequestParam(value = "order", required = false, defaultValue = "ID") ShipOrder order
    ) {
        return ResponseEntity.ok(shipService.getShips(name, planet, shipType, after, before, isUsed,
                minSpeed, maxSpeed, minCrewSize, maxCrewSize, minRating, maxRating, pageNumber, pageSize, order));
    }

    @GetMapping("/rest/ships/count")
    public ResponseEntity<Integer> getShipsCountWithFilter(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "planet", required = false) String planet,
            @RequestParam(value = "shipType", required = false) ShipType shipType,
            @RequestParam(value = "after", required = false) Long after,
            @RequestParam(value = "before", required = false) Long before,
            @RequestParam(value = "isUsed", required = false) Boolean isUsed,
            @RequestParam(value = "minSpeed", required = false) Double minSpeed,
            @RequestParam(value = "maxSpeed", required = false) Double maxSpeed,
            @RequestParam(value = "minCrewSize", required = false) Double minCrewSize,
            @RequestParam(value = "maxCrewSize", required = false) Double maxCrewSize,
            @RequestParam(value = "minRating", required = false) Double minRating,
            @RequestParam(value = "maxRating", required = false) Double maxRating
    ) {
        return ResponseEntity.ok(shipService.getShips(name, planet, shipType, after, before, isUsed,
                minSpeed, maxSpeed, minCrewSize, maxCrewSize, minRating, maxRating, 0, Integer.MAX_VALUE, ShipOrder.ID).size());
    }

    @GetMapping("/rest/ships/{id}")
    public ResponseEntity<Ship> getShip(@PathVariable("id") long id) {

        return id == 0 ? ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null) :
                shipService.getShip(id) != null ? ResponseEntity.ok(shipService.getShip(id))
                        : ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }


    @DeleteMapping("/rest/ships/{id}")
    public ResponseEntity deleteShip(@PathVariable("id") long id) {
        return id == 0 ? ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null) :
                shipService.getShip(id) != null ? ResponseEntity.ok(shipService.deleteShip(id))
                        : ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }


    @PostMapping("/rest/ships/{id}")
    public ResponseEntity updateShip(@PathVariable long id, @RequestBody Ship shipRequest) {
        if ( id <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        if (shipService.getShip(id) == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Ship ship = shipService.getShip(id);

        if (shipRequest.getName() != null) {
            if (shipRequest.getName().length() > 50 || shipRequest.getName().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            } else {
                ship.setName(shipRequest.getName());
            }
        }

        if (shipRequest.getPlanet() != null) {
            if (shipRequest.getPlanet().length() > 50 || shipRequest.getPlanet().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            } else {
                ship.setPlanet(shipRequest.getPlanet());
            }
        }

        if (shipRequest.getShipType() != null) {
            ship.setShipType(shipRequest.getShipType());
        }

        if (shipRequest.getProdDate()!= null) {
              Calendar ca = Calendar.getInstance();
              ca.setTime(shipRequest.getProdDate());
              int year = ca.get(Calendar.YEAR);
              if (year>=2800 && year<=3020) {
                  ship.setProdDate(shipRequest.getProdDate());
              } else {
                  return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
              }
        }

        if (shipRequest.getUsed()!=null) {
            ship.setUsed(shipRequest.getUsed());
        }

        if (shipRequest.getSpeed()!=null) {
            if (shipRequest.getSpeed()>0 && shipRequest.getSpeed()<1) {
                ship.setSpeed(Math.round(shipRequest.getSpeed() * 100) / 100D);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }
        }

        if (shipRequest.getCrewSize()!= null) {
            if (shipRequest.getCrewSize()>=1 && shipRequest.getCrewSize()<=9999) {
                ship.setCrewSize(shipRequest.getCrewSize());
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(ship.getProdDate());

        Double rating = (80 * ship.getSpeed() * (ship.getUsed() ? 0.5 : 1))/(3019 - calendar.get(Calendar.YEAR)+1);
        ship.setRating(Math.round(rating * 100) / 100D);

        return ResponseEntity.ok(shipService.updateShip(ship));
    }

    @PostMapping("/rest/ships")
    public ResponseEntity<?> createShip(@RequestBody Ship shipRequest) {
        if (shipRequest.getName()==null || shipRequest.getName().length()>50 || shipRequest.getPlanet()==null || shipRequest.getPlanet().length()>50
            || shipRequest.getSpeed()==null || shipRequest.getSpeed()<0 || shipRequest.getSpeed()>1 || shipRequest.getCrewSize()==null || shipRequest.getCrewSize()<1 || shipRequest.getCrewSize()>9999
                || shipRequest.getName().isEmpty() || shipRequest.getPlanet().isEmpty() || shipRequest.getProdDate()==null || shipRequest.getProdDate().getTime()<0
        ) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        boolean used = false;
        if (shipRequest.getUsed()!=null) {
            used = shipRequest.getUsed();
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(shipRequest.getProdDate());
        Double rating = (80 * shipRequest.getSpeed() * (used ? 0.5 : 1))/(3019 - calendar.get(Calendar.YEAR)+1);

        Ship ship = new Ship(shipRequest.getName(),shipRequest.getPlanet(), shipRequest.getShipType(), shipRequest.getProdDate(), used, shipRequest.getSpeed(), shipRequest.getCrewSize(), rating);
        return ResponseEntity.ok(shipService.updateShip(ship));
    }
 }
