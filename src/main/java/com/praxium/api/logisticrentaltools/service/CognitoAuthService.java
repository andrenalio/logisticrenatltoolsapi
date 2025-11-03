package com.praxium.api.logisticrentaltools.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.model.*;
import com.praxium.api.logisticrentaltools.util.CognitoSecretHashUtil;
import software.amazon.awssdk.services.cognitoidentityprovider.model.CognitoIdentityProviderException;




@Service
public class CognitoAuthService {

	private final AWSCognitoIdentityProvider cognitoClient;

	@Value("${aws.cognito.clientId}")
	private String clientId;

	@Value("${aws.cognito.userPoolId}")
	private String userPoolId;

	@Value("${aws.cognito.clientSecret}")
	private String clientSecret;

	public CognitoAuthService(AWSCognitoIdentityProvider cognitoClient) {
		this.cognitoClient = cognitoClient;
	}

	public InitiateAuthResult authenticate(String username, String password) {
		InitiateAuthRequest authRequest = new InitiateAuthRequest()
				.withClientId(clientId)
				.withAuthFlow(AuthFlowType.USER_PASSWORD_AUTH)
				.addAuthParametersEntry("USERNAME", username)
				.addAuthParametersEntry("PASSWORD", password)
				.addAuthParametersEntry("SECRET_HASH", CognitoSecretHashUtil.calculateSecretHash(username, clientId, clientSecret));

		try {
			return cognitoClient.initiateAuth(authRequest);
		} catch (NotAuthorizedException e) {
			throw new RuntimeException("Invalid credentials");
		} catch (UserNotConfirmedException e) {
			throw new RuntimeException("User not confirmed");
		} catch (Exception e) {
			throw new RuntimeException("Error authenticating user: " + e.getMessage(), e);
		}
	}

	public AuthenticationResultType completeNewPasswordChallenge(String username, String newPassword, String session) {
		RespondToAuthChallengeRequest challengeRequest = new RespondToAuthChallengeRequest()
				.withClientId(clientId)
				.withChallengeName(ChallengeNameType.NEW_PASSWORD_REQUIRED)
				.withSession(session)
				.addChallengeResponsesEntry("USERNAME", username)
				.addChallengeResponsesEntry("NEW_PASSWORD", newPassword)
				.addChallengeResponsesEntry("SECRET_HASH", CognitoSecretHashUtil.calculateSecretHash(username, clientId, clientSecret));

		RespondToAuthChallengeResult challengeResult = cognitoClient.respondToAuthChallenge(challengeRequest);
		return challengeResult.getAuthenticationResult();
	}


	public void forgotPassword(String email) {
        try {
            ForgotPasswordRequest request = new ForgotPasswordRequest()
                    .withClientId(clientId)
                    .withSecretHash(CognitoSecretHashUtil.calculateSecretHash(email, clientId, clientSecret))
                    .withUsername(email);
            cognitoClient.forgotPassword(request);

        } catch (UserNotFoundException e) {
            throw new RuntimeException("Usuário não encontrado no Cognito");
        } catch (InvalidParameterException e) {
            throw new RuntimeException("Parâmetros inválidos");
        } catch (AWSCognitoIdentityProviderException e) {
            throw new RuntimeException("Erro ao contatar o Cognito: " + e.getErrorMessage());
        }
    }

	 public void confirmForgotPassword(String email, String confirmationCode, String newPassword) {
	        try {
	            ConfirmForgotPasswordRequest request = new ConfirmForgotPasswordRequest()
	                    .withClientId(clientId)
	                    .withUsername(email)
	                    .withSecretHash(CognitoSecretHashUtil.calculateSecretHash(email, clientId, clientSecret))
	                    .withConfirmationCode(confirmationCode)
	                    .withPassword(newPassword);

	            cognitoClient.confirmForgotPassword(request);

	        } catch (ExpiredCodeException e) {
	            throw new RuntimeException("O código de verificação expirou");
	        } catch (CodeMismatchException e) {
	            throw new RuntimeException("Código de verificação inválido");
	        } catch (UserNotFoundException e) {
	            throw new RuntimeException("Usuário não encontrado no Cognito");
	        } catch (AWSCognitoIdentityProviderException e) {
	            throw new RuntimeException("Erro ao contatar o Cognito: " + e.getErrorMessage());
	        }
	    }
	/**
	 * Optional: administratively sign-out user from all devices/sessions
	 */
	public void adminGlobalSignOut(String username) {
		AdminUserGlobalSignOutRequest req = new AdminUserGlobalSignOutRequest()
				.withUserPoolId(userPoolId)
				.withUsername(username);
		cognitoClient.adminUserGlobalSignOut(req);
	}
}

