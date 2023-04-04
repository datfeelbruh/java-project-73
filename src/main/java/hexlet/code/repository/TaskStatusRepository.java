package hexlet.code.repository;

import hexlet.code.model.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
/**
 * A class TaskStatusRepository extends and inherits methods {@link JpaRepository}
 * for working with an entity TaskStatus.
 * @author sobadxx
 */
@Repository
public interface TaskStatusRepository extends JpaRepository<TaskStatus, Long> {
}
