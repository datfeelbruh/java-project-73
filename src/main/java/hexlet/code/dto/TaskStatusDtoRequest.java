package hexlet.code.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TaskStatusDtoRequest implements Dto {
    @NotBlank
    @Size(min = 3, max = 1000)
    private String name;
}
