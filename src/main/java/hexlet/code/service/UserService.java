package hexlet.code.service;

import hexlet.code.dto.UserDtoRequest;
import hexlet.code.exception.ResourceNotFoundException;

import hexlet.code.model.User;
import hexlet.code.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import static hexlet.code.config.security.SecurityConfig.DEFAULT_AUTHORITIES;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public final class UserService implements UserDetailsService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final Logger USER_SERVICE_LOGGER = LoggerFactory.getLogger(UserService.class);

    public User getUserById(Long id) {
        return findById(id);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User createUser(UserDtoRequest userDtoRequest) {
        User user = new User();

        user.setEmail(userDtoRequest.getEmail());
        user.setFirstName(userDtoRequest.getFirstName());
        user.setLastName(userDtoRequest.getLastName());
        user.setPassword(passwordEncoder.encode(userDtoRequest.getPassword()));

        return userRepository.save(user);
    }

    public User updateUser(UserDtoRequest userDtoRequest, Long id) {
        User userToUpdate = findById(id);

        userToUpdate.setEmail(userDtoRequest.getEmail());
        userToUpdate.setFirstName(userDtoRequest.getFirstName());
        userToUpdate.setLastName(userDtoRequest.getLastName());
        userToUpdate.setPassword(passwordEncoder.encode(userDtoRequest.getPassword()));

        return userRepository.save(userToUpdate);
    }

    public void deleteUser(Long id) {
        User user = findById(id);
        userRepository.delete(user);
    }

    private User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException("User with id " + id + " not found"));
    }


    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(
                        () -> new ResourceNotFoundException("User with login " + email + " not found")
                );
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
