package lime.assignment.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.time.Duration;
import java.time.LocalDateTime;

@NoArgsConstructor
@RequiredArgsConstructor
@Data
public class TimeSlot {

    @NonNull
    @JsonIgnore
    private LocalDateTime start;

    @NonNull
    @JsonIgnore
    private LocalDateTime end;

    @NonNull
    @JsonIgnore
    private Boolean isFree;

    @JsonIgnore
    public Integer getDuration() {
        return (int) Duration.between(start, end).toMinutes();
    }

    public String getStartDate() {
        return start.toLocalDate().toString();
    }

    public String getStartTime() {
        return start.toLocalTime().toString();
    }

    public String getEndDate() {
        return end.toLocalDate().toString();
    }

    public String getEndTime() {
        return end.toLocalTime().toString();
    }
}
