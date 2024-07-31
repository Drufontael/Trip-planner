package br.dev.drufontael.Trip_Planer.service;

import br.dev.drufontael.Trip_Planer.dto.UserDto;
import br.dev.drufontael.Trip_Planer.exception.InvalidDataException;
import br.dev.drufontael.Trip_Planer.exception.ResourceAlreadyExistsException;
import br.dev.drufontael.Trip_Planer.exception.ResourceNotFoundException;
import br.dev.drufontael.Trip_Planer.model.User;
import br.dev.drufontael.Trip_Planer.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository repository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    @Spy
    private UserService userService;

    @Test
    void registerUser() {
        // Arrange
        UserDto newUser = new UserDto("testUser", "password");
        User user = new User();
        user.setUsername(newUser.getUsername());

        Mockito.when(repository.findByUsername(newUser.getUsername())).thenReturn(Optional.empty());
        Mockito.when(passwordEncoder.encode(newUser.getPassword())).thenReturn("encodedPassword");

        // Act
        userService.registerUser(newUser);

        // Assert
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        Mockito.verify(repository).save(userCaptor.capture());
        User savedUser = userCaptor.getValue();

        assertEquals("testUser", savedUser.getUsername());
        assertEquals("encodedPassword", savedUser.getPassword());
    }

    @Test
    void registerUser_UsernameAlreadyExists() {
        // Arrange
        UserDto newUser = new UserDto("testUser", "password");
        User existingUser = new User();
        existingUser.setUsername(newUser.getUsername());

        Mockito.when(repository.findByUsername(newUser.getUsername())).thenReturn(Optional.of(existingUser));

        // Act & Assert
        assertThrows(ResourceAlreadyExistsException.class, () -> userService.registerUser(newUser));
        Mockito.verify(repository, Mockito.never()).save(Mockito.any(User.class));
    }
    @Test
    void authenticateUser() {
        // Arrange
        UserDto userDto = new UserDto("testUser", "password");
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPassword("encodedPassword");

        Mockito.when(repository.findByUsername(userDto.getUsername())).thenReturn(Optional.of(user));
        Mockito.when(passwordEncoder.matches(userDto.getPassword(), user.getPassword())).thenReturn(true);

        // Act
        User authenticatedUser = userService.authenticateUser(userDto);

        // Assert
        assertNotNull(authenticatedUser);
        assertEquals("testUser", authenticatedUser.getUsername());
    }
    @Test
    void authenticateUser_InvalidPassword() {
        // Arrange
        UserDto userDto = new UserDto("testUser", "wrongPassword");
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPassword("encodedPassword");

        Mockito.when(repository.findByUsername(userDto.getUsername())).thenReturn(Optional.of(user));
        Mockito.when(passwordEncoder.matches(userDto.getPassword(), user.getPassword())).thenReturn(false);

        // Act & Assert
        assertThrows(InvalidDataException.class, () -> userService.authenticateUser(userDto));

    }

    @Test
    void authenticateUser_UserNotFound() {
        // Arrange
        UserDto userDto = new UserDto("nonExistentUser", "password");

        Mockito.when(repository.findByUsername(userDto.getUsername())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> userService.authenticateUser(userDto));
    }

}