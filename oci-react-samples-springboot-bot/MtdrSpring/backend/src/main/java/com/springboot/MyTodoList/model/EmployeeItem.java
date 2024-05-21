package com.springboot.MyTodoList.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/*
    representation of the TODOITEM table that exists already
    in the autonomous database
 */
@Entity
@Table(name = "employee")
public class EmployeeItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int ID;
    @Column(name = "name")
    String name;
    @Column(name = "lastname")
    String lastname;
    @Column(name = "mail")
    String mail;
    @Column(name = "cellphone")
    String cellphone;
    @Column(name = "address")
    String address;
    @Column(name = "status")
    boolean status;
    @Column(name = "manager")
    boolean manager;
    @Column(name = "mynumber")
    int mynumber;
    @Column(name = "departamentid")
    int departamentid;
    @Column(name = "projectid")
    int projectid;

    public EmployeeItem() {

    }

    public EmployeeItem(int ID, String name, String lastname, String mail, String cellphone, String address,
            boolean status, boolean manager, int mynumber, int departamentid, int projectid) {
        this.ID = ID;
        this.name = name;
        this.lastname = lastname;
        this.mail = mail;
        this.cellphone = cellphone;
        this.address = address;
        this.status = status;
        this.manager = manager;
        this.mynumber = mynumber;
        this.departamentid = departamentid;
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

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getCellphone() {
        return cellphone;
    }

    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public boolean getManager() {
        return manager;
    }

    public void setManager(boolean manager) {
        this.manager = manager;
    }

    public int getMynumber() {
        return mynumber;
    }

    public void setMynumber(int mynumber) {
        this.mynumber = mynumber;
    }

    public int getDepartamentid() {
        return departamentid;
    }

    public void setDepartamentid(int departamentid) {
        this.departamentid = departamentid;
    }

    public int getProjectid() {
        return projectid;
    }

    public void setProjectid(int projectid) {
        this.projectid = projectid;
    }

    @Override
    public String toString() {
        return "ToDoItem{" +
                "ID=" + ID +
                ", name='" + name +
                ", lastname='" + lastname + '\'' +
                ", mail='" + mail +
                ", cellphone='" + cellphone +
                ", address='" + address +
                ", status=" + status +
                ", manager=" + manager +
                ", mynumber=" + mynumber +
                ", departamentid=" + departamentid +
                ", projectid=" + projectid +
                '}';
    }
}
