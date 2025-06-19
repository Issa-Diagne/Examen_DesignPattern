package sn.ism.gestiondettes.data.repositories;

import sn.ism.gestiondettes.data.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    Optional<Client> findByTelephone(String telephone);

    @Query("SELECT c FROM Client c WHERE c.nom LIKE %:nom%")
    List<Client> findByNomContaining(@Param("nom") String nom);

    @Query("SELECT c FROM Client c LEFT JOIN FETCH c.dettes WHERE c.id = :id")
    Optional<Client> findByIdWithDettes(@Param("id") Long id);

    boolean existsByTelephone(String telephone);
}