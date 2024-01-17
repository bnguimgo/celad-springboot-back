package com.bnguimgo.springboot.celad.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class UserDTO {

    private Long id;

    private String lastName;

    private String firstName;

    @NotBlank(message = "Email is missing")
    private String email;

}
