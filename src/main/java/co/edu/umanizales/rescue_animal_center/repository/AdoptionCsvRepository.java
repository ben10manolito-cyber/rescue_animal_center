package co.edu.umanizales.rescue_animal_center.repository;

import co.edu.umanizales.rescue_animal_center.model.Adoption;
import co.edu.umanizales.rescue_animal_center.infrastructure.csv.CsvFileStorage;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

@Repository
public class AdoptionCsvRepository implements CrudRepository<Adoption, String> {
    private static final String FILE = "adoptions.csv";
    private static final String HEADER = "id,adopterId,animalId,date,notes";

    private final CsvFileStorage storage;

    public AdoptionCsvRepository(CsvFileStorage storage) {
        this.storage = storage;
    }

    @Override
    public List<Adoption> findAll() {
        try {
            List<String> lines = storage.readAll(FILE);
            if (lines.isEmpty()) return new ArrayList<>();
            if (!lines.isEmpty() && lines.get(0).startsWith("id,")) {
                lines = lines.subList(1, lines.size());
            }
            List<Adoption> result = new ArrayList<>();
            for (String line : lines) {
                if (line == null || line.isBlank()) continue;
                String[] p = line.split(",", -1);
                if (p.length < 5) continue;
                result.add(new Adoption(
                        p[0],
                        p[1],
                        p[2],
                        safeDate(p[3]),
                        p[4]
                ));
            }
            return result;
        } catch (IOException e) {
            throw new RuntimeException("Error reading adoptions CSV", e);
        }
    }

    @Override
    public Optional<Adoption> findById(String id) {
        return findAll().stream().filter(a -> Objects.equals(a.getId(), id)).findFirst();
    }

    @Override
    public Adoption save(Adoption entity) {
        List<Adoption> current = findAll();
        if (entity.getId() == null || entity.getId().isBlank()) {
            entity.setId(UUID.randomUUID().toString());
        }
        current.add(entity);
        writeAll(current);
        return entity;
    }

    @Override
    public Adoption update(String id, Adoption entity) {
        List<Adoption> current = findAll();
        int idx = -1;
        for (int i = 0; i < current.size(); i++) {
            if (Objects.equals(current.get(i).getId(), id)) { idx = i; break; }
        }
        if (idx < 0) throw new NoSuchElementException("Adoption not found: " + id);
        entity.setId(id);
        current.set(idx, entity);
        writeAll(current);
        return entity;
    }

    @Override
    public void delete(String id) {
        List<Adoption> current = findAll();
        List<Adoption> filtered = new ArrayList<>();
        for (Adoption a : current) {
            if (!Objects.equals(a.getId(), id)) filtered.add(a);
        }
        if (filtered.size() == current.size()) {
            throw new NoSuchElementException("Adoption not found: " + id);
        }
        writeAll(filtered);
    }

    private void writeAll(List<Adoption> adoptions) {
        List<String> lines = new ArrayList<>();
        lines.add(HEADER);
        for (Adoption a : adoptions) {
            lines.add(String.join(",",
                    ns(a.getId()),
                    ns(a.getAdopterId()),
                    ns(a.getAnimalId()),
                    a.getDate() != null ? a.getDate().toString() : "",
                    ns(a.getNotes())
            ));
        }
        try {
            storage.writeAll(FILE, lines);
        } catch (IOException e) {
            throw new RuntimeException("Error writing adoptions CSV", e);
        }
    }

    private static String ns(String s) { return s == null ? "" : s; }
    private static LocalDate safeDate(String s) { try { return s == null || s.isBlank() ? null : LocalDate.parse(s); } catch (Exception e) { return null; } }
}
