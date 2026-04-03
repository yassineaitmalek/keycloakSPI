package com.test.keyclock.spi.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.test.keyclock.spi.models.constants.Gender;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private String username;

    private String password;

    private String firstName;

    private String lastName;

    private String email;

    private String mobile;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;

    private Gender gender;
}
