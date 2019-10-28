package jcache.L2C;

import com.hazelcast.core.Hazelcast;
import jcache.L2C.util.HibernateUtil;
import jcache.L2C.entity.Department;
import jcache.L2C.entity.Employee;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;

public class CollectionCache {

    public static void main(String[] args){

        initializeDatabase();

        Session session1, session2;
        Transaction tx1, tx2;

        // Get Department from DB.
        // This call will not get employees list.
        // It has to be called explicitly.
        session1 = HibernateUtil.getSession();
        tx1 = session1.getTransaction();
        tx1.begin();
        Department department = session1.get(Department.class, 1);
        System.out.println("Department :" + department.getName());
        // Call collection for cached department. Cache miss is expected.
        List<Employee> employees=department.getEmployees();
        for (Employee employee : employees) {
            System.out.println("\tEmployee Name : " + employee.getName());
        }
        session1.close();

        //Get Department again. Cache hit is expected.
        session2 = HibernateUtil.getSession();
        tx2 = session2.getTransaction();
        tx2.begin();
        department = session2.get(Department.class, 1);
        System.out.println("Department :" + department.getName());
        // Call collection for cached department. Cache hit is expected since
        // it has already cached in session1.
        // No SQL queries must be shown on the log.
        employees = department.getEmployees();
        for (Employee employee : employees) {
            System.out.println("\tEmployee Name : " + employee.getName());
        }
        session2.close();
        HibernateUtil.closeFactory();
        Hazelcast.shutdownAll();

    }

    private static void initializeDatabase(){

        Session session;
        Transaction transaction;

        Department dpt = new Department("Department 1", null);

        Employee employee1 = new Employee("Employee 1", dpt);
        Employee employee2 = new Employee("Employee 2", dpt);

        List<Employee> employeeList = new ArrayList<Employee>();
        employeeList.add(employee1);
        employeeList.add(employee2);
        dpt.setEmployees(employeeList);

        session = HibernateUtil.getSession();
        transaction = session.getTransaction();
        transaction.begin();
        session.save(dpt);
        transaction.commit();
        session.close();
        HibernateUtil.evictAllRegions();
    }
}
