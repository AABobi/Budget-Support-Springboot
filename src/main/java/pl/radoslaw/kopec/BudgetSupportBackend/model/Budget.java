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

    private String budgetName;

    private String userName;

    public Budget(){}


    public Budget(Budget b){
        this.id = b.id;
        this.value = b.value;
        this.uniqueGroupCode = b.uniqueGroupCode;
        this.budgetName = b.budgetName;
        this.userName = b.userName;
    }

    public Budget(String description, String budgetName, String randomUniqueCodeForBudget, String userName) {
    this.description = description;
    this.budgetName = budgetName;
    this.uniqueGroupCode = randomUniqueCodeForBudget;
    this.userName = userName;
    }

    public String getBudgetName() {
        return budgetName;
    }

    public void setBudgetName(String budgetName) {
        this.budgetName = budgetName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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
        return budgetName;
    }

    public void setGroupName(String budgetName) {
        this.budgetName = budgetName;
    }

    public String getUser() {
        return userName;
    }

    public void setUser(String userName) {
        this.userName = userName;
    }
}
