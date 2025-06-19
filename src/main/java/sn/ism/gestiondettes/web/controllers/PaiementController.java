package sn.ism.gestiondettes.web.controllers;

import sn.ism.gestiondettes.data.entities.Paiement;
import sn.ism.gestiondettes.data.services.PaiementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/paiements")
@RequiredArgsConstructor
@Tag(name = "Paiements", description = "API de gestion des paiements")
public class PaiementController {

    private final PaiementService paiementService;

    @GetMapping
    @Operation(summary = "Récupérer tous les paiements", description = "Retourne la liste de tous les paiements")
    @ApiResponse(responseCode = "200", description = "Liste des paiements récupérée avec succès")
    public ResponseEntity<List<Paiement>> getAllPaiements() {
        List<Paiement> paiements = paiementService.getAllPaiements();
        return ResponseEntity.ok(paiements);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer un paiement par ID", description = "Retourne un paiement spécifique par son ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Paiement trouvé"),
            @ApiResponse(responseCode = "404", description = "Paiement non trouvé")
    })
    public ResponseEntity<Paiement> getPaiementById(
            @Parameter(description = "ID du paiement", required = true)
            @PathVariable Long id) {
        return paiementService.getPaiementById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/dette/{detteId}")
    @Operation(summary = "Récupérer les paiements d'une dette", description = "Retourne tous les paiements d'une dette spécifique")
    @ApiResponse(responseCode = "200", description = "Paiements de la dette récupérés")
    public ResponseEntity<List<Paiement>> getPaiementsByDette(
            @Parameter(description = "ID de la dette", required = true)
            @PathVariable Long detteId) {
        List<Paiement> paiements = paiementService.getPaiementsByDetteId(detteId);
        return ResponseEntity.ok(paiements);
    }

    @GetMapping("/client/{clientId}")
    @Operation(summary = "Récupérer les paiements d'un client", description = "Retourne tous les paiements effectués par un client")
    @ApiResponse(responseCode = "200", description = "Paiements du client récupérés")
    public ResponseEntity<List<Paiement>> getPaiementsByClient(
            @Parameter(description = "ID du client", required = true)
            @PathVariable Long clientId) {
        List<Paiement> paiements = paiementService.getPaiementsByClientId(clientId);
        return ResponseEntity.ok(paiements);
    }

    @PostMapping("/dette/{detteId}")
    @Operation(summary = "Ajouter un paiement à une dette", description = "Crée un nouveau paiement pour une dette")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Paiement créé avec succès"),
            @ApiResponse(responseCode = "400", description = "Données invalides ou montant supérieur au montant restant"),
            @ApiResponse(responseCode = "404", description = "Dette non trouvée")
    })
    public ResponseEntity<Paiement> createPaiement(
            @Parameter(description = "ID de la dette", required = true)
            @PathVariable Long detteId,
            @Parameter(description = "Données du paiement", required = true)
            @Valid @RequestBody Paiement paiement) {
        try {
            Paiement savedPaiement = paiementService.createPaiement(detteId, paiement);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedPaiement);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("non trouvée")) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un paiement", description = "Supprime un paiement et met à jour la dette associée")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Paiement supprimé avec succès"),
            @ApiResponse(responseCode = "404", description = "Paiement non trouvé")
    })
    public ResponseEntity<Void> deletePaiement(
            @Parameter(description = "ID du paiement", required = true)
            @PathVariable Long id) {
        try {
            paiementService.deletePaiement(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
