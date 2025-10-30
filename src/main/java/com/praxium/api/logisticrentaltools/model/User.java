package com.praxium.api.logisticrentaltools.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "user")
public class User {

		@Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    private String name;

	    @Column(unique = true, nullable = false)
	    private String email;

	    private String position;

	    private Boolean active = true;

	    @Enumerated(EnumType.STRING)
	    private UserType type = UserType.HUMANO;

	    @Column(name="cognito_user_id")
	    private String cognitoUserId; // ID do usuário no Cognito

	    @ManyToOne(fetch = FetchType.LAZY)
	    @JoinColumn(name = "perfil_id")
	    private Profile profile;
	    
	    @Column(name="date_register")
	    private LocalDateTime dateRegister = LocalDateTime.now();

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public String getPosition() {
			return position;
		}

		public void setPosition(String position) {
			this.position = position;
		}

		public Boolean getActive() {
			return active;
		}

		public void setActive(Boolean active) {
			this.active = active;
		}

		public UserType getType() {
			return type;
		}

		public void setType(UserType type) {
			this.type = type;
		}

		public String getCognitoUserId() {
			return cognitoUserId;
		}

		public void setCognitoUserId(String cognitoUserId) {
			this.cognitoUserId = cognitoUserId;
		}


		public LocalDateTime getDateRegister() {
			return dateRegister;
		}

		public void setDateRegister(LocalDateTime dateRegister) {
			this.dateRegister = dateRegister;
		}

		public Profile getProfile() {
			return profile;
		}

		public void setProfile(Profile profile) {
			this.profile = profile;
		}
	    
	    
}
