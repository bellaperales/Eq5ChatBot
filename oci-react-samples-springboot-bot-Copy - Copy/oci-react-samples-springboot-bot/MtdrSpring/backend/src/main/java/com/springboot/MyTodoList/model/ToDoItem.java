package com.springboot.MyTodoList.model;

import javax.persistence.*;

import java.sql.Timestamp;
import java.time.OffsetDateTime;

/*
    representation of the TODOITEM table that exists already
    in the autonomous database
 */
@Entity
@Table(name = "task")
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
    boolean status;
    @Column(name = "datelimit")
    Timestamp datelimit;
    @Column(name = "type")
    String type;
    @Column(name = "employeeid")
    int employeeid;
    @Column(name = "projectid")
    int projectid;

    public ToDoItem() {

    }

    public ToDoItem(int ID, String name, String description, Timestamp datecreated, boolean status, Timestamp datelimit,
            String type, int employeeid, int projectid) {
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

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
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

    public int getEmployeeID() {
        return employeeid;
    }

    public void setEmployeeID(int employeeid) {
        this.employeeid = employeeid;
    }

    public int getProjectID() {
        return projectid;
    }

    public void setProjectID(int projectid) {
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
