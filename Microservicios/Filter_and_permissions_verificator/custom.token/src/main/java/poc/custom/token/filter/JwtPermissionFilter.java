package poc.custom.token.filter;


import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;


public class JwtPermissionFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String jwtToken = authorizationHeader.substring(7);  // Extraer el token eliminando el "Bearer "

            try {
                // Decodificar el JWT
                DecodedJWT decodedJWT = JWT.decode(jwtToken);

                // Extraer el claim "permissions"
                String permissionsClaim = decodedJWT.getClaim("permissions").asString();

                if (permissionsClaim != null && !permissionsClaim.isEmpty()) {
                    // Deserializar permisos
                    List<String> permissions = Arrays.asList(objectMapper.readValue(permissionsClaim, String[].class));

                    // Guardar permisos en el request para acceder desde el controlador
                    request.setAttribute("userPermissions", permissions);
                }

            } catch (Exception e) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token inválido o no autorizado");
                return;
            }
        } else {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token no encontrado");
            return;
        }

        // Continuar con el siguiente filtro en la cadena
        filterChain.doFilter(request, response);
    }
}