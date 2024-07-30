package br.dev.drufontael.Trip_Planer.controller;

import br.dev.drufontael.Trip_Planer.dto.UserDto;
import br.dev.drufontael.Trip_Planer.model.User;
import br.dev.drufontael.Trip_Planer.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AuthController {

    private final UserService service;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserDto userDto, HttpServletRequest request) {
        User user = service.authenticateUser(userDto);
        if (user != null) {
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            return ResponseEntity.ok("Login successful");
        }
        return ResponseEntity.status(401).body("Invalid username or password");
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody UserDto user) {
        service.registerUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/session")
    public ResponseEntity<String> getSession(HttpServletRequest request) {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user != null) {
            return ResponseEntity.ok(user.getUsername());
        }
        return ResponseEntity.status(401).body("session not found");
    }


}
