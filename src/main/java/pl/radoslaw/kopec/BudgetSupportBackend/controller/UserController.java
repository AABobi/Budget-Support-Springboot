package pl.radoslaw.kopec.BudgetSupportBackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.radoslaw.kopec.BudgetSupportBackend.model.Budget;
import pl.radoslaw.kopec.BudgetSupportBackend.model.User;
import pl.radoslaw.kopec.BudgetSupportBackend.model.UserAssignmentToGroup;
import pl.radoslaw.kopec.BudgetSupportBackend.repository.BudgetRepository;
import pl.radoslaw.kopec.BudgetSupportBackend.repository.UserAssignmentToGroupRepository;
import pl.radoslaw.kopec.BudgetSupportBackend.repository.UserRepository;
import org.apache.commons.lang.RandomStringUtils;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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



    @PostMapping({"/createBudget"})
    public void createBudget(@RequestBody String[] arrayWithInformationAboutNewBudget) {
        System.out.println("Connection test");
        List<User> findUser = userRepository.findByNickname(arrayWithInformationAboutNewBudget[1]);
        //todo - exception if there is no user
        String randomUniqueCodeForBudget;
        do{
            randomUniqueCodeForBudget = RandomStringUtils.randomAlphanumeric(10);
        }while(budgetRepository.findByUniqueGroupCode(randomUniqueCodeForBudget).size()!=0);

        Budget budget = new Budget("Create budget", arrayWithInformationAboutNewBudget[0], findUser.get(0),randomUniqueCodeForBudget);
        List<Budget> budgetListForUserAssignment = new ArrayList<>();
        budgetListForUserAssignment.add(budget);
        UserAssignmentToGroup userAssignmentToGroup = new UserAssignmentToGroup(budgetListForUserAssignment,findUser.get(0), arrayWithInformationAboutNewBudget[0], randomUniqueCodeForBudget);
        budgetRepository.save(budget);
        userAssignmentToGroupRepository.save(userAssignmentToGroup);

    }



    @PostMapping({"/checkLogin"})
    public User checkLogin(@RequestBody User userPass){
        List<User> listForFindUserToLogin = new LinkedList<>(userRepository.findByNickname(userPass.getNickname()));
        User returnUser;
        System.out.println(listForFindUserToLogin.size());
        if (listForFindUserToLogin.size() > 0 && listForFindUserToLogin.get(0).getPassword().getPassword().equals(userPass.getPassword().getPassword())) {
            returnUser = new User(listForFindUserToLogin.get(0));
            System.out.println("good");
            return returnUser;
        } else {
            System.out.println("no good");
            return returnUser = new User("NC", "NC");
        }
    }
    //Just test
    @PostMapping({"/findAllBudgetsOfUser2"})
    public UserAssignmentToGroup[] findAllBudgetsOfUser2(@RequestBody User user){

        UserAssignmentToGroup[] arrayToExceptions = new UserAssignmentToGroup[1];
        //Find user
        List<User> findUser = userRepository.findByNickname(user.getNickname());
        //IndexOutOfBoundsException possibility
        if(findUser.size() == 0){
            UserAssignmentToGroup usg = new UserAssignmentToGroup();
            usg.setBudgetName("Cannot find user " + user.getNickname());
            System.out.println("null1");
            arrayToExceptions[1] = usg;
            return  arrayToExceptions;
        }

        //Find user in UserAssignmentToGroup database
        List<UserAssignmentToGroup> findBudgetsOfUser = userAssignmentToGroupRepository.findByUserId(findUser.get(0).getId());
        if(findBudgetsOfUser.size() == 0){
            UserAssignmentToGroup usg = new UserAssignmentToGroup();
            usg.setBudgetName("Cannot find budget " + user.getNickname());
            System.out.println("null2");
            arrayToExceptions[1] = usg;
            return  arrayToExceptions;
        }

        for(UserAssignmentToGroup x: findBudgetsOfUser){
            System.out.println(x.getBudgetName());
        }
        List<UserAssignmentToGroup> segregatedList = findBudgetsOfUser.stream()
                .sorted(Comparator.comparing(UserAssignmentToGroup::getBudgetName))
                .collect(Collectors.toList());
        int iteratorFinalList = 0;

        List<UserAssignmentToGroup> finalList = new ArrayList<>();
        finalList.add(segregatedList.get(0));
        System.out.println(segregatedList.size());

        for(UserAssignmentToGroup x: findBudgetsOfUser){
            if(!finalList.get(iteratorFinalList).equals(x)){
                iteratorFinalList++;
                finalList.add(x);
            }
        }

        System.out.println("final size "+ finalList.size());
        return null;
    }


    //Probably to change
    @PostMapping({"/findAllBudgetsOfUser"})
    public UserAssignmentToGroup[] findAllBudgetsOfUser(@RequestBody User user){

        UserAssignmentToGroup[] arrayToExceptions = new UserAssignmentToGroup[1];
        //Find user
        List<User> findUser = userRepository.findByNickname(user.getNickname());
        //IndexOutOfBoundsException possibility
        if(findUser.size() == 0){
            UserAssignmentToGroup usg = new UserAssignmentToGroup();
            usg.setBudgetName("Cannot find user " + user.getNickname());
            System.out.println("null1");
            arrayToExceptions[1] = usg;
            return  arrayToExceptions;
        }
        //Find user in UserAssignmentToGroup database
        List<UserAssignmentToGroup> findBudgetsOfUser = userAssignmentToGroupRepository.findByUserId(findUser.get(0).getId());
        if(findBudgetsOfUser.size() == 0){
            UserAssignmentToGroup usg = new UserAssignmentToGroup();
            usg.setBudgetName("Cannot find budget " + user.getNickname());
            System.out.println("null2");
            arrayToExceptions[1] = usg;
            return  arrayToExceptions;
        }
        for(UserAssignmentToGroup x: findBudgetsOfUser){
            List<UserAssignmentToGroup> segregatedList = new LinkedList<>();

        }
        List<UserAssignmentToGroup> budgetList = new LinkedList<>();
        for(int i = 0; i < findBudgetsOfUser.size(); i++) {
            budgetList.add(userAssignmentToGroupRepository.findByUniqueGroupCode(findBudgetsOfUser.get(i).getUniqueGroupCode()).get(0));
        }
        System.out.println(budgetList.size());
        //IndexOutOfBoundsException
        if(budgetList.size() == 0){
            UserAssignmentToGroup usg = new UserAssignmentToGroup();
            usg.setBudgetName("Cannot find budget " + user.getNickname());
            System.out.println("null3");
            arrayToExceptions[1] = usg;
            return  arrayToExceptions;
        }
        System.out.println("before done");
        System.out.println(budgetList.get(0).getBudgetList().size());

        UserAssignmentToGroup[] arrayToSend = budgetList.toArray(new UserAssignmentToGroup[0]);
        return arrayToSend;
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
