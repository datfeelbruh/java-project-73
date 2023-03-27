package hexlet.code.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskFilter {
    private Long taskStatus;
    private Long executorId;
    private Long labels;
    private Long authorId;
}
