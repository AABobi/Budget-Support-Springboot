package pl.radoslaw.kopec.BudgetSupportBackend.controller;


import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.web.bind.annotation.*;
import pl.radoslaw.kopec.BudgetSupportBackend.model.*;
import pl.radoslaw.kopec.BudgetSupportBackend.repository.*;
import pl.radoslaw.kopec.BudgetSupportBackend.service.MailService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin(
        origins = {"http://localhost:4200"}
)
@RestController
@RequestMapping({"/"})
public class UserController {

    @Autowired
    public UserRepository userRepository;

    @Autowired
    public BudgetRepository budgetRepository;

    @Autowired
    public UserAssignmentToGroupRepository userAssignmentToGroupRepository;

    @Autowired
    public ExpectedExpensesRepository expectedExpensesRepository;

    @Autowired
    public PermissionRepository permissionRepository;

    @Autowired
    private MailService notificationService;

    @Autowired
    private UserInBudgetRepository userInBudgetRepository;

    @Autowired
    private HistoryRepository historyRepository;

    //private Object ResponseEntity;


    @PostMapping("/zob")
    public UserAssignmentToGroup zob() throws Exception {
        User user = new User();
        user.setEmail("radoslawkopec93@gmail.com");
        user.setLastname("Kopec");
        user.setName("Radoslaw");
        user.setNickname("radokop");
        Password password = new Password();
        password.setPassword("radokop");
        user.setPassword(password);
        user.setConfirm("Confirm");
        List<UserAssignmentToGroup> newGroupList = new LinkedList<>();
        user.setUserAssignmentToGroup(newGroupList);

        UserInBudget userInBudget = new UserInBudget();
        userInBudget.setNickname("radokop");
        Budget budget = new Budget();
        budget.setUserName("qweqweqwe");
        budget.setBudgetName("Test1Budget");
        budget.setValue(11);
        budget.setDescription("ValueForTest");
        budget.setUniqueGroupCode("12345");

        List<Budget> budgetListForUserAssignment = new LinkedList<>();
        budgetListForUserAssignment.add(budget);

        String randomUniqueCodeForBudget = "12345";
        List<ExpectedExpenses> expectedExpensesList = new LinkedList<>();


        UserAssignmentToGroup userAssignmentToGroup =
                new UserAssignmentToGroup(budgetListForUserAssignment, "Test1Budget", randomUniqueCodeForBudget, "2022-01-0", "2022-01-31");
        List<UserInBudget> listOfUsersForUatg = userInBudgetRepository.findByNickname("radokop");
        userAssignmentToGroup.setListOfMembers(listOfUsersForUatg);

        user.getUserAssignmentToGroup().add(userAssignmentToGroup);

        userAssignmentToGroup.setExpectedExpensesList(expectedExpensesList);
        Permission permission1 = new Permission();
        // All without remove group and can't make permission one nad two to new members
        Permission permission2 = new Permission();
        // can only remove own description, add new and leave
        Permission permission3 = new Permission();

        permission1.setUniqueGroupCode(randomUniqueCodeForBudget);
        permission1.setTypeOfPermission(1);

        permission2.setUniqueGroupCode(randomUniqueCodeForBudget);
        permission2.setTypeOfPermission(2);

        permission3.setUniqueGroupCode(randomUniqueCodeForBudget);
        permission3.setTypeOfPermission(3);

        permissionRepository.save(permission1);
        permissionRepository.save(permission2);
        permissionRepository.save(permission3);

        if (user.getPermission() == null) {
            List<Permission> permissionsList = new LinkedList<>();
            permissionsList.add(permission1);
        } else {
            user.getPermission().add(permission1);
        }
        userInBudgetRepository.save(userInBudget);
        userRepository.save(user);



        User user2 = new User();
        user2.setEmail("radekopec@gmail.com");
        user2.setLastname("Niekop");
        user2.setName("Jaroslaw");
        user2.setNickname("Jaronie");
        Password password1 = new Password();
        password1.setPassword("Jaronie");
        user2.setPassword(password1);
        user2.setConfirm("Confirm");

        UserInBudget userInBudget2 = new UserInBudget();
        userInBudget2.setNickname("Jaronie");
        userInBudgetRepository.save(userInBudget2);
        userRepository.save(user2);

        return userAssignmentToGroup;
    }

    @GetMapping("/findAllTest")
    public User findAllTest(@RequestBody User user){
       /* User user = new User("qwe","lastname");
        userRepository.save(user);*/
        List<User> list = userRepository.findByNickname(user.getNickname());
        return list.get(0);
    }
    @GetMapping("/findAllTest2")
    public List<User> findAllTest2(){
       /* User user = new User("qwe","lastname");
        userRepository.save(user);*/
        List<User> list = userRepository.findAll();
        return list;
    }

