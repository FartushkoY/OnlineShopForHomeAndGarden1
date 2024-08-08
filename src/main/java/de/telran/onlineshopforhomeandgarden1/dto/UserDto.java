package de.telran.onlineshopforhomeandgarden1.dto;

import de.telran.onlineshopforhomeandgarden1.entity.enums.Role;
import jakarta.persistence.Column;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.antlr.v4.runtime.misc.NotNull;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Long id;

    @NotNull
    @Length(max = 45, message = "{validation.user.name}")
    @Pattern(regexp ="[A-Za-z\\s.'-]{1,45}", message = "validation.user.name")
    private String name;

    @NotNull
    @Length(max =45 , message = "{validation.user.email}")
    @Email
    @Column(unique = true)
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$\n", message = "{validation.user.email}")
    private String email;

    @NotNull
    @Length(max = 13, message = "{validation.user.phoneNumber}")
    @Pattern(regexp = "[+\\d{12}]", message = "{validation.user.phoneNumber}")
    private String phoneNumber;

    @NotNull
    @Length(max = 45, message = "{validation.user.passwordHash}")
    private String passwordHash;

    private Role role;
}
