package com.apache.cxf.xml.json.service;

import com.shap.employee.Employees;
import com.shap.employee.Employee;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/employeeservice")
public interface IEmployeeService {

	// Basic CRUD operations for Employee Service

	// http://localhost:8080/ApacheCXF-XML-JSON-IO/services/employeeservice/addemployee
	@POST
	@Path("addemployee")
	@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_FORM_URLENCODED})
	public String createOrSaveNewEmployeeInfo(Employee employee);

	// http://localhost:8080/ApacheCXF-XML-JSON-IO/services/employeeservice/getemployee/{eno}
	@GET
	@Path("getemployee/{eno}")
	@Consumes({MediaType.APPLICATION_FORM_URLENCODED})
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Employees getEmployeeInfo(@PathParam("eno") int employeeId);

	// http://localhost:8080/ApacheCXF-XML-JSON-IO/services/employeeservice/updateemployee
	@PUT
	@Path("updateemployee")
	@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_FORM_URLENCODED})
	public String updateEmployeeInfo(Employee employee);

	// http://localhost:8080/ApacheCXF-XML-JSON-IO/services/employeeservice/deleteemployee
	@DELETE
	@Path("deleteemployee")
	@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON,})
	@Produces({MediaType.APPLICATION_FORM_URLENCODED})
	public String deleteEmployeeInfo(Employee employee);

	// http://localhost:8080/ApacheCXF-XML-JSON-IO/services/employeeservice/getallemployee
	@GET
	@Path("getallemployee")
	@Consumes({MediaType.APPLICATION_FORM_URLENCODED})
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Employees getAllEmployeeInfo();
}