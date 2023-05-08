package com.api.parkingcontrol.services;

import com.api.parkingcontrol.models.ParkingSpotModel;
import com.api.parkingcontrol.repository.ParkingSpotRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ParkingSpotService {

    @Autowired
    ParkingSpotRepository parkingSpotRepository;

    @Transactional
    public ParkingSpotModel save(ParkingSpotModel parkingSpotModel){
        return parkingSpotRepository.save(parkingSpotModel);
    }


    public boolean existsByLicensePlateCar(String licensePlateCar) {
        return parkingSpotRepository.existsByLicensePlateCar(licensePlateCar);
    }
//Metodo um pouco mais customizado, não é aqueles metodos prontos como o save, então precisamos declarar esse metodo dentro do repository, antes de chamar ele no Service;


    public boolean existsByParkingSpotNumber(String parkingSpotNumber) {
        return  parkingSpotRepository.existsByParkingSpotNumber(parkingSpotNumber);
    }

    public boolean existsByApartmentAndBlock(String apartment, String block) {
        return parkingSpotRepository.existsByApartmentAndBlock(apartment, block);
    }

    public List<ParkingSpotModel> findAll() {
        return parkingSpotRepository.findAll();
    }

    public Optional<ParkingSpotModel> findById(UUID id) {
        return parkingSpotRepository.findById(id);
    }

    @Transactional
    public void delete(ParkingSpotModel parkingSpotModel) {
        parkingSpotRepository.delete(parkingSpotModel);
    }
}








//Service é um Bean

//////

//Ponto de injeção, comunicação interna; Controller - Service - Repository;

//@Autowired - Estou dizendo ao Spring que em determinado momentos, ele tera que injetar uma dependencia do repository no service

//////

// Metodos construtivos ou destrutivos, é importante colocar o @Transactional;
//principalmente quando temos relacionamentos, que tera deleção e cascata, ou salvamento em cascata, porque
//Caso algo de errado durante a transação, ele garante que tudo volte ao normal, e assim não teremos dados "quebrados";