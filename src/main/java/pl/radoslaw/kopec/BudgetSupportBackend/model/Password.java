package pl.radoslaw.kopec.BudgetSupportBackend.model;

import javax.persistence.*;

@Entity
@Table(name = "PASSWORD")
public class Password {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private int id;
    private String password;

    public Password() {
    }

    public Password(String password) {
        this.password = password;
    }

    public Password(int id, String password) {
        this.id = id;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
