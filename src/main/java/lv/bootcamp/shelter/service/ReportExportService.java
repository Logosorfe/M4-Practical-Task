package lv.bootcamp.shelter.service;

import lv.bootcamp.shelter.service.data.ShelterReportData;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.format.DateTimeFormatter;

public class ReportExportService {

    public void writeReport(Path outputPath, ShelterReportData reportData) {

        try (BufferedWriter bw = Files.newBufferedWriter(outputPath)) {
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            StringBuilder sb = new StringBuilder();

            sb.append("=== Shelter Intake Report ===\n");
            sb.append("Date generated: ").append(reportData.generatedDate().format(fmt)).append("\n\n");

            sb.append("Total imported: ").append(reportData.allAnimals().size()).append("\n");
            sb.append("Total skipped: ").append(reportData.skippedRows()).append("\n");
            sb.append("Invalid rows: ").append(reportData.invalidRows()).append("\n\n");

            sb.append("--- Unique species ---\n");
            sb.append(String.join(", ", reportData.uniqueSpecies())).append("\n\n");

            sb.append("--- Per species breakdown ---\n");
            reportData.vaccinatedPerSpecies().forEach((species, vaccinatedCount) -> {
                int total = reportData.animalsBySpecies().get(species).size();
                sb.append(species)
                        .append(": ")
                        .append(total).append(" total, ")
                        .append(vaccinatedCount).append(" vaccinated\n");
            });
            sb.append("\n");

            sb.append("--- Oldest per species ---\n");
            reportData.oldestAnimals().forEach((species, animal) -> {
                sb.append(species)
                        .append(": ")
                        .append(animal.getName())
                        .append(" (age ").append(animal.getAge()).append(")\n");
            });
            sb.append("\n");

            sb.append("--- Needs Vet input to determine age ---\n");
            sb.append(String.join(", ", reportData.animalsNeedingVetInput()));

            bw.write(sb.toString());

        } catch (FileNotFoundException w) {
            System.out.println("Incorrect file path. Try again");
        } catch (IOException e) {
            System.out.println("Error while accessing file. Try again");
        }
    }
}
