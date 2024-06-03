package com.springboot.MyTodoList.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/*
    representation of the TODOITEM table that exists already
    in the autonomous database
 */
@Entity
@Table(name = "tasks")
public class ToDoItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int ID;
    @Column(name = "name")
    String name;
    @Column(name = "description")
    String description;
    @Column(name = "datecreated")
    Timestamp datecreated;
    @Column(name = "status")
    int status;
    @Column(name = "datelimit")
    Timestamp datelimit;
    @Column(name = "type")
    String type;
    @ManyToOne
    @JoinColumn(name = "employeeid", referencedColumnName = "id")
    private EmployeeItem employeeid;

    @ManyToOne
    @JoinColumn(name = "projectid", referencedColumnName = "id")
    private ProjectItem projectid;

    public ToDoItem() {

    }

    public ToDoItem(int ID, String name, String description, Timestamp datecreated, int status, Timestamp datelimit,
            String type, EmployeeItem employeeid, ProjectItem projectid) {
        this.ID = ID;
        this.name = name;
        this.description = description;
        this.datecreated = datecreated;
        this.status = status;
        this.datelimit = datelimit;
        this.type = type;
        this.employeeid = employeeid;
        this.projectid = projectid;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getDateCreated() {
        return datecreated;
    }

    public void setDateCreated(Timestamp datecreated) {
        this.datecreated = datecreated;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Timestamp getDateLimit() {
        return datelimit;
    }

    public void setDateLimit(Timestamp datelimit) {
        this.datelimit = datelimit;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public EmployeeItem getEmployeeID() {
        return employeeid;
    }

    public void setEmployeeID(EmployeeItem employeeid) {
        this.employeeid = employeeid;
    }

    public ProjectItem getProjectID() {
        return projectid;
    }

    public void setProjectID(ProjectItem projectid) {
        this.projectid = projectid;
    }

    @Override
    public String toString() {
        return "ToDoItem{" +
                "ID=" + ID +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", datecreated=" + datecreated +
                ", status=" + status +
                ", datelimit=" + datelimit +
                ", type='" + type + '\'' +
                ", employeeid=" + employeeid +
                ", projectid=" + projectid +
                '}';
    }
}
