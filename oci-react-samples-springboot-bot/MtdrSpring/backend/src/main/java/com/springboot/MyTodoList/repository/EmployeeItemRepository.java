package com.springboot.MyTodoList.repository;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.springboot.MyTodoList.model.EmployeeItem;

@Repository
@Transactional
@EnableTransactionManagement
public interface EmployeeItemRepository extends JpaRepository<EmployeeItem, Integer> {

    Optional<EmployeeItem> findByMynumber(int mynumber);

    List<EmployeeItem> findByProjectid(int projectid);

    //List<EmployeeItem> findByEmployeeid(int employeeid);

}
