package pl.radoslaw.kopec.BudgetSupportBackend.model;

import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String nickname;
    private String name;
    private String lastname;
    private String email;

    @OneToOne(cascade = {CascadeType.ALL})
    private Password password;

    @OneToMany(cascade = {CascadeType.ALL})
    private List<UserAssignmentToGroup> userAssignmentToGroup;

    @OneToMany(cascade = {CascadeType.ALL})
    private List<Permission> permission;

    public User() {
    }

    public User(String name, String lastname) {
        this.name = name;
        this.lastname = lastname;
    }

    public User(User user) {
        this.nickname = user.nickname;
        this.lastname = user.lastname;
        this.email = user.email;
        this.name = user.name;
        this.id = user.id;
        this.password = user.password;
        this.userAssignmentToGroup = user.userAssignmentToGroup;
        this.permission = user.permission;
    }

    public User(String nickname, String name, String email, String lastname) {
        this.nickname = nickname;
        this.name = name;
        this.email = email;
        this.lastname = lastname;
    }

    public Password getPassword() {
        return password;
    }

    public void setPassword(Password password) {
        this.password = password;
    }


    public List<UserAssignmentToGroup> getUserAssignmentToGroup() {
        return userAssignmentToGroup;
    }

    public void setUserAssignmentToGroup(List<UserAssignmentToGroup> userAssignmentToGroup) {
        this.userAssignmentToGroup = userAssignmentToGroup;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Permission> getPermission() {
        return permission;
    }

    public void setPermission(List<Permission> permission) {
        this.permission = permission;
    }
}
