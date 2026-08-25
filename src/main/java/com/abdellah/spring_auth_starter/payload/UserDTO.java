package com.abdellah.spring_auth_starter.payload;


import com.abdellah.spring_auth_starter.enums.USER_ROLE;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private String firstName;
    private String lastName;
    private String email;
    private USER_ROLE role;
}
