package pl.radoslaw.kopec.BudgetSupportBackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.radoslaw.kopec.BudgetSupportBackend.model.ExpectedExpenses;

import java.util.List;

@Repository
public interface ExpectedExpensesRepository extends JpaRepository<ExpectedExpenses,Integer> {
    List<ExpectedExpenses> findByUniqueGroupCode(String uniqueGroupCode);

    List<ExpectedExpenses> findById(int id);

}
