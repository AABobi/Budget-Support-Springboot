package pl.radoslaw.kopec.BudgetSupportBackend.controller;


import org.aspectj.weaver.ast.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.radoslaw.kopec.BudgetSupportBackend.model.*;
import pl.radoslaw.kopec.BudgetSupportBackend.repository.*;
import org.apache.commons.lang.RandomStringUtils;

import java.util.*;
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

    //This method adds new budget group to the user list.
    @PostMapping("/addNewMemberToTheBudget")
    public void addNewMemberToTheBudget(@RequestBody User user){
       userRepository.save(user);
    }

    //This method adds new description position to the budget.
    //Not finished yet
    @PostMapping({"/addDescriptionToTheBudget"})
    public User addDescriptionToTheBudget(@RequestBody User user){
        try{
            userRepository.save(user);
        }catch (NullPointerException e){
        }

        /*  for(int i = 0; i < user.getUserAssignmentToGroup().size();i++){
            System.out.println(user.getUserAssignmentToGroup().get(0).getUniqueGroupCode());
        }*/
           return user;
    }

   //This method gets String array [0] group name ( to create new group ) and [1] usernick name.
    //With user nickname we can find obj of this user and create group with this user
    @PostMapping({"/createBudget"})
    public void createBudget(@RequestBody String[] arrayWithInformationAboutNewBudget) {
        List<User> findUser = userRepository.findByNickname(arrayWithInformationAboutNewBudget[1]);
        //todo - exception if there is no user
        String randomUniqueCodeForBudget;
        do{
            randomUniqueCodeForBudget = RandomStringUtils.randomAlphanumeric(10);
        }while(budgetRepository.findByUniqueGroupCode(randomUniqueCodeForBudget).size()!=0);

        List<Budget> budgetListForUserAssignment = new ArrayList<>();


        UserAssignmentToGroup userAssignmentToGroup =
                new UserAssignmentToGroup(budgetListForUserAssignment, arrayWithInformationAboutNewBudget[0], randomUniqueCodeForBudget);

        if(findUser.get(0).getUserAssignmentToGroup() != null){
            findUser.get(0).getUserAssignmentToGroup().add(userAssignmentToGroup);

        }else{
            List<UserAssignmentToGroup> newListOfUserAssignmentToGroup = List.of(userAssignmentToGroup);
            findUser.get(0).setUserAssignmentToGroup(newListOfUserAssignmentToGroup);
        }

        Permission permission = new Permission();
        permission.setUniqueGroupCode(randomUniqueCodeForBudget);
        List<Permission> permissionsList = new LinkedList<>();
        permissionsList.add(permission);

        findUser.get(0).getPermission().add(permission);
        userRepository.save(findUser.get(0));

    }



    //Simple login to application.
    //Will be overwriting soon
    @PostMapping({"/checkLogin"})
    public User checkLogin(@RequestBody User userPass){
        System.out.println("test");
        List<User> listForFindUserToLogin = new LinkedList<>(userRepository.findByNickname(userPass.getNickname()));
        User returnUser;
        if (listForFindUserToLogin.size() > 0 && listForFindUserToLogin.get(0).getPassword().getPassword().equals(userPass.getPassword().getPassword())) {
            returnUser = new User(listForFindUserToLogin.get(0));
            System.out.println("good");
            return returnUser;
        } else {
            System.out.println("no good");
            return returnUser = new User("NC", "NC");
        }
    }


    //This method will delete the descriptions
    @PostMapping(value = "/deleteEntry")
    public UserAssignmentToGroup deleteEntry(@RequestBody Budget budget){
          List<UserAssignmentToGroup> userAssignmentToGroupList = userAssignmentToGroupRepository.findByUniqueGroupCode(budget.getUniqueGroupCode());
        try{
            for(Budget x: userAssignmentToGroupList.get(0).getBudgetList()){
                if(x.getId() == budget.getId()){
                    userAssignmentToGroupList.get(0).getBudgetList().remove(x);
                    return userAssignmentToGroupList.get(0);
                }
            }
        }catch (NullPointerException e){
            System.out.println("No lsit");
        }
        return null;
    }
    //This method can deletes budget.
    //This method gets a parametr id of user assignment to group object.
    //Uses repository and id to find an object in DB.
    //Finds all users who are members to the budget and deletes it from user's objetcs and DB
    //todo - response entity

    //This method deletes budget
    @DeleteMapping(value = "/deleteBudget/{id}")
    public void  deleteBudget(@PathVariable int id){
        List<UserAssignmentToGroup> userAssignmentToGroupList = userAssignmentToGroupRepository.findById(id);
        List<User> userList = userRepository.findByUserAssignmentToGroup(userAssignmentToGroupList.get(0));

        try {
            for (User x : userList) {
                for (int i = 0; x.getUserAssignmentToGroup().size() > i; i++) {
                    if (x.getUserAssignmentToGroup().get(i).equals(userAssignmentToGroupList.get(0))) {
                        x.getUserAssignmentToGroup().remove(x.getUserAssignmentToGroup().get(i));
                    }
                }
            }
            userAssignmentToGroupRepository.delete(userAssignmentToGroupList.get(0));
        }catch (ConcurrentModificationException e){
            System.out.println(userList.get(0).getUserAssignmentToGroup().size());


        }

    }



    //Probably to change
    @PostMapping({"/findAllBudgetsOfUser"})
    public User findAllBudgetsOfUser(@RequestBody User user){
        List<User> userList = userRepository.findByNickname(user.getNickname());
        return userList.get(0);
    }

    //Return user with list of budgets. This method returns all all entries included in
    @PostMapping({"findUser"})
    public User findUser(@RequestBody User user){

        return  userRepository.findByNickname(user.getNickname()).get(0);
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
