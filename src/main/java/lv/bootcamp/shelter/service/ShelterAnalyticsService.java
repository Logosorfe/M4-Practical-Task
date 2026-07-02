package lv.bootcamp.shelter.service;

import lv.bootcamp.shelter.model.Animal;
import lv.bootcamp.shelter.service.data.ImportResult;
import lv.bootcamp.shelter.service.data.ShelterReportData;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class ShelterAnalyticsService {

    public ShelterReportData buildReportData(ImportResult importResult) {
        List<Animal> allAnimals = importResult.allAnimals();

        Set<String> uniqueSpecies = new TreeSet<>();
        allAnimals.stream()
                .map(Animal::getSpecies)
                .forEach(uniqueSpecies::add);

        Map<String, List<Animal>> animalsBySpecies = new TreeMap<>();
        for (String species : uniqueSpecies) {
            List<Animal> unique = allAnimals.stream()
                    .filter(animal -> animal.getSpecies().equals(species))
                    .toList();
            animalsBySpecies.put(species, unique);
        }

        List<String> animalsNeedingVetInput = new ArrayList<>();
        allAnimals.stream()
                .filter(animal -> animal.getAge() == null)
                .forEach(animal -> animalsNeedingVetInput.add(animal.getName()
                        + "(" + animal.getSpecies() + ")"));

        int skippedRows = importResult.skippedRows();

        List<Integer> invalidRows = new ArrayList<>(importResult.invalidRows());

        Map<String, Animal> oldestAnimals = new TreeMap<>();
        for (String species : uniqueSpecies) {
            Optional<Animal> oldestPerSpecies = allAnimals.stream()
                    .filter(animal -> animal.getSpecies().equals(species))
                    .filter(animal -> animal.getAge() != null)
                    .max((o1, o2) -> o1.getAge() - o2.getAge());
            oldestPerSpecies.ifPresent(animal -> oldestAnimals.put(species, animal));
        }

        Map<String, Long> vaccinatedPerSpecies =
                allAnimals.stream()
                        .collect(Collectors.groupingBy(
                                Animal::getSpecies,
                                TreeMap::new,
                                Collectors.filtering(Animal::isVaccinated, Collectors.counting())
                        ));


        LocalDate generatedDate = LocalDate.now();

        return new ShelterReportData(allAnimals,
                uniqueSpecies,
                animalsBySpecies,
                animalsNeedingVetInput,
                skippedRows,
                invalidRows,
                oldestAnimals,
                vaccinatedPerSpecies,
                generatedDate);
    }
}
