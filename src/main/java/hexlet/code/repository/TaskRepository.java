package hexlet.code.repository;

import hexlet.code.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;


/**
 * A class TaskStatusRepository extends and inherits methods {@link JpaRepository}, {@link QuerydslPredicateExecutor}.
 * for working with an entity Task
 * @author sobadxx
 */
@Repository
public interface TaskRepository extends
        JpaRepository<Task, Long>,
        QuerydslPredicateExecutor<Task> {

}
