package com.springboot.MyTodoList.repository;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.springboot.MyTodoList.model.DepartamentItem;
import com.springboot.MyTodoList.model.EmployeeItem;
import com.springboot.MyTodoList.model.ProjectItem;

@Repository
@Transactional
@EnableTransactionManagement
public interface EmployeeItemRepository extends JpaRepository<EmployeeItem, Integer> {

    Optional<EmployeeItem> findByMynumber(int mynumber);

    EmployeeItem findByNameAndLastname(String name, String lastname);

    List<EmployeeItem> findByProjectid(ProjectItem projectid);

    EmployeeItem findByID (int id);

    List<EmployeeItem> findByDepartamentid (DepartamentItem departamentid);


}
