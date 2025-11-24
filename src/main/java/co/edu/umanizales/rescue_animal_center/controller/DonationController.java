package co.edu.umanizales.rescue_animal_center.controller;

import co.edu.umanizales.rescue_animal_center.model.Donation;
import co.edu.umanizales.rescue_animal_center.model.dto.DonationRequest;
import co.edu.umanizales.rescue_animal_center.model.dto.DonationResponse;
import co.edu.umanizales.rescue_animal_center.service.DonationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/donations")
public class DonationController {

    private final DonationService donationService;

    public DonationController(DonationService donationService) {
        this.donationService = donationService;
    }

    @GetMapping
    public List<DonationResponse> getAll() {
        return donationService.getAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public DonationResponse getById(@PathVariable String id) {
        return toResponse(donationService.getById(id));
    }

    @PostMapping
    public ResponseEntity<DonationResponse> create(@Valid @RequestBody DonationRequest request) {
        Donation created = donationService.create(fromRequest(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(created));
    }

    @PutMapping("/{id}")
    public DonationResponse update(@PathVariable String id, @Valid @RequestBody DonationRequest request) {
        Donation updated = donationService.update(id, fromRequest(request));
        return toResponse(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        donationService.delete(id);
        return ResponseEntity.noContent().build();
    }

    private Donation fromRequest(DonationRequest req) {
        return new Donation(null, req.getDonorName(), req.getType(), req.getAmount(), req.getDate());
    }

    private DonationResponse toResponse(Donation d) {
        return new DonationResponse(d.getId(), d.getDonorName(), d.getType(), d.getAmount(), d.getDate());
    }
}
