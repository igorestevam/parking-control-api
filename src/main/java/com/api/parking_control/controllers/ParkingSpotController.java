package com.api.parking_control.controllers;

import com.api.parking_control.dtos.ParkingSpotDTO;
import com.api.parking_control.models.ParkingSpotModel;
import com.api.parking_control.services.ParkingSpotService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/parking-spot")
public class ParkingSpotController {

    final ParkingSpotService parkingSpotService;

    public ParkingSpotController(ParkingSpotService parkingSpotService){
        this.parkingSpotService = parkingSpotService;
    }

    @PostMapping
    public ResponseEntity<Object> saveParkingSpot(@RequestBody @Valid ParkingSpotDTO parkingSpotDTO){

        if(parkingSpotService.existsByLicensePlateCar(parkingSpotDTO.getLicensePlateCar())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: License Plate Car is already is use!");
        }

        if(parkingSpotService.existsByParkingSpotNumber(parkingSpotDTO.getParkingSpotNumber())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: Parking Spot is already is use!");
        }

        if(parkingSpotService.existsByApartmentAndBlock(parkingSpotDTO.getApartment(), parkingSpotDTO.getBlock())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: Parking Spot is already registered for this apartment/block!");
        }

        ParkingSpotModel parkingSpotModel = new ParkingSpotModel();

        BeanUtils.copyProperties(parkingSpotDTO, parkingSpotModel);

        parkingSpotModel.setRegistrationDate(LocalDateTime.now(ZoneId.of("UTC")));

        return ResponseEntity.status(HttpStatus.CREATED).body(parkingSpotService.save(parkingSpotModel));
    }

    @GetMapping
    public ResponseEntity<Page<ParkingSpotModel>> getAllParkingSpots(@PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable){
        return ResponseEntity.status(HttpStatus.OK).body(parkingSpotService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getOneParkingSpot(@PathVariable UUID id){

        Optional<ParkingSpotModel> parkingSpotModelOptional = parkingSpotService.findById(id);

        return parkingSpotModelOptional
                .<ResponseEntity<Object>>map(parkingSpotModel -> ResponseEntity
                        .status(HttpStatus.OK)
                        .body(parkingSpotModel))
                .orElseGet(() -> ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("Parking Spot not found."));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteParkingSpot(@PathVariable UUID id){

        Optional<ParkingSpotModel> parkingSpotModelOptional = parkingSpotService.findById(id);

        if(parkingSpotModelOptional.isEmpty()){
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Parking Spot not found.");
        }

        parkingSpotService.delete(parkingSpotModelOptional.get());
        return ResponseEntity
                    .status(HttpStatus.OK)
                    .body("Parking Spot deleted successfully.");
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateParkingSpot(@PathVariable UUID id,
                                                    @RequestBody @Valid ParkingSpotDTO parkingSpotDTO
    ){
        Optional<ParkingSpotModel> parkingSpotModelOptional = parkingSpotService.findById(id);

        if(parkingSpotModelOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Parking Spot not found.");
        }

        ParkingSpotModel parkingSpotModel = new ParkingSpotModel();

        BeanUtils.copyProperties(parkingSpotDTO, parkingSpotModel);
        parkingSpotModel.setId(parkingSpotModelOptional.get().getId());
        parkingSpotModel.setRegistrationDate(parkingSpotModelOptional.get().getRegistrationDate());

        return ResponseEntity.status(HttpStatus.OK).body(parkingSpotService.save(parkingSpotModel));
    }

}
