package models;

import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Table(name = "reports")
@NamedQueries({
    @NamedQuery(
            name = "getAllReports",
            query = "SELECT r FROM Report AS r WHERE r.client = :client ORDER BY r.id DESC"
            ),
    @NamedQuery(
            name = "getReportsCount",
            query = "SELECT COUNT(r) FROM Report AS r"
            ),
    @NamedQuery(
            name = "getMyAllReports",
            query = "SELECT r FROM Report AS r WHERE r.employee = :employee ORDER BY r.id DESC"
            ),
    @NamedQuery(
            name = "getMyReportsCount",
            query = "SELECT COUNT(r) FROM Report AS r WHERE r.employee = :employee"
            ),
    @NamedQuery(
            name = "getNotSectionManagerApprovalReports",
            query = "SELECT r FROM Report AS r WHERE r.approval_status = 1 AND r.employee <> :login_employee ORDER BY r.id DESC"
            ),
    @NamedQuery(
            name = "getNotManagerApprovalReports",
            query = "SELECT r FROM Report AS r WHERE r.approval_status = 3 AND r.employee <> :login_employee ORDER BY r.id DESC"
            ),
    @NamedQuery(
            name = "getNotSectionManagerApprovalCount",
            query = "SELECT COUNT(r) FROM Report AS r WHERE r.approval_status = 1 AND r.employee <> :login_employee"
            ),
    @NamedQuery(
            name = "getNotManagerApprovalCount",
            query = "SELECT COUNT(r) FROM Report AS r WHERE r.approval_status = 3 AND r.employee <> :login_employee"
            ),
    @NamedQuery(
            name = "getClientReports",
            query = "SELECT r FROM Report AS r WHERE r.client = :client ORDER BY r.id DESC"
            ),
    @NamedQuery(
            name = "getClientReportsCount",
            query = "SELECT COUNT(r) FROM Report AS r WHERE r.client = :client"
            ),
    @NamedQuery(
            name = "getRejectReports",
            query = "SELECT r FROM Report AS r WHERE r.employee = :login_employee AND r.approval_status IN (2 , 4) ORDER BY r.id DESC"
            ),
    @NamedQuery(
            name = "getEditingReports",
            query = "SELECT r FROM Report AS r WHERE r.employee = :login_employee AND r.approval_status = 0 ORDER BY r.id DESC"
            ),
    @NamedQuery(
            name = "getRejectReportsCount",
            query = "SELECT COUNT(r) FROM Report AS r WHERE r.employee = :login_employee AND r.approval_status IN (2 , 4)"
            ),
    @NamedQuery(
            name = "getEditingReportsCount",
            query = "SELECT COUNT(r) FROM Report AS r WHERE r.employee = :login_employee AND r.approval_status = 0"
            ),


})
@Entity
public class Report {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Column(name = "report_date", nullable = false)
    private Date report_date;

    @Column(name = "title", length = 255, nullable = false)
    private String title;

    @Lob
    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "created_at", nullable = false)
    private Timestamp created_at;

    @Column(name = "updated_at", nullable = false)
    private Timestamp updated_at;

    @ManyToOne
    @JoinColumn(name = "section_manager_approval" , nullable = true)
    private Employee section_manager_approval;

    @ManyToOne
    @JoinColumn(name = "manager_approval" , nullable = true)
    private Employee manager_approval;

    @ManyToOne
    @JoinColumn(name = "client_id" , nullable = true)
    private Client client;

    @Column(name = "negotiation_status", length = 255)
    private String negotiation_status;

    @Column(name = "approval_status", nullable = false)
    private Integer approval_status;

    @Column(name = "denial_text" , length = 255)
    private String  denial_text;

    @ManyToOne
    @JoinColumn(name = "repudiation_user")
    private Employee  repudiation_user;



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

    public Date getReport_date() {
        return report_date;
    }

    public void setReport_date(Date report_date) {
        this.report_date = report_date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public Employee getSection_manager_approval() {
        return section_manager_approval;
    }

    public void setSection_manager_approval(Employee section_manager_approval) {
        this.section_manager_approval = section_manager_approval;
    }

    public Employee getManager_approval() {
        return manager_approval;
    }

    public void setManager_approval(Employee manager_approval) {
        this.manager_approval = manager_approval;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public String getNegotiation_status() {
        return negotiation_status;
    }

    public void setNegotiation_status(String negotiation_status) {
        this.negotiation_status = negotiation_status;
    }

    public Integer getApproval_status() {
        return approval_status;
    }

    public void setApproval_status(Integer approval_status) {
        this.approval_status = approval_status;
    }

    public String getDenial_text() {
        return denial_text;
    }

    public void setDenial_text(String denial_text) {
        this.denial_text = denial_text;
    }

    public Employee getRepudiation_user() {
        return repudiation_user;
    }

    public void setRepudiation_user(Employee repudiation_user) {
        this.repudiation_user = repudiation_user;
    }
}