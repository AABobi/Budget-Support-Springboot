package pl.radoslaw.kopec.BudgetSupportBackend;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.hibernate.Hibernate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pl.radoslaw.kopec.BudgetSupportBackend.model.*;
import pl.radoslaw.kopec.BudgetSupportBackend.repository.*;
import pl.radoslaw.kopec.BudgetSupportBackend.service.MailService;

import java.util.LinkedList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.apache.commons.lang.RandomStringUtils;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pl.radoslaw.kopec.BudgetSupportBackend.controller.UserController;
import pl.radoslaw.kopec.BudgetSupportBackend.model.*;
import pl.radoslaw.kopec.BudgetSupportBackend.repository.*;
import pl.radoslaw.kopec.BudgetSupportBackend.service.MailService;

import java.text.SimpleDateFormat;
import java.util.*;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.RequestEntity.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ClassForTestTests {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

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

    @BeforeEach
    public void fillDatabase(){
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
      /*  Budget budget = new Budget();
        budget.setUserName("qweqweqwe");
        budget.setBudgetName("Test1Budget");
        budget.setValue(11);
        budget.setDescription("ValueForTest");
        budget.setUniqueGroupCode("12345");*/

        List<Budget> budgetListForUserAssignment = new LinkedList<>();
       // budgetListForUserAssignment.add(budget);

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


    }


    @AfterEach
    public void clearDataBase() throws Exception{
        userRepository.deleteAll();
        permissionRepository.deleteAll();
        userInBudgetRepository.deleteAll();
        userAssignmentToGroupRepository.deleteAll();
        expectedExpensesRepository.deleteAll();
        budgetRepository.deleteAll();
    }
}
