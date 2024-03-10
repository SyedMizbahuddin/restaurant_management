package com.mizbah.dto;

import com.mizbah.enums.UserRole;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class SignupRequest {

	private String name;
	private String email;
	private String password;
	private UserRole role;
}
