package valemobi.auth.model;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.UUID;

@NamedQueries({
        @NamedQuery(name="getUserByUserName", query="select * from User u where u.userName = :userName"),
        @NamedQuery(name="getUserByEmail", query="select * from User u where u.email = :email"),
        @NamedQuery(name="getUsers", query="select * from User u"),
        @NamedQuery(name="getUsersCount", query="select COUNT(u) from User u")
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
    @Column(unique = true)
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private Boolean emailVerified;
}
