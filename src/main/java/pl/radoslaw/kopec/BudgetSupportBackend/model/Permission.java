package pl.radoslaw.kopec.BudgetSupportBackend.model;

import javax.persistence.*;

@Entity
@Table(name = "permission")
public class Permission {

    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private int id;

    private String uniqueGroupCode;

    private int typeOfPermission;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUniqueGroupCode() {
        return uniqueGroupCode;
    }

    public void setUniqueGroupCode(String uniqueGroupCode) {
        this.uniqueGroupCode = uniqueGroupCode;
    }

    public int getTypeOfPermission() {
        return typeOfPermission;
    }

    public void setTypeOfPermission(int typeOfPermission) {
        this.typeOfPermission = typeOfPermission;
    }
}
