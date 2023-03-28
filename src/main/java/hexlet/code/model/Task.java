package hexlet.code.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tasks")
public class Task extends BaseModel {
    @NotBlank
    @Size(min = 3, max = 1000)
    private String name;
    @Lob
    private String description;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "task_status_id", foreignKey = @ForeignKey(name = "FK_TASKS_TASK_STATUS_ID_COL"))
    private TaskStatus taskStatus;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "author_id", foreignKey = @ForeignKey(name = "FK_TASKS_AUTHOR_ID_COL"))
    private User author;
    @ManyToOne
    @JoinColumn(name = "executor_id", foreignKey = @ForeignKey(name = "FK_TASKS_EXECUTOR_ID_COL"))
    private User executor;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {
        CascadeType.PERSIST,
        CascadeType.MERGE
    })
    @JoinTable(name = "task_labels",
        joinColumns = @JoinColumn(name = "task_id"),
        inverseJoinColumns = @JoinColumn(name = "labels_id")
    )
    private Set<Label> labelsIds;
}
