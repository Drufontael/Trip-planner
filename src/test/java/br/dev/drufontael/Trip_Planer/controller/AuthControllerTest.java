package br.dev.drufontael.Trip_Planer.controller;

import br.dev.drufontael.Trip_Planer.configuration.WebSecurityConfig;
import br.dev.drufontael.Trip_Planer.dto.UserDto;
import br.dev.drufontael.Trip_Planer.model.User;
import br.dev.drufontael.Trip_Planer.service.UserService;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;



import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@ExtendWith(SpringExtension.class)
@WebMvcTest(AuthController.class)
@Import(WebSecurityConfig.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    void login_Success() throws Exception {
        // Arrange
        UserDto userDto = new UserDto("testUser", "password");
        User user = new User();
        user.setUsername(userDto.getUsername());

        Mockito.when(userService.authenticateUser(userDto)).thenReturn(user);

        // Act & Assert
        mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"testUser\",\"password\":\"password\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Login successful"));
    }

    @Test
    void login_InvalidCredentials() throws Exception {
        // Arrange
        UserDto userDto = new UserDto("testUser", "wrongPassword");

        Mockito.when(userService.authenticateUser(userDto)).thenReturn(null);

        // Act & Assert
        mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"testUser\",\"password\":\"wrongPassword\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Invalid username or password"));
    }

    @Test
    void register_Success() throws Exception {
        // Arrange
        UserDto userDto = new UserDto("newUser", "password");

        // Act & Assert
        mockMvc.perform(post("/api/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"newUser\",\"password\":\"password\"}"))
                .andExpect(status().isCreated());

        Mockito.verify(userService).registerUser(Mockito.any(UserDto.class));
    }

    @Test
    void getSession_Success() throws Exception {
        // Arrange
        User user = new User();
        user.setUsername("testUser");

        // Act & Assert
        mockMvc.perform(get("/api/session")
                        .sessionAttr("user", user))
                .andExpect(status().isOk())
                .andExpect(content().string("testUser"));
    }

    @Test
    void getSession_NotFound() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/session"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("session not found"));
    }
}
