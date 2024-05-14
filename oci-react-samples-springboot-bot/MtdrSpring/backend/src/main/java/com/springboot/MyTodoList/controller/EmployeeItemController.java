package com.springboot.MyTodoList.controller;

import com.springboot.MyTodoList.model.EmployeeItem;
import com.springboot.MyTodoList.service.EmployeeItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

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
    public ResponseEntity<Boolean> isManagerByMynumber(@PathVariable int mynumber) {
        boolean isManager = employeeItemService.isManagerByMynumber(mynumber);
        return new ResponseEntity<>(isManager, HttpStatus.OK);
    }

}
