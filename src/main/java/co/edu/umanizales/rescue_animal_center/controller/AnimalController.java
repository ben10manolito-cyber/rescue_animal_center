package co.edu.umanizales.rescue_animal_center.controller;

import co.edu.umanizales.rescue_animal_center.model.Animal;
import co.edu.umanizales.rescue_animal_center.model.dto.AnimalRequest;
import co.edu.umanizales.rescue_animal_center.model.dto.AnimalResponse;
import co.edu.umanizales.rescue_animal_center.service.AnimalService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/animals")
public class AnimalController {

    private final AnimalService animalService;

    public AnimalController(AnimalService animalService) {
        this.animalService = animalService;
    }

    @GetMapping
    public List<AnimalResponse> getAll() {
        return animalService.getAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public AnimalResponse getById(@PathVariable String id) {
        return toResponse(animalService.getById(id));
    }

    @PostMapping
    public ResponseEntity<AnimalResponse> create(@Valid @RequestBody AnimalRequest request) {
        Animal created = animalService.create(fromRequest(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(created));
    }

    @PutMapping("/{id}")
    public AnimalResponse update(@PathVariable String id, @Valid @RequestBody AnimalRequest request) {
        Animal updated = animalService.update(id, fromRequest(request));
        return toResponse(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        animalService.delete(id);
        return ResponseEntity.noContent().build();
    }

    private Animal fromRequest(AnimalRequest req) {
        Animal a = new Animal();
        a.setName(req.getName());
        a.setSpecies(req.getSpecies());
        a.setAge(req.getAge());
        a.setGender(req.getGender());
        a.setStatus(req.getStatus());
        a.setHabitatId(req.getHabitatId());
        return a;
    }

    private AnimalResponse toResponse(Animal a) {
        return new AnimalResponse(
                a.getId(),
                a.getName(),
                a.getSpecies(),
                a.getAge(),
                a.getGender(),
                a.getStatus(),
                a.getHabitatId()
        );
    }
}
