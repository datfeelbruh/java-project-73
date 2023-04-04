package hexlet.code.repository;

import hexlet.code.model.Label;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * A class LabelRepository extends and inherits methods {@link JpaRepository} for working with an entity TaskStatus.
 * @author sobadxx
 */
@Repository
public interface LabelRepository extends JpaRepository<Label, Long> {
}
