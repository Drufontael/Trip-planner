package br.dev.drufontael.Trip_Planer.service;

import br.dev.drufontael.Trip_Planer.dto.UserDto;
import br.dev.drufontael.Trip_Planer.exception.ResourceAlreadyExistsException;
import br.dev.drufontael.Trip_Planer.model.User;
import br.dev.drufontael.Trip_Planer.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void registerUser(UserDto newUser) {
        if(repository.findByUsername(newUser.getUsername()).isPresent()) {
            throw new ResourceAlreadyExistsException("Username already exists");
        }
        User user = new User();
        user.setUsername(newUser.getUsername());
        String encodedPassword = passwordEncoder.encode(newUser.getPassword());
        user.setPassword(encodedPassword);

        repository.save(user);
    }

    public User authenticateUser(User user) {
        String username = user.getUsername();
        String password = user.getPassword();
        Optional<User> optionalUser = repository.findByUsername(username);
        if (optionalUser.isPresent() && passwordEncoder.matches(password, optionalUser.get().getPassword())) {
            return optionalUser.get();
        }
        return null;
    }


}
