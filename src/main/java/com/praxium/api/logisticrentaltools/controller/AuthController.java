package com.praxium.api.logisticrentaltools.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.praxium.api.logisticrentaltools.dto.ConfirmForgotPasswordRequestDTO;
import com.praxium.api.logisticrentaltools.dto.ForgotPasswordRequestDTO;
import com.praxium.api.logisticrentaltools.service.CognitoAuthService;
import com.praxium.api.logisticrentaltools.service.UserService;
import com.amazonaws.services.cognitoidp.model.AuthenticationResultType;
import com.amazonaws.services.cognitoidp.model.InitiateAuthResult;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

	private final CognitoAuthService cognitoAuthService;
	private final UserService userService;


	public AuthController(CognitoAuthService cognitoAuthService, UserService userService) {
		this.cognitoAuthService = cognitoAuthService;
		this.userService = userService;
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
		String email = body.get("email");
		String password = body.get("password");
		if (email == null || password == null) {
			return ResponseEntity.badRequest().body("Email and password are required");
		}

		try {
			InitiateAuthResult result = cognitoAuthService.authenticate(email, password);
			if ("NEW_PASSWORD_REQUIRED".equals(result.getChallengeName())) {
				return ResponseEntity.status(409).body(Map.of(
						"status", "NEW_PASSWORD_REQUIRED",
						"session", result.getSession(),
						"challengeParameters", result.getChallengeParameters()
						));
			}
			AuthenticationResultType auth = result.getAuthenticationResult();
			// Update lastLogin only if user exists locally (you requested no auto-create)
			userService.updateLastLogin(email);

			// return the tokens to frontend
			return ResponseEntity.ok(Map.of(
					"accessToken", auth.getAccessToken(),
					"idToken", auth.getIdToken(),
					"refreshToken", auth.getRefreshToken(),
					"expiresIn", auth.getExpiresIn()
					));
		} catch (RuntimeException e) {
			return ResponseEntity.status(401).body(Map.of("error", e.getMessage()));
		}
	}

	@PostMapping("/complete-new-password")
	public ResponseEntity<?> completeNewPassword(@RequestBody Map<String, String> body) {
		String email = body.get("email");
		String newPassword = body.get("newPassword");
		String session = body.get("session");

		if (email == null || newPassword == null || session == null) {
			return ResponseEntity.badRequest().body(Map.of("error", "Missing required parameters"));
		}

		try {
			AuthenticationResultType auth = cognitoAuthService.completeNewPasswordChallenge(email, newPassword, session);

			userService.updateLastLogin(email);

			return ResponseEntity.ok(Map.of(
					"status", "SUCCESS",
					"accessToken", auth.getAccessToken(),
					"idToken", auth.getIdToken(),
					"refreshToken", auth.getRefreshToken()
					));
		} catch (RuntimeException e) {
			return ResponseEntity.status(400).body(Map.of("error", e.getMessage()));
		}
	}
	
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordRequestDTO request) {
    	if(userService.findByEmail(request.getEmail()) == null){
    		return ResponseEntity.badRequest().body(Map.of("error", "E-mail not register ")); 
    	}
    	cognitoAuthService.forgotPassword(request.getEmail());
        return ResponseEntity.ok("Código de redefinição de senha enviado para o e-mail.");
    }
    
    @PostMapping("/confirm-forgot-password")
    public ResponseEntity<?> confirmForgotPassword(@RequestBody ConfirmForgotPasswordRequestDTO request) {
    	cognitoAuthService.confirmForgotPassword(
                request.getEmail(),
                request.getConfirmationCode(),
                request.getNewPassword()
        );
        return ResponseEntity.ok("Senha redefinida com sucesso.");
    }

	// Optional: backend logout using adminGlobalSignOut
	@PostMapping("/logout")
	public ResponseEntity<?> logout(@RequestBody Map<String, String> body) {
		String username = body.get("username"); // the Cognito username (often email)
		if (username == null) return ResponseEntity.badRequest().body("username required");

		try {
			cognitoAuthService.adminGlobalSignOut(username);
			return ResponseEntity.ok(Map.of("message", "Global sign-out executed"));
		} catch (Exception e) {
			return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
		}
	}
}
