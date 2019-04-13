package com.JSPServelt.app;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;



public class Employeedata {
	private DataSource dataSource;

	public Employeedata(DataSource theDataSource) {
		dataSource = theDataSource;
	}
	
	public List<Employee> getEmployees() throws Exception {
		
		List<Employee> employees = new ArrayList<>();
		
		Connection myConn = null;
		Statement myStmt = null;
		ResultSet myRs = null;
		
		try {
			myConn = dataSource.getConnection();

			String sql = "select * from employee order by last_name";
			
			myStmt = myConn.createStatement();

			myRs = myStmt.executeQuery(sql);

			while (myRs.next()) {

				int id = myRs.getInt("id");
				String firstName = myRs.getString("first_name");
				String lastName = myRs.getString("last_name");
				String email = myRs.getString("email");
				Employee tempEmp = new Employee(id, firstName, lastName, email);

				employees.add(tempEmp);				
			}
			
			return employees;		
		}
		finally {
			
			close(myConn, myStmt, myRs);
		}	
		
	}

	private void close(Connection myConn, Statement myStmt, ResultSet myRs) {

		try {
			if (myRs != null) {
				myRs.close();
			}
			
			if (myStmt != null) {
				myStmt.close();
			}
			
			if (myConn != null) {
				myConn.close();   
			}
		}
		catch (Exception exc) {
			exc.printStackTrace();
		}
	}

	public void addEmployee(Employee Emp) throws Exception {

		Connection myConn = null;
		PreparedStatement myStmt = null;
		
		try {
		
			myConn = dataSource.getConnection();

			String sql = "insert into employee "
					   + "(first_name, last_name, email) "
					   + "values (?, ?, ?)";
			
			myStmt = myConn.prepareStatement(sql);

			myStmt.setString(1, Emp.getFirstName());
			myStmt.setString(2, Emp.getLastName());
			myStmt.setString(3, Emp.getEmail());
			myStmt.execute();
		}
		finally {
			
			close(myConn, myStmt, null);
		}
	}

	public Employee getEmployees(String EmpId) throws Exception {

		Employee Emp = null;
		
		Connection myConn = null;
		PreparedStatement myStmt = null;
		ResultSet myRs = null;
		int empId;
		
		try {
			
			empId = Integer.parseInt(EmpId);
			
			myConn = dataSource.getConnection();
			String sql = "select * from employee where id=?";
			myStmt = myConn.prepareStatement(sql);
			myStmt.setInt(1, empId);
			myRs = myStmt.executeQuery();
			if (myRs.next()) {
				String firstName = myRs.getString("first_name");
				String lastName = myRs.getString("last_name");
				String email = myRs.getString("email");
				Emp = new Employee(empId, firstName, lastName, email);
			}
			else {
				throw new Exception("Could not find employee id: " + empId);
			}				
			
			return Emp;
		}
		finally {
			close(myConn, myStmt, myRs);
		}
	}

	public void updateEmployee(Employee Emp) throws Exception {
		
		Connection myConn = null;
		PreparedStatement myStmt = null;

		try {
			myConn = dataSource.getConnection();
			String sql = "update employee "
						+ "set first_name=?, last_name=?, email=? "
						+ "where id=?";
			myStmt = myConn.prepareStatement(sql);
			myStmt.setString(1, Emp.getFirstName());
			myStmt.setString(2, Emp.getLastName());
			myStmt.setString(3, Emp.getEmail());
			myStmt.setInt(4, Emp.getId());
			myStmt.execute();
		}
		finally {
			close(myConn, myStmt, null);
		}
	}

	public void deleteEmployee(String EmpId) throws Exception {

		Connection myConn = null;
		PreparedStatement myStmt = null;
		
		try {
			int empId = Integer.parseInt(EmpId);
			myConn = dataSource.getConnection();
			String sql = "delete from employee where id=?";
			myStmt = myConn.prepareStatement(sql);
			myStmt.setInt(1, empId);
			myStmt.execute();
		}
		finally {
			close(myConn, myStmt, null);
		}	
	}

}