    @PostMapping("/addExpectedExpanses")
    public UserAssignmentToGroup addExpectedExpanses(@RequestBody ExpectedExpenses expectedExpenses) {
        UserAssignmentToGroup userAssignmentToGroup = userAssignmentToGroupRepository.findByUniqueGroupCode(expectedExpenses.getUniqueGroupCode()).get(0);
        expectedExpenses.setBudgetName(userAssignmentToGroup.getBudgetName());

        expectedExpensesRepository.save(expectedExpenses);
        if (userAssignmentToGroup.getExpectedExpensesList() == null) {
            List<ExpectedExpenses> expectedExpensesList = List.of(expectedExpenses);
            userAssignmentToGroup.setExpectedExpensesList(expectedExpensesList);
        } else {
            userAssignmentToGroup.getExpectedExpensesList().add(expectedExpenses);
        }

        userAssignmentToGroupRepository.save(userAssignmentToGroup);
        return userAssignmentToGroup;
    }

    @PostMapping("/addExpectedExpensesToDescription/{nickname}")
    public void addExpectedExpensesToDescription(@RequestBody ExpectedExpenses expectedExpenses, @PathVariable String nickname){
        List<User> findUser = userRepository.findByNickname(nickname);

        Budget budget = new Budget(expectedExpenses);
        budget.setUserName(nickname);

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 1);
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String formatted = format1.format(cal.getTime());

        budget.setDate(formatted);
        ExpectedExpenses expectedExpensesDatabase = expectedExpensesRepository.findById(expectedExpenses.getId()).get(0);
        if(findUser != null){
            for(UserAssignmentToGroup x: findUser.get(0).getUserAssignmentToGroup()){
                if(x.getUniqueGroupCode().equals(expectedExpenses.getUniqueGroupCode())){
                    x.getBudgetList().add(budget);
                    x.getExpectedExpensesList().remove(expectedExpensesDatabase);
                    expectedExpensesRepository.delete(expectedExpenses);
                    break;
                }
            }

        }

