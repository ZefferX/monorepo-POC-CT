package com.cognito.connection.controller;


import com.amazonaws.services.cognitoidp.model.AdminInitiateAuthResult;
import com.amazonaws.services.cognitoidp.model.NotAuthorizedException;
import com.amazonaws.services.cognitoidp.model.UserNotFoundException;
import com.cognito.connection.service.CognitoService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private CognitoService cognitoService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            AdminInitiateAuthResult authResult = cognitoService.login(request.getUsername(), request.getPassword());
            LoginResponse response = new LoginResponse(
                    authResult.getAuthenticationResult().getAccessToken(),
                    authResult.getAuthenticationResult().getIdToken(),
                    authResult.getAuthenticationResult().getRefreshToken()
            );
            return ResponseEntity.ok(response);
        } catch (NotAuthorizedException e) {
            return ResponseEntity.status(401).body("Credenciales inválidas.");
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(404).body("Usuario no encontrado.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error interno del servidor.");
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class LoginRequest {
        private String username;
        private String password;
    }

    @Data
    @AllArgsConstructor
    static class LoginResponse {
        private String accessToken;
        private String idToken;
        private String refreshToken;
    }
}
