package co.edu.umanizales.rescue_animal_center.service;

import co.edu.umanizales.rescue_animal_center.model.Animal;
import co.edu.umanizales.rescue_animal_center.repository.AnimalCsvRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class AnimalService {
    private final AnimalCsvRepository repository;

    public AnimalService(AnimalCsvRepository repository) {
        this.repository = repository;
    }

    public List<Animal> getAll() { return repository.findAll(); }

    public Animal getById(String id) {
        return repository.findById(id).orElseThrow(() -> new NoSuchElementException("Animal not found: " + id));
    }

    public Animal create(Animal animal) { return repository.save(animal); }

    public Animal update(String id, Animal animal) { return repository.update(id, animal); }

    public void delete(String id) { repository.delete(id); }
}
