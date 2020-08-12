package models;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Table(name = "follows")
@NamedQueries({
@NamedQuery(
        name = "getFollows",
        query = "SELECT f FROM Follow AS f WHERE f.employee = :employee AND f.follow = :follow"
        ),
@NamedQuery(
        name = "getFollowers",
        query = "SELECT f FROM Follow AS f WHERE f.follow = :login_employee AND f.employee  = :employee"
        ),
@NamedQuery(
        name = "getFollowCount",
        query = "SELECT COUNT(f) FROM Follow AS f WHERE f.employee  = :employee"
        ),
@NamedQuery(
        name = "getFollowerCount",
        query = "SELECT COUNT(f) FROM Follow AS f WHERE f.follow  = :employee"
        ),
@NamedQuery(
        name = "getFollowEmployees",
        query = "SELECT f FROM Follow AS f WHERE f.employee  = :login_employee"
        ),
@NamedQuery(
        name = "getFollowerEmployees",
        query = "SELECT f FROM Follow AS f WHERE f.follow  = :login_employee"
        )


})
@Entity
public class Follow{


    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "employee", nullable = false)
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "follow", nullable = false)
    private Employee follow;

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

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Employee getFollow() {
        return follow;
    }

    public void setFollow(Employee follow) {
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