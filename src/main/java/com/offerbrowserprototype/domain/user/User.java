package com.offerbrowserprototype.domain.user;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@Document(collection = "users")
public class User {

    @Id
    private String id; // MongoDB automatycznie generuje unikalne ID

    private String username;

    private String email;

    private String password;
}
