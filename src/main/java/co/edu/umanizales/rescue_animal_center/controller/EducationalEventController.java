package co.edu.umanizales.rescue_animal_center.controller;

import co.edu.umanizales.rescue_animal_center.model.EducationalEvent;
import co.edu.umanizales.rescue_animal_center.model.dto.EducationalEventRequest;
import co.edu.umanizales.rescue_animal_center.model.dto.EducationalEventResponse;
import co.edu.umanizales.rescue_animal_center.service.EducationalEventService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/events")
public class EducationalEventController {

    private final EducationalEventService eventService;

    public EducationalEventController(EducationalEventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public List<EducationalEventResponse> getAll() {
        return eventService.getAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public EducationalEventResponse getById(@PathVariable String id) {
        return toResponse(eventService.getById(id));
    }

    @PostMapping
    public ResponseEntity<EducationalEventResponse> create(@Valid @RequestBody EducationalEventRequest request) {
        EducationalEvent created = eventService.create(fromRequest(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(created));
    }

    @PutMapping("/{id}")
    public EducationalEventResponse update(@PathVariable String id, @Valid @RequestBody EducationalEventRequest request) {
        EducationalEvent updated = eventService.update(id, fromRequest(request));
        return toResponse(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        eventService.delete(id);
        return ResponseEntity.noContent().build();
    }

    private EducationalEvent fromRequest(EducationalEventRequest req) {
        return new EducationalEvent(null, req.getTitle(), req.getDate(), req.getLocation());
    }

    private EducationalEventResponse toResponse(EducationalEvent ev) {
        return new EducationalEventResponse(ev.getId(), ev.getTitle(), ev.getDate(), ev.getLocation());
    }
}
