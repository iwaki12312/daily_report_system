package models;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Table(name = "follows")
@NamedQueries({
@NamedQuery(
        name = "getFollows",
        query = "SELECT f FROM Follow AS f WHERE f.employee_id  = :employee_id AND f.follow = :follow"
        ),
@NamedQuery(
        name = "getFollowers",
        query = "SELECT f FROM Follow AS f WHERE f.follow = :login_employee_id AND f.employee_id  = :employee_id"
        ),
@NamedQuery(
        name = "getFollowCount",
        query = "SELECT COUNT(f) FROM Follow AS f WHERE f.employee_id  = :employee_id"
        ),
@NamedQuery(
        name = "getFollowerCount",
        query = "SELECT COUNT(f) FROM Follow AS f WHERE f.follow  = :employee_id"
        ),
@NamedQuery(
        name = "getFollowEmployees",
        query = "SELECT f FROM Follow AS f WHERE f.employee_id  = :login_employee_id"
        ),
@NamedQuery(
        name = "getFollowerEmployees",
        query = "SELECT f FROM Follow AS f WHERE f.follow  = :login_employee_id"
        )


})
@Entity
public class Follow{


    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @JoinColumn(name = "employee_id", nullable = false)
    private int employee_id;

    @Column(name = "follow", nullable = false)
    private int follow;

    @Column(name = "created_at", nullable = false)
    private Timestamp created_at;

    @Column(name = "updated_at", nullable = false)
    private Timestamp updated_at;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getEmployee_id() {
        return employee_id;
    }

    public void setEmployee_id(int employee_id) {
        this.employee_id = employee_id;
    }

    public int getFollow() {
        return follow;
    }

    public void setFollow(int follow) {
        this.follow = follow;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }

    public Timestamp getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Timestamp updated_at) {
        this.updated_at = updated_at;
    }
}