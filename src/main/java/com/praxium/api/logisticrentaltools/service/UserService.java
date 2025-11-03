package com.praxium.api.logisticrentaltools.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.amazonaws.services.cognitoidp.model.AdminCreateUserResult;
import com.praxium.api.logisticrentaltools.dto.UserRequestDTO;
import com.praxium.api.logisticrentaltools.model.Profile;
import com.praxium.api.logisticrentaltools.model.User;
import com.praxium.api.logisticrentaltools.repository.ProfileRepository;
import com.praxium.api.logisticrentaltools.repository.UserRepository;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class UserService {

	private final  UserRepository userRepository;
	private final  ProfileRepository profileRepository;
	private final CognitoUserService cognitoUserService;
	

	public UserService(UserRepository userRepository, ProfileRepository perfilRepository,CognitoUserService cognitoUserService) {
		this.userRepository = userRepository;
		this.profileRepository = perfilRepository;
		this.cognitoUserService = cognitoUserService;
	}


	@Transactional
	public User createUser(UserRequestDTO dto) {
		// 1. Cria o usuário no Cognito
		AdminCreateUserResult cognitoResult = cognitoUserService.createUser(dto.getEmail(), dto.getName());
		// 2. Cria o usuário localmente
		Profile perfil = profileRepository.findById(dto.getProfileId())
				.orElseThrow(() -> new RuntimeException("Profile Not Found"));
		User usuario = new User();
		usuario.setName(dto.getName());
		usuario.setEmail(dto.getEmail());
		usuario.setPosition(dto.getPosition());
		usuario.setProfile(perfil);//perfil
		usuario.setCognitoUserId(cognitoResult.getUser().getUsername());

		return userRepository.save(usuario);
	}
	
	 public User updateUser(Long id, String name, String position, Boolean active, Long profileId) {
	        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
	        if (name != null) user.setName(name);
	        if (position != null) user.setPosition(position);
	        if (active != null) user.setActive(active);
	        if (profileId != null) {
	            Profile profile = profileRepository.findById(profileId).orElseThrow(() -> new RuntimeException("Profile not found"));
	            user.setProfile(profile);
	        }
	        return userRepository.save(user);
	    }
	 
	    public void deleteUser(Long id) {
	        userRepository.deleteById(id);
	    }

	    public List<User> listAll() {
	        return userRepository.findAll();
	    }
	    
	    public User findById(Long id) {
	        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
	    }
	    
	    public User findByEmail(String email) {
	        return userRepository.findByEmail(email).orElse(null);
	    }
	    
	    @Transactional
	    public void updateLastLogin(String email) {
	        User user = userRepository.findByEmail(email).orElse(null);
	        if (user != null) {
	            user.setLastLogin(LocalDateTime.now());
	            userRepository.save(user);
	        }
	    }
}