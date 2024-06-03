package com.springboot.MyTodoList.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.springboot.MyTodoList.model.EmployeeItem;
import com.springboot.MyTodoList.model.ProjectItem;
import com.springboot.MyTodoList.model.ToDoItem;

@Repository
@Transactional
@EnableTransactionManagement
public interface ToDoItemRepository extends JpaRepository<ToDoItem, Integer> {
    List<ToDoItem> findByEmployeeid(int employeeid);

    List<ToDoItem> findByProjectid(ProjectItem projectItem);

    List<ToDoItem> findByProjectidOrderByDatelimitDesc(ProjectItem projectid);

    List<ToDoItem> findByEmployeeidOrderByDatelimitAsc(EmployeeItem employeeid);



    
    


}
