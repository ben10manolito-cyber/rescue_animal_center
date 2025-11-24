package co.edu.umanizales.rescue_animal_center.service;

import co.edu.umanizales.rescue_animal_center.model.MedicalTreatment;
import co.edu.umanizales.rescue_animal_center.repository.MedicalTreatmentCsvRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class MedicalTreatmentService {
    private final MedicalTreatmentCsvRepository repository;

    public MedicalTreatmentService(MedicalTreatmentCsvRepository repository) {
        this.repository = repository;
    }

    public List<MedicalTreatment> getAll() { return repository.findAll(); }

    public MedicalTreatment getById(String id) { return repository.findById(id).orElseThrow(() -> new NoSuchElementException("MedicalTreatment not found: " + id)); }

    public MedicalTreatment create(MedicalTreatment treatment) { return repository.save(treatment); }

    public MedicalTreatment update(String id, MedicalTreatment treatment) { return repository.update(id, treatment); }

    public void delete(String id) { repository.delete(id); }
}
