package lv.bootcamp.shelter.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter //when every field is final?
@AllArgsConstructor
@ToString
public class Animal {

    private final String name;
    private final String species;
    private final Integer age;
    private final boolean vaccinated;
    private final LocalDate intakeDate;

}
