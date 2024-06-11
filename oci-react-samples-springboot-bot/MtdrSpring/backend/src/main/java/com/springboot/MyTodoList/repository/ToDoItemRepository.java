package com.springboot.MyTodoList.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.springboot.MyTodoList.model.ToDoItem;

@Repository
@Transactional
@EnableTransactionManagement
public interface ToDoItemRepository extends JpaRepository<ToDoItem, Integer> {
    List<ToDoItem> findByEmployeeid(int employeeid);

    List<ToDoItem> findByProjectid(int projectItem);

    List<ToDoItem> findByProjectidOrderByDatelimitDesc(int projectid);

    List<ToDoItem> findByEmployeeidOrderByDatelimitAsc(int employeeid);



    
    


}
