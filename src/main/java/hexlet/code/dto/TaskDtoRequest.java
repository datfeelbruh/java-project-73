package hexlet.code.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class TaskDtoRequest implements Dto {
    private String name;
    private String description;
    private Long executorId;
    private Long taskStatusId;
    private Set<Long> labels;
}
