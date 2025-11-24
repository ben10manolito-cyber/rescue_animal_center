package co.edu.umanizales.rescue_animal_center.service;

import co.edu.umanizales.rescue_animal_center.model.EducationalEvent;
import co.edu.umanizales.rescue_animal_center.repository.EducationalEventCsvRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class EducationalEventService {
    private final EducationalEventCsvRepository repository;

    public EducationalEventService(EducationalEventCsvRepository repository) {
        this.repository = repository;
    }

    public List<EducationalEvent> getAll() { return repository.findAll(); }

    public EducationalEvent getById(String id) { return repository.findById(id).orElseThrow(() -> new NoSuchElementException("EducationalEvent not found: " + id)); }

    public EducationalEvent create(EducationalEvent event) { return repository.save(event); }

    public EducationalEvent update(String id, EducationalEvent event) { return repository.update(id, event); }

    public void delete(String id) { repository.delete(id); }
}
