package pl.radoslaw.kopec.BudgetSupportBackend;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BudgetSupportBackEndApplication {

	public static void main(String[] args) {
		SpringApplication.run(BudgetSupportBackEndApplication.class, args);
	}
//       spring.jpa.properties.hibernate.hbm2ddl.auto= update
/*@Bean
public ObjectMapper objectMapper() {
	ObjectMapper objectMapper = new ObjectMapper();
	objectMapper.registerModule(new Hibernate5Module());
	return objectMapper;
}*/
}
