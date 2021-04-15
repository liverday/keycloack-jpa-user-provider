package com.liverday.keycloak.model;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.UUID;

@NamedQueries({
        @NamedQuery(name="getUserByUserName", query="select u from User u where u.userName = :userName"),
        @NamedQuery(name="getUserByEmail", query="select u from User u where u.email = :email"),
        @NamedQuery(name="getUsers", query="select u from User u " +
                "WHERE (u.userName = :search or u.email = :search) order by u.userName"),
        @NamedQuery(name="getUsersCount", query="select COUNT(u) from User u ")
})
@Entity
@Table(name = "users")
@Data
@Accessors(chain = true)
public class User {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(unique = true)
    private String userName;
    private String firstName;
    private String lastName;
    @Column(unique = true)
    private String email;
    private String password;
    private Boolean emailVerified;
}
