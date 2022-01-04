package pl.radoslaw.kopec.BudgetSupportBackend.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "history")
public class History {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToMany(cascade = {CascadeType.ALL})
    private List<Budget> budgetList;

    private String budgetName;

    private String uniqueGroupCode;

    private String budgetStartDate;

    private String budgetEndDate;

    @ManyToMany(cascade={CascadeType.PERSIST,CascadeType.REMOVE})
    private List<UserInBudget> listOfMembers;

    public History(){

    }

    public History(UserAssignmentToGroup userAssignmentToGroup){
        this.budgetList = userAssignmentToGroup.getBudgetList();
        this.budgetName = userAssignmentToGroup.getBudgetName();
        this.uniqueGroupCode = userAssignmentToGroup.getUniqueGroupCode();
        this.budgetStartDate = userAssignmentToGroup.getBudgetStartDate();
        this.budgetEndDate = userAssignmentToGroup.getBudgetEndDate();
        this.listOfMembers = userAssignmentToGroup.getListOfMembers();
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

    public String getBudgetStartDate() {
        return budgetStartDate;
    }

    public void setBudgetStartDate(String budgetStartDate) {
        this.budgetStartDate = budgetStartDate;
    }

    public String getBudgetEndDate() {
        return budgetEndDate;
    }

    public void setBudgetEndDate(String budgetEndDate) {
        this.budgetEndDate = budgetEndDate;
    }

    public List<UserInBudget> getListOfMembers() {
        return listOfMembers;
    }

    public void setListOfMembers(List<UserInBudget> listOfMembers) {
        this.listOfMembers = listOfMembers;
    }
}
