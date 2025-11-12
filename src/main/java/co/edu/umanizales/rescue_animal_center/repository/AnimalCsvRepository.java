package co.edu.umanizales.rescue_animal_center.repository;

import co.edu.umanizales.rescue_animal_center.infrastructure.csv.CsvFileStorage;
import co.edu.umanizales.rescue_animal_center.model.*;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class AnimalCsvRepository implements CrudRepository<Animal, String> {
    private static final String FILE = "animals.csv";
    private static final String HEADER = "id,name,species,age,gender,status,habitatId";

    private final CsvFileStorage storage;

    public AnimalCsvRepository(CsvFileStorage storage) {
        this.storage = storage;
    }

    @Override
    public List<Animal> findAll() {
        try {
            List<String> lines = storage.readAll(FILE);
            if (lines.isEmpty()) return new ArrayList<>();
            // Remove header if present
            if (!lines.isEmpty() && lines.get(0).startsWith("id,")) {
                lines = lines.subList(1, lines.size());
            }
            List<Animal> animals = new ArrayList<>();
            for (String line : lines) {
                if (line == null || line.isBlank()) continue;
                String[] parts = line.split(",", -1);
                if (parts.length < 7) continue;
                Animal a = new Animal(
                        parts[0],
                        parts[1],
                        safeEnum(Species.class, parts[2]),
                        safeInt(parts[3]),
                        safeEnum(Gender.class, parts[4]),
                        safeEnum(AnimalStatus.class, parts[5]),
                        parts[6]
                );
                animals.add(a);
            }
            return animals;
        } catch (IOException e) {
            throw new RuntimeException("Error reading animals CSV", e);
        }
    }

    @Override
    public Optional<Animal> findById(String id) {
        return findAll().stream().filter(a -> Objects.equals(a.getId(), id)).findFirst();
    }

    @Override
    public Animal save(Animal entity) {
        List<Animal> current = findAll();
        if (entity.getId() == null || entity.getId().isBlank()) {
            entity.setId(UUID.randomUUID().toString());
        }
        current.add(entity);
        writeAll(current);
        return entity;
    }

    @Override
    public Animal update(String id, Animal entity) {
        List<Animal> current = findAll();
        int idx = -1;
        for (int i = 0; i < current.size(); i++) {
            if (Objects.equals(current.get(i).getId(), id)) { idx = i; break; }
        }
        if (idx < 0) {
            throw new NoSuchElementException("Animal not found: " + id);
        }
        entity.setId(id);
        current.set(idx, entity);
        writeAll(current);
        return entity;
    }

    @Override
    public void delete(String id) {
        List<Animal> current = findAll();
        List<Animal> filtered = current.stream()
                .filter(a -> !Objects.equals(a.getId(), id))
                .collect(Collectors.toList());
        if (filtered.size() == current.size()) {
            throw new NoSuchElementException("Animal not found: " + id);
        }
        writeAll(filtered);
    }

    private void writeAll(List<Animal> animals) {
        List<String> lines = new ArrayList<>();
        lines.add(HEADER);
        for (Animal a : animals) {
            lines.add(String.join(",",
                    nullSafe(a.getId()),
                    nullSafe(a.getName()),
                    enumName(a.getSpecies()),
                    String.valueOf(a.getAge()),
                    enumName(a.getGender()),
                    enumName(a.getStatus()),
                    nullSafe(a.getHabitatId())
            ));
        }
        try {
            storage.writeAll(FILE, lines);
        } catch (IOException e) {
            throw new RuntimeException("Error writing animals CSV", e);
        }
    }

    private static String nullSafe(String s) { return s == null ? "" : s; }
    private static <E extends Enum<E>> String enumName(E e) { return e == null ? "" : e.name(); }
    private static int safeInt(String s) { try { return Integer.parseInt(s); } catch (Exception e) { return 0; } }
    private static <E extends Enum<E>> E safeEnum(Class<E> type, String s) {
        try { return s == null || s.isBlank() ? null : Enum.valueOf(type, s); }
        catch (Exception e) { return null; }
    }
}
