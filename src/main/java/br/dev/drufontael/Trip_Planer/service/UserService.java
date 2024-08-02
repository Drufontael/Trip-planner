package br.dev.drufontael.Trip_Planer.service;

import br.dev.drufontael.Trip_Planer.dto.UserDto;
import br.dev.drufontael.Trip_Planer.exception.InvalidDataException;
import br.dev.drufontael.Trip_Planer.exception.ResourceAlreadyExistsException;
import br.dev.drufontael.Trip_Planer.exception.ResourceNotFoundException;
import br.dev.drufontael.Trip_Planer.model.User;
import br.dev.drufontael.Trip_Planer.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

    @Transactional
    public User authenticateUser(UserDto user) {
        String username = user.getUsername();
        String password = user.getPassword();

        return repository.findByUsername(username).map(user1->{
            if(passwordEncoder.matches(password, user1.getPassword())) {
                return user1;
            }else {
                throw new InvalidDataException("Incorrect password");
            }
        }).orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }



}
