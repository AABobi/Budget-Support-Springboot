package pl.radoslaw.kopec.BudgetSupportBackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.radoslaw.kopec.BudgetSupportBackend.model.Permission;

import java.util.List;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Integer> {
    List<Permission> findByUniqueGroupCode(String uniqueCodeGroup);

    List<Permission> findByUniqueGroupCodeAndTypeOfPermission(String uniqueCodeGroup, int typeOfPermission);
}
