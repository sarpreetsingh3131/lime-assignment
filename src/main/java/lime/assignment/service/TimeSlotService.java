package lime.assignment.service;

import lime.assignment.dto.ClientRequestDto;
import lime.assignment.model.Employee;
import lime.assignment.model.TimeSlot;
import lime.assignment.util.DataLoader;
import lime.assignment.util.DateTimeParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TimeSlotService {

    @Autowired
    private DataLoader dataLoader;

    public List<Employee> findEmployeesWithFreeTimeSlots(ClientRequestDto dto) throws Exception {
        verifyMeetingLength(dto.getMeetingLength());
        List<Employee> employees = findEmployee(dto.getParticipantsIds());
        LocalDateTime earliestDateTime = DateTimeParser.parse(dto.getEarliestDateTime());
        LocalDateTime latestDateTime = DateTimeParser.parse(dto.getLatestDateTime());
        Long daysDifference = Duration.between(earliestDateTime, latestDateTime).toDays();

        for (Employee employee : employees) {
            List<TimeSlot> timeSlots = new LinkedList<>();
            LocalDateTime dateTime = earliestDateTime;
            for (int i = 0; i <= daysDifference; i++) { // for each day between earliest and latest
                timeSlots.addAll(addFreeTimeSlots(
                        findFilledTimeSlots(employee.getTimeSlots(), dateTime),
                        dateTime,
                        dto.getMeetingLength()
                ));
                dateTime = dateTime.plusDays(1); // increment to next day
            }
            employee.setTimeSlots(timeSlots);
        }
        return employees;
    }

    List<TimeSlot> findFilledTimeSlots(List<TimeSlot> allTimeSlots, LocalDateTime dateTime) {
        List<TimeSlot> filledTimeSlots = new LinkedList<>();
        allTimeSlots.forEach(timeSlot -> {
            if (timeSlot.getStart().toLocalDate().isEqual(dateTime.toLocalDate())) {
                filledTimeSlots.add(timeSlot);
            }
        });
        return filledTimeSlots;
    }

    List<TimeSlot> addFreeTimeSlots(List<TimeSlot> filledTimeSlots, LocalDateTime dateTime, int meetingLength) {
        // no booked time slot -> free whole day
        if (filledTimeSlots.isEmpty()) {
            filledTimeSlots.add(new TimeSlot(
                    LocalDateTime.of(dateTime.toLocalDate(), LocalTime.of(8, 0, 0)),
                    LocalDateTime.of(dateTime.toLocalDate(), LocalTime.of(17, 0, 0)),
                    true
            ));
            return filledTimeSlots;
        }

        List<TimeSlot> timeSlots = new LinkedList<>(filledTimeSlots); // make copy of existing time slots
        timeSlots.sort(Comparator.comparing(timeSlot -> timeSlot.getStart())); // for finding free time slots

        int index = -1;
        while (++index < timeSlots.size()) {
            TimeSlot current = timeSlots.get(index);
            TimeSlot next = index + 1 < timeSlots.size() - 1 ? timeSlots.get(index + 1) : null;

            // if first time slot starts before 8 AM, then add 1 free time slot
            if (index == 0) {
                long duration = Duration
                        .between(LocalTime.of(8, 0, 0), current.getStart().toLocalTime())
                        .getSeconds();

                if (duration > 0) {
                    filledTimeSlots.add(new TimeSlot(
                            LocalDateTime.of(dateTime.toLocalDate(), LocalTime.of(8, 0, 0)),
                            current.getStart(), true
                    ));
                }
            }

            // if last time slot ends before 5 PM, then add 1 free time slot
            if (index == timeSlots.size() - 1) {
                long duration = Duration
                        .between(LocalTime.of(17, 0, 0), current.getEnd().toLocalTime())
                        .getSeconds();

                if (duration < 0) {
                    filledTimeSlots.add(new TimeSlot(
                            current.getEnd(),
                            LocalDateTime.of(dateTime.toLocalDate(), LocalTime.of(17, 0, 0)),
                            true
                    ));
                }
                break;
            }

            if (next != null) {
                filledTimeSlots.add(new TimeSlot(current.getEnd(), next.getStart(), true));
            } else {
                filledTimeSlots.add(new TimeSlot(current.getEnd(), timeSlots.get(index + 1).getStart(), true));
            }
        }

        // return free time slots with duration >= desired meeting length
        return filledTimeSlots
                .stream()
                .filter(timeSlot -> timeSlot.getIsFree() && timeSlot.getDuration() >= meetingLength)
                .sorted(Comparator.comparing(t -> t.getStart()))
                .collect(Collectors.toList());
    }


    List<Employee> findEmployee(List<String> participantsIds) throws Exception {
        List<Employee> employees = dataLoader.load()
                .stream()
                .filter(employee -> participantsIds.contains(employee.getId()))
                .collect(Collectors.toList());

        if (employees.size() != participantsIds.size()) {
            throw new Exception("cannot find all the employees");
        }
        return employees;
    }

    void verifyMeetingLength(int meetingLength) throws Exception {
        if (meetingLength % 30 != 0) {
            throw new Exception("meeting length should be either whole or half hour. e.g. 08.00, 08.30, 09.00, etc");
        }
    }
}
