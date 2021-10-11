package pl.radoslaw.kopec.BudgetSupportBackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.radoslaw.kopec.BudgetSupportBackend.model.User;
import pl.radoslaw.kopec.BudgetSupportBackend.repository.UserRepository;
@CrossOrigin(
        origins = {"http://localhost:4200"}
)
@RequestMapping({"/"})
@RestController
public class UserController {

    @Autowired
    public UserRepository userRepository;

    @GetMapping({"/firstConn"})
    public User hello(){
        System.out.println("aaa");
        User user = new User();
        user.setName("name");
        user.setEmail("email");
        user.setLastname("lastName");
        return user;
    }
}
