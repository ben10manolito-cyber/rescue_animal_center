package co.edu.umanizales.rescue_animal_center.service;

import co.edu.umanizales.rescue_animal_center.model.Adopter;
import co.edu.umanizales.rescue_animal_center.repository.AdopterCsvRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class AdopterService {
    private final AdopterCsvRepository repository;

    public AdopterService(AdopterCsvRepository repository) {
        this.repository = repository;
    }

    public List<Adopter> getAll() { return repository.findAll(); }

    public Adopter getById(String id) {
        return repository.findById(id).orElseThrow(() -> new NoSuchElementException("Adopter not found: " + id));
    }

    public Adopter create(Adopter adopter) { return repository.save(adopter); }

    public Adopter update(String id, Adopter adopter) { return repository.update(id, adopter); }

    public void delete(String id) { repository.delete(id); }
}
