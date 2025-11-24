package co.edu.umanizales.rescue_animal_center.service;

import co.edu.umanizales.rescue_animal_center.model.Rescuer;
import co.edu.umanizales.rescue_animal_center.repository.RescuerCsvRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class RescuerService {
    private final RescuerCsvRepository repository;

    public RescuerService(RescuerCsvRepository repository) {
        this.repository = repository;
    }

    public List<Rescuer> getAll() { return repository.findAll(); }

    public Rescuer getById(String id) {
        return repository.findById(id).orElseThrow(() -> new NoSuchElementException("Rescuer not found: " + id));
    }

    public Rescuer create(Rescuer rescuer) { return repository.save(rescuer); }

    public Rescuer update(String id, Rescuer rescuer) { return repository.update(id, rescuer); }

    public void delete(String id) { repository.delete(id); }
}
