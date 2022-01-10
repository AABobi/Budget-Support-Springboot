package pl.radoslaw.kopec.BudgetSupportBackend;

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
public class ClassTest {


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

        List<Budget> budgetListForUserAssignment = new LinkedList<>();

        String randomUniqueCodeForBudget = "12345";
        List<ExpectedExpenses> expectedExpensesList = new LinkedList<>();
        ExpectedExpenses expectedExpenses = new ExpectedExpenses();
        expectedExpenses.setUniqueGroupCode(randomUniqueCodeForBudget);
        expectedExpenses.setBudgetName("Test1Budget");
        expectedExpenses.setDate("2022-01-30");
        expectedExpenses.setDescription("Water bill");
        expectedExpenses.setValue(100);
        expectedExpensesList.add(expectedExpenses);

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

        Budget budget = new Budget();
        budget.setUserName("radokop");
        budget.setBudgetName("Test2History");
        budget.setValue(11);
        budget.setDescription("ValueForTest");
        budget.setUniqueGroupCode("123456");
        //budgetRepository.save(budget);

        List<Budget> listOfBudgetsForHistory = List.of(budget);
        History history = new History();
        history.setBudgetName("Test2History");
        history.setUniqueGroupCode("123456");
        history.setBudgetStartDate("2022-01-01");
        history.setBudgetEndDate("2022-01-09");
        history.setBudgetList(listOfBudgetsForHistory);
        ///historyRepository.save(history);

        List<History> listOfHistory = List.of(history);
        user.setHistory(listOfHistory);



       // permissionRepository.save(permission1);
        permissionRepository.save(permission2);
        permissionRepository.save(permission3);


            List<Permission> permissionsList = new LinkedList<>();
            permissionsList.add(permission1);
            user.setPermission(permissionsList);
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
        user2.setConfirm("12345");

