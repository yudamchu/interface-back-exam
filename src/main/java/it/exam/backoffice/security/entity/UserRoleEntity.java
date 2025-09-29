package it.exam.backoffice.security.entity;
import it.exam.backoffice.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="user_role")
public class UserRoleEntity extends BaseEntity {

    @Id
    private String roleId;

    private String roleName;

    @Column( columnDefinition = "CHAR(1)")
    private String useYn;
}
