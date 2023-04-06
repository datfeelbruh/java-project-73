package hexlet.code.service;

import com.rollbar.notifier.Rollbar;
import hexlet.code.dto.UserDtoRequest;
import hexlet.code.model.User;
import hexlet.code.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import static hexlet.code.config.security.SecurityConfig.DEFAULT_AUTHORITIES;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * A class that implements the application logic for processing requests
 * from the {@link hexlet.code.controller.UserController}.
 * <p></p>
 * Contains a {@link UserRepository} bean for interacting with the users table in the database.
 * It also contains a {@link PasswordEncoder} bean to encrypt the user's password.
 *
 * @author sobadxx
 * @see hexlet.code.controller.UserController
 */
@Service
public final class UserService implements UserDetailsService {
    @Autowired
    private Rollbar rollbar;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Retrieves an entity by its id.
     *
     * @param id entity id
     * @return result of the findById method
     */
    public User getUserById(Long id) {
        return findById(id);
    }

    /**
     * Returns all entities of type {@link User} from the database.
     *
     * @return all entities of type {@link User}
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Creates an entity type {@link User} and saves it to the database.
     *
     * @param userDtoRequest
     * {@link UserDtoRequest} DTO-object with the fields necessary to create a {@link User}
     * @return created {@link User}
     */
    public User createUser(UserDtoRequest userDtoRequest) {
        User user = new User();

        user.setEmail(userDtoRequest.getEmail());
        user.setFirstName(userDtoRequest.getFirstName());
        user.setLastName(userDtoRequest.getLastName());
        user.setPassword(passwordEncoder.encode(userDtoRequest.getPassword()));

        return userRepository.save(user);
    }

    /**
     * Updates the User object and stores the updated {@link User} in the database.
     *
     * @param userDtoRequest
     * {@link UserDtoRequest} DTO-object with the fields necessary to update a {@link User}
     * @param id id of the user whose data is being updated
     * @return updated {@link User}
     */
    public User updateUser(UserDtoRequest userDtoRequest, Long id) {
        User userToUpdate = findById(id);

        if (userToUpdate.getId() != getCurrentUser().getId()) {
            rollbar.error(new AccessDeniedException("rollbar"), "rollbar");
            throw new AccessDeniedException("Unable to delete user by another user");
        }

        userToUpdate.setEmail(userDtoRequest.getEmail());
        userToUpdate.setFirstName(userDtoRequest.getFirstName());
        userToUpdate.setLastName(userDtoRequest.getLastName());
        userToUpdate.setPassword(passwordEncoder.encode(userDtoRequest.getPassword()));

        return userRepository.save(userToUpdate);
    }

    /**
     * Delete the {@link User} object in the database.
     *
     * @param id id of the {@link User} whose is being deleted
     */
    public void deleteUser(Long id) {
        User user = findById(id);

        if (user.getId() != getCurrentUser().getId()) {
            throw new AccessDeniedException("Unable to delete user by another user");
        }

        userRepository.delete(user);
    }

    /**
     * Retrieves an entity by its id.
     *
     * @param id entity id
     * @return the entity with given id
     * @throws NoSuchElementException if Entity with {@literal id} not found
     */
    private User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(
                        () -> new NoSuchElementException("User with id " + id + " not found"));
    }

    /**
     * Returns the authenticated {@link User} to check the actions associated with
     * {@link #updateUser(UserDtoRequest, Long)},
     * {@link #deleteUser(Long)},
     * {@link TaskService},
     * {@link TaskService},
     * {@link TaskService}.
     *
     * @return current authenticated {@link User}
     */
    public User getCurrentUser() {
        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(currentUserEmail).get();
    }

    @Override
    public UserDetails loadUserByUsername(final String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User with this " + email + " not found"));

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                DEFAULT_AUTHORITIES
        );

    }
}
