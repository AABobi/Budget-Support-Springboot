package pl.radoslaw.kopec.BudgetSupportBackend.model;

import org.hibernate.annotations.Cascade;

import javax.persistence.*;

@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int  id;
    private String nickname;
    private String name;
    private String lastname;
    private String email;
    private String permissions;
    @OneToOne(
            cascade = {CascadeType.ALL}
    )
    private Password password;

    public User() {

    }


    public Password getPassword() {
        return password;
    }

    public void setPassword(Password password) {
        this.password = password;
    }


    public User(String name, String lastname){
        this.name = name;
        this.lastname = lastname;
    }

    public User(User user) {
        this.nickname = user.nickname;
        this.lastname = user.lastname;
        this.email = user.email;
        this.name = user.name;
        this.id = user.id;
        this.permissions = user.permissions;
        this.password = user.password;
    }
    public User( String nickname,String name, String email, String lastname,String permissions) {
        this.nickname = nickname;
        this.name = name;
        this.email = email;
        this.lastname = lastname;
    }

    public String getNickname() {
        return this.nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLastname() {
        return this.lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPermissions() {
        return this.permissions;
    }

    public void setPermissions(String permissions) {
        this.permissions = permissions;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
