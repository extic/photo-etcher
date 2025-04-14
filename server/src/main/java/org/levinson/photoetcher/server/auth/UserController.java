package org.levinson.photoetcher.server.auth;

import lombok.RequiredArgsConstructor;
import org.levinson.photoetcher.server.config.UserAuthTokenCreator;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserAuthTokenCreator tokenCreator;

    @PostMapping("/login")
    public ResponseEntity<LoginUserDto> login(@RequestBody CredentialsDto credentials) {
        var user = userService.login(credentials);
        var token = tokenCreator.createToken(user);

        var roles = user.roles().stream().map(Role::name).toList();
        var response = new LoginUserDto(user.firstName(), user.lastName(), user.username(), roles);

        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .body(response);
    }

    @GetMapping("/current-user")
    public ResponseEntity<LoginUserDto> getCurrentUser(@AuthenticationPrincipal String username) {
        var user = userService.findByUsername(username);

        var roles = user.roles().stream().map(Role::name).toList();
        var response = new LoginUserDto(user.firstName(), user.lastName(), user.username(), roles);

        return ResponseEntity.ok(response);
    }

    public record LoginUserDto(
            String firstName,
            String lastName,
            String username,
            List<String> roles
    ) {}
}
