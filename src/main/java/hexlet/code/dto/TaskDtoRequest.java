package hexlet.code.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class TaskDtoRequest implements Dto {
    @NotBlank
    @Size(min = 3, max = 1000)
    private String name;
    private String description;
    private Long executorId;
    private Long taskStatusId;
    private Set<Long> labelIds;
}
