package com.api.parkingcontrol.repository;

import com.api.parkingcontrol.models.ParkingSpotModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ParkingSpotRepository extends JpaRepository<ParkingSpotModel, UUID> {

    //Declarando metodo, apenas declarando e não implementando;
    boolean existsByLicensePlateCar(String LicensePlateCar);
    boolean existsByParkingSpotNumber(String ParkingSpotNumber);

    boolean existsByApartmentAndBlock(String apartment, String block);

    //ExistsBy - query para saber se existe no banco de dados

}

//Extendemos o JPaRepository, por ele já possuir varios metodos prontos, para utilizarmos para transações
//com o banco de dados

//Dentro do <> Passamos qual vai ser o model desse repository, e também, o tipo de identificador;