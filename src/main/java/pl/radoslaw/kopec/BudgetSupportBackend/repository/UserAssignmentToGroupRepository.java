package pl.radoslaw.kopec.BudgetSupportBackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.radoslaw.kopec.BudgetSupportBackend.model.UserAssignmentToGroup;

import java.util.List;

@Repository
public interface UserAssignmentToGroupRepository extends JpaRepository<UserAssignmentToGroup,Integer> {

    List<UserAssignmentToGroup> findByUniqueGroupCode(String uniqueGroupCode);
}
