package pl.radoslaw.kopec.BudgetSupportBackend;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import pl.radoslaw.kopec.BudgetSupportBackend.model.Permission;
import pl.radoslaw.kopec.BudgetSupportBackend.repository.*;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.RequestEntity.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.hamcrest.Matchers;
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
@AutoConfigureMockMvc
@SpringBootTest
class BudgetSupportBackEndApplicationTests {
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserInBudgetRepository userInBudgetRepository;

	@Autowired
	private PermissionRepository permissionRepository;


	@BeforeEach
	public void create() throws Exception {
		User user = new User();
		user.setEmail("radoslawkopec93@gmail.com");
		user.setLastname("qweqweqwe");
		user.setName("qweqweqwe");
		user.setNickname("qweqweqwe");
		Password password = new Password();
		password.setPassword("qweqweqwe");
		user.setPassword(password);
		user.setConfirm("Confirm");

		UserInBudget userInBudget = new UserInBudget();
		userInBudget.setNickname("qweqweqwe");


		userInBudgetRepository.save(userInBudget);
		userRepository.save(user);

		String[] informationToCreateNewBudget = new String[2];
		informationToCreateNewBudget[0] = "NewGroup";
		informationToCreateNewBudget[1] = "qwe";

		ObjectMapper objectMapper = new ObjectMapper();
		String json = objectMapper.writeValueAsString(informationToCreateNewBudget);

		mockMvc.perform(MockMvcRequestBuilders.post("/createBudget")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(json))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.[0]", Matchers.is("Budget created")));

		User user2 = new User();
		user2.setEmail("radekopec@gmail.com");
		user2.setLastname("asdasdasd");
		user2.setName("asdasdasd");
		user2.setNickname("asasdasdasdd");
		Password password1 = new Password();
		password1.setPassword("asdasdasd");
		user2.setPassword(password1);
		user2.setConfirm("Confirm");

		UserInBudget userInBudget2 = new UserInBudget();
		userInBudget2.setNickname("asdasdasd");
		userInBudgetRepository.save(userInBudget2);
		userRepository.save(user2);
	}

	@Test
	void contextLoads() {

		//assertTrue(userRepository.findAll().size()>0);
	}

	@Test
	void newTest(){
		userRepository.deleteAll();
		permissionRepository.deleteAll();
		userInBudgetRepository.deleteAll();
	}

}
