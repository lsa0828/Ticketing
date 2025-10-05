package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Member {
    private String id;
    private String password;
    private String role;

    public Member(String id, String password) {
        this.id = id;
        this.password = password;
    }
}
