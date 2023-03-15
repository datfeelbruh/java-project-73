package hexlet.code.service;

import hexlet.code.dto.UserDtoRq;
import hexlet.code.exception.ResourceNotFoundException;

import hexlet.code.model.User;
import hexlet.code.repository.UserRepository;
import hexlet.code.security.JwtTokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public final class UserService implements UserDetailsService {
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

    public User createUser(UserDtoRq userDtoRq) {
        User user = new User();

        user.setEmail(userDtoRq.getEmail());
        user.setFirstName(userDtoRq.getFirstName());
        user.setLastName(userDtoRq.getLastName());
        user.setPassword(passwordEncoder.encode(userDtoRq.getPassword()));

        return userRepository.save(user);
    }

    public User updateUser(UserDtoRq userDtoRq, Long id) {
        User userToUpdate = findById(id);

        userToUpdate.setEmail(userDtoRq.getEmail());
        userToUpdate.setFirstName(userDtoRq.getFirstName());
        userToUpdate.setLastName(userDtoRq.getLastName());
        userToUpdate.setPassword(passwordEncoder.encode(userDtoRq.getPassword()));

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
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User with this " + email + " not found"));
    }
}
