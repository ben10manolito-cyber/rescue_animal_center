package co.edu.umanizales.rescue_animal_center.service;

import co.edu.umanizales.rescue_animal_center.model.Volunteer;
import co.edu.umanizales.rescue_animal_center.repository.VolunteerCsvRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class VolunteerService {
    private final VolunteerCsvRepository repository;

    public VolunteerService(VolunteerCsvRepository repository) {
        this.repository = repository;
    }

    public List<Volunteer> getAll() { return repository.findAll(); }

    public Volunteer getById(String id) {
        return repository.findById(id).orElseThrow(() -> new NoSuchElementException("Volunteer not found: " + id));
    }

    public Volunteer create(Volunteer volunteer) { return repository.save(volunteer); }

    public Volunteer update(String id, Volunteer volunteer) { return repository.update(id, volunteer); }

    public void delete(String id) { repository.delete(id); }
}
