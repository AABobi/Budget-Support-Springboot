package pl.radoslaw.kopec.BudgetSupportBackend.model;


import javax.persistence.*;

@Entity
@Table(name = "budget")
public class Budget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String description;

    private int value;

    private String uniqueGroupCode;

    private String groupName;

    @OneToOne(
            cascade = {CascadeType.ALL}
    )
    private User user;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getUniqueGroupCode() {
        return uniqueGroupCode;
    }

    public void setUniqueGroupCode(String uniqueGroupCode) {
        this.uniqueGroupCode = uniqueGroupCode;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
