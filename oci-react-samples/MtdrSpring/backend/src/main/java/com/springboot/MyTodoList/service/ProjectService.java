package com.springboot.MyTodoList.service;

import com.springboot.MyTodoList.model.ProjectItem;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProjectService {

    public List<ProjectItem> findAll() {
        return new ArrayList<>();
    }

    public ResponseEntity<ProjectItem> getProjectItemById(int id) {
        ProjectItem projectItem = new ProjectItem();
        projectItem.setID(id);
        return ResponseEntity.ok(projectItem);
    }

    public ProjectItem addProjectItem(ProjectItem projectItem) {
        return projectItem;
    }

    public ProjectItem updateProjectItem(int id, ProjectItem projectItem) {
        projectItem.setID(id);
        return projectItem;
    }
}
