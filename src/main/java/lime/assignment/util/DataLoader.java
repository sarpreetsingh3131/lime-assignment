package lime.assignment.util;

import lime.assignment.model.Employee;
import lime.assignment.model.TimeSlot;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class DataLoader {

    @Value("classpath:freebusy.txt")
    private Resource resource;

    public List<Employee> load() throws Exception {
        Map<String, Employee> employees = new LinkedHashMap<>();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(resource.getInputStream()));
        String line;

        while ((line = bufferedReader.readLine()) != null) {
            try {
                String[] data = line.split(";");
                Employee employee = employees.get(data[0]);
                if (employee == null) {
                    employee = new Employee(data[0], new LinkedList<>());
                    employees.put(data[0], employee);
                }

                if (data.length == 2) {
                    employee.setName(data[1]);
                } else {
                    employee.getTimeSlots().add(new TimeSlot(
                            DateTimeParser.parse(data[1]), // start
                            DateTimeParser.parse(data[2]), // end
                            false // time slot is not free
                    ));
                }
            } catch (Exception e) {
                // ignore bad data
                System.out.println(e.getMessage());
            }
        }

        bufferedReader.close();
        return employees.values()
                .stream()
                .filter(employee -> employee.getName() != null) // remove employees with missing data
                .collect(Collectors.toList());
    }
}
