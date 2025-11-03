package com.praxium.api.logisticrentaltools.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.praxium.api.logisticrentaltools.dto.UserRequestDTO;
import com.praxium.api.logisticrentaltools.dto.UserResponseDTO;
import com.praxium.api.logisticrentaltools.model.User;
import com.praxium.api.logisticrentaltools.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;


	public UserController(UserService userService) {
		this.userService = userService;
	}

	@PostMapping
	public ResponseEntity<UserResponseDTO> createUser(@RequestBody UserRequestDTO dto) {
		var usuario = userService.createUser(dto);
		var response = new UserResponseDTO(usuario.getId(), usuario.getName(), usuario.getEmail());
		return ResponseEntity.ok(response);
	}

	@GetMapping
	public ResponseEntity<List<User>> list() {
		List<User> users = userService.listAll();
		return ResponseEntity.ok(users);
	}

	@GetMapping("/{id}")
	public ResponseEntity<User> get(@PathVariable Long id) {
		return ResponseEntity.ok(userService.findById(id));
	}

	@PutMapping("/{id}")
	public ResponseEntity<User> update(@PathVariable Long id, @RequestBody UserRequestDTO dto) {
		User updated = userService.updateUser(id, dto.getName(), dto.getPosition(), dto.getActive(), dto.getProfileId());
		return ResponseEntity.ok(updated);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		userService.deleteUser(id);
		return ResponseEntity.ok(Map.of("message", "User deleted"));
	}
}
