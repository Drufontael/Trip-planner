package br.dev.drufontael.Trip_Planer.repository;

import br.dev.drufontael.Trip_Planer.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")  // Define o perfil de teste para usar application-test.properties
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void testFindByUsername() {
        // Arrange
        User user = new User();
        user.setUsername("testUser");
        user.setPassword("password");
        userRepository.save(user);

        // Act
        Optional<User> foundUser = userRepository.findByUsername("testUser");

        // Assert
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getUsername()).isEqualTo("testUser");
    }

    @Test
    void testFindByUsername_UserNotFound() {
        // Act
        Optional<User> foundUser = userRepository.findByUsername("nonexistentUser");

        // Assert
        assertThat(foundUser).isNotPresent();
    }
}