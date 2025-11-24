package co.edu.umanizales.rescue_animal_center.repository;

import co.edu.umanizales.rescue_animal_center.infrastructure.csv.CsvFileStorage;
import co.edu.umanizales.rescue_animal_center.model.Donation;
import co.edu.umanizales.rescue_animal_center.model.DonationType;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Repository
public class DonationCsvRepository implements CrudRepository<Donation, String> {
    private static final String FILE = "donations.csv";
    private static final String HEADER = "id,donorName,type,amount,date";

    private final CsvFileStorage storage;

    public DonationCsvRepository(CsvFileStorage storage) {
        this.storage = storage;
    }

    @Override
    public List<Donation> findAll() {
        try {
            List<String> lines = storage.readAll(FILE);
            if (lines.isEmpty()) return new ArrayList<>();
            if (!lines.isEmpty() && lines.get(0).startsWith("id,")) {
                lines = lines.subList(1, lines.size());
            }
            List<Donation> result = new ArrayList<>();
            for (String line : lines) {
                if (line == null || line.isBlank()) continue;
                String[] p = line.split(",", -1);
                if (p.length < 5) continue;
                result.add(new Donation(
                        p[0],
                        p[1],
                        safeEnum(DonationType.class, p[2]),
                        safeDecimal(p[3]),
                        safeDate(p[4])
                ));
            }
            return result;
        } catch (IOException e) {
            throw new RuntimeException("Error reading donations CSV", e);
        }
    }

    @Override
    public Optional<Donation> findById(String id) {
        return findAll().stream().filter(d -> Objects.equals(d.getId(), id)).findFirst();
    }

    @Override
    public Donation save(Donation entity) {
        List<Donation> current = findAll();
        if (entity.getId() == null || entity.getId().isBlank()) {
            entity.setId(UUID.randomUUID().toString());
        }
        current.add(entity);
        writeAll(current);
        return entity;
    }

    @Override
    public Donation update(String id, Donation entity) {
        List<Donation> current = findAll();
        int idx = -1;
        for (int i = 0; i < current.size(); i++) {
            if (Objects.equals(current.get(i).getId(), id)) { idx = i; break; }
        }
        if (idx < 0) throw new NoSuchElementException("Donation not found: " + id);
        entity.setId(id);
        current.set(idx, entity);
        writeAll(current);
        return entity;
    }

    @Override
    public void delete(String id) {
        List<Donation> current = findAll();
        List<Donation> filtered = new ArrayList<>();
        for (Donation d : current) {
            if (!Objects.equals(d.getId(), id)) filtered.add(d);
        }
        if (filtered.size() == current.size()) {
            throw new NoSuchElementException("Donation not found: " + id);
        }
        writeAll(filtered);
    }

    private void writeAll(List<Donation> donations) {
        List<String> lines = new ArrayList<>();
        lines.add(HEADER);
        for (Donation d : donations) {
            lines.add(String.join(",",
                    ns(d.getId()),
                    ns(d.getDonorName()),
                    d.getType() == null ? "" : d.getType().name(),
                    d.getAmount() == null ? "" : d.getAmount().toPlainString(),
                    d.getDate() == null ? "" : d.getDate().toString()
            ));
        }
        try {
            storage.writeAll(FILE, lines);
        } catch (IOException e) {
            throw new RuntimeException("Error writing donations CSV", e);
        }
    }

    private static String ns(String s) { return s == null ? "" : s; }
    private static <E extends Enum<E>> E safeEnum(Class<E> type, String s) { try { return s == null || s.isBlank() ? null : Enum.valueOf(type, s); } catch (Exception e) { return null; } }
    private static BigDecimal safeDecimal(String s) { try { return s == null || s.isBlank() ? null : new BigDecimal(s); } catch (Exception e) { return null; } }
    private static LocalDate safeDate(String s) { try { return s == null || s.isBlank() ? null : LocalDate.parse(s); } catch (Exception e) { return null; } }
}
