package com.api.parkingcontrol.controller;

import com.api.parkingcontrol.dto.ParkingSpotDto;
import com.api.parkingcontrol.models.ParkingSpotModel;
import com.api.parkingcontrol.repository.ParkingSpotRepository;
import com.api.parkingcontrol.services.ParkingSpotService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.support.Repositories;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/parking-spot")
public class ParkingSpotController {

    @Autowired
    ParkingSpotRepository parkingSpotRepository;
    @Autowired
    ParkingSpotService parkingSpotService;


//getAll - Listagem de todas as vagas de estacionamento salvas da api;
    @GetMapping
    public ResponseEntity<List<ParkingSpotModel>> getAllParkingSpots(){
        return ResponseEntity.status(HttpStatus.OK).body(parkingSpotService.findAll());
    }


//getOneParkingSpot - Buscar a vaga pelo ID;
    @GetMapping("/{id}")
    public ResponseEntity<Object> getOneParkingSpot(@PathVariable(value = "id")UUID id){
        Optional<ParkingSpotModel> parkingSpotModelOptional = parkingSpotService.findById(id);
        if(!parkingSpotModelOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Parking spot not found");
        }
        return ResponseEntity.status(HttpStatus.OK).body(parkingSpotModelOptional.get());
    }



    @PostMapping
    public ResponseEntity<Object> saveParkingSpot(@RequestBody @Valid ParkingSpotDto parkingSpotDto){
        if(parkingSpotService.existsByLicensePlateCar(parkingSpotDto.getLicensePlateCar())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: License Car is already in use!");
        }
//O Service está usando a função "exists..." para:
//Verificar se já existe alguma placa com os mesmos dados, se sim, aparece a mensagem;

        if(parkingSpotService.existsByParkingSpotNumber(parkingSpotDto.getParkingSpotNumber())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: Parking Spot is already in use!");
        }
//Verifincnado se para essa vaga que esta sendo criada, existe algum registro;

        if(parkingSpotService.existsByApartmentAndBlock(parkingSpotDto.getApartment(), parkingSpotDto.getBlock())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: Parking Spot already registred for this apartment/block!");
        }
//Verifica se esse apartamento com esse bloco já existe registro, por que ele tem direito a uma vaga de estacionamento;
//Colocar tudo em um "custom validation" fora do metodo;

        var parkingSpotModel = new ParkingSpotModel();
        BeanUtils.copyProperties(parkingSpotDto, parkingSpotModel);
        parkingSpotModel.setRegistrationDate(LocalDateTime.now(ZoneId.of("UTC")));
        return ResponseEntity.status(HttpStatus.CREATED).body(parkingSpotRepository.save(parkingSpotModel));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateParkingSpot(@PathVariable (value = "id") UUID id,
                                                    @RequestBody @Valid ParkingSpotDto parkingSpotDto){
        Optional<ParkingSpotModel> parkingSpotModelOptional = parkingSpotService.findById(id);
        if(!parkingSpotModelOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Parking Spot not found!");
        }

        var parkingSpotModel = new ParkingSpotModel();
        BeanUtils.copyProperties(parkingSpotDto, parkingSpotModel);
        parkingSpotModel.setId(parkingSpotModelOptional.get().getId());
        parkingSpotModel.setRegistrationDate(parkingSpotModelOptional.get().getRegistrationDate());
        return ResponseEntity.status(HttpStatus.OK).body(parkingSpotService.save(parkingSpotModel));

//Crio uma nova instancia de Model;
//Conveto usando o BeanUtils;
//Seto o id para permanecer o mesmo id e também a data de registro;


    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteParkingSport(@PathVariable(value = "id") UUID id){
        Optional<ParkingSpotModel> parkingSpotModelOptional = parkingSpotService.findById(id);
        if(!parkingSpotModelOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Parking spot not found!");
        }
        parkingSpotService.delete(parkingSpotModelOptional.get());
        return ResponseEntity.status(HttpStatus.OK).body("Parking Spot deleted successfuly!");
    }





}

//@CrossOrigin(origin = "*"), estou dizendo que pode ser acessado de qualquer fonte;


//Metedo POST:

//RespondeEntity para montar uma resposta, tanto com o status e também com o corpo;

//////

//O <Object>, por que teremos diferentes tipos de retorno, dependendo de algumas verificações;

//////

//@Valid - Valida se as validações pedidas no DTO são válidas;

/////


//BeanUtils.copyProperties(parkingspotDto, parkingSpotModel) - estou convertendo o dto em model;

//Os dados de entrada são DTOs, porém quando vamos salvar na base de dados, o MODEL que é salvo
//então convertemos o DTO para MODEL

//////



