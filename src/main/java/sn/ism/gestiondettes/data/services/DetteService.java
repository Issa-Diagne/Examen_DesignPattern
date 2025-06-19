package sn.ism.gestiondettes.data.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sn.ism.gestiondettes.data.entities.Client;
import sn.ism.gestiondettes.data.entities.Dette;
import sn.ism.gestiondettes.data.repositories.DetteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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

    public Page<Dette> getDettesByClientIdWithPagination(Long clientId, String telephone, Pageable pageable) {
        return detteRepository.findByClientIdWithTelephoneFilter(clientId, telephone, pageable);
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
        dette.setMontantPaye(BigDecimal.ZERO);
        dette.setMontantRestant(dette.getMontantDette());

        return detteRepository.save(dette);
    }

    public List<Dette> createMultipleDettes(Long clientId, List<Dette> dettes) {
        Client client = clientService.getClientById(clientId)
                .orElseThrow(() -> new RuntimeException("Client non trouvé avec l'id: " + clientId));

        dettes.forEach(dette -> {
            dette.setClient(client);
            dette.setMontantPaye(BigDecimal.ZERO);
            dette.setMontantRestant(dette.getMontantDette());
        });

        return detteRepository.saveAll(dettes);
    }

    public Dette updateDette(Long id, Dette detteDetails) {
        Dette dette = detteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dette non trouvée avec l'id: " + id));

        dette.setDate(detteDetails.getDate());
        dette.setMontantDette(detteDetails.getMontantDette());

        // Recalculer le montant restant
        dette.setMontantRestant(dette.getMontantDette().subtract(dette.getMontantPaye()));

        return detteRepository.save(dette);
    }

    public void deleteDette(Long id) {
        Dette dette = detteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dette non trouvée avec l'id: " + id));
        detteRepository.delete(dette);
    }
}