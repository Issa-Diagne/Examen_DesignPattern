package sn.ism.gestiondettes.config;

import sn.ism.gestiondettes.data.entities.Client;
import sn.ism.gestiondettes.data.entities.Dette;
import sn.ism.gestiondettes.data.entities.Paiement;
import sn.ism.gestiondettes.data.repositories.ClientRepository;
import sn.ism.gestiondettes.data.repositories.DetteRepository;
import sn.ism.gestiondettes.data.repositories.PaiementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final ClientRepository clientRepository;
    private final DetteRepository detteRepository;
    private final PaiementRepository paiementRepository;

    @Override
    public void run(String... args) throws Exception {
        // Vérifier si des données existent déjà
        if (clientRepository.count() > 0) {
            return;
        }

        // Créer des clients de test
        Client client1 = new Client("Amadou Ba", "772345678", "Thiès, Sénégal");
        Client client2 = new Client("Fatou Diop", "783456789", "Dakar, Sénégal");
        Client client3 = new Client("Moussa Sall", "704567890", "Saint-Louis, Sénégal");


        clientRepository.save(client1);
        clientRepository.save(client2);
        clientRepository.save(client3);

        // Créer des dettes de test
        Dette dette1 = new Dette();
        dette1.setDate("2024-01-15");
        dette1.setMontantDette(new BigDecimal("50000"));
        dette1.setMontantPaye(new BigDecimal("20000"));
        dette1.setClient(client1);
        detteRepository.save(dette1);

        Dette dette2 = new Dette();
        dette2.setDate("2024-02-10");
        dette2.setMontantDette(new BigDecimal("75000"));
        dette2.setMontantPaye(new BigDecimal("0"));
        dette2.setClient(client1);
        detteRepository.save(dette2);

        Dette dette3 = new Dette();
        dette3.setDate("2024-01-20");
        dette3.setMontantDette(new BigDecimal("30000"));
        dette3.setMontantPaye(new BigDecimal("30000"));
        dette3.setClient(client2);
        detteRepository.save(dette3);

        Dette dette4 = new Dette();
        dette4.setDate("2024-03-05");
        dette4.setMontantDette(new BigDecimal("100000"));
        dette4.setMontantPaye(new BigDecimal("25000"));
        dette4.setClient(client3);
        detteRepository.save(dette4);

        // Créer des paiements de test
        Paiement paiement1 = new Paiement();
        paiement1.setMontant(new BigDecimal("10000"));
        paiement1.setDatePaiement(LocalDateTime.of(2024, 1, 20, 10, 0));
        paiement1.setDette(dette1);
        paiementRepository.save(paiement1);

        Paiement paiement2 = new Paiement();
        paiement2.setMontant(new BigDecimal("10000"));
        paiement2.setDatePaiement(LocalDateTime.of(2024, 2, 5, 14, 30));
        paiement2.setDette(dette1);
        paiementRepository.save(paiement2);

        Paiement paiement3 = new Paiement();
        paiement3.setMontant(new BigDecimal("30000"));
        paiement3.setDatePaiement(LocalDateTime.of(2024, 2, 1, 9, 15));
        paiement3.setDette(dette3);
        paiementRepository.save(paiement3);

        Paiement paiement4 = new Paiement();
        paiement4.setMontant(new BigDecimal("25000"));
        paiement4.setDatePaiement(LocalDateTime.of(2024, 3, 15, 16, 45));
        paiement4.setDette(dette4);
        paiementRepository.save(paiement4);

        System.out.println("Données d'initialisation chargées avec succès !");
        System.out.println("- " + clientRepository.count() + " clients créés");
        System.out.println("- " + detteRepository.count() + " dettes créées");
        System.out.println("- " + paiementRepository.count() + " paiements créés");
    }
}