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

//	public void forgotPassword(String email) {
//		try {
//			ForgotPasswordRequest request = ForgotPasswordRequest.builder()
//					.clientId(clientId)
//					.username(email)
//					.build();
//
//			cognitoClient.forgotPassword(request);
//
//		} catch (UserNotFoundException e) {
//			throw new RuntimeException("User not found in Cognito");
//		} catch (InvalidParameterException e) {
//			throw new RuntimeException("Invalid request parameters");
//		} catch (CognitoIdentityProviderException e) {
//			throw new RuntimeException("Error contacting Cognito: " + e.awsErrorDetails().errorMessage());
//		}
//	}
//
//
//	public void confirmForgotPassword(String email, String confirmationCode, String newPassword) {
//		ConfirmForgotPasswordRequest request = ConfirmForgotPasswordRequest.builder()
//				.clientId(clientId)
//				.username(email)
//				.confirmationCode(confirmationCode)
//				.password(newPassword)
//				.build();
//
//		cognitoClient.confirmForgotPassword(request);
//	}

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

