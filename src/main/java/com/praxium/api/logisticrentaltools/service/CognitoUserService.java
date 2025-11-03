package com.praxium.api.logisticrentaltools.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProviderClientBuilder;
import com.amazonaws.services.cognitoidp.model.AdminCreateUserRequest;
import com.amazonaws.services.cognitoidp.model.AdminCreateUserResult;
import com.amazonaws.services.cognitoidp.model.AdminDisableUserRequest;
import com.amazonaws.services.cognitoidp.model.AttributeType;
import com.amazonaws.services.cognitoidp.model.DeliveryMediumType;

import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;

@Service
@RequiredArgsConstructor
public class CognitoUserService {


	private static final String TEMP_PASSWORD = "Temp#1234";

	@Autowired
	private  AWSCognitoIdentityProvider cognitoClient;


	@Value("${aws.cognito.userPoolId}")
	private String userPoolId;

	@Value("${aws.cognito.region}")
	private String region;

	public AdminCreateUserResult createUser(String email, String nome) {
		AdminCreateUserRequest request = new AdminCreateUserRequest()
				.withUserPoolId(userPoolId)
				.withUsername(email)
				.withUserAttributes(
						new AttributeType().withName("email").withValue(email),
						new AttributeType().withName("name").withValue(nome),
						new AttributeType().withName("email_verified").withValue("true")
						)
				.withDesiredDeliveryMediums(DeliveryMediumType.EMAIL)
				.withTemporaryPassword(TEMP_PASSWORD);
		
		return cognitoClient.adminCreateUser(request);
	}

	public void disableUser(String username) {
		AdminDisableUserRequest request = new AdminDisableUserRequest()
				.withUserPoolId(userPoolId)
				.withUsername(username);
		cognitoClient.adminDisableUser(request);
	}
}
