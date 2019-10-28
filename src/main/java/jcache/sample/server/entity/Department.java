package jcache.sample.server.entity;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "DEPARTMENT")
@Cache(region = "Dpt-Entity-Cache",usage = CacheConcurrencyStrategy.READ_WRITE)
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DPT_ID")
    private int id;

    @Column(name = "NAME", nullable = false, unique = true)
    private String name;


    @OneToMany(cascade = CascadeType.ALL, mappedBy = "department")
    @Cache(region = "Emp-Collection-Cache",usage = CacheConcurrencyStrategy.READ_WRITE)
    private List<Employee> employees = new ArrayList<Employee>();

    public Department() {}

    public Department(String name, List<Employee> employees) {
        this.name = name;
        this.employees = employees;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }
}
