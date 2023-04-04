package hexlet.code.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;


/**
 * A class TaskStatus extends {@link BaseModel} to describe the fields an entity of type TaskStatus.
 * @author sobadxx
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "task_statuses")
public class TaskStatus extends BaseModel {
    @NotBlank
    @Column(unique = true)
    @Size(min = 1)
    private String name;
}
