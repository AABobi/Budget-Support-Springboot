package pl.radoslaw.kopec.BudgetSupportBackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.radoslaw.kopec.BudgetSupportBackend.model.UserInBudget;

import java.util.List;

@Repository
public interface UserInBudgetRepository extends JpaRepository <UserInBudget, Integer> {
    List<UserInBudget> findByNickname (String nickname);
}
