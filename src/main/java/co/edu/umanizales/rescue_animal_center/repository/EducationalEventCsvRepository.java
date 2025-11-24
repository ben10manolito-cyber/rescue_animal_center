package co.edu.umanizales.rescue_animal_center.repository;

import co.edu.umanizales.rescue_animal_center.infrastructure.csv.CsvFileStorage;
import co.edu.umanizales.rescue_animal_center.model.EducationalEvent;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

@Repository
public class EducationalEventCsvRepository implements CrudRepository<EducationalEvent, String> {
    private static final String FILE = "events.csv";
    private static final String HEADER = "id,title,date,location";

    private final CsvFileStorage storage;

    public EducationalEventCsvRepository(CsvFileStorage storage) {
        this.storage = storage;
    }

    @Override
    public List<EducationalEvent> findAll() {
        try {
            List<String> lines = storage.readAll(FILE);
            if (lines.isEmpty()) return new ArrayList<>();
            if (!lines.isEmpty() && lines.get(0).startsWith("id,")) {
                lines = lines.subList(1, lines.size());
            }
            List<EducationalEvent> result = new ArrayList<>();
            for (String line : lines) {
                if (line == null || line.isBlank()) continue;
                String[] p = line.split(",", -1);
                if (p.length < 4) continue;
                result.add(new EducationalEvent(p[0], p[1], safeDate(p[2]), p[3]));
            }
            return result;
        } catch (IOException e) {
            throw new RuntimeException("Error reading events CSV", e);
        }
    }

    @Override
    public Optional<EducationalEvent> findById(String id) {
        return findAll().stream().filter(ev -> Objects.equals(ev.getId(), id)).findFirst();
    }

    @Override
    public EducationalEvent save(EducationalEvent entity) {
        List<EducationalEvent> current = findAll();
        if (entity.getId() == null || entity.getId().isBlank()) {
            entity.setId(UUID.randomUUID().toString());
        }
        current.add(entity);
        writeAll(current);
        return entity;
    }

    @Override
    public EducationalEvent update(String id, EducationalEvent entity) {
        List<EducationalEvent> current = findAll();
        int idx = -1;
        for (int i = 0; i < current.size(); i++) {
            if (Objects.equals(current.get(i).getId(), id)) { idx = i; break; }
        }
        if (idx < 0) throw new NoSuchElementException("EducationalEvent not found: " + id);
        entity.setId(id);
        current.set(idx, entity);
        writeAll(current);
        return entity;
    }

    @Override
    public void delete(String id) {
        List<EducationalEvent> current = findAll();
        List<EducationalEvent> filtered = new ArrayList<>();
        for (EducationalEvent ev : current) {
            if (!Objects.equals(ev.getId(), id)) filtered.add(ev);
        }
        if (filtered.size() == current.size()) {
            throw new NoSuchElementException("EducationalEvent not found: " + id);
        }
        writeAll(filtered);
    }

    private void writeAll(List<EducationalEvent> events) {
        List<String> lines = new ArrayList<>();
        lines.add(HEADER);
        for (EducationalEvent ev : events) {
            lines.add(String.join(",",
                    ns(ev.getId()),
                    ns(ev.getTitle()),
                    ev.getDate() == null ? "" : ev.getDate().toString(),
                    ns(ev.getLocation())
            ));
        }
        try {
            storage.writeAll(FILE, lines);
        } catch (IOException e) {
            throw new RuntimeException("Error writing events CSV", e);
        }
    }

    private static String ns(String s) { return s == null ? "" : s; }
    private static LocalDate safeDate(String s) { try { return s == null || s.isBlank() ? null : LocalDate.parse(s); } catch (Exception e) { return null; } }
}
