package co.edu.umanizales.rescue_animal_center.controller;

import co.edu.umanizales.rescue_animal_center.model.Address;
import co.edu.umanizales.rescue_animal_center.model.Rescuer;
import co.edu.umanizales.rescue_animal_center.model.dto.RescuerRequest;
import co.edu.umanizales.rescue_animal_center.model.dto.RescuerResponse;
import co.edu.umanizales.rescue_animal_center.service.RescuerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/rescuers")
public class RescuerController {

    private final RescuerService rescuerService;

    public RescuerController(RescuerService rescuerService) {
        this.rescuerService = rescuerService;
    }

    @GetMapping
    public List<RescuerResponse> getAll() {
        return rescuerService.getAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public RescuerResponse getById(@PathVariable String id) {
        return toResponse(rescuerService.getById(id));
    }

    @PostMapping
    public ResponseEntity<RescuerResponse> create(@Valid @RequestBody RescuerRequest request) {
        Rescuer created = rescuerService.create(fromRequest(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(created));
    }

    @PutMapping("/{id}")
    public RescuerResponse update(@PathVariable String id, @Valid @RequestBody RescuerRequest request) {
        Rescuer updated = rescuerService.update(id, fromRequest(request));
        return toResponse(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        rescuerService.delete(id);
        return ResponseEntity.noContent().build();
    }

    private Rescuer fromRequest(RescuerRequest req) {
        Address addr = new Address(
                req.getAddress().getStreet(),
                req.getAddress().getCity(),
                req.getAddress().getState(),
                req.getAddress().getZipCode()
        );
        return new Rescuer(null, req.getFirstName(), req.getLastName(), req.getIdNumber(), req.getPhone(), addr, req.getZone());
    }

    private RescuerResponse toResponse(Rescuer r) {
        return new RescuerResponse(
                r.getId(),
                r.getFirstName(),
                r.getLastName(),
                r.getIdNumber(),
                r.getPhone(),
                r.getAddress(),
                r.getZone()
        );
    }
}
