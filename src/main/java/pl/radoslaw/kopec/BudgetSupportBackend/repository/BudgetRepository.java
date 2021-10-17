package pl.radoslaw.kopec.BudgetSupportBackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.radoslaw.kopec.BudgetSupportBackend.model.Budget;

import java.util.List;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Integer> {
    List<Budget> findByUniqueGroupCode(String uniqueGroupCode);
}
