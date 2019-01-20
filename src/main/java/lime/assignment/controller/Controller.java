package lime.assignment.controller;

import lime.assignment.dto.ClientRequestDto;
import lime.assignment.model.Employee;
import lime.assignment.service.TimeSlotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/api/free-time-slots", produces = MediaType.APPLICATION_JSON_VALUE)
@ResponseBody
public class Controller {

    @Autowired
    private TimeSlotService service;

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public List<Employee> findEmployeesWithFreeTimeSlots(@Valid @RequestBody ClientRequestDto dto) throws Exception {
        return service.findEmployeesWithFreeTimeSlots(dto);
    }
}