         userRepository.save(findUser.get(0));


    }

    //This method adds new budget group to the user list.
    @PostMapping("/addNewMemberToTheBudget/{code}")
    public String[] addNewMemberToTheBudget(@RequestBody User user, @PathVariable String code) {
        String[] message = new String[1];
        User findUserForCheckIfHeBelongsToThisBudget = userRepository.findByNickname(user.getNickname()).get(0);
        if(findUserForCheckIfHeBelongsToThisBudget.getUserAssignmentToGroup().size() > 0){
            for(UserAssignmentToGroup x : findUserForCheckIfHeBelongsToThisBudget.getUserAssignmentToGroup()){
                if(x.getUniqueGroupCode().equals(code)){
                    message[0] = "This user belongs to this budget";
                    return message;
                }
            }
        }

        User dbUser = userRepository.findByNickname(user.getNickname()).get(0);
        Permission findPermission = permissionRepository.findByUniqueGroupCodeAndTypeOfPermission(code, 3).get(0);
        UserInBudget userInBudget = userInBudgetRepository.findByNickname(user.getNickname()).get(0);
        user.getUserAssignmentToGroup().get(user.getUserAssignmentToGroup().size()-1).getListOfMembers().add(userInBudget);
        if(user.getPermission().size() != 0 && user.getPermission() != null){
            user.getPermission().add(findPermission);
        }else{
            List<Permission> newPermissionList = new LinkedList<>();
            newPermissionList.add(findPermission);
            user.setPermission(newPermissionList);
        }

        userRepository.save(user);
        message[0] = "added";
        return message;


    }

    //This method adds new description position to the budget.
    //Not finished yet
    @PostMapping({"/addDescriptionToTheBudget"})
    public UserAssignmentToGroup addDescriptionToTheBudget(@RequestBody UserAssignmentToGroup userAssignmentToGroup) {
        // Date to string.
        System.out.println("1");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 1);
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String formatted = format1.format(cal.getTime());
        try {
            // Add date to the last object in list.e

            userAssignmentToGroup.getBudgetList().get(userAssignmentToGroup.getBudgetList().size() - 1).setDate(formatted);

            budgetRepository.save(userAssignmentToGroup.getBudgetList().get(userAssignmentToGroup.getBudgetList().size() - 1));

            userAssignmentToGroupRepository.save(userAssignmentToGroup);

        } catch (IndexOutOfBoundsException e) {

            System.out.println(e);
        }
        return userAssignmentToGroup;
    }

    @GetMapping("/countTheDays/{year}&{month}")
    public List<Integer> countTheDays(@PathVariable int year, @PathVariable String month) {
        YearMonth yearMonthObject = YearMonth.of(year, Integer.parseInt(month));
        System.out.println(yearMonthObject+ " co tototo");
        int daysInMonth = yearMonthObject.lengthOfMonth();
        System.out.println(daysInMonth+"    i to");
        List<Integer> test = new LinkedList<>();
        test.add(null);
        for (int i = 1; i < daysInMonth+1; i++) {
            test.add(i);
        }
        return test;
    }

    //This method gets String array [0] group name ( to create new group ) and [1] usernick name.
    //With user nickname we can find obj of this user and create group with this user
    @PostMapping({"/createBudget"})
    public String[] createBudget(@RequestBody String[] arrayWithInformationAboutNewBudget) {
        List<User> findUser = userRepository.findByNickname(arrayWithInformationAboutNewBudget[1]);
        //todo - exception if there is no user
        String randomUniqueCodeForBudget;
        do {
            randomUniqueCodeForBudget = RandomStringUtils.randomAlphanumeric(20);
        } while (budgetRepository.findByUniqueGroupCode(randomUniqueCodeForBudget).size() != 0);

        List<Budget> budgetListForUserAssignment = new ArrayList<>();

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 1);
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        String formatted = format1.format(cal.getTime());

        UserAssignmentToGroup userAssignmentToGroup =
                new UserAssignmentToGroup(budgetListForUserAssignment, arrayWithInformationAboutNewBudget[0], randomUniqueCodeForBudget, formatted, null);


        List<UserInBudget> listOfUsersForUatg = userInBudgetRepository.findByNickname(arrayWithInformationAboutNewBudget[1]);
        //listOfUsersForUatg.add(userInBudgetRepository.findByNickname(arrayWithInformationAboutNewBudget[1]).get(0));
        userAssignmentToGroup.setListOfMembers(listOfUsersForUatg);
        if (findUser.get(0).getUserAssignmentToGroup() != null) {
            findUser.get(0).getUserAssignmentToGroup().add(userAssignmentToGroup);

        } else {
            List<UserAssignmentToGroup> newListOfUserAssignmentToGroup = List.of(userAssignmentToGroup);
            findUser.get(0).setUserAssignmentToGroup(newListOfUserAssignmentToGroup);
        }

        // All permissions
        Permission permission1 = new Permission();
        // All without remove group and can't make permission one nad two to new members
        Permission permission2 = new Permission();
        // can only remove own description, add new and leave
        Permission permission3 = new Permission();

        permission1.setUniqueGroupCode(randomUniqueCodeForBudget);
        permission1.setTypeOfPermission(1);

        permission2.setUniqueGroupCode(randomUniqueCodeForBudget);
        permission2.setTypeOfPermission(2);

        permission3.setUniqueGroupCode(randomUniqueCodeForBudget);
        permission3.setTypeOfPermission(3);

        permissionRepository.save(permission1);
        permissionRepository.save(permission2);
        permissionRepository.save(permission3);

        if (findUser.get(0).getPermission() == null) {
            List<Permission> permissionsList = new LinkedList<>();
            permissionsList.add(permission1);
        } else {
            findUser.get(0).getPermission().add(permission1);
        }
        userRepository.save(findUser.get(0));

        String[] message = new String[1];
        message[0] = "Budget created";

        return message;

    }

    @GetMapping("/checkPermission/{uniqueCode}&{nickname}")
    public Permission checkPermission(@PathVariable String uniqueCode, @PathVariable String nickname) {
        System.out.println(nickname);
        System.out.println(uniqueCode);
        List<User> findUser = userRepository.findByNickname(nickname);
        System.out.println(findUser.size());
        if (findUser.size() > 0) {
            for (Permission x : findUser.get(0).getPermission()) {
                if (x.getUniqueGroupCode().equals(uniqueCode)) {
                    System.out.println("found");
                    return x;
                }
            }
        }
        Permission permission = new Permission();
        permission.setTypeOfPermission(404);
        return permission;

    }

    //Simple login to application.
    //Will be overwriting soon
    @PostMapping({"/checkLogin"})
    public User checkLogin(@RequestBody User userPass) throws Exception {
        List<User> listForFindUserToLogin = new LinkedList<>(userRepository.findByNickname(userPass.getNickname()));
        User returnUser;
        if (listForFindUserToLogin.size() > 0 && listForFindUserToLogin.get(0).getPassword().getPassword().equals(userPass.getPassword().getPassword())
                && listForFindUserToLogin.get(0).getConfirm().equals("Confirm")) {
            returnUser = new User(listForFindUserToLogin.get(0));
            System.out.println(returnUser.getRole());
            System.out.println(listForFindUserToLogin.get(0).getNickname());
            System.out.println(listForFindUserToLogin.get(0).getRole());
            System.out.println(listForFindUserToLogin.get(0).getEmail());
            return returnUser;
        } else {
            return returnUser = new User("NC", "NC");
        }
        //List<User> findUserToLogin =
    }

    @PostMapping({"/createFastTwojAccount"})
    public void createTwoAccount() throws Exception {
        System.out.println("works");
        User user = new User();
        User user2 = new User();

        user.setEmail("radoslawkopec93@gmail.com");
        user.setLastname("qwe");
        user.setName("qwe");
        user.setNickname("qwe");
        Password password = new Password();
        password.setPassword("qwe");
        user.setPassword(password);
        user.setConfirm("Confirm");
        UserInBudget userInBudget = new UserInBudget();
        userInBudget.setNickname("qwe");
        List<Permission> listToSetItInNewUser = new LinkedList<>();
        List<UserAssignmentToGroup> listToSetItInNewUserSecond = new LinkedList<>();

        userInBudgetRepository.save(userInBudget);

        user2.setEmail("radekopec@gmail.com");
        user2.setLastname("asd");
        user2.setName("asd");
        user2.setNickname("asd");
        Password password1 = new Password();
        password1.setPassword("asd");
        user2.setPassword(password1);
        user2.setConfirm("Confirm");
        UserInBudget userInBudget2 = new UserInBudget();
        userInBudget2.setNickname("asd");
        userInBudgetRepository.save(userInBudget2);

        user.setUserAssignmentToGroup(listToSetItInNewUserSecond);
        user.setPermission(listToSetItInNewUser);

        user2.setUserAssignmentToGroup(listToSetItInNewUserSecond);
        user2.setPermission(listToSetItInNewUser);

        List<History> historyList = new LinkedList<>();
        user.setHistory(historyList);
        user2.setHistory(historyList);
        user.setRole("ADMIN");
        user2.setRole("USER");
        userRepository.save(user);
        userRepository.save(user2);
    }

    @PostMapping("/createUser")
    public String[] createUser(@RequestBody User user) {
        List<User> findNicknameInDataBase = userRepository.findByNickname(user.getNickname());
        if (findNicknameInDataBase.size() != 0) {
            String[] message = {"Nickname already exist."};
            return message;
        } else {
            String randomUniqueCodeForBudget;
            do {
                randomUniqueCodeForBudget = RandomStringUtils.randomAlphanumeric(10);
            } while (userRepository.findByConfirm(randomUniqueCodeForBudget).size() != 0);
            List<Permission> listToSetItInNewUser = new LinkedList<>();

            List<UserAssignmentToGroup> listToSetItInNewUserSecond = new LinkedList<>();

            user.setPermission(listToSetItInNewUser);

            user.setUserAssignmentToGroup(listToSetItInNewUserSecond);

            user.setConfirm(randomUniqueCodeForBudget);

            UserInBudget userInBudget = new UserInBudget();

            userInBudget.setNickname(user.getNickname());

            userInBudgetRepository.save(userInBudget);
            this.notificationService.sendEmail(user);

            userRepository.save(user);
            String[] message = {"Created"};

            return message;
        }
    }

    @PutMapping("/confirmUser")
    public String[] confirmUser(@RequestBody String codeToConfirm) {
        List<User> findUserToConfirmHisAccount = userRepository.findByConfirm(codeToConfirm);
        String[] message = new String[1];
        if (findUserToConfirmHisAccount.size() != 0) {
            findUserToConfirmHisAccount.get(0).setConfirm("Confirm");
            userRepository.save(findUserToConfirmHisAccount.get(0));
            message[0] = "Account confirmed";
        } else {
            message[0] = "Problem with activate your account, make conntact with administrator";
        }
        return message;

    }

    //This method will delete the descriptions
    @PostMapping(value = "/deleteEntry/{budgetId}")
    public UserAssignmentToGroup deleteEntry(@RequestBody Budget budget, @PathVariable int budgetId) {
        // Find object from we want to delete a description
        UserAssignmentToGroup budgetWhatWeWantRemoveADescription = userAssignmentToGroupRepository.findByUniqueGroupCode(budget.getUniqueGroupCode()).get(0);
        // And find a description (from database) to remove.
        System.out.println(budgetId);
        Budget budgetToRemove = budgetRepository.findById(budgetId).get(0);
        // List with users what thay have this description
        List<User> listOfUser = userRepository.findByUserAssignmentToGroup(budgetWhatWeWantRemoveADescription);
        for (int i = 0; i < listOfUser.size(); i++) {
            for (UserAssignmentToGroup x : listOfUser.get(i).getUserAssignmentToGroup()) {
                if (x.equals(budgetWhatWeWantRemoveADescription)) {
                    x.getBudgetList().remove(budgetToRemove);
                }
            }
            userRepository.save(listOfUser.get(i));
        }
        budgetWhatWeWantRemoveADescription.getBudgetList().remove(budgetToRemove);
        userAssignmentToGroupRepository.save(budgetWhatWeWantRemoveADescription);
        budgetRepository.delete(budgetToRemove);

        return budgetWhatWeWantRemoveADescription;
    }

    @PostMapping(value = "/deleteEntryHistory/{budgetId}")
    public History deleteEntryHistory(@RequestBody Budget budget, @PathVariable int budgetId) {
        // Find object from we want to delete a description
        History history = historyRepository.findByUniqueGroupCode(budget.getUniqueGroupCode());
        // And find a description (from database) to remove.
        Budget budgetToRemove = budgetRepository.findById(budgetId).get(0);
        // List with users what thay have this description
        List<User> listOfUser = userRepository.findByHistory(history);
        for (int i = 0; i < listOfUser.size(); i++) {
            for (History x : listOfUser.get(i).getHistory()) {
                if (x.equals(history)) {
                    x.getBudgetList().remove(budgetToRemove);
                }
            }
            userRepository.save(listOfUser.get(i));
        }
        history.getBudgetList().remove(budgetToRemove);
        historyRepository.save(history);
        budgetRepository.delete(budgetToRemove);

        return history;
    }

    @DeleteMapping("/deleteExpectedEntry/{id}&{code}")
    public UserAssignmentToGroup deleteExpectedEntry(@PathVariable int id, @PathVariable String code) {

        UserAssignmentToGroup userAssignmentToGroup = userAssignmentToGroupRepository.findByUniqueGroupCode(code).get(0);
        ExpectedExpenses expectedExpenses = expectedExpensesRepository.findById(id).get(0);


        userAssignmentToGroup.getExpectedExpensesList().remove(expectedExpenses);

        userAssignmentToGroupRepository.save(userAssignmentToGroup);
        expectedExpensesRepository.delete(expectedExpenses);
        return userAssignmentToGroup;
    }

    //This method get an object uasg (budget to delete) and
    @PostMapping({"/deleteBudget"})
    public String[] deleteBudget(@RequestBody UserAssignmentToGroup userAssignmentToGroup) {
        return deleteBudgetMethodForAll(userAssignmentToGroup);
    }

    @PostMapping("/deleteHistoryEntry/{nickname}")
    public List<History> deleteHistoryEntry(@PathVariable String nickname, @RequestBody History history){
       List<User> findUser = userRepository.findByNickname(nickname);

       return deleteHistoryForALlUsers(history, findUser.get(0));
    }

    //Probably to change
    @PostMapping({"/findAllBudgetsOfUser"})
    public User findAllBudgetsOfUser(@RequestBody User user) {
        List<User> userList = userRepository.findByNickname(user.getNickname());
        return userList.get(0);
    }

    @GetMapping("/findAll")
    public List findAll(){
        System.out.println("test");
        List<User> usetList = userRepository.findAll();
        System.out.println(usetList.size());
        return usetList;
    }

    @GetMapping("/findHistory/{code}")
    public History findHistory(@PathVariable String code){
        return historyRepository.findByUniqueGroupCode(code);
    }

    //Return user with list of budgets. This method returns all all entries included in
    @GetMapping({"/findUser/{nickname}"})
    public User findUser(@PathVariable String nickname) {
        List<User> findUser = userRepository.findByNickname(nickname);
        //TODO if no result
        return findUser.get(0);
    }

    @GetMapping("/findEmail/{email}")
    public String[] findEmail(@PathVariable String email) {
       List<User> findEmailToCreateNewUser = userRepository.findByEmail(email);
        String[] message = new String[1];
        if (findEmailToCreateNewUser.size() != 0) {
            message[0] = "Found";
        } else {
            message[0] = "Free";
        }
        return message;

    }

    @GetMapping("/findBudget/{uniqueCode}")
    public UserAssignmentToGroup findBudget(@PathVariable String uniqueCode) {
        List<UserAssignmentToGroup> findBudget = userAssignmentToGroupRepository.findByUniqueGroupCode(uniqueCode);
        if (findBudget.size() != 0) {
            return findBudget.get(0);
        }
        UserAssignmentToGroup us = new UserAssignmentToGroup();
        us.setUniqueGroupCode("NE");
        return us;
    }

    @GetMapping("/findBudgetForListOfMembers/{code}&{nickname}")
    public UserAssignmentToGroup findBudgetForListOfMembers(@PathVariable String code, @PathVariable String nickname) {
        List<UserAssignmentToGroup> findBudget = userAssignmentToGroupRepository.findByUniqueGroupCode(code);
      /*  if (findBudget.size() != 0 && findBudget.get(0).getListOfMembers().size() > 0) {
            for(UserInBudget x: findBudget.get(0).getListOfMembers()){
                if(x.getNickname().equals(nickname)){
                    findBudget.get(0).getListOfMembers().remove(x);
                    break;
                }
            }
            return findBudget.get(0);
        }*/
      /* if(findBudget != null) {
           return findBudget.get(0);
       }else{
           return null;
       }*/

        if(findBudget != null){
            switch(findBudget.size()){
                case 1:{
                    return findBudget.get(0);
                }
            }

        }
        return null;
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
        ;*/

/*
        UserAssignmentToGroup userAssignmentToGroup = new UserAssignmentToGroup();
        userAssignmentToGroup.setUniqueGroupCode("zzz");
        userAssignmentToGroup.setBudgetName("Group");
        userAssignmentToGroupRepository.save(userAssignmentToGroup);*/

        for (Budget x : budgetRepository.findByUniqueGroupCode(userAssignmentToGroupRepository.findAll().get(0).getUniqueGroupCode())) {

        }


    }

    //userToFind have only nickname of user and one object to delete (obj represent group what user want to leave).
    @PostMapping({"/leaveTheGroup/{uniqueCode}&{checkMessage}"})
    public void leaveTheGroup(@RequestBody User user, @PathVariable String uniqueCode) {
        UserAssignmentToGroup userAssignmentToGroup = userAssignmentToGroupRepository.findByUniqueGroupCode(uniqueCode).get(0);

        User userDatabase = userRepository.findByNickname(user.getNickname()).get(0);

        List<Permission> permissionsList = permissionRepository.findByUniqueGroupCode(uniqueCode);
        Permission permission = new Permission();
        for(Permission p: permissionsList){
            if(p.getTypeOfPermission() == 1){
                permission = p;
            }
        }

        List<User> groupMembers = userRepository.findByUserAssignmentToGroup(userAssignmentToGroup);


      if(userAssignmentToGroup.getListOfMembers().size() == 1) {
          for (Permission x : user.getPermission()) {
              if (x.getUniqueGroupCode().equals(uniqueCode)) {
                  user.getPermission().remove(x);
                  break;
              }
          }

          for (UserAssignmentToGroup uax : user.getUserAssignmentToGroup()) {
              if (uax.getUniqueGroupCode().equals(uniqueCode)) {
                  for (UserInBudget uib : uax.getListOfMembers()) {
                      if (uib.getNickname().equals(user.getNickname())) {
                          uax.getListOfMembers().remove(uib);
                          break;
                      }
                  }
                  break;
              }
          }
          userRepository.save(user);
          for (UserAssignmentToGroup x : user.getUserAssignmentToGroup()) {
              if (x.getUniqueGroupCode().equals(uniqueCode)) {
                  user.getUserAssignmentToGroup().remove(x);
                  userRepository.save(user);
                  break;
              }
          }
          userAssignmentToGroupRepository.delete(userAssignmentToGroup);
      }else if(userAssignmentToGroup.getListOfMembers().size() > 1){
          for (Permission x : user.getPermission()) {
              if (x.getUniqueGroupCode().equals(uniqueCode)) {
                  user.getPermission().remove(x);
                  break;
              }
          }

          for (UserAssignmentToGroup uax : user.getUserAssignmentToGroup()) {
              if (uax.getUniqueGroupCode().equals(uniqueCode)) {
                  for (UserInBudget uib : uax.getListOfMembers()) {
                      if (uib.getNickname().equals(user.getNickname())) {
                          uax.getListOfMembers().remove(uib);
                          break;
                      }
                  }
                  break;
              }
          }
          userRepository.save(user);
          for (UserAssignmentToGroup x : user.getUserAssignmentToGroup()) {
              if (x.getUniqueGroupCode().equals(uniqueCode)) {
                  user.getUserAssignmentToGroup().remove(x);
                  userRepository.save(user);
                  break;
              }
          }
          userAssignmentToGroup = userAssignmentToGroupRepository.findByUniqueGroupCode(uniqueCode).get(0);

          List<User> listToGivePermOne = userRepository.findByUserAssignmentToGroup(userAssignmentToGroup);
          int howManyWithPermOne = 0;
          for(User u: listToGivePermOne){
              for(Permission p: u.getPermission()){
                  if(p.getUniqueGroupCode().equals(uniqueCode)){
                      if(p.getTypeOfPermission() == 1){
                          howManyWithPermOne++;
                      }
                  }
              }
          }
          if(howManyWithPermOne==0) {
              for (Permission p1 : listToGivePermOne.get(0).getPermission()) {
                  if (p1.getUniqueGroupCode().equals(uniqueCode)) {
                      listToGivePermOne.get(0).getPermission().remove(p1);
                      listToGivePermOne.get(0).getPermission().add(permission);
                      userRepository.save(listToGivePermOne.get(0));
                  }
              }
          }
        }

    }



    @PostMapping({"/howManyDays2"})
    public UserAssignmentToGroup howManyDaysInParticularMonth2(@RequestBody UserAssignmentToGroup userAssignmentToGroup) {
        YearMonth yearMonthObject = YearMonth.of(userAssignmentToGroup.getId(), Integer.parseInt(userAssignmentToGroup.getBudgetStartDate()));
        int daysInMonth = yearMonthObject.lengthOfMonth();

        userAssignmentToGroup.setBudgetStartDate(Integer.toString(daysInMonth));
        return userAssignmentToGroup;
    }

    @PostMapping({"/moveGroupToTheHistory"})
    public User moveGroupToTheHistory(@RequestBody User user) throws ParseException {

        List<UserAssignmentToGroup> budgets = new LinkedList<>();


        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 0);
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");

        String todayDateString = format1.format(cal.getTime());

        Date todayDate = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(todayDateString);

        Date endDate;

        for(UserAssignmentToGroup x: user.getUserAssignmentToGroup()){
            if(x.getBudgetEndDate() != null) {
                endDate = new SimpleDateFormat("yyyy-MM-dd").parse(x.getBudgetEndDate());
                if(todayDate.after(endDate)) {
                    budgetToHistoryForAllMembers(x);
                }
            }

        }
     User userAfterAll = userRepository.findByNickname(user.getNickname()).get(0);
   return  userAfterAll;

    }

    @PostMapping({"/saveBudgetSettings"})
    public void saveBudgetSettings(@RequestBody UserAssignmentToGroup userAssignmentToGroup) {
        System.out.println(userAssignmentToGroup.getBudgetEndDate());
        List<UserAssignmentToGroup> uatg = userAssignmentToGroupRepository.findByUniqueGroupCode(userAssignmentToGroup.getUniqueGroupCode());
        uatg.get(0).setBudgetStartDate(userAssignmentToGroup.getBudgetStartDate());
        uatg.get(0).setBudgetEndDate(userAssignmentToGroup.getBudgetEndDate());
        uatg.get(0).setGoal(userAssignmentToGroup.getGoal());
        userAssignmentToGroupRepository.save(uatg.get(0));
    }

    @PostMapping("/setRemoveButtonPermission/{nickname}")
    public List<List<UserAssignmentToGroup>> setRemoveButtonPermission(@RequestBody UserAssignmentToGroup userAssignmentToGroup[],
                                               @PathVariable String nickname){
        List<UserAssignmentToGroup> listWithAdminPermission = new LinkedList<>();
        List<UserAssignmentToGroup> listWithoutAdminPermission = new LinkedList<>();
        List<List<UserAssignmentToGroup>> listOfLists = new LinkedList<>();
        User user = userRepository.findByNickname(nickname).get(0);
        for(int i = 0; i < userAssignmentToGroup.length; i++){
            for(Permission p: user.getPermission()){
                if(p.getUniqueGroupCode().equals(userAssignmentToGroup[i].getUniqueGroupCode())){
                    if(p.getTypeOfPermission() == 1){
                        listWithAdminPermission.add(userAssignmentToGroup[i]);
                    }else{
                        listWithoutAdminPermission.add(userAssignmentToGroup[i]);
                    }
                }
            }
        }
        listOfLists.add(listWithAdminPermission);
        listOfLists.add(listWithoutAdminPermission);
        return listOfLists;
    }

    @PutMapping("/setPermission/{permissionType}&{uniqueCode}")
    public String[] setPermission(@RequestBody User user, @PathVariable int permissionType, @PathVariable String uniqueCode) {
        String[] message = new String[1];
        List<Permission> findNewPermissionForUser = permissionRepository.findByUniqueGroupCodeAndTypeOfPermission(uniqueCode, permissionType);
        int permissionUserAlreadyHave = 0;
        for(Permission p: user.getPermission()){
            if(p.getUniqueGroupCode().equals(uniqueCode)){
                permissionUserAlreadyHave = p.getTypeOfPermission();
            }
        }

        if (findNewPermissionForUser.size() != 0 && permissionUserAlreadyHave > 1) {
            for (int i = 0; i < user.getPermission().size(); i++) {
                if (user.getPermission().get(i).getUniqueGroupCode().equals(uniqueCode)) {
                    user.getPermission().remove(user.getPermission().get(i));
                }
            }
            user.getPermission().add(findNewPermissionForUser.get(0));
            userRepository.save(user);
            message[0] = "Permission changed";
        }else{
            message[0] = "Cannot change the permission";
        }

        return message;
    }

    @RequestMapping({"send-mail"})
    public String send(User user) {
        try {
            this.notificationService.sendEmail(user);
        } catch (MailException var3) {

        }

        return "Congratulations! Your mail has been send to the user.";
    }

    public List<History> deleteHistoryForALlUsers(History h, User user){

         History history = historyRepository.findByUniqueGroupCode(h.getUniqueGroupCode());

         List<User> howManyUsersBelongsToThisHistory = userRepository.findByHistory(history);

         user.getHistory().remove(history);
         userRepository.save(user);

         return user.getHistory();
    }

    public String[] deleteBudgetMethodForAll(UserAssignmentToGroup userAssignmentToGroup){
        boolean checker = false;
        UserAssignmentToGroup newUserAssignmentToGroup = userAssignmentToGroupRepository.findByUniqueGroupCode(userAssignmentToGroup.getUniqueGroupCode()).get(0);

        List<User> findAllUsersBelongsToThisBudget = userRepository.findByUserAssignmentToGroup(newUserAssignmentToGroup);

        List<Permission> listOfPermission = permissionRepository.findByUniqueGroupCode(userAssignmentToGroup.getUniqueGroupCode());

        for(User us: findAllUsersBelongsToThisBudget){
            for(UserAssignmentToGroup ua: us.getUserAssignmentToGroup()){
                if(ua.getUniqueGroupCode().equals(newUserAssignmentToGroup.getUniqueGroupCode())){
                    us.getUserAssignmentToGroup().remove(ua);
                    break;
                }
            }

            for(Permission p: us.getPermission()){
                for(Permission list: listOfPermission){
                    if(p.equals(list)){
                        us.getPermission().remove(list);
                        checker = true;
                        break;
                    }
                }
                if(checker){
                    break;
                }
            }

            userRepository.save(us);

        }
        List<Budget> budgets = newUserAssignmentToGroup.getBudgetList();

        newUserAssignmentToGroup.getListOfMembers().removeAll(newUserAssignmentToGroup.getListOfMembers());

        newUserAssignmentToGroup.getBudgetList().removeAll(newUserAssignmentToGroup.getBudgetList());

        newUserAssignmentToGroup.getExpectedExpensesList().removeAll(newUserAssignmentToGroup.getExpectedExpensesList());


        List<Permission> listOfPermission2 = permissionRepository.findByUniqueGroupCode(userAssignmentToGroup.getUniqueGroupCode());

        for(Permission p: listOfPermission2){
            permissionRepository.delete(p);
        }

        for(int i = 0; i < budgets.size();i++){
            budgetRepository.delete(budgets.get(i));
        }
        userAssignmentToGroupRepository.delete(newUserAssignmentToGroup);

        String[] message = {"OK"};
        return message;

    }

    public void budgetToHistoryForAllMembers(UserAssignmentToGroup userAssignmentToGroup){
        List<UserAssignmentToGroup> testList = userAssignmentToGroupRepository.findByUniqueGroupCode(userAssignmentToGroup.getUniqueGroupCode());

        List<User> members = userRepository.findByUserAssignmentToGroup(userAssignmentToGroup);

        History history = new History(userAssignmentToGroup);

        boolean variableToBlockMultiSaveSameObject = false;
        for(User m: members){
            if(!variableToBlockMultiSaveSameObject) {
                m.getHistory().add(history);
                m.getUserAssignmentToGroup().remove(testList.get(0));
                userRepository.save(m);
                variableToBlockMultiSaveSameObject = true;
            }else{
                m.getHistory().add(historyRepository.findByUniqueGroupCode(history.getUniqueGroupCode()));
                m.getUserAssignmentToGroup().remove(testList.get(0));
                System.out.println(m.getHistory().size());
                System.out.println(m.getNickname());
                userRepository.save(m);
            }
        }

        userAssignmentToGroupRepository.delete(userAssignmentToGroup);
    }

}
