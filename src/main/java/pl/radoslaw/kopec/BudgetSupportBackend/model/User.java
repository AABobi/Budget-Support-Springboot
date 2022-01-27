package pl.radoslaw.kopec.BudgetSupportBackend.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.*;
import java.util.List;
//@Data
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
    private String confirm;
    private String role;

    @OneToOne(cascade = {CascadeType.ALL})
    private Password password;


    @ManyToMany(cascade = {CascadeType.ALL})
    private List<UserAssignmentToGroup> userAssignmentToGroup;


    @ManyToMany(cascade = {CascadeType.ALL})
    private List<History> history;

    @ManyToMany(cascade = {CascadeType.ALL})
    private List<Permission> permission;

    public User() {
    }

    public User(String nickname){
        this.nickname = nickname;
    }
    public User(String name, String lastname) {
        this.name = name;
        this.lastname = lastname;
    }
    public User(String nickname, String name, String lastname, String email, String confirm) {
        this.name = name;
        this.lastname = lastname;
        this.nickname = nickname;
        this.email = email;
        this.confirm = confirm;
    }

    public User(String nickname, String name, String lastname, String email, Password password) {
        this.name = name;
        this.lastname = lastname;
        this.nickname = nickname;
        this.email = email;
        this.password = password;
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
        this.confirm = user.confirm;
        this.role = user.role;
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

    public String getConfirm() {
        return confirm;
    }

    public void setConfirm(String confirm) {
        this.confirm = confirm;
    }

    public List<History> getHistory() {
        return history;
    }

    public void setHistory(List<History> history) {
        this.history = history;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
