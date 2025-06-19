package sn.ism.gestiondettes.data.repositories;

import sn.ism.gestiondettes.data.entities.Dette;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    // Nouvelle méthode pour la pagination avec filtre par téléphone
    @Query("SELECT d FROM Dette d WHERE d.client.id = :clientId " +
            "AND (:telephone IS NULL OR d.client.telephone LIKE %:telephone%) " +
            "ORDER BY d.date DESC")
    Page<Dette> findByClientIdWithTelephoneFilter(@Param("clientId") Long clientId,
                                                  @Param("telephone") String telephone,
                                                  Pageable pageable);
}