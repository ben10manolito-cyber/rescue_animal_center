package co.edu.umanizales.rescue_animal_center.repository;

import co.edu.umanizales.rescue_animal_center.infrastructure.csv.CsvFileStorage;
import co.edu.umanizales.rescue_animal_center.model.Address;
import co.edu.umanizales.rescue_animal_center.model.Rescuer;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.*;

@Repository
public class RescuerCsvRepository implements CrudRepository<Rescuer, String> {
    private static final String FILE = "rescuers.csv";
    private static final String HEADER = "id,firstName,lastName,idNumber,phone,addressStreet,addressCity,addressState,addressZip,zone";

    private final CsvFileStorage storage;

    public RescuerCsvRepository(CsvFileStorage storage) {
        this.storage = storage;
    }

    @Override
    public List<Rescuer> findAll() {
        try {
            List<String> lines = storage.readAll(FILE);
            if (lines.isEmpty()) return new ArrayList<>();
            if (!lines.isEmpty() && lines.get(0).startsWith("id,")) {
                lines = lines.subList(1, lines.size());
            }
            List<Rescuer> result = new ArrayList<>();
            for (String line : lines) {
                if (line == null || line.isBlank()) continue;
                String[] p = line.split(",", -1);
                if (p.length < 10) continue;
                Address addr = new Address(p[5], p[6], p[7], p[8]);
                Rescuer r = new Rescuer(
                        p[0], p[1], p[2], p[3], p[4], addr, p[9]
                );
                result.add(r);
            }
            return result;
        } catch (IOException e) {
            throw new RuntimeException("Error reading rescuers CSV", e);
        }
    }

    @Override
    public Optional<Rescuer> findById(String id) {
        return findAll().stream().filter(r -> Objects.equals(r.getId(), id)).findFirst();
    }

    @Override
    public Rescuer save(Rescuer entity) {
        List<Rescuer> current = findAll();
        if (entity.getId() == null || entity.getId().isBlank()) {
            entity.setId(UUID.randomUUID().toString());
        }
        current.add(entity);
        writeAll(current);
        return entity;
    }

    @Override
    public Rescuer update(String id, Rescuer entity) {
        List<Rescuer> current = findAll();
        int idx = -1;
        for (int i = 0; i < current.size(); i++) {
            if (Objects.equals(current.get(i).getId(), id)) { idx = i; break; }
        }
        if (idx < 0) throw new NoSuchElementException("Rescuer not found: " + id);
        entity.setId(id);
        current.set(idx, entity);
        writeAll(current);
        return entity;
    }

    @Override
    public void delete(String id) {
        List<Rescuer> current = findAll();
        List<Rescuer> filtered = new ArrayList<>();
        for (Rescuer r : current) {
            if (!Objects.equals(r.getId(), id)) filtered.add(r);
        }
        if (filtered.size() == current.size()) {
            throw new NoSuchElementException("Rescuer not found: " + id);
        }
        writeAll(filtered);
    }

    private void writeAll(List<Rescuer> rescuers) {
        List<String> lines = new ArrayList<>();
        lines.add(HEADER);
        for (Rescuer r : rescuers) {
            Address addr = r.getAddress();
            lines.add(String.join(",",
                    ns(r.getId()),
                    ns(r.getFirstName()),
                    ns(r.getLastName()),
                    ns(r.getIdNumber()),
                    ns(r.getPhone()),
                    addr != null ? ns(addr.street()) : "",
                    addr != null ? ns(addr.city()) : "",
                    addr != null ? ns(addr.state()) : "",
                    addr != null ? ns(addr.zipCode()) : "",
                    ns(r.getZone())
            ));
        }
        try {
            storage.writeAll(FILE, lines);
        } catch (IOException e) {
            throw new RuntimeException("Error writing rescuers CSV", e);
        }
    }

    private static String ns(String s) { return s == null ? "" : s; }
}
