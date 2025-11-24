package co.edu.umanizales.rescue_animal_center.service;

import co.edu.umanizales.rescue_animal_center.model.Donation;
import co.edu.umanizales.rescue_animal_center.repository.DonationCsvRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class DonationService {
    private final DonationCsvRepository repository;

    public DonationService(DonationCsvRepository repository) {
        this.repository = repository;
    }

    public List<Donation> getAll() { return repository.findAll(); }

    public Donation getById(String id) { return repository.findById(id).orElseThrow(() -> new NoSuchElementException("Donation not found: " + id)); }

    public Donation create(Donation donation) { return repository.save(donation); }

    public Donation update(String id, Donation donation) { return repository.update(id, donation); }

    public void delete(String id) { repository.delete(id); }
}
