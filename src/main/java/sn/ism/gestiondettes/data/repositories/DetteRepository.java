package sn.ism.gestiondettes.data.repositories;

import sn.ism.gestiondettes.data.entities.Dette;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DetteRepository extends JpaRepository<Dette, Long> {

    List<Dette> findByClientId(Long clientId);

    @Query("SELECT d FROM Dette d WHERE d.client.id = :clientId ORDER BY d.date DESC")
    List<Dette> findByClientIdOrderByDateDesc(@Param("clientId") Long clientId);

    @Query("SELECT d FROM Dette d LEFT JOIN FETCH d.paiements WHERE d.id = :id")
    Optional<Dette> findByIdWithPaiements(@Param("id") Long id);

    @Query("SELECT d FROM Dette d WHERE d.montantRestant > 0")
    List<Dette> findDettesNonSoldees();

    @Query("SELECT d FROM Dette d WHERE d.client.id = :clientId AND d.montantRestant > 0")
    List<Dette> findDettesNonSoldeesByClientId(@Param("clientId") Long clientId);
}