package com.apache.cxf.xml.json.service;

import com.shap.employee.Employees;
import com.shap.employee.Employee;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.stereotype.Service;

@Service("employeeService")
public class EmployeeServiceImpl implements IEmployeeService {

	/**
	 * returns a String value with SUCCESS message after adding a employee
	 */
	@Override
	public String createOrSaveNewEmployeeInfo(Employee employee) {

		// get the employee information from formal arguments and inserts into database & return employeeId (primary_key)
		Connection con = null;
		Statement stmt = null;
		String status = null;
		try {
			Class.forName("org.postgresql.Driver");
			con = DriverManager.getConnection("jdbc:postgresql://10.43.100.246:5432/cxfrest", "cxf", "rest");
			stmt = con.createStatement();
			stmt.execute("INSERT INTO EMPLOYEE_TBL (eno, name, salary) VALUES ('"+employee.getEno()+"','"+employee.getName()+"','"+employee.getSalary()+"')");
			status = "Employee information saved successfully, with id: "+employee.getEno();
		}catch (ClassNotFoundException | SQLException e) {
			status = "Employee information saved failed, with id: "+employee.getEno();
			e.printStackTrace();
		}finally {
			try {stmt.close();}catch (Exception e) {e.printStackTrace();}finally {stmt = null;}
			try {con.close();}catch (Exception e) {e.printStackTrace();}finally {con = null;}
		}
		return status;
	}

	/**
	 * retrieves a employee object based on the employeeId supplied in the formal argument using @PathParam
	 */
	@Override
	public Employees getEmployeeInfo(int employeeId) {

		// retrieve employee based on the id supplied in the formal argument
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		Employees employees = null;
		Employee employee = null;
		try {
			Class.forName("org.postgresql.Driver");
			con = DriverManager.getConnection("jdbc:postgresql://10.43.100.246:5432/cxfrest", "cxf", "rest");
			stmt = con.createStatement();
			rs = stmt.executeQuery("select * from EMPLOYEE_TBL where eno = '"+employeeId+"'");
			employees = new Employees();
			while(rs.next()) {
				employee = new Employee();
				employee.setEno(rs.getInt("eno"));
				employee.setName(rs.getString("name"));
				employee.setSalary(rs.getDouble("salary"));
				employees.getEmployee().add(employee);
				employee = null;
			}
		}catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}finally {
			try {rs.close();}catch (Exception e) {e.printStackTrace();}finally {rs = null;}
			try {stmt.close();}catch (Exception e) {e.printStackTrace();}finally {stmt = null;}
			try {con.close();}catch (Exception e) {e.printStackTrace();}finally {con = null;}
		}
		return employees;
	}

	/**
	 * returns a String value with SUCCESS message after updating a employee
	 */
	@Override
	public String updateEmployeeInfo(Employee employee) {

		// update employee info & return SUCCESS message
		Connection con = null;
		Statement stmt = null;
		String status = null;
		try {
			Class.forName("org.postgresql.Driver");
			con = DriverManager.getConnection("jdbc:postgresql://10.43.100.246:5432/cxfrest", "cxf", "rest");
			stmt = con.createStatement();
			stmt.execute("UPDATE EMPLOYEE_TBL SET name = '"+employee.getName()+"', salary = '"+employee.getSalary()+"' WHERE eno = '"+employee.getEno()+"'");
			status = "Employee information updated successfully, with id: "+employee.getEno();
		}catch (ClassNotFoundException | SQLException e) {
			status = "Employee information updated failed, with id: "+employee.getEno();
		}finally {
			try {stmt.close();}catch (Exception e) {e.printStackTrace();}finally {stmt = null;}
			try {con.close();}catch (Exception e) {e.printStackTrace();}finally {con = null;}
		}
		return status;
	}

	/**
	 * returns a String value with SUCCESS message after deleting a employee
	 */
	@Override
	public String deleteEmployeeInfo(Employee employee) {

		// delete employee info & return SUCCESS message
		Connection con = null;
		Statement stmt = null;
		String status = null;
		try {
			Class.forName("org.postgresql.Driver");
			con = DriverManager.getConnection("jdbc:postgresql://10.43.100.246:5432/cxfrest", "cxf", "rest");
			stmt = con.createStatement();
			stmt.execute("DELETE FROM EMPLOYEE_TBL WHERE eno='"+employee.getEno()+"'");
			status = "Employee information deleted successfully, with id: "+employee.getEno();
		}catch (ClassNotFoundException | SQLException e) {
			status = "Employee information deleted failed, with id: "+employee.getEno();
		}finally {
			try {stmt.close();}catch (Exception e) {e.printStackTrace();}finally {stmt = null;}
			try {con.close();}catch (Exception e) {e.printStackTrace();}finally {con = null;}
		}
		return status;
	}

	/**
	 * retrieves all employees stored
	 */
	@Override
	public Employees getAllEmployeeInfo() {

		// create a object of type Employee which takes employee objects in its list
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		Employees employees = null;
		Employee employee = null;
		try {
			Class.forName("org.postgresql.Driver");
			con = DriverManager.getConnection("jdbc:postgresql://10.43.100.246:5432/cxfrest", "cxf", "rest");
			stmt = con.createStatement();
			rs = stmt.executeQuery("select * from EMPLOYEE_TBL");
			employees = new Employees();
			while(rs.next()) {
				employee = new Employee();
				employee.setEno(rs.getInt("eno"));
				employee.setName(rs.getString("name"));
				employee.setSalary(rs.getDouble("salary"));
				employees.getEmployee().add(employee);
				employee = null;
			}
		}catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}finally {
			try {rs.close();}catch (Exception e) {e.printStackTrace();}finally {rs = null;}
			try {stmt.close();}catch (Exception e) {e.printStackTrace();}finally {stmt = null;}
			try {con.close();}catch (Exception e) {e.printStackTrace();}finally {con = null;}
		}
		return employees;
	}
}
