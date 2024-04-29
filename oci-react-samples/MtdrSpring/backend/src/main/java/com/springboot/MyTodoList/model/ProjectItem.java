package com.springboot.MyTodoList.model;

import javax.persistence.*;
import java.time.OffsetDateTime;

/*
    representation of the TODOITEM table that exists already
    in the autonomous database
 */
@Entity
@Table(name = "PROYECTO")
public class ProjectItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int ID;
    @Column(name = "nombre")
    String nombre;
    @Column(name = "fechaInicio")
    OffsetDateTime fechaInicio;
    @Column(name = "fechaFin")
    OffsetDateTime fechaFin;
    @Column(name = "estatus")
    boolean estatus;

    @ManyToOne
    @JoinColumn(name = "id")
    private DepartmentItem departmentID;

    public ProjectItem() {

    }

    public ProjectItem(int ID, String nombre, OffsetDateTime fechaInicio, OffsetDateTime fechaFin, boolean estatus,
            DepartmentItem departmentID) {
        this.ID = ID;
        this.nombre = nombre;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.estatus = estatus;
        this.departmentID = departmentID;

    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public OffsetDateTime getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(OffsetDateTime fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public OffsetDateTime getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(OffsetDateTime fechaFin) {
        this.fechaFin = fechaFin;
    }

    public boolean getEstatus() {
        return estatus;
    }

    public void setEstatus(boolean estatus) {
        this.estatus = estatus;
    }

    public DepartmentItem getDepartmentID() {
        return departmentID;
    }

    public void setDepartmentID(DepartmentItem departmentID) {
        this.departmentID = departmentID;
    }

    @Override
    public String toString() {
        return "ToDoItem{" +
                "ID=" + ID +
                ", name='" + nombre + '\'' +
                ", start_ts=" + fechaInicio +
                ", end_ts=" + fechaFin +
                ", done=" + estatus +
                ", departmentID=" + departmentID +
                '}';
    }
}
