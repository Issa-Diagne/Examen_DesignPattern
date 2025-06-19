package sn.ism.gestiondettes.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sn.ism.gestiondettes.data.entities.Client;
import sn.ism.gestiondettes.data.entities.Dette;
import sn.ism.gestiondettes.data.entities.Paiement;
import sn.ism.gestiondettes.data.repositories.ClientRepository;
import sn.ism.gestiondettes.data.repositories.DetteRepository;
import sn.ism.gestiondettes.data.repositories.PaiementRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner loadData(ClientRepository clientRepository,
                               DetteRepository detteRepository,
                               PaiementRepository paiementRepository) {
        return args -> {
            // Créer des clients de test
            Client client1 = new Client("Amadou Fall", "776543210", "Dakar, Sénégal");
            Client client2 = new Client("Fatou Diop", "775432109", "Thiès, Sénégal");
            Client client3 = new Client("Moussa Sarr", "774321098", "Saint-Louis, Sénégal");

            clientRepository.save(client1);
            clientRepository.save(client2);
            clientRepository.save(client3);

            // Créer des dettes de test
            Dette dette1 = new Dette();
            dette1.setDate("2024-01-15");
            dette1.setMontantDette(new BigDecimal("100000"));
            dette1.setMontantPaye(new BigDecimal("30000"));
            dette1.setMontantRestant(new BigDecimal("70000"));
            dette1.setClient(client1);

            Dette dette2 = new Dette();
            dette2.setDate("2024-02-10");
            dette2.setMontantDette(new BigDecimal("150000"));
            dette2.setMontantPaye(new BigDecimal("0"));
            dette2.setMontantRestant(new BigDecimal("150000"));
            dette2.setClient(client1);

            Dette dette3 = new Dette();
            dette3.setDate("2024-01-20");
            dette3.setMontantDette(new BigDecimal("75000"));
            dette3.setMontantPaye(new BigDecimal("75000"));
            dette3.setMontantRestant(new BigDecimal("0"));
            dette3.setClient(client2);

            detteRepository.save(dette1);
            detteRepository.save(dette2);
            detteRepository.save(dette3);

            // Créer des paiements de test
            Paiement paiement1 = new Paiement();
            paiement1.setMontant(new BigDecimal("20000"));
            paiement1.setDatePaiement(LocalDateTime.of(2024, 1, 20, 10, 0));
            paiement1.setDette(dette1);

            Paiement paiement2 = new Paiement();
            paiement2.setMontant(new BigDecimal("10000"));
            paiement2.setDatePaiement(LocalDateTime.of(2024, 1, 25, 14, 30));
            paiement2.setDette(dette1);

            Paiement paiement3 = new Paiement();
            paiement3.setMontant(new BigDecimal("75000"));
            paiement3.setDatePaiement(LocalDateTime.of(2024, 1, 22, 16, 45));
            paiement3.setDette(dette3);

            paiementRepository.save(paiement1);
            paiementRepository.save(paiement2);
            paiementRepository.save(paiement3);

            System.out.println("Données de test chargées avec succès !");
        };
    }
}