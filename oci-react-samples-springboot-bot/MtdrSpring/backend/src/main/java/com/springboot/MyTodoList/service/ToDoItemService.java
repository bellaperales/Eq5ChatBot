package com.springboot.MyTodoList.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.springboot.MyTodoList.model.ToDoItem;
import com.springboot.MyTodoList.repository.ProjectItemRepository;
import com.springboot.MyTodoList.repository.ToDoItemRepository;

@Service
public class ToDoItemService {

    @Autowired
    private ToDoItemRepository toDoItemRepository;

    @Autowired
    private ProjectItemRepository projectItemRepository;

    public List<ToDoItem> findAll() {
        List<ToDoItem> todoItems = toDoItemRepository.findAll();
        return todoItems;
    }

    public ToDoItem findById(int id) {
        Optional<ToDoItem> todoData = toDoItemRepository.findById(id);
        if (todoData.isPresent()) {
            return todoData.get();
        } else {
            return null;
        }
    }

    public List<ToDoItem> findByEmployeeid(int employeeid) {
        return toDoItemRepository.findByEmployeeid(employeeid);
    }

    /*
    public List<ToDoItem> findByProjectid(int projectid) {
       ProjectItem projectItem = projectItemRepository.findById(projectid).orElseThrow(() -> new RuntimeException("Project not found"));
        return toDoItemRepository.findByProjectid(projectItem);
    }*/

    public List<ToDoItem> findByProjectid(int projectid) {
        return toDoItemRepository.findByProjectid(projectid);
    }

    public List<ToDoItem> findByProjectidOrderByDatelimitDesc(int projectid) {
        return toDoItemRepository.findByProjectidOrderByDatelimitDesc(projectid);
    }

    public List<ToDoItem> findByEmployeeidOrderByDatelimitAsc(int employeeid) {
        return toDoItemRepository.findByEmployeeidOrderByDatelimitAsc(employeeid);
    }

    public ResponseEntity<ToDoItem> getItemById(int id) {
        Optional<ToDoItem> todoData = toDoItemRepository.findById(id);
        if (todoData.isPresent()) {
            return new ResponseEntity<>(todoData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    //getItemById
    public ToDoItem getToDoItemById(int id) {
        Optional<ToDoItem> todoData = toDoItemRepository.findById(id);
        if (todoData.isPresent()) {
            return todoData.get();
        } else {
            return null;
        }
    }

    public ToDoItem addToDoItem(ToDoItem toDoItem) {
        return toDoItemRepository.save(toDoItem);
    }

    public boolean deleteToDoItem(int id) {
        try {
            toDoItemRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public ToDoItem updateToDoItem(int id, ToDoItem td) {
        Optional<ToDoItem> toDoItemData = toDoItemRepository.findById(id);
        if (toDoItemData.isPresent()) {
            ToDoItem toDoItem = toDoItemData.get();
            toDoItem.setID(id);
            toDoItem.setName(td.getName());
            toDoItem.setDescription(td.getDescription());
            toDoItem.setDateCreated(td.getDateCreated());
            toDoItem.setStatus(td.getStatus());
            toDoItem.setDateLimit(td.getDateLimit());
            toDoItem.setType(td.getType());
            toDoItem.setEmployeeID(td.getEmployeeID());
            toDoItem.setProjectID(td.getProjectID());

            return toDoItemRepository.save(toDoItem);
        } else {
            return null;
        }
    }

}
