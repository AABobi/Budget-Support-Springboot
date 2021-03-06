package pl.radoslaw.kopec.BudgetSupportBackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.radoslaw.kopec.BudgetSupportBackend.model.History;
import pl.radoslaw.kopec.BudgetSupportBackend.model.User;
import pl.radoslaw.kopec.BudgetSupportBackend.model.UserAssignmentToGroup;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    List<User> findAll();

    List<User> findByNameOrLastname(String name, String lastname);

    List<User> findByNameAndLastname(String name, String lastname);

    User findByName(String name);

    List<User> findById(int id);

    List<User> findByNickname(String nickname);

    List<User> findByEmail(String email);

    List<User> findByUserAssignmentToGroup(UserAssignmentToGroup uatg);

    List<User> findByHistory(History history);

    List<User> findByConfirm(String code);
}