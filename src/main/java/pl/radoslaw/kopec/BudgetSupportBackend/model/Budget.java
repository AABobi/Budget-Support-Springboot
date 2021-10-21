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

    public Budget(){}

    public Budget(String description,String groupName, User user, String uniqueGroupCode){
        this.description = description;
        this.groupName = groupName;
        this.user = user;
        this.uniqueGroupCode = uniqueGroupCode;
    }

    public Budget(Budget b){
        this.id = b.id;
        this.value = b.value;
        this.uniqueGroupCode = b.uniqueGroupCode;
        this.groupName = b.groupName;
        this.user = b.user;
    }

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
