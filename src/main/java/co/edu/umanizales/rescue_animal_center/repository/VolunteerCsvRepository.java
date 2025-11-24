package co.edu.umanizales.rescue_animal_center.repository;

import co.edu.umanizales.rescue_animal_center.infrastructure.csv.CsvFileStorage;
import co.edu.umanizales.rescue_animal_center.model.Address;
import co.edu.umanizales.rescue_animal_center.model.Volunteer;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.*;

@Repository
public class VolunteerCsvRepository implements CrudRepository<Volunteer, String> {
    private static final String FILE = "volunteers.csv";
    private static final String HEADER = "id,firstName,lastName,idNumber,phone,addressStreet,addressCity,addressState,addressZip,hoursPerWeek";

    private final CsvFileStorage storage;

    public VolunteerCsvRepository(CsvFileStorage storage) {
        this.storage = storage;
    }

    @Override
    public List<Volunteer> findAll() {
        try {
            List<String> lines = storage.readAll(FILE);
            if (lines.isEmpty()) return new ArrayList<>();
            if (!lines.isEmpty() && lines.get(0).startsWith("id,")) {
                lines = lines.subList(1, lines.size());
            }
            List<Volunteer> result = new ArrayList<>();
            for (String line : lines) {
                if (line == null || line.isBlank()) continue;
                String[] p = line.split(",", -1);
                if (p.length < 10) continue;
                Address addr = new Address(p[5], p[6], p[7], p[8]);
                Volunteer v = new Volunteer(
                        p[0], p[1], p[2], p[3], p[4], addr,
                        safeInt(p[9])
                );
                result.add(v);
            }
            return result;
        } catch (IOException e) {
            throw new RuntimeException("Error reading volunteers CSV", e);
        }
    }

    @Override
    public Optional<Volunteer> findById(String id) {
        return findAll().stream().filter(v -> Objects.equals(v.getId(), id)).findFirst();
    }

    @Override
    public Volunteer save(Volunteer entity) {
        List<Volunteer> current = findAll();
        if (entity.getId() == null || entity.getId().isBlank()) {
            entity.setId(UUID.randomUUID().toString());
        }
        current.add(entity);
        writeAll(current);
        return entity;
    }

    @Override
    public Volunteer update(String id, Volunteer entity) {
        List<Volunteer> current = findAll();
        int idx = -1;
        for (int i = 0; i < current.size(); i++) {
            if (Objects.equals(current.get(i).getId(), id)) { idx = i; break; }
        }
        if (idx < 0) throw new NoSuchElementException("Volunteer not found: " + id);
        entity.setId(id);
        current.set(idx, entity);
        writeAll(current);
        return entity;
    }

    @Override
    public void delete(String id) {
        List<Volunteer> current = findAll();
        List<Volunteer> filtered = new ArrayList<>();
        for (Volunteer v : current) {
            if (!Objects.equals(v.getId(), id)) filtered.add(v);
        }
        if (filtered.size() == current.size()) {
            throw new NoSuchElementException("Volunteer not found: " + id);
        }
        writeAll(filtered);
    }

    private void writeAll(List<Volunteer> volunteers) {
        List<String> lines = new ArrayList<>();
        lines.add(HEADER);
        for (Volunteer v : volunteers) {
            Address addr = v.getAddress();
            lines.add(String.join(",",
                    ns(v.getId()),
                    ns(v.getFirstName()),
                    ns(v.getLastName()),
                    ns(v.getIdNumber()),
                    ns(v.getPhone()),
                    addr != null ? ns(addr.street()) : "",
                    addr != null ? ns(addr.city()) : "",
                    addr != null ? ns(addr.state()) : "",
                    addr != null ? ns(addr.zipCode()) : "",
                    String.valueOf(v.getHoursPerWeek())
            ));
        }
        try {
            storage.writeAll(FILE, lines);
        } catch (IOException e) {
            throw new RuntimeException("Error writing volunteers CSV", e);
        }
    }

    private static String ns(String s) { return s == null ? "" : s; }
    private static int safeInt(String s) { try { return Integer.parseInt(s); } catch (Exception e) { return 0; } }
}
