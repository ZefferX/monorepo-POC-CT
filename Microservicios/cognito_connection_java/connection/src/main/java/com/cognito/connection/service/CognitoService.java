package com.cognito.connection.service;

import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProviderClientBuilder;
import com.amazonaws.services.cognitoidp.model.AdminInitiateAuthRequest;
import com.amazonaws.services.cognitoidp.model.AdminInitiateAuthResult;
import com.amazonaws.services.cognitoidp.model.AuthFlowType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class CognitoService {
    private final AWSCognitoIdentityProvider cognitoClient;

    @Value("${cognito.clientId}")
    private String clientId;

    @Value("${cognito.userPoolId}")
    private String userPoolId;

    public AdminInitiateAuthResult login(String username, String password) {
        AdminInitiateAuthRequest authRequest = new AdminInitiateAuthRequest()
                .withUserPoolId(userPoolId)
                .withClientId(clientId)
                .withAuthFlow(AuthFlowType.ADMIN_USER_PASSWORD_AUTH) // Usamos un flujo soportado
                .addAuthParametersEntry("USERNAME", username)
                .addAuthParametersEntry("PASSWORD", password);

        return cognitoClient.adminInitiateAuth(authRequest);
    }

    // Configuración del cliente Cognito
    @Service
    public static class CognitoClientConfig {

        @Value("${aws.region}")
        private String region;

        @Bean
        public AWSCognitoIdentityProvider cognitoClient() {
            return AWSCognitoIdentityProviderClientBuilder.standard()
                    .withRegion(region)
                    .build();
        }
    }
}
