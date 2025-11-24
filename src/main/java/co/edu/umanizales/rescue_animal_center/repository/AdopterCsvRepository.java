package co.edu.umanizales.rescue_animal_center.repository;

import co.edu.umanizales.rescue_animal_center.infrastructure.csv.CsvFileStorage;
import co.edu.umanizales.rescue_animal_center.model.Address;
import co.edu.umanizales.rescue_animal_center.model.Adopter;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.*;

@Repository
public class AdopterCsvRepository implements CrudRepository<Adopter, String> {
    private static final String FILE = "adopters.csv";
    private static final String HEADER = "id,firstName,lastName,idNumber,phone,addressStreet,addressCity,addressState,addressZip,hasOtherPets";

    private final CsvFileStorage storage;

    public AdopterCsvRepository(CsvFileStorage storage) {
        this.storage = storage;
    }

    @Override
    public List<Adopter> findAll() {
        try {
            List<String> lines = storage.readAll(FILE);
            if (lines.isEmpty()) return new ArrayList<>();
            if (!lines.isEmpty() && lines.get(0).startsWith("id,")) {
                lines = lines.subList(1, lines.size());
            }
            List<Adopter> result = new ArrayList<>();
            for (String line : lines) {
                if (line == null || line.isBlank()) continue;
                String[] p = line.split(",", -1);
                if (p.length < 10) continue;
                Address addr = new Address(p[5], p[6], p[7], p[8]);
                Adopter a = new Adopter(
                        p[0],
                        p[1],
                        p[2],
                        p[3],
                        p[4],
                        addr,
                        parseBoolean(p[9])
                );
                result.add(a);
            }
            return result;
        } catch (IOException e) {
            throw new RuntimeException("Error reading adopters CSV", e);
        }
    }

    @Override
    public Optional<Adopter> findById(String id) {
        return findAll().stream().filter(a -> Objects.equals(a.getId(), id)).findFirst();
    }

    @Override
    public Adopter save(Adopter entity) {
        List<Adopter> current = findAll();
        if (entity.getId() == null || entity.getId().isBlank()) {
            entity.setId(UUID.randomUUID().toString());
        }
        current.add(entity);
        writeAll(current);
        return entity;
    }

    @Override
    public Adopter update(String id, Adopter entity) {
        List<Adopter> current = findAll();
        int idx = -1;
        for (int i = 0; i < current.size(); i++) {
            if (Objects.equals(current.get(i).getId(), id)) { idx = i; break; }
        }
        if (idx < 0) throw new NoSuchElementException("Adopter not found: " + id);
        entity.setId(id);
        current.set(idx, entity);
        writeAll(current);
        return entity;
    }

    @Override
    public void delete(String id) {
        List<Adopter> current = findAll();
        List<Adopter> filtered = new ArrayList<>();
        for (Adopter a : current) {
            if (!Objects.equals(a.getId(), id)) filtered.add(a);
        }
        if (filtered.size() == current.size()) {
            throw new NoSuchElementException("Adopter not found: " + id);
        }
        writeAll(filtered);
    }

    private void writeAll(List<Adopter> adopters) {
        List<String> lines = new ArrayList<>();
        lines.add(HEADER);
        for (Adopter a : adopters) {
            Address addr = a.getAddress();
            lines.add(String.join(",",
                    ns(a.getId()),
                    ns(a.getFirstName()),
                    ns(a.getLastName()),
                    ns(a.getIdNumber()),
                    ns(a.getPhone()),
                    addr != null ? ns(addr.street()) : "",
                    addr != null ? ns(addr.city()) : "",
                    addr != null ? ns(addr.state()) : "",
                    addr != null ? ns(addr.zipCode()) : "",
                    a.isHasOtherPets() ? "true" : "false"
            ));
        }
        try {
            storage.writeAll(FILE, lines);
        } catch (IOException e) {
            throw new RuntimeException("Error writing adopters CSV", e);
        }
    }

    private static String ns(String s) { return s == null ? "" : s; }
    private static boolean parseBoolean(String s) { return s != null && s.equalsIgnoreCase("true"); }
}
