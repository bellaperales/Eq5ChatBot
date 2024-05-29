package com.springboot.MyTodoList.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.MyTodoList.model.EmployeeItem;
import com.springboot.MyTodoList.service.EmployeeItemService;

@RestController
public class EmployeeItemController {
    @Autowired
    private EmployeeItemService employeeItemService;

    // @CrossOrigin
    @GetMapping(value = "/employeelist")
    public List<EmployeeItem> getAllEmployeeItems() {
        return employeeItemService.findAll();
    }

        // @CrossOrigin
    /*@GetMapping(value = "/todolist")
    public List<EmployeeItem> getAllProjectItems(int employeeid) {
        return employeeItemService.findByEmployeeid(employeeid);
    }*/

    // @CrossOrigin
    @GetMapping(value = "/employeelist/{id}")
    public ResponseEntity<EmployeeItem> getEmployeeItemById(@PathVariable int id) {
        try {
            ResponseEntity<EmployeeItem> responseEntity = employeeItemService.getEmployeeItemById(id);
            return new ResponseEntity<EmployeeItem>(responseEntity.getBody(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // @CrossOrigin
    @PostMapping(value = "/employeelist")
    public ResponseEntity addEmployeeItem(@RequestBody EmployeeItem employeeItem) throws Exception {
        EmployeeItem td = employeeItemService.addEmployeeItem(employeeItem);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("location", "" + td.getID());
        responseHeaders.set("Access-Control-Expose-Headers", "location");
        // URI location = URI.create(""+td.getID())

        return ResponseEntity.ok()
                .headers(responseHeaders).build();
    }

    // @CrossOrigin
    @PutMapping(value = "employee/{id}")
    public ResponseEntity updateToDoItem(@RequestBody EmployeeItem employeeItem, @PathVariable int id) {
        try {
            EmployeeItem employeeItem1 = employeeItemService.updateEmployeeItem(id, employeeItem);
            System.out.println(employeeItem1.toString());
            return new ResponseEntity<>(employeeItem1, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    // @CrossOrigin
    @DeleteMapping(value = "employee/{id}")
    public ResponseEntity<Boolean> deleteEmployeeItem(@PathVariable("id") int id) {
        Boolean flag = false;
        try {
            flag = employeeItemService.deleteEmployeeItem(id);
            return new ResponseEntity<>(flag, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(flag, HttpStatus.NOT_FOUND);
        }
    }

    // @CrossOrigin
    @GetMapping(value = "/employeelist/{mynumber}")
    public ResponseEntity<EmployeeItem> getEmployeeItemByMynumber(@PathVariable int mynumber) {
        EmployeeItem employeeItem = employeeItemService.getEmployeeItemByMynumber(mynumber).orElse(null);
        return new ResponseEntity<>(employeeItem, HttpStatus.OK);
    }


    // @CrossOrigin
    @GetMapping(value = "/employeelist/{name}/{lastname}")
    public ResponseEntity<EmployeeItem> getEmployeeItemByNameAndLastname(@PathVariable String name, @PathVariable String lastname) {
        EmployeeItem employeeItem = employeeItemService.getEmployeeItemByNameAndLastname(name, lastname);
        return new ResponseEntity<>(employeeItem, HttpStatus.OK);
    }

    // @CrossOrigin
    @GetMapping(value = "/employeelist/{projectid}")
    public List<EmployeeItem> findByProjectid(int projectid) {
        return employeeItemService.findByProjectid(projectid);
    }

}
