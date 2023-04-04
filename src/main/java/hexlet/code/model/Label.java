package hexlet.code.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * A class Label extends {@link BaseModel} to describe the fields an entity of type Label.
 * @author sobadxx
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "labels")
public class Label extends BaseModel {
    @NotNull
    @Size(min = 1)
    private String name;
}
