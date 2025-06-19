package sn.ism.gestiondettes.web.controllers;

import sn.ism.gestiondettes.data.entities.Client;
import sn.ism.gestiondettes.data.services.ClientService;
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
@RequestMapping("/clients")
@RequiredArgsConstructor
@Tag(name = "Clients", description = "API de gestion des clients")
public class ClientController {

    private final ClientService clientService;

    @GetMapping
    @Operation(summary = "Récupérer tous les clients", description = "Retourne la liste de tous les clients")
    @ApiResponse(responseCode = "200", description = "Liste des clients récupérée avec succès")
    public ResponseEntity<List<Client>> getAllClients() {
        List<Client> clients = clientService.getAllClients();
        return ResponseEntity.ok(clients);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer un client par ID", description = "Retourne un client spécifique par son ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Client trouvé"),
            @ApiResponse(responseCode = "404", description = "Client non trouvé")
    })
    public ResponseEntity<Client> getClientById(
            @Parameter(description = "ID du client", required = true)
            @PathVariable Long id) {
        return clientService.getClientById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/with-dettes")
    @Operation(summary = "Récupérer un client avec ses dettes", description = "Retourne un client avec toutes ses dettes")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Client avec dettes trouvé"),
            @ApiResponse(responseCode = "404", description = "Client non trouvé")
    })
    public ResponseEntity<Client> getClientWithDettes(
            @Parameter(description = "ID du client", required = true)
            @PathVariable Long id) {
        return clientService.getClientByIdWithDettes(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    @Operation(summary = "Rechercher des clients par nom", description = "Recherche des clients par nom (recherche partielle)")
    @ApiResponse(responseCode = "200", description = "Clients trouvés")
    public ResponseEntity<List<Client>> searchClients(
            @Parameter(description = "Nom à rechercher", required = true)
            @RequestParam String nom) {
        List<Client> clients = clientService.searchClientsByNom(nom);
        return ResponseEntity.ok(clients);
    }

    @PostMapping
    @Operation(summary = "Créer un nouveau client", description = "Crée un nouveau client")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Client créé avec succès"),
            @ApiResponse(responseCode = "400", description = "Données invalides"),
            @ApiResponse(responseCode = "409", description = "Client avec ce téléphone existe déjà")
    })
    public ResponseEntity<Client> createClient(
            @Parameter(description = "Données du client", required = true)
            @Valid @RequestBody Client client) {
        try {
            Client savedClient = clientService.createClient(client);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedClient);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Mettre à jour un client", description = "Met à jour les informations d'un client existant")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Client mis à jour avec succès"),
            @ApiResponse(responseCode = "404", description = "Client non trouvé"),
            @ApiResponse(responseCode = "409", description = "Téléphone déjà utilisé par un autre client")
    })
    public ResponseEntity<Client> updateClient(
            @Parameter(description = "ID du client", required = true)
            @PathVariable Long id,
            @Parameter(description = "Nouvelles données du client", required = true)
            @Valid @RequestBody Client clientDetails) {
        try {
            Client updatedClient = clientService.updateClient(id, clientDetails);
            return ResponseEntity.ok(updatedClient);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("non trouvé")) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un client", description = "Supprime un client et toutes ses dettes")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Client supprimé avec succès"),
            @ApiResponse(responseCode = "404", description = "Client non trouvé")
    })
    public ResponseEntity<Void> deleteClient(
            @Parameter(description = "ID du client", required = true)
            @PathVariable Long id) {
        try {
            clientService.deleteClient(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}