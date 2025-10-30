package com.praxium.api.logisticrentaltools.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazonaws.services.cognitoidp.model.AdminCreateUserResult;
import com.praxium.api.logisticrentaltools.dto.UserRequestDTO;
import com.praxium.api.logisticrentaltools.model.Profile;
import com.praxium.api.logisticrentaltools.model.User;
import com.praxium.api.logisticrentaltools.repository.ProfileRepository;
import com.praxium.api.logisticrentaltools.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final  UserRepository userRepository;
	private final  ProfileRepository perfilRepository;
	private final CognitoUserService cognitoUserService;
	

	public UserService(UserRepository userRepository, ProfileRepository perfilRepository,CognitoUserService cognitoUserService) {
		this.userRepository = userRepository;
		this.perfilRepository = perfilRepository;
		this.cognitoUserService = cognitoUserService;
	}


	@Transactional
	public User createUser(UserRequestDTO dto) {
		// 1. Cria o usuário no Cognito
		AdminCreateUserResult cognitoResult = cognitoUserService.createUser(dto.getEmail(), dto.getName());

		// 2. Cria o usuário localmente
		Profile perfil = perfilRepository.findById(dto.getProfileId())
				.orElseThrow(() -> new RuntimeException("Perfil não encontrado"));

		User usuario = new User();
		usuario.setName(dto.getName());
		usuario.setEmail(dto.getEmail());
		usuario.setPosition(dto.getPosition());
		usuario.setProfile(perfil);//perfil
		usuario.setCognitoUserId(cognitoResult.getUser().getUsername());

		return userRepository.save(usuario);
	}
}