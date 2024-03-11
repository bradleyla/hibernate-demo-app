package org.example;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.StaleObjectStateException;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.query.Query;

import javax.security.auth.login.AppConfigurationEntry;
import java.util.List;
import java.util.Scanner;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) {
        //create a configuration object
        Configuration config = new Configuration();

        // read the configuration object
        config.configure("hibernate.cfg.xml");

        // create session factory
        SessionFactory sessionFactory = config.buildSessionFactory();

        // open session
        Session session = sessionFactory.openSession();

        Scanner scanner = new Scanner(System.in);

        boolean flag = true;
        while(flag) {
            System.out.println("*************************");
            System.out.println("Select from the options below");
            System.out.println("*************************");
            System.out.println("PRESS 1: Add Employee");
            System.out.println("PRESS 2: Update Employee");
            System.out.println("PRESS 3: Delete Employee");
            System.out.println("PRESS 4: Get All Employees");
            System.out.println("PRESS 5: Get Employee by Id");
            System.out.println("PRESS 6: Exit");

            int input = scanner.nextInt();
            switch (input) {
                case 1: {
                    // add
                    // begin transaction
                    Transaction transaction = session.beginTransaction();
                    System.out.print("Enter employee id: ");
                    int id = scanner.nextInt();
                    System.out.print("Enter Name: ");
                    String name = scanner.next();
                    System.out.print("Enter Email: ");
                    String email = scanner.next();
                    Employee employee = new Employee();
                    employee.setId(id);
                    employee.setName(name);
                    employee.setEmail(email);
                    try {
                        session.save(employee);
                        transaction.commit();
                    } catch (ConstraintViolationException | StaleObjectStateException e) {
                        System.out.println("Oops! Something went wrong. Please try again.");
                    }
                    break;
                }
                case 2: {
                    // update
                    // begin transaction
                    Transaction transaction = session.beginTransaction();
                    System.out.print("Enter employee id you want to update: ");
                    int id = scanner.nextInt();
                    System.out.print("Enter New Name: ");
                    String newName = scanner.next();
                    System.out.print("Enter New Email: ");
                    String newEmail = scanner.next();
                    Employee employee = session.get(Employee.class, id);
                    employee.setName(newName);
                    employee.setEmail(newEmail);
                    try {
                        session.update(employee);
                        transaction.commit();
                    } catch (StaleObjectStateException e) {
                        System.out.println("Oops! Something went wrong. Please try again.");
                    }
                    break;
                }
                case 3: {
                    // delete
                    // begin transaction
                    Transaction transaction = session.beginTransaction();
                    System.out.print("Enter employee id you want to delete: ");
                    int id = scanner.nextInt();
                    Employee employee = session.get(Employee.class, id);
                    try {
                        session.delete(employee);
                        transaction.commit();
                    } catch (StaleObjectStateException e) {
                        System.out.println("Oops! Something went wrong. Please try again.");
                    }
                    break;
                }
                case 4: {
                    // get all employees
                    String hql = "SELECT employee FROM Employee employee";
                    Query<Employee> query = session.createQuery(hql, Employee.class);
                    List<Employee> employees = query.list();
                    if (!(employees.isEmpty())) {
                        for (Employee employee : employees) {
                            System.out.println(employee.toString());
                        }
                    } else
                        System.out.println("There are no employees in the database. Please add one or more employees and try again.");
                    break;
                }
                case 5: {
                    // get employee by id
                    System.out.print("Enter employee id you want to find: ");
                    int id = scanner.nextInt();
                    try {
                        Employee employee = session.get(Employee.class, id);
                        System.out.println(employee.toString());
                    } catch (NullPointerException e) {
                        System.out.println("The employee with this id was not found. Please try again.");
                    }
                    break;
                }
                case 6: {
                    // exit
                    System.out.println("Thank you");
                    System.out.println("Exiting...");
                    flag = false;
                    break;
                }
                default: {
                    System.out.println("Please select an option from 1 to 6.");
                }
            }
        }
        // close the connection
        session.close();
    }
}
