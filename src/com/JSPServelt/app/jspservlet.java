package com.JSPServelt.app;

import java.io.IOException;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
/**
 * Servlet implementation class jspservlet
 */
@WebServlet("/jspservlet")
public class jspservlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Employeedata employeedata; 
	@Resource(name="jdbc/JSP_Servlet")
	private DataSource dataSource;
	@Override
	public void init() throws ServletException {
		super.init();
		try {
			employeedata = new Employeedata(dataSource);
		}
		catch (Exception exc) {
			throw new ServletException(exc);
		}
	}
    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			String theCommand = request.getParameter("command");
			if (theCommand == null) {
				theCommand = "LIST";
			}
			switch (theCommand) {
			
			case "LIST":
				listEmployees(request, response);
				break;
				
			case "ADD":
				addEmployee(request, response);
				break;
				
			case "LOAD":
				loadEmployee(request, response);
				break;
				
			case "UPDATE":
				updateEmployee(request, response);
				break;
			
			case "DELETE":
				deleteEmployee(request, response);
				break;
				
			default:
				listEmployees(request, response);
			}
				
		}
		catch (Exception exc) {
			throw new ServletException(exc);
		}
		
	}
	private void deleteEmployee(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			String theEmployeeId = request.getParameter("employeeId");
			employeedata.deleteEmployee(theEmployeeId);
			listEmployees(request, response);
		}
	private void updateEmployee(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			int id = Integer.parseInt(request.getParameter("employeeId"));
			String firstName = request.getParameter("firstName");
			String lastName = request.getParameter("lastName");
			String email = request.getParameter("email");
			Employee theEmployee = new Employee(id, firstName, lastName, email);
			employeedata.updateEmployee(theEmployee);
			listEmployees(request, response);
			
		}
	private void addEmployee(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		String email = request.getParameter("email");
		Employee theEmployee = new Employee(firstName, lastName, email);
	    employeedata.addEmployee(theEmployee);
		listEmployees(request, response);
	}
	private void listEmployees(HttpServletRequest request, HttpServletResponse response) 
			throws Exception {
			List<Employee> employees = employeedata.getEmployees();
			request.setAttribute("EMPLOYEE_LIST", employees);
			RequestDispatcher dispatcher = request.getRequestDispatcher("/listemployee.jsp");
			dispatcher.forward(request, response);
		}
	private void loadEmployee(HttpServletRequest request, HttpServletResponse response) 
			throws Exception {
			String theEmployeeId = request.getParameter("employeeId");
			Employee theEmployee = employeedata.getEmployees(theEmployeeId);
			request.setAttribute("THE_EMPLOYEE", theEmployee);
			RequestDispatcher dispatcher = 
					request.getRequestDispatcher("/updateemployee.jsp");
			dispatcher.forward(request, response);		
		}



}
