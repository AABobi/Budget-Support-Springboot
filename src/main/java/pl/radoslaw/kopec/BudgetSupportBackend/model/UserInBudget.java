package pl.radoslaw.kopec.BudgetSupportBackend.model;

import javax.persistence.*;

@Entity
@Table(name = "User_In_Budget")
public class UserInBudget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String nickname;

    public UserInBudget(){};

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
