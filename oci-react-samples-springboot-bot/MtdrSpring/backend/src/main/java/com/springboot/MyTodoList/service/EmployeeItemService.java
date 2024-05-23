package com.springboot.MyTodoList.service;

import com.springboot.MyTodoList.model.EmployeeItem;
import com.springboot.MyTodoList.repository.EmployeeItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import static org.mockito.ArgumentMatchers.eq;

import java.util.List;
import java.util.Optional;

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

    public boolean isManagerByMynumber(int mynumber) {
        Optional<EmployeeItem> employee = employeeItemRepository.findByMynumber(mynumber);
        return employee.map(EmployeeItem::getManager).orElse(false);
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

            return employeeItemRepository.save(employeeItem);
        } else {
            return null;
        }
    }

}
