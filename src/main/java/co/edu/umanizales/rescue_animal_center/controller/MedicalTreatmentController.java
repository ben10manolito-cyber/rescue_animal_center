package co.edu.umanizales.rescue_animal_center.controller;

import co.edu.umanizales.rescue_animal_center.model.MedicalTreatment;
import co.edu.umanizales.rescue_animal_center.model.dto.MedicalTreatmentRequest;
import co.edu.umanizales.rescue_animal_center.model.dto.MedicalTreatmentResponse;
import co.edu.umanizales.rescue_animal_center.service.MedicalTreatmentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/treatments")
public class MedicalTreatmentController {

    private final MedicalTreatmentService treatmentService;

    public MedicalTreatmentController(MedicalTreatmentService treatmentService) {
        this.treatmentService = treatmentService;
    }

    @GetMapping
    public List<MedicalTreatmentResponse> getAll() {
        return treatmentService.getAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public MedicalTreatmentResponse getById(@PathVariable String id) {
        return toResponse(treatmentService.getById(id));
    }

    @PostMapping
    public ResponseEntity<MedicalTreatmentResponse> create(@Valid @RequestBody MedicalTreatmentRequest request) {
        MedicalTreatment created = treatmentService.create(fromRequest(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(created));
    }

    @PutMapping("/{id}")
    public MedicalTreatmentResponse update(@PathVariable String id, @Valid @RequestBody MedicalTreatmentRequest request) {
        MedicalTreatment updated = treatmentService.update(id, fromRequest(request));
        return toResponse(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        treatmentService.delete(id);
        return ResponseEntity.noContent().build();
    }

    private MedicalTreatment fromRequest(MedicalTreatmentRequest req) {
        return new MedicalTreatment(null, req.getAnimalId(), req.getType(), req.getStartDate(), req.getEndDate(), req.getNotes());
    }

    private MedicalTreatmentResponse toResponse(MedicalTreatment t) {
        return new MedicalTreatmentResponse(t.getId(), t.getAnimalId(), t.getType(), t.getStartDate(), t.getEndDate(), t.getNotes());
    }
}
