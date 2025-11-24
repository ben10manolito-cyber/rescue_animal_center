package co.edu.umanizales.rescue_animal_center.controller;

import co.edu.umanizales.rescue_animal_center.model.Adoption;
import co.edu.umanizales.rescue_animal_center.model.dto.AdoptionRequest;
import co.edu.umanizales.rescue_animal_center.model.dto.AdoptionResponse;
import co.edu.umanizales.rescue_animal_center.service.AdoptionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/adoptions")
public class AdoptionController {

    private final AdoptionService adoptionService;

    public AdoptionController(AdoptionService adoptionService) {
        this.adoptionService = adoptionService;
    }

    @GetMapping
    public List<AdoptionResponse> getAll() {
        return adoptionService.getAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public AdoptionResponse getById(@PathVariable String id) {
        return toResponse(adoptionService.getById(id));
    }

    @PostMapping
    public ResponseEntity<AdoptionResponse> create(@Valid @RequestBody AdoptionRequest request) {
        Adoption created = adoptionService.create(fromRequest(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(created));
    }

    private Adoption fromRequest(AdoptionRequest req) {
        return new Adoption(null, req.getAdopterId(), req.getAnimalId(), req.getDate(), req.getNotes());
    }

    private AdoptionResponse toResponse(Adoption a) {
        return new AdoptionResponse(a.getId(), a.getAdopterId(), a.getAnimalId(), a.getDate(), a.getNotes());
    }
}
