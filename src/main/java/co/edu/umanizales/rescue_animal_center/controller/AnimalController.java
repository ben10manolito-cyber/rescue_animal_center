package co.edu.umanizales.rescue_animal_center.controller;

import co.edu.umanizales.rescue_animal_center.model.Animal;
import co.edu.umanizales.rescue_animal_center.service.AnimalService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/animals")
public class AnimalController {

    private final AnimalService animalService;

    public AnimalController(AnimalService animalService) {
        this.animalService = animalService;
    }

    @GetMapping
    public List<Animal> getAll() {
        return animalService.getAll();
    }

    @GetMapping("/{id}")
    public Animal getById(@PathVariable String id) {
        return animalService.getById(id);
    }

    @PostMapping
    public ResponseEntity<Animal> create(@RequestBody Animal animal) {
        Animal created = animalService.create(animal);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public Animal update(@PathVariable String id, @RequestBody Animal animal) {
        return animalService.update(id, animal);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        animalService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
