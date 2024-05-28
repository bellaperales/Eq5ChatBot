package com.springboot.MyTodoList.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.springboot.MyTodoList.model.ProjectItem;

@Repository
@Transactional
@EnableTransactionManagement
public interface ProjectItemRepository extends JpaRepository<ProjectItem, Integer> {

    //List<ProjectItem> findByEmployeeid(int employeeid);

    // List<ProjectItem> findByProjectid(int projectid);

     //List<ProjectItem> findByProjectidOrderByDatelimitDesc(int projectid);
}
