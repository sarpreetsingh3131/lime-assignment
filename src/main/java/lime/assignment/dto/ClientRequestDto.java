package lime.assignment.dto;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class ClientRequestDto {

    @NotNull
    private List<String> participantsIds;

    @Min(30)
    @Max(540) // 8 hrs
    @NotNull
    private Integer meetingLength;

    @NotBlank
    private String earliestDateTime;

    @NotBlank
    private String latestDateTime;

    @NotBlank
    private String timeZone;
}
