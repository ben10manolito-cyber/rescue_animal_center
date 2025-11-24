package co.edu.umanizales.rescue_animal_center.repository;

import co.edu.umanizales.rescue_animal_center.infrastructure.csv.CsvFileStorage;
import co.edu.umanizales.rescue_animal_center.model.MedicalTreatment;
import co.edu.umanizales.rescue_animal_center.model.TreatmentType;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

@Repository
public class MedicalTreatmentCsvRepository implements CrudRepository<MedicalTreatment, String> {
    private static final String FILE = "treatments.csv";
    private static final String HEADER = "id,animalId,type,startDate,endDate,notes";

    private final CsvFileStorage storage;

    public MedicalTreatmentCsvRepository(CsvFileStorage storage) {
        this.storage = storage;
    }

    @Override
    public List<MedicalTreatment> findAll() {
        try {
            List<String> lines = storage.readAll(FILE);
            if (lines.isEmpty()) return new ArrayList<>();
            if (!lines.isEmpty() && lines.get(0).startsWith("id,")) {
                lines = lines.subList(1, lines.size());
            }
            List<MedicalTreatment> result = new ArrayList<>();
            for (String line : lines) {
                if (line == null || line.isBlank()) continue;
                String[] p = line.split(",", -1);
                if (p.length < 6) continue;
                result.add(new MedicalTreatment(
                        p[0],
                        p[1],
                        safeEnum(TreatmentType.class, p[2]),
                        safeDate(p[3]),
                        safeDate(p[4]),
                        p[5]
                ));
            }
            return result;
        } catch (IOException e) {
            throw new RuntimeException("Error reading treatments CSV", e);
        }
    }

    @Override
    public Optional<MedicalTreatment> findById(String id) {
        return findAll().stream().filter(t -> Objects.equals(t.getId(), id)).findFirst();
    }

    @Override
    public MedicalTreatment save(MedicalTreatment entity) {
        List<MedicalTreatment> current = findAll();
        if (entity.getId() == null || entity.getId().isBlank()) {
            entity.setId(UUID.randomUUID().toString());
        }
        current.add(entity);
        writeAll(current);
        return entity;
    }

    @Override
    public MedicalTreatment update(String id, MedicalTreatment entity) {
        List<MedicalTreatment> current = findAll();
        int idx = -1;
        for (int i = 0; i < current.size(); i++) {
            if (Objects.equals(current.get(i).getId(), id)) { idx = i; break; }
        }
        if (idx < 0) throw new NoSuchElementException("MedicalTreatment not found: " + id);
        entity.setId(id);
        current.set(idx, entity);
        writeAll(current);
        return entity;
    }

    @Override
    public void delete(String id) {
        List<MedicalTreatment> current = findAll();
        List<MedicalTreatment> filtered = new ArrayList<>();
        for (MedicalTreatment t : current) {
            if (!Objects.equals(t.getId(), id)) filtered.add(t);
        }
        if (filtered.size() == current.size()) {
            throw new NoSuchElementException("MedicalTreatment not found: " + id);
        }
        writeAll(filtered);
    }

    private void writeAll(List<MedicalTreatment> treatments) {
        List<String> lines = new ArrayList<>();
        lines.add(HEADER);
        for (MedicalTreatment t : treatments) {
            lines.add(String.join(",",
                    ns(t.getId()),
                    ns(t.getAnimalId()),
                    t.getType() == null ? "" : t.getType().name(),
                    t.getStartDate() == null ? "" : t.getStartDate().toString(),
                    t.getEndDate() == null ? "" : t.getEndDate().toString(),
                    ns(t.getNotes())
            ));
        }
        try {
            storage.writeAll(FILE, lines);
        } catch (IOException e) {
            throw new RuntimeException("Error writing treatments CSV", e);
        }
    }

    private static String ns(String s) { return s == null ? "" : s; }
    private static <E extends Enum<E>> E safeEnum(Class<E> type, String s) { try { return s == null || s.isBlank() ? null : Enum.valueOf(type, s); } catch (Exception e) { return null; } }
    private static LocalDate safeDate(String s) { try { return s == null || s.isBlank() ? null : LocalDate.parse(s); } catch (Exception e) { return null; } }
}
