package sn.ism.gestiondettes.data.services;

import sn.ism.gestiondettes.data.entities.Dette;
import sn.ism.gestiondettes.data.entities.Paiement;
import sn.ism.gestiondettes.data.repositories.PaiementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class PaiementService {

    private final PaiementRepository paiementRepository;
    private final DetteService detteService;

    public List<Paiement> getAllPaiements() {
        return paiementRepository.findAll();
    }

    public Optional<Paiement> getPaiementById(Long id) {
        return paiementRepository.findById(id);
    }

    public List<Paiement> getPaiementsByDetteId(Long detteId) {
        return paiementRepository.findByDetteIdOrderByDatePaiementDesc(detteId);
    }

    public List<Paiement> getPaiementsByClientId(Long clientId) {
        return paiementRepository.findByClientId(clientId);
    }

    public Paiement createPaiement(Long detteId, Paiement paiement) {
        Dette dette = detteService.getDetteById(detteId)
                .orElseThrow(() -> new RuntimeException("Dette non trouvée avec l'id: " + detteId));

        // Vérifier que le montant du paiement ne dépasse pas le montant restant
        if (paiement.getMontant().compareTo(dette.getMontantRestant()) > 0) {
            throw new RuntimeException("Le montant du paiement (" + paiement.getMontant() +
                    ") ne peut pas être supérieur au montant restant (" + dette.getMontantRestant() + ")");
        }

        paiement.setDette(dette);
        Paiement savedPaiement = paiementRepository.save(paiement);

        // Mettre à jour le montant payé de la dette
        BigDecimal nouveauMontantPaye = dette.getMontantPaye().add(paiement.getMontant());
        dette.setMontantPaye(nouveauMontantPaye);
        dette.setMontantRestant(dette.getMontantDette().subtract(nouveauMontantPaye));

        return savedPaiement;
    }

    public void deletePaiement(Long id) {
        Paiement paiement = paiementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Paiement non trouvé avec l'id: " + id));

        Dette dette = paiement.getDette();

        // Mettre à jour le montant payé de la dette
        BigDecimal nouveauMontantPaye = dette.getMontantPaye().subtract(paiement.getMontant());
        dette.setMontantPaye(nouveauMontantPaye);
        dette.setMontantRestant(dette.getMontantDette().subtract(nouveauMontantPaye));

        paiementRepository.delete(paiement);
    }
}