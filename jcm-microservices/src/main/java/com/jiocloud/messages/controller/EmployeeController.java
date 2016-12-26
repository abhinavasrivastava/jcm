package com.jiocloud.messages.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import com.jiocloud.messages.model.Employee;

@RestController
public class EmployeeController {
	@RequestMapping("/")
    public DeferredResult<List<Employee>> getEmployees() 
    {
		final DeferredResult<List<Employee>>deferredResult = new DeferredResult<List<Employee>>();
        List<Employee> employeesList = new ArrayList<Employee>();
        employeesList.add(new Employee(1,"lokesh","gupta","howtodoinjava@gmail.com"));
        deferredResult.setResult(employeesList);
        return deferredResult;
    }

}
