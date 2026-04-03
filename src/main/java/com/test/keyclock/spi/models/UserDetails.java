package com.test.keyclock.spi.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.test.keyclock.spi.models.constants.Gender;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.keycloak.representations.idm.UserRepresentation;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDetails {

    private UserRepresentation userRepresentation;

    private String mobile;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;

    private Gender gender;
}
