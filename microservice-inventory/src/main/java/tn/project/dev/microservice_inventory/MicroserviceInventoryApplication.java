package tn.project.dev.microservice_inventory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;
import tn.project.dev.microservice_inventory.model.Product;
import tn.project.dev.microservice_inventory.ProductRepository;

@SpringBootApplication
public class MicroserviceInventoryApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroserviceInventoryApplication.class, args);
	}

	@Component
	public static class DataLoader implements CommandLineRunner {

		@Autowired
		private ProductRepository repository;

		@Override
		public void run(String... args) {
			// Vérifier si la base est vide
			if (repository.count() == 0) {
				// CORRECTION : Utiliser le bon constructeur
				// Product(name, description, price, quantity)
				repository.save(new Product("Ordinateur Portable", "PC Gamer", 999.99, 10));
				repository.save(new Product("Souris Gaming", "Souris RGB", 49.99, 50));

				// Optionnel : ajouter plus de produits
				repository.save(new Product("Clavier Mécanique", "Clavier RGB", 89.99, 30));
				repository.save(new Product("Écran 27\"", "Écran 4K", 299.99, 15));

				System.out.println("✅ Produits de test chargés dans la base de données");
			} else {
				System.out.println("📊 Base de données déjà initialisée avec " + repository.count() + " produits");
			}
		}
	}
}