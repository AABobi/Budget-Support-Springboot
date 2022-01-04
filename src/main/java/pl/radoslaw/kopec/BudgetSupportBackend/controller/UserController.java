package pl.radoslaw.kopec.BudgetSupportBackend.controller;


import org.aspectj.weaver.ast.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.radoslaw.kopec.BudgetSupportBackend.model.*;
import pl.radoslaw.kopec.BudgetSupportBackend.repository.*;
import org.apache.commons.lang.RandomStringUtils;

import java.text.SimpleDateFormat;
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
    public void addNewMemberToTheBudget(@RequestBody User user) {
        userRepository.save(user);
    }

    //This method adds new description position to the budget.
    //Not finished yet
    @PostMapping({"/addDescriptionToTheBudget"})
    public UserAssignmentToGroup addDescriptionToTheBudget(@RequestBody UserAssignmentToGroup userAssignmentToGroup) {
        // Date to string.
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 1);
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String formatted = format1.format(cal.getTime());
        try{
                // Add date to the last object in list.
                userAssignmentToGroup.getBudgetList().get(userAssignmentToGroup.getBudgetList().size() - 1).setDate(formatted);
                userAssignmentToGroupRepository.save(userAssignmentToGroup);

        }catch (IndexOutOfBoundsException e){
            System.out.println(e);
        }

        return userAssignmentToGroup;
    }

    //This method gets String array [0] group name ( to create new group ) and [1] usernick name.
    //With user nickname we can find obj of this user and create group with this user
    @PostMapping({"/createBudget"})
    public void createBudget(@RequestBody String[] arrayWithInformationAboutNewBudget) {
        List<User> findUser = userRepository.findByNickname(arrayWithInformationAboutNewBudget[1]);
        //todo - exception if there is no user
        String randomUniqueCodeForBudget;
        do {
            randomUniqueCodeForBudget = RandomStringUtils.randomAlphanumeric(10);
        } while (budgetRepository.findByUniqueGroupCode(randomUniqueCodeForBudget).size() != 0);

        List<Budget> budgetListForUserAssignment = new ArrayList<>();


        UserAssignmentToGroup userAssignmentToGroup =
                new UserAssignmentToGroup(budgetListForUserAssignment, arrayWithInformationAboutNewBudget[0], randomUniqueCodeForBudget);


        if (findUser.get(0).getUserAssignmentToGroup() != null) {
            findUser.get(0).getUserAssignmentToGroup().add(userAssignmentToGroup);

        } else {
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
    public User checkLogin(@RequestBody User userPass) {
        System.out.println("test");
        List<User> listForFindUserToLogin = new LinkedList<>(userRepository.findByNickname(userPass.getNickname()));
        User returnUser;
        if (listForFindUserToLogin.size() > 0 && listForFindUserToLogin.get(0).getPassword().getPassword().equals(userPass.getPassword().getPassword())) {
            returnUser = new User(listForFindUserToLogin.get(0));
            return returnUser;
        } else {
            return returnUser = new User("NC", "NC");
        }
    }


    //This method will delete the descriptions
    @PostMapping(value = "/deleteEntry")
    public UserAssignmentToGroup deleteEntry(@RequestBody UserAssignmentToGroup userAssignmentToGroup) {

        return null;
    }

    //This method get an object uasg (budget to delete) and
    @PostMapping({"/deleteBudget"})
    public void deleteBudget(@RequestBody UserAssignmentToGroup userAssignmentToGroup) {
        UserAssignmentToGroup exactlyObjectUasg;
        // Find correct object and save it to the variable
        exactlyObjectUasg = userAssignmentToGroupRepository.findByUniqueGroupCode(userAssignmentToGroup.getUniqueGroupCode()).get(0);
        // List of all budget members.
        List<User> findUserWithBudget = userRepository.findByUserAssignmentToGroup(userAssignmentToGroup);
        // Remove budget(userAssignmentToGroup) from users object
        for(User x : findUserWithBudget){
            x.getUserAssignmentToGroup().remove(exactlyObjectUasg);
            userRepository.save(x);
        }

    }


    //Probably to change
    @PostMapping({"/findAllBudgetsOfUser"})
    public User findAllBudgetsOfUser(@RequestBody User user) {
        List<User> userList = userRepository.findByNickname(user.getNickname());
        return userList.get(0);
    }

    //Return user with list of budgets. This method returns all all entries included in
    @PostMapping({"findUser"})
    public User findUser(@RequestBody User user) {

        return userRepository.findByNickname(user.getNickname()).get(0);
    }

    @GetMapping({"/firstConn"})
    public void hello() {
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
        for (Budget x : budgetRepository.findByUniqueGroupCode(userAssignmentToGroupRepository.findAll().get(0).getUniqueGroupCode())) {
            System.out.println(x.getDescription());
        }


    }

    //userToFind have only nickname of user and one object to delete (obj represent group what user want to leave).
    @PostMapping({"/leaveTheGroup"})
    public void leaveTheGroup(@RequestBody User userToFind){
        System.out.println("test");
        try {
            //User jpa method to find this user with only nickname
            User user = userRepository.findByNickname(userToFind.getNickname()).get(0);
            //And find uasg object with unique code. We need this objects to use them.
            UserAssignmentToGroup userAssignmentToGroup = userAssignmentToGroupRepository
                    .findByUniqueGroupCode(userToFind
                            .getUserAssignmentToGroup()
                            .get(0).getUniqueGroupCode()
                    ).get(0);

            //Remove budget from user list and save this user in data base.
            user.getUserAssignmentToGroup().remove(userAssignmentToGroup);
            userRepository.save(user);
        }catch (IndexOutOfBoundsException e){
            System.out.println(e);
        }


    }
}
