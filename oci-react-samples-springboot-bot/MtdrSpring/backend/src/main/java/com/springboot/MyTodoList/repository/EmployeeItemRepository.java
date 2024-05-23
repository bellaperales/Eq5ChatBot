package com.springboot.MyTodoList.repository;

import com.springboot.MyTodoList.model.EmployeeItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

@Repository
@Transactional
@EnableTransactionManagement
public interface EmployeeItemRepository extends JpaRepository<EmployeeItem, Integer> {

    Optional<EmployeeItem> findByMynumber(int mynumber);

}
