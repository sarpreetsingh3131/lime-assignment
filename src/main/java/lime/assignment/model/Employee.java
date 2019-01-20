package lime.assignment.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.List;

@NoArgsConstructor
@RequiredArgsConstructor
@Data
public class Employee {

    @NonNull
    private String id;

    private String name;

    @NonNull
    private List<TimeSlot> timeSlots;
}
