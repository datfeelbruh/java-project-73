package hexlet.code.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
/**
 * Data transfer class for entity type {@link hexlet.code.model.Label}.
 *
 * @author sobadxx
 * @see hexlet.code.controller.LabelController
 * @see hexlet.code.service.LabelService
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LabelDtoRequest implements Dto {
    @NotBlank
    @Size(min = 3, max = 1000)
    private String name;
}
