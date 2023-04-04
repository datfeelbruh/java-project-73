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
/**
 * Data transfer class for entity type {@link hexlet.code.model.Task}.
 *
 * @author sobadxx
 * @see hexlet.code.controller.TaskController
 * @see hexlet.code.service.TaskService
 */
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
