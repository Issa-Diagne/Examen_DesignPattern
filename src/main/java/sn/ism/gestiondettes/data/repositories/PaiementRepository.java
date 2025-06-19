package sn.ism.gestiondettes.data.repositories;

import sn.ism.gestiondettes.data.entities.Paiement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaiementRepository extends JpaRepository<Paiement, Long> {

    List<Paiement> findByDetteId(Long detteId);

    @Query("SELECT p FROM Paiement p WHERE p.dette.id = :detteId ORDER BY p.datePaiement DESC")
    List<Paiement> findByDetteIdOrderByDatePaiementDesc(@Param("detteId") Long detteId);

    @Query("SELECT p FROM Paiement p WHERE p.dette.client.id = :clientId")
    List<Paiement> findByClientId(@Param("clientId") Long clientId);
}