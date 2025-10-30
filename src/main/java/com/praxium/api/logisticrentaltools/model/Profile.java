package com.praxium.api.logisticrentaltools.model;

import java.util.Set;

import jakarta.persistence.*;
@Entity
@Table(name = "profile")
public class Profile {

		@Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    @Column(unique = true, nullable = false)
	    private String name;

	    @ManyToMany(fetch = FetchType.LAZY)
	    @JoinTable(
	        name = "profile_permission",
	        joinColumns = @JoinColumn(name = "profile_id"),
	        inverseJoinColumns = @JoinColumn(name = "permission_id")
	    )
	    private Set<Permission> permission;

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

		public Set<Permission> getPermission() {
			return permission;
		}

		public void setPermission(Set<Permission> permission) {
			this.permission = permission;
		}
	    
	    
}
