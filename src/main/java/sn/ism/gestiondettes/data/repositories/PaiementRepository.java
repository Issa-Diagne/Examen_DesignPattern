package sn.ism.gestiondettes.data.repositories;

import sn.ism.gestiondettes.data.entities.Paiement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    // Nouvelle méthode pour la pagination avec filtres
    @Query("SELECT p FROM Paiement p WHERE p.dette.id = :detteId " +
            "AND (:telephone IS NULL OR p.dette.client.telephone LIKE %:telephone%) " +
            "AND (:numerodette IS NULL OR p.dette.id = :numerodette) " +
            "ORDER BY p.datePaiement DESC")
    Page<Paiement> findByDetteIdWithFilters(@Param("detteId") Long detteId,
                                            @Param("telephone") String telephone,
                                            @Param("numerodette") Long numerodette,
                                            Pageable pageable);

    // Méthode alternative pour chercher par numéro de dette directement
    @Query("SELECT p FROM Paiement p WHERE " +
            "(:detteId IS NULL OR p.dette.id = :detteId) " +
            "AND (:telephone IS NULL OR p.dette.client.telephone LIKE %:telephone%) " +
            "AND (:numerodette IS NULL OR p.dette.id = :numerodette) " +
            "ORDER BY p.datePaiement DESC")
    Page<Paiement> findPaiementsWithFilters(@Param("detteId") Long detteId,
                                            @Param("telephone") String telephone,
                                            @Param("numerodette") Long numerodette,
                                            Pageable pageable);
}