package com.springboot.MyTodoList.model;

import javax.persistence.*;

import java.sql.Timestamp;
import java.time.OffsetDateTime;

/*
    representation of the TODOITEM table that exists already
    in the autonomous database
 */
@Entity
@Table(name = "project")
public class ProjectItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int ID;
    @Column(name = "name")
    String name;
    @Column(name = "datestart")
    Timestamp datestart;
    @Column(name = "dateend")
    Timestamp dateend;
    @Column(name = "status")
    boolean status;
    @Column(name = "departamentid")
    int departamentid;

    public ProjectItem() {

    }

    public ProjectItem(int ID, String name, Timestamp datestart, Timestamp dateend, boolean status, int departamentid) {
        this.ID = ID;
        this.name = name;
        this.datestart = datestart;
        this.dateend = dateend;
        this.status = status;
        this.departamentid = departamentid;

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

    public Timestamp getDateStart() {
        return datestart;
    }

    public void setDateStart(Timestamp datestart) {
        this.datestart = datestart;
    }

    public Timestamp getDateEnd() {
        return dateend;
    }

    public void setDateEnd(Timestamp dateend) {
        this.dateend = dateend;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getDepartamentID() {
        return departamentid;
    }

    public void setDepartamentID(int departamentid) {
        this.departamentid = departamentid;
    }

    @Override
    public String toString() {
        return "ToDoItem{" +
                "ID=" + ID +
                ", name='" + name + '\'' +
                ", datestart=" + datestart +
                ", dateend=" + dateend +
                ", status=" + status +
                ", departamentid=" + departamentid +
                '}';
    }
}
