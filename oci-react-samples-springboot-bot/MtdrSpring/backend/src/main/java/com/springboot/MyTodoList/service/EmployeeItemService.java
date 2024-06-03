package com.springboot.MyTodoList.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.springboot.MyTodoList.model.DepartamentItem;
import com.springboot.MyTodoList.model.EmployeeItem;
import com.springboot.MyTodoList.model.ProjectItem;
import com.springboot.MyTodoList.repository.EmployeeItemRepository;

@Service
public class EmployeeItemService {

    @Autowired
    private EmployeeItemRepository employeeItemRepository;

    public List<EmployeeItem> findAll() {
        List<EmployeeItem> employeeItems = employeeItemRepository.findAll();
        return employeeItems;
    }

    public ResponseEntity<EmployeeItem> getEmployeeItemById(int id) {
        Optional<EmployeeItem> employeeData = employeeItemRepository.findById(id);
        if (employeeData.isPresent()) {
            return new ResponseEntity<>(employeeData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    public EmployeeItem addEmployeeItem(EmployeeItem employeeItem) {
        return employeeItemRepository.save(employeeItem);
    }

    public boolean deleteEmployeeItem(int id) {
        try {
            employeeItemRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    

    public Optional<EmployeeItem> getEmployeeItemByMynumber(int mynumber) {
        return employeeItemRepository.findByMynumber(mynumber);
    }

    public EmployeeItem  getEmployeeItemByNameAndLastname(String name, String lastname) {
        return employeeItemRepository.findByNameAndLastname(name, lastname);
    }

    public List<EmployeeItem> findByProjectid(ProjectItem projectid) {
        return employeeItemRepository.findByProjectid(projectid);
    }
    /* 
    public EmployeeItem getEmployeeItemByToDoItem(ToDoItem toDoItem) {
    return employeeItemRepository.findByID(toDoItem.getEmployeeID());
    }*/

    //fix this
    public EmployeeItem getEmployeeItemByToDoItem(int id) {
        return employeeItemRepository.findByID(id);
    }

    //findByDepartamentid(int)
    public List<EmployeeItem> findByDepartamentid(DepartamentItem departamentid) {
        return employeeItemRepository.findByDepartamentid(departamentid);
    }


    public EmployeeItem updateEmployeeItem(int id, EmployeeItem td) {
        Optional<EmployeeItem> employeeItemData = employeeItemRepository.findById(id);
        if (employeeItemData.isPresent()) {
            EmployeeItem employeeItem = employeeItemData.get();
            employeeItem.setID(id);
            employeeItem.setName(td.getName());
            employeeItem.setLastname(td.getLastname());
            employeeItem.setMail(td.getMail());
            employeeItem.setCellphone(td.getCellphone());
            employeeItem.setAddress(td.getAddress());
            employeeItem.setStatus(td.getStatus());
            employeeItem.setManager(td.getManager());
            employeeItem.setMynumber(td.getMynumber());
            employeeItem.setDepartamentid(td.getDepartamentid());
            employeeItem.setProjectid(td.getProjectid());

            return employeeItemRepository.save(employeeItem);
        } else {
            return null;
        }
    }

}
