package co.edu.umanizales.rescue_animal_center.infrastructure.csv;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class CsvFileStorage {
    private final Path basePath;

    public CsvFileStorage(String baseDir) {
        this.basePath = Paths.get(baseDir);
    }

    public List<String> readAll(String relativeFile) throws IOException {
        Path file = basePath.resolve(relativeFile);
        if (!Files.exists(file)) {
            return new ArrayList<>();
        }
        return Files.readAllLines(file, StandardCharsets.UTF_8);
    }

    public void writeAll(String relativeFile, List<String> lines) throws IOException {
        Path file = basePath.resolve(relativeFile);
        if (file.getParent() != null && !Files.exists(file.getParent())) {
            Files.createDirectories(file.getParent());
        }
        Files.write(file, lines, StandardCharsets.UTF_8);
    }

    public void append(String relativeFile, String line) throws IOException {
        Path file = basePath.resolve(relativeFile);
        if (file.getParent() != null && !Files.exists(file.getParent())) {
            Files.createDirectories(file.getParent());
        }
        if (!Files.exists(file)) {
            Files.createFile(file);
        }
        Files.write(file, List.of(line), StandardCharsets.UTF_8, java.nio.file.StandardOpenOption.APPEND);
    }
}
