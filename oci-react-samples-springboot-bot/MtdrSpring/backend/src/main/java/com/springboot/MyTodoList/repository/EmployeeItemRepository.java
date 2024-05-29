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

    EmployeeItem findByNameAndLastname(String name, String lastname);

    List<EmployeeItem> findByProjectid(int projectid);

    EmployeeItem findByID (int id);

    //findByDepartamentid(int)

    List<EmployeeItem> findByDepartamentid (int departamentid);


}
