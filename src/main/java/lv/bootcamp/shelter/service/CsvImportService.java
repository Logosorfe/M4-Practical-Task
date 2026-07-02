package lv.bootcamp.shelter.service;

import lombok.extern.slf4j.Slf4j;
import lv.bootcamp.shelter.model.Animal;
import lv.bootcamp.shelter.service.data.ImportResult;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class CsvImportService {

    public ImportResult importAnimals(Path inputPath) {
        log.info("Starting import from {}", inputPath);

        List<Animal> allAnimals = new ArrayList<>();

        int skippedRows = 0;
        int invalidRow = 2;
        List<Integer> invalidRows = new ArrayList<>();

        try (BufferedReader br = Files.newBufferedReader(inputPath, StandardCharsets.UTF_8)) {
            String header = br.readLine();
            log.warn("Skipping header row {}", header);

            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",", -1);
                if (parts.length < 5) {
                    log.warn("Skipping malformed row - {}", line);
                    skippedRows++;
                    invalidRows.add(invalidRow);
                    invalidRow++;
                    continue;
                }

                String name = parts[0].trim();
                if (name.isBlank()) {
                    log.warn("Skipping malformed row - {}", line);
                    System.out.println("Wrong name format: \"\"");
                    skippedRows++;
                    invalidRows.add(invalidRow);
                    invalidRow++;
                    continue;
                }

                String species = parts[1].trim();
                if (species.isBlank()) {
                    log.warn("Skipping malformed row - {}", line);
                    System.out.println("Wrong species format: \"\"");
                    skippedRows++;
                    invalidRows.add(invalidRow);
                    invalidRow++;
                    continue;
                }

                Integer age;
                try {
                    age = parts[2].trim().isEmpty() ? null : Integer.parseInt(parts[2].trim());
                } catch (NumberFormatException e) {
                    System.out.println("Wrong age format: " + parts[2].trim());
                    log.warn("Skipping malformed row - {}", line);
                    skippedRows++;
                    invalidRows.add(invalidRow);
                    invalidRow++;
                    continue;
                }

                String isBoolean = parts[3].trim();
                if (!isBoolean.equals("true") && !isBoolean.equals("false")) {
                    System.out.println("Wrong boolean format: " + parts[3].trim());
                    log.warn("Skipping malformed row - {}", line);
                    skippedRows++;
                    invalidRows.add(invalidRow);
                    invalidRow++;
                    continue;
                }
                boolean vaccinated = Boolean.parseBoolean(parts[3].trim());

                LocalDate intakeDate = null;
                try {
                    intakeDate = LocalDate.parse(
                            parts[4].trim(),
                            DateTimeFormatter.ofPattern("dd.MM.yyyy")
                    );
                } catch (DateTimeException e) {
                    System.out.println("Wrong date format: " + parts[4].trim());
                    log.warn("Skipping malformed row - {}", line);
                    skippedRows++;
                    invalidRows.add(invalidRow);
                    invalidRow++;
                    continue;
                }

                allAnimals.add(new Animal(name, species, age, vaccinated, intakeDate));
                invalidRow++;
            }
        } catch (FileNotFoundException w) {
            System.out.println("Incorrect file path. Try again");
        } catch (IOException e) {
            System.out.println("Error while accessing file. Try again");
        }

        return new ImportResult(allAnimals, skippedRows, invalidRows);
    }
}
