package com.mizbah.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserDto {

	private Long id;

	@NotBlank(message = "Name shoud be not blank")
	private String name;

	@Email(message = "Invalid email")
	private String email;

}
