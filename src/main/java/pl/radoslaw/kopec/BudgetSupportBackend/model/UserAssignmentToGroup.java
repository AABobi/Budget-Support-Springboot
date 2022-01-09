package pl.radoslaw.kopec.BudgetSupportBackend.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "user_assignment_to_group")
public class UserAssignmentToGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    @ManyToMany(cascade = {CascadeType.ALL})
    private List<ExpectedExpenses> expectedExpensesList;

    @ManyToMany(cascade = {CascadeType.ALL})
    private List<Budget> budgetList;

    private String budgetName;

    private String uniqueGroupCode;

    private String budgetStartDate;

    private String budgetEndDate;

    @ManyToMany(cascade={CascadeType.PERSIST,CascadeType.REMOVE})
    private List<UserInBudget> listOfMembers;

    private int goal;

    public UserAssignmentToGroup() {
    }

    public UserAssignmentToGroup(UserAssignmentToGroup u){
        this.expectedExpensesList = u.expectedExpensesList;
        this.budgetList = u.getBudgetList();
        this.budgetName = u.getBudgetName();
        this.uniqueGroupCode = u.getUniqueGroupCode();
        this.budgetStartDate = u.getBudgetStartDate();
        this.budgetEndDate = u.getBudgetEndDate();
    }

    public UserAssignmentToGroup(List<Budget> budgetList,  String budgetName, String uniqueGroupCode, String budgetStartDate, String budgetEndDate) {
        this.budgetList = budgetList;
        this.budgetName = budgetName;
        this.uniqueGroupCode = uniqueGroupCode;
        this.budgetStartDate = budgetStartDate;
        this.budgetEndDate = budgetEndDate;
    }

    public List<ExpectedExpenses> getExpectedExpensesList() {
        return expectedExpensesList;
    }

    public void setExpectedExpensesList(List<ExpectedExpenses> expectedExpensesList) {
        this.expectedExpensesList = expectedExpensesList;
    }

    public int getGoal() {
        return goal;
    }

    public void setGoal(int goal) {
        this.goal = goal;
    }

    public String getBudgetEndDate() {
        return budgetEndDate;
    }

    public void setBudgetEndDate(String budgetEndDate) {
        this.budgetEndDate = budgetEndDate;
    }

    public String getBudgetStartDate() {
        return budgetStartDate;
    }

    public void setBudgetStartDate(String budgetStartDate) {
        this.budgetStartDate = budgetStartDate;
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

    public List<UserInBudget> getListOfMembers() {
        return listOfMembers;
    }

    public void setListOfMembers(List<UserInBudget> listOfMembers) {
        this.listOfMembers = listOfMembers;
    }
}
