package com.praxium.api.logisticrentaltools.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.praxium.api.logisticrentaltools.dto.UserRequestDTO;
import com.praxium.api.logisticrentaltools.dto.UserResponseDTO;
import com.praxium.api.logisticrentaltools.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("logisticrentaltools/users")
//@RequiredArgsConstructor
public class UserController {

	private final UserService usuarioService;

	
	public UserController(UserService usuarioService) {
		this.usuarioService = usuarioService;
	}


	@PostMapping
	public ResponseEntity<UserResponseDTO> createUser(@RequestBody UserRequestDTO dto) {
		var usuario = usuarioService.createUser(dto);
		var response = new UserResponseDTO(usuario.getId(), usuario.getName(), usuario.getEmail());
		return ResponseEntity.ok(response);
	}
}
