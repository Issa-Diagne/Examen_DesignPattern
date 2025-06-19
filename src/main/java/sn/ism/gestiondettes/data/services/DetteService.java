package sn.ism.gestiondettes.data.services;

import sn.ism.gestiondettes.data.entities.Client;
import sn.ism.gestiondettes.data.entities.Dette;
import sn.ism.gestiondettes.data.repositories.DetteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class DetteService {

    private final DetteRepository detteRepository;
    private final ClientService clientService;

    public List<Dette> getAllDettes() {
        return detteRepository.findAll();
    }

    public Optional<Dette> getDetteById(Long id) {
        return detteRepository.findById(id);
    }

    public Optional<Dette> getDetteByIdWithPaiements(Long id) {
        return detteRepository.findByIdWithPaiements(id);
    }

    public List<Dette> getDettesByClientId(Long clientId) {
        return detteRepository.findByClientIdOrderByDateDesc(clientId);
    }

    public List<Dette> getDettesNonSoldees() {
        return detteRepository.findDettesNonSoldees();
    }

    public List<Dette> getDettesNonSoldeesByClientId(Long clientId) {
        return detteRepository.findDettesNonSoldeesByClientId(clientId);
    }

    public Dette createDette(Long clientId, Dette dette) {
        Client client = clientService.getClientById(clientId)
                .orElseThrow(() -> new RuntimeException("Client non trouvé avec l'id: " + clientId));

        dette.setClient(client);
        // Le montant restant sera calculé automatiquement par @PrePersist
        return detteRepository.save(dette);
    }

    public Dette updateDette(Long id, Dette detteDetails) {
        Dette dette = detteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dette non trouvée avec l'id: " + id));

        dette.setDate(detteDetails.getDate());
        dette.setMontantDette(detteDetails.getMontantDette());

        return detteRepository.save(dette);
    }

    public void deleteDette(Long id) {
        Dette dette = detteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dette non trouvée avec l'id: " + id));
        detteRepository.delete(dette);
    }

    public boolean existsById(Long id) {
        return detteRepository.existsById(id);
    }
}