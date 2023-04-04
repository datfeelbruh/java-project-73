package hexlet.code.repository;

import hexlet.code.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * A class UserRepository extends and inherits methods {@link JpaRepository} for working with an entity User.
 * @author sobadxx
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * @param email of User
     * @return A container object which may or may not contain a User
     */
    Optional<User> findByEmail(String email);
}
