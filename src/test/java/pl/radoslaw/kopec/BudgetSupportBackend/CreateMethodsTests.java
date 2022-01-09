package pl.radoslaw.kopec.BudgetSupportBackend;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.http.RequestEntity.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
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
import pl.radoslaw.kopec.BudgetSupportBackend.model.Password;
import pl.radoslaw.kopec.BudgetSupportBackend.model.User;
import pl.radoslaw.kopec.BudgetSupportBackend.model.UserInBudget;
import pl.radoslaw.kopec.BudgetSupportBackend.repository.UserInBudgetRepository;
import pl.radoslaw.kopec.BudgetSupportBackend.repository.UserRepository;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class CreateMethodsTests {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserInBudgetRepository userInBudgetRepository;

   /* @Test
    public void test1() throws Exception{
        User user = new User();
        user.setNickname("testname");
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(user);
        mockMvc.perform(MockMvcRequestBuilders.post("/zob")
                .contentType(MediaType.valueOf("application/json"))
                 .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]" , Matchers.is("z")));
    }*/
   @Test
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

    @AfterEach
    public void deleteAllThings(){
        userRepository.deleteAll();
    }
}
