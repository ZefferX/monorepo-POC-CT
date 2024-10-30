package poc.custom.token.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/test")
@AllArgsConstructor

public class PermissionsController {

    @GetMapping("/protected")
        public ResponseEntity<String> accessProtectedResource(HttpServletRequest request) {
        // Recuperar los permisos del usuario que fueron agregados por el filtro
        List<String> userPermissions = (List<String>) request.getAttribute("userPermissions");

        if (userPermissions == null || userPermissions.isEmpty()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No se encontraron permisos en el token.");
        }

        // Verificar si el usuario tiene el permiso "permiso_3"
        if (userPermissions.contains("permiso_3")) {
            return ResponseEntity.ok("Acceso concedido, tienes el permiso_3.");
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No tienes el permiso necesario (permiso_3).");
        }
    }


    @GetMapping("/protected2")
    public ResponseEntity<String> accessProtectedResource2(HttpServletRequest request) {
        // Recuperar los permisos del usuario que fueron agregados por el filtro
        List<String> userPermissions = (List<String>) request.getAttribute("userPermissions");

        if (userPermissions == null || userPermissions.isEmpty()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No se encontraron permisos en el token.");
        }


        if (userPermissions.contains("read")) {
            return ResponseEntity.ok("Acceso concedido, tienes el read.");
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No tienes el permiso necesario (read).");
        }
    }
}