package sn.ism.gestiondettes.data.services;

import sn.ism.gestiondettes.data.entities.Client;
import sn.ism.gestiondettes.data.repositories.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ClientService {

    private final ClientRepository clientRepository;

    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    public Optional<Client> getClientById(Long id) {
        return clientRepository.findById(id);
    }

    public Optional<Client> getClientByIdWithDettes(Long id) {
        return clientRepository.findByIdWithDettes(id);
    }

    public Optional<Client> getClientByTelephone(String telephone) {
        return clientRepository.findByTelephone(telephone);
    }

    public List<Client> searchClientsByNom(String nom) {
        return clientRepository.findByNomContaining(nom);
    }

    public Client createClient(Client client) {
        if (clientRepository.existsByTelephone(client.getTelephone())) {
            throw new RuntimeException("Un client avec ce numéro de téléphone existe déjà");
        }
        return clientRepository.save(client);
    }

    public Client updateClient(Long id, Client clientDetails) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client non trouvé avec l'id: " + id));

        // Vérifier si le nouveau téléphone n'existe pas déjà pour un autre client
        if (!client.getTelephone().equals(clientDetails.getTelephone()) &&
                clientRepository.existsByTelephone(clientDetails.getTelephone())) {
            throw new RuntimeException("Un client avec ce numéro de téléphone existe déjà");
        }

        client.setNom(clientDetails.getNom());
        client.setTelephone(clientDetails.getTelephone());
        client.setAdresse(clientDetails.getAdresse());

        return clientRepository.save(client);
    }

    public void deleteClient(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client non trouvé avec l'id: " + id));
        clientRepository.delete(client);
    }

    public boolean existsById(Long id) {
        return clientRepository.existsById(id);
    }
}