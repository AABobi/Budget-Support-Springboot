package pl.radoslaw.kopec.BudgetSupportBackend.model;


import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "user_assignment_to_group")
public class UserAssignmentToGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToMany(cascade = {CascadeType.ALL})
    private List<Budget> budgetList;

    private String budgetName;

    private String uniqueGroupCode;


    public UserAssignmentToGroup() {
    }

    public UserAssignmentToGroup(List<Budget> budgetList,  String budgetName, String uniqueGroupCode) {
        this.budgetList = budgetList;
        this.budgetName = budgetName;
        this.uniqueGroupCode = uniqueGroupCode;
    }




    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Budget> getBudgetList() {
        return budgetList;
    }

    public void setBudgetList(List<Budget> budgetList) {
        this.budgetList = budgetList;
    }


    public String getBudgetName() {
        return budgetName;
    }

    public void setBudgetName(String budgetName) {
        this.budgetName = budgetName;
    }

    public String getUniqueGroupCode() {
        return uniqueGroupCode;
    }

    public void setUniqueGroupCode(String uniqueGroupCode) {
        this.uniqueGroupCode = uniqueGroupCode;
    }
}
