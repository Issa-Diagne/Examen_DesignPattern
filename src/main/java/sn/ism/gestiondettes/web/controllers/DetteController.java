package sn.ism.gestiondettes.web.controllers;

import sn.ism.gestiondettes.data.entities.Dette;
import sn.ism.gestiondettes.data.services.DetteService;
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
@RequestMapping("/dettes")
@RequiredArgsConstructor
@Tag(name = "Dettes", description = "API de gestion des dettes")
public class DetteController {

    private final DetteService detteService;

    @GetMapping
    @Operation(summary = "Récupérer toutes les dettes", description = "Retourne la liste de toutes les dettes")
    @ApiResponse(responseCode = "200", description = "Liste des dettes récupérée avec succès")
    public ResponseEntity<List<Dette>> getAllDettes() {
        List<Dette> dettes = detteService.getAllDettes();
        return ResponseEntity.ok(dettes);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer une dette par ID", description = "Retourne une dette spécifique par son ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Dette trouvée"),
            @ApiResponse(responseCode = "404", description = "Dette non trouvée")
    })
    public ResponseEntity<Dette> getDetteById(
            @Parameter(description = "ID de la dette", required = true)
            @PathVariable Long id) {
        return detteService.getDetteById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/with-paiements")
    @Operation(summary = "Récupérer une dette avec ses paiements", description = "Retourne une dette avec tous ses paiements")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Dette avec paiements trouvée"),
            @ApiResponse(responseCode = "404", description = "Dette non trouvée")
    })
    public ResponseEntity<Dette> getDetteWithPaiements(
            @Parameter(description = "ID de la dette", required = true)
            @PathVariable Long id) {
        return detteService.getDetteByIdWithPaiements(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/client/{clientId}")
    @Operation(summary = "Récupérer les dettes d'un client", description = "Retourne toutes les dettes d'un client spécifique")
    @ApiResponse(responseCode = "200", description = "Dettes du client récupérées")
    public ResponseEntity<List<Dette>> getDettesByClient(
            @Parameter(description = "ID du client", required = true)
            @PathVariable Long clientId) {
        List<Dette> dettes = detteService.getDettesByClientId(clientId);
        return ResponseEntity.ok(dettes);
    }

    @GetMapping("/non-soldees")
    @Operation(summary = "Récupérer les dettes non soldées", description = "Retourne toutes les dettes qui ne sont pas encore soldées")
    @ApiResponse(responseCode = "200", description = "Dettes non soldées récupérées")
    public ResponseEntity<List<Dette>> getDettesNonSoldees() {
        List<Dette> dettes = detteService.getDettesNonSoldees();
        return ResponseEntity.ok(dettes);
    }

    @GetMapping("/client/{clientId}/non-soldees")
    @Operation(summary = "Récupérer les dettes non soldées d'un client", description = "Retourne les dettes non soldées d'un client spécifique")
    @ApiResponse(responseCode = "200", description = "Dettes non soldées du client récupérées")
    public ResponseEntity<List<Dette>> getDettesNonSoldeesByClient(
            @Parameter(description = "ID du client", required = true)
            @PathVariable Long clientId) {
        List<Dette> dettes = detteService.getDettesNonSoldeesByClientId(clientId);
        return ResponseEntity.ok(dettes);
    }

    @PostMapping("/client/{clientId}")
    @Operation(summary = "Ajouter une dette à un client", description = "Crée une nouvelle dette pour un client")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Dette créée avec succès"),
            @ApiResponse(responseCode = "400", description = "Données invalides"),
            @ApiResponse(responseCode = "404", description = "Client non trouvé")
    })
    public ResponseEntity<Dette> createDette(
            @Parameter(description = "ID du client", required = true)
            @PathVariable Long clientId,
            @Parameter(description = "Données de la dette", required = true)
            @Valid @RequestBody Dette dette) {
        try {
            Dette savedDette = detteService.createDette(clientId, dette);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedDette);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Mettre à jour une dette", description = "Met à jour les informations d'une dette existante")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Dette mise à jour avec succès"),
            @ApiResponse(responseCode = "404", description = "Dette non trouvée")
    })
    public ResponseEntity<Dette> updateDette(
            @Parameter(description = "ID de la dette", required = true)
            @PathVariable Long id,
            @Parameter(description = "Nouvelles données de la dette", required = true)
            @Valid @RequestBody Dette detteDetails) {
        try {
            Dette updatedDette = detteService.updateDette(id, detteDetails);
            return ResponseEntity.ok(updatedDette);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer une dette", description = "Supprime une dette et tous ses paiements")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Dette supprimée avec succès"),
            @ApiResponse(responseCode = "404", description = "Dette non trouvée")
    })
    public ResponseEntity<Void> deleteDette(
            @Parameter(description = "ID de la dette", required = true)
            @PathVariable Long id) {
        try {
            detteService.deleteDette(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}