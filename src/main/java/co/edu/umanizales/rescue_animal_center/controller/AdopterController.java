package co.edu.umanizales.rescue_animal_center.controller;

import co.edu.umanizales.rescue_animal_center.model.Address;
import co.edu.umanizales.rescue_animal_center.model.Adopter;
import co.edu.umanizales.rescue_animal_center.model.dto.AdopterRequest;
import co.edu.umanizales.rescue_animal_center.model.dto.AdopterResponse;
import co.edu.umanizales.rescue_animal_center.service.AdopterService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/adopters")
public class AdopterController {

    private final AdopterService adopterService;

    public AdopterController(AdopterService adopterService) {
        this.adopterService = adopterService;
    }

    @GetMapping
    public List<AdopterResponse> getAll() {
        return adopterService.getAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public AdopterResponse getById(@PathVariable String id) {
        return toResponse(adopterService.getById(id));
    }

    @PostMapping
    public ResponseEntity<AdopterResponse> create(@Valid @RequestBody AdopterRequest request) {
        Adopter created = adopterService.create(fromRequest(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(created));
    }

    @PutMapping("/{id}")
    public AdopterResponse update(@PathVariable String id, @Valid @RequestBody AdopterRequest request) {
        Adopter updated = adopterService.update(id, fromRequest(request));
        return toResponse(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        adopterService.delete(id);
        return ResponseEntity.noContent().build();
    }

    private Adopter fromRequest(AdopterRequest req) {
        Address addr = new Address(
                req.getAddress().getStreet(),
                req.getAddress().getCity(),
                req.getAddress().getState(),
                req.getAddress().getZipCode()
        );
        return new Adopter(null, req.getFirstName(), req.getLastName(), req.getIdNumber(), req.getPhone(), addr, req.isHasOtherPets());
    }

    private AdopterResponse toResponse(Adopter a) {
        return new AdopterResponse(
                a.getId(),
                a.getFirstName(),
                a.getLastName(),
                a.getIdNumber(),
                a.getPhone(),
                a.getAddress(),
                a.isHasOtherPets()
        );
    }
}
