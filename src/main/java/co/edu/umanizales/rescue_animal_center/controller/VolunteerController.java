package co.edu.umanizales.rescue_animal_center.controller;

import co.edu.umanizales.rescue_animal_center.model.Address;
import co.edu.umanizales.rescue_animal_center.model.Volunteer;
import co.edu.umanizales.rescue_animal_center.model.dto.VolunteerRequest;
import co.edu.umanizales.rescue_animal_center.model.dto.VolunteerResponse;
import co.edu.umanizales.rescue_animal_center.service.VolunteerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/volunteers")
public class VolunteerController {

    private final VolunteerService volunteerService;

    public VolunteerController(VolunteerService volunteerService) {
        this.volunteerService = volunteerService;
    }

    @GetMapping
    public List<VolunteerResponse> getAll() {
        return volunteerService.getAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public VolunteerResponse getById(@PathVariable String id) {
        return toResponse(volunteerService.getById(id));
    }

    @PostMapping
    public ResponseEntity<VolunteerResponse> create(@Valid @RequestBody VolunteerRequest request) {
        Volunteer created = volunteerService.create(fromRequest(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(created));
    }

    @PutMapping("/{id}")
    public VolunteerResponse update(@PathVariable String id, @Valid @RequestBody VolunteerRequest request) {
        Volunteer updated = volunteerService.update(id, fromRequest(request));
        return toResponse(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        volunteerService.delete(id);
        return ResponseEntity.noContent().build();
    }

    private Volunteer fromRequest(VolunteerRequest req) {
        Address addr = new Address(
                req.getAddress().getStreet(),
                req.getAddress().getCity(),
                req.getAddress().getState(),
                req.getAddress().getZipCode()
        );
        return new Volunteer(null, req.getFirstName(), req.getLastName(), req.getIdNumber(), req.getPhone(), addr, req.getHoursPerWeek());
    }

    private VolunteerResponse toResponse(Volunteer v) {
        return new VolunteerResponse(
                v.getId(),
                v.getFirstName(),
                v.getLastName(),
                v.getIdNumber(),
                v.getPhone(),
                v.getAddress(),
                v.getHoursPerWeek()
        );
    }
}
