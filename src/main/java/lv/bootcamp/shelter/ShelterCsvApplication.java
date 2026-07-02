package lv.bootcamp.shelter;

import lv.bootcamp.shelter.service.CsvImportService;
import lv.bootcamp.shelter.service.data.ImportResult;
import lv.bootcamp.shelter.service.ReportExportService;
import lv.bootcamp.shelter.service.ShelterAnalyticsService;
import lv.bootcamp.shelter.service.data.ShelterReportData;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;

public class ShelterCsvApplication {

    public static void main(String[] args) {

        Path dataFolder = Path.of("src", "main", "resources", "data");
        Path outputFolder = Path.of("output");

        CsvImportService importService = new CsvImportService();
        ShelterAnalyticsService analyticsService = new ShelterAnalyticsService();
        ReportExportService reportExportService = new ReportExportService();

        try (Stream<Path> paths = Files.walk(dataFolder)) {

            paths.filter(Files::isRegularFile)
                    .filter(p -> p.toString().toLowerCase().endsWith(".csv"))
                    .forEach(csvPath -> {
                        ImportResult result = importService.importAnimals(csvPath);
                        ShelterReportData reportData = analyticsService.buildReportData(result);
                        String reportName = csvPath.getFileName().toString().replace(".csv", "")
                                + "-report-" + LocalDateTime.now()
                                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss")) + ".txt";
                        Path outputPath = outputFolder.resolve(reportName);

                        reportExportService.writeReport(outputPath, reportData);
                    });

        } catch (IOException e) {
            System.out.println("Error scanning directory: " + dataFolder);
        }
    }
}
