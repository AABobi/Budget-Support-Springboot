package pl.radoslaw.kopec.BudgetSupportBackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.radoslaw.kopec.BudgetSupportBackend.model.Budget;
import pl.radoslaw.kopec.BudgetSupportBackend.model.User;
import pl.radoslaw.kopec.BudgetSupportBackend.model.UserAssignmentToGroup;
import pl.radoslaw.kopec.BudgetSupportBackend.repository.BudgetRepository;
import pl.radoslaw.kopec.BudgetSupportBackend.repository.UserAssignmentToGroupRepository;
import pl.radoslaw.kopec.BudgetSupportBackend.repository.UserRepository;

import java.util.LinkedList;
import java.util.List;

@CrossOrigin(
        origins = {"http://localhost:4200"}
)
@RequestMapping({"/"})
@RestController
public class UserController {

    @Autowired
    public UserRepository userRepository;

    @Autowired
    public BudgetRepository budgetRepository;

    @Autowired
    public UserAssignmentToGroupRepository userAssignmentToGroupRepository;


    @PostMapping({"/checkLogin"})
    public User checkLogin(@RequestBody User userPass){
        List<User> listForFindUserToLogin = new LinkedList<>(userRepository.findByNickname(userPass.getNickname()));
        User returnUser;
        System.out.println(listForFindUserToLogin.size());
        if (listForFindUserToLogin.size() > 0 && listForFindUserToLogin.get(0).getPassword().getPassword().equals(userPass.getPassword().getPassword())) {
            returnUser = new User(listForFindUserToLogin.get(0));
           // System.out.println("test3");
            return returnUser;
        } else {
          //  System.out.println("test4");
            return returnUser = new User("NC", "NC");
        }


    }
    @GetMapping({"/firstConn"})
    public void hello(){
     //   User user = new User("a","b","c","d","e");
     /*   if (userRepository.findAll().size() == 0) {
            userRepository.save(user);
        }
        List<User> l = userRepository.findAll();
        Budget budget = new Budget();
        budget.setGroupName("Group");
        budget.setDescription("test");
        budget.setUniqueGroupCode("zzz");
        budget.setValue(1);
        budget.setUser(l.get(0));
        budgetRepository.save(budget);
        System.out.println("test");*/

/*
        UserAssignmentToGroup userAssignmentToGroup = new UserAssignmentToGroup();
        userAssignmentToGroup.setUniqueGroupCode("zzz");
        userAssignmentToGroup.setBudgetName("Group");
        userAssignmentToGroupRepository.save(userAssignmentToGroup);*/
        System.out.println(userAssignmentToGroupRepository.findByUniqueGroupCode("zzz").size());
        System.out.println(userAssignmentToGroupRepository.findAll().size());
        for (Budget x : budgetRepository.findByUniqueGroupCode(userAssignmentToGroupRepository.findAll().get(0).getUniqueGroupCode())){
            System.out.println(x.getDescription());
        }



    }
}