        UserInBudget userInBudget2 = new UserInBudget();
        userInBudget2.setNickname("Jaronie");
        userInBudgetRepository.save(userInBudget2);
        userRepository.save(user2);
    }
   //TODO leaveTheGroup method
    @Test
    public void shouldReturnUserWithOneHistoryMoreAndOneUserAssignmentToGroupLess() throws Exception {
        MvcResult findUser = mockMvc.perform(get("/findUser/radokop").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        ObjectMapper objectMapper = new ObjectMapper();
        String userJson = findUser.getResponse().getContentAsString();
        User user = new ObjectMapper().readValue(userJson, User.class);
        user.getUserAssignmentToGroup().get(0).setBudgetEndDate("2022-01-02");
        String userJson2 = objectMapper.writeValueAsString(user);



        mockMvc.perform(MockMvcRequestBuilders.post("/moveGroupToTheHistory").contentType(MediaType.APPLICATION_JSON).content(userJson2))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userAssignmentToGroup", hasSize(0)));
    }


   /* @Test
    public void deleteHistoryEntry() throws Exception{
        MvcResult findHistory = mockMvc.perform(get("/findHistory/123456").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.uniqueGroupCode", Matchers.is("123456")))
                .andReturn();

        String findHistoryResponse = findHistory.getResponse().getContentAsString();

        mockMvc.perform(MockMvcRequestBuilders.post("/deleteHistoryEntry/radokop").contentType(MediaType.APPLICATION_JSON).content(findHistoryResponse))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(0)));


    }

    @Test
    public void shouldReturnOkMessageAfterDeleteBudget() throws Exception{
        UserAssignmentToGroup userAssignmentToGroup = userAssignmentToGroupRepository.findByUniqueGroupCode("12345").get(0);
        userAssignmentToGroup.setExpectedExpensesList(expectedExpensesRepository.findByUniqueGroupCode("12345"));
        userAssignmentToGroup.setBudgetList(budgetRepository.findByUniqueGroupCode("12345"));
        userAssignmentToGroup.setListOfMembers(userInBudgetRepository.findByNickname("radokop"));

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(userAssignmentToGroup);

        mockMvc.perform(MockMvcRequestBuilders.post("/deleteBudget").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0]", Matchers.is("OK")));
    }
    @Test
    public void shouldReturnUserAssignmentToGroupWithExpectedListRemovedExpectedObject() throws Exception{
        ExpectedExpenses expectedExpenses = new ExpectedExpenses();
        expectedExpenses.setUniqueGroupCode("12345");
        expectedExpenses.setBudgetName("Test1Budget");
        expectedExpenses.setDate("2022-01-20");
        expectedExpenses.setDescription("Power bill");
        expectedExpenses.setValue(200);
        expectedExpensesRepository.save(expectedExpenses);
        List<ExpectedExpenses> expectedExpensesList = expectedExpensesRepository.findByUniqueGroupCode("12345");

        UserAssignmentToGroup userAssignmentToGroup = userAssignmentToGroupRepository.findByUniqueGroupCode("12345").get(0);
        userAssignmentToGroup.setExpectedExpensesList(expectedExpensesList);
        userAssignmentToGroup.setBudgetList(budgetRepository.findByUniqueGroupCode("12345"));
        userAssignmentToGroup.setListOfMembers(userInBudgetRepository.findByNickname("radokop"));

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(userAssignmentToGroup);

        mockMvc.perform(MockMvcRequestBuilders.delete("/deleteExpectedEntry/"+expectedExpenses.getId()+"&12345").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.expectedExpensesList", hasSize(expectedExpensesList.size()-1)));
    }

    @Test
    public void shouldReturnUserAssignmentToGroupWithoutRemovedEntry() throws Exception{
        Budget budget = new Budget();
        budget.setUserName("qweqweqwe");
        budget.setBudgetName("Test1Budget");
        budget.setValue(11);
        budget.setDescription("ValueForTest");
        budget.setUniqueGroupCode("12345");
        budgetRepository.save(budget);

        List<Budget> budgetsList = budgetRepository.findByUniqueGroupCode("12345");
        //budgetsList.add(budget);

        List<UserAssignmentToGroup> findGroup = userAssignmentToGroupRepository.findByUniqueGroupCode("12345");
        findGroup.get(0).setBudgetList(budgetsList);

        ObjectMapper objectMapper = new ObjectMapper();

        String json = objectMapper.writeValueAsString(budget);

        mockMvc.perform(MockMvcRequestBuilders.post("/deleteEntry/"+budget.getId()).contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.budgetList", hasSize(budgetsList.size()-1)));

        assertTrue(budgetRepository.findByUniqueGroupCode("12345").size() < budgetsList.size());

    }*/


   /* @Test
    public void shouldReturnHistoryWithoutBudget() throws Exception {
        Budget budget = new Budget();
        budget.setUserName("radokop");
        budget.setBudgetName("Test2History");
        budget.setValue(1234);
        budget.setDescription("SecondValueTest");
        budget.setUniqueGroupCode("123456");
        budgetRepository.save(budget);

        int listOfBudgetsSize = budgetRepository.findByUniqueGroupCode("123456").size();

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(budget);

        mockMvc.perform(MockMvcRequestBuilders.post("/deleteEntryHistory/"+budget.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.budgetList", hasSize(listOfBudgetsSize-1)));

        //assertTrue(budgetRepository.findByUniqueGroupCode("12345").size() < budgetsList.size());
    }*/

   /* @Test
    public void shouldReturnProblemWithActivateYourAccountMakeContactWithAdministrator() throws Exception{
        String stringToJson = "12345";

        mockMvc.perform(put("/confirmUser").contentType(MediaType.APPLICATION_JSON).content(stringToJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0]", Matchers.is("Account confirmed")));
    }


    @Test
    public void shouldReturnAccountConfirmedMessage() throws Exception{
        String stringToJson = "Confirm";

        mockMvc.perform(put("/confirmUser").contentType(MediaType.APPLICATION_JSON).content(stringToJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0]", Matchers.is("Account confirmed")));
    }

   @Test
    public void shouldReturnMessageCreated() throws Exception{
        Password password = new Password("qwerty12");
        User user = new User("Testnickname", "Testname", "Testlastname", "radekopec16@gmail.com", password);
        ObjectMapper objectMapper = new ObjectMapper();

        String json = objectMapper.writeValueAsString(user);

        mockMvc.perform(MockMvcRequestBuilders.post("/createUser").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]", Matchers.is("Created")));
    }
    @Test
    public void shouldReturnMessageNicknameAlreadyExist() throws Exception {
        Password password = new Password("qwerty12");
        User user = new User("radokop", "Testname", "Testlastname", "radekopec16@gmail.com", password);
        ObjectMapper objectMapper= new ObjectMapper();

        String json = objectMapper.writeValueAsString(user);

        mockMvc.perform(MockMvcRequestBuilders.post("/createUser").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]", Matchers.is("Nickname already exist.")));

    }

   @Test
    public void shouldReturnNull() throws Exception{
        mockMvc.perform(get("/checkPermission/123456&radokopp").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.typeOfPermission", Matchers.is(404)));
    }

    @Test
    public void shouldReturnPermissionWithValueOne() throws Exception{
        mockMvc.perform(get("/checkPermission/12345&radokop").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.typeOfPermission", Matchers.is(1)));
    }
*/

   // @Test
    public void testShouldCreateNewBudget() throws Exception {
        User user = new User();
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
        userInBudgetRepository.save(userInBudget);
        userRepository.save(user);

        String[] informationToCreateNewBudget = new String[2];
        informationToCreateNewBudget[0] = "NewGroup";
        informationToCreateNewBudget[1] = "qwe";

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(informationToCreateNewBudget);

        /*MvcResult result = */mockMvc.perform(MockMvcRequestBuilders.post("/createBudget")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0]", Matchers.is("Budget created")));
        //.andReturn();

    /*   String resultString = result.getResponse().getContentAsString();
       String id = JsonPath.parse(resultString).read("$[0]");
       assertEquals("Budget created", id);*/
    }

   // @Test
    public void shouldCreateListOfIntegerWithDaysInTheMonth() throws Exception{
        mockMvc.perform(get("/countTheDays/2022&05").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(32)))
                .andExpect(jsonPath("$[31]", Matchers.is(31)));
    }

   // @Test
    public void shouldAddDescriptionToTheBudgetList() throws Exception{
        Budget budget = new Budget();
        budget.setUserName("qweqweqwe");
        budget.setBudgetName("Test1Budget");
        budget.setValue(11);
        budget.setDescription("ValueForTest");
        budget.setUniqueGroupCode("12345");

        List<Budget> budgetsList = budgetRepository.findByUniqueGroupCode("12345");
        budgetsList.add(budget);

        UserAssignmentToGroup userAssignmentToGroup = new UserAssignmentToGroup(userAssignmentToGroupRepository.findAll().get(0));
        userAssignmentToGroup.setBudgetList(budgetsList);
        userAssignmentToGroup.setExpectedExpensesList(expectedExpensesRepository.findAll());

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(userAssignmentToGroup);


       mockMvc.perform(MockMvcRequestBuilders.post("/addDescriptionToTheBudget")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.budgetList",hasSize(1)));

    }

   // @Test
    public void shouldCreateExpectedExpenses() throws  Exception {
        ExpectedExpenses expectedExpenses = new ExpectedExpenses("Bread", 10,"2022-01-12","Test1Budget","12345","Radoslaw");

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(expectedExpenses);

        mockMvc.perform(MockMvcRequestBuilders.post("/addExpectedExpanses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.expectedExpensesList", hasSize(1)));
    }

    //@Test
    public void shouldMoveExpectedExpensesToDescription() throws Exception {
        ExpectedExpenses expectedExpenses = new ExpectedExpenses("Bread", 10,"2022-01-12","Test1Budget","12345","Radoslaw");

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(expectedExpenses);
        //Create object and test if created
        mockMvc.perform(MockMvcRequestBuilders.post("/addExpectedExpanses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.expectedExpensesList", hasSize(1)));

        Integer expectedExpensesObjects = expectedExpensesRepository.findAll().size();
        Integer budgetDescritpion = budgetRepository.findAll().size();


        //Have to find object becuse to delete this object after remove method have to know his id
        String jsonSecond = objectMapper.writeValueAsString(expectedExpensesRepository.findAll().get(0));
        mockMvc.perform(MockMvcRequestBuilders.post("/addExpectedExpensesToDescription/radokop")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonSecond))
                .andExpect(status().isOk());

        assertNotEquals(expectedExpensesObjects, expectedExpensesRepository.findAll().size());

        assertTrue(budgetDescritpion < budgetRepository.findAll().size());
    }

   // @Test
    public void showFindAllUser() throws Exception {
        mockMvc.perform(get("/findAll")
                .contentType(MediaType.valueOf("application/json")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$" , hasSize(2)));
    }

   // @Test
    public void testFindEmailIfExist() throws Exception {
        MvcResult result = mockMvc.perform(get("/findEmail/{email}", "radoslawkopec93@gmail.com")
                .contentType(MediaType.valueOf("application/json")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0]" , Matchers.is("Found")))
                .andReturn();


       String resultString = result.getResponse().getContentAsString();
       String id = JsonPath.parse(resultString).read("$.[0]");
       assertEquals("Found", id);
    }
    //@Test
    public void testFindEmailIfDoesntExist() throws Exception {
        MvcResult result = mockMvc.perform(get("/findEmail/{email}", "radoslawasdsadsadsa3@gmail.com")
                .contentType(MediaType.valueOf("application/json")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0]" , Matchers.is("Free")))
                .andReturn();


        String resultString = result.getResponse().getContentAsString();
        String id = JsonPath.parse(resultString).read("$.[0]");
        assertEquals("Free", id);
    }

    @AfterEach
    public void clearDataBase() throws Exception{
        userRepository.deleteAll();
        permissionRepository.deleteAll();
        userInBudgetRepository.deleteAll();
        userAssignmentToGroupRepository.deleteAll();
        expectedExpensesRepository.deleteAll();
        historyRepository.deleteAll();
        budgetRepository.deleteAll();
    }


}
