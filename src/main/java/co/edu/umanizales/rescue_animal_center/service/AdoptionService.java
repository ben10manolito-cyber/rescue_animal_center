package co.edu.umanizales.rescue_animal_center.service;

import co.edu.umanizales.rescue_animal_center.model.Adoption;
import co.edu.umanizales.rescue_animal_center.model.Animal;
import co.edu.umanizales.rescue_animal_center.model.AnimalStatus;
import co.edu.umanizales.rescue_animal_center.repository.AdoptionCsvRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class AdoptionService {
    private final AdoptionCsvRepository repository;
    private final AnimalService animalService;
    private final AdopterService adopterService;

    public AdoptionService(AdoptionCsvRepository repository, AnimalService animalService, AdopterService adopterService) {
        this.repository = repository;
        this.animalService = animalService;
        this.adopterService = adopterService;
    }

    public List<Adoption> getAll() { return repository.findAll(); }

    public Adoption getById(String id) {
        return repository.findById(id).orElseThrow(() -> new NoSuchElementException("Adoption not found: " + id));
    }

    public Adoption create(Adoption adoption) {
        // Validate adopter exists
        adopterService.getById(adoption.getAdopterId());
        // Validate animal exists and is AVAILABLE
        Animal animal = animalService.getById(adoption.getAnimalId());
        if (animal.getStatus() != AnimalStatus.AVAILABLE) {
            throw new IllegalStateException("Animal is not AVAILABLE for adoption");
        }
        // Persist adoption
        Adoption saved = repository.save(adoption);
        // Update animal status to ADOPTED
        animal.setStatus(AnimalStatus.ADOPTED);
        animalService.update(animal.getId(), animal);
        return saved;
    }

    public void delete(String id) { repository.delete(id); }
}
