package lv.bootcamp.shelter.service.data;

import lv.bootcamp.shelter.model.Animal;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;

public record ShelterReportData(List<Animal> allAnimals,
                                Set<String> uniqueSpecies,
                                Map<String, List<Animal>> animalsBySpecies,
                                List<String> animalsNeedingVetInput,
                                int skippedRows,
                                List<Integer> invalidRows,
                                Map<String, Animal> oldestAnimals,
                                Map<String, Long> vaccinatedPerSpecies,
                                LocalDate generatedDate) {

}
