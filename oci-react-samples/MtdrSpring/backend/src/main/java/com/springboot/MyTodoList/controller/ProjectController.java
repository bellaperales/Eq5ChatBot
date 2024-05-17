package com.springboot.MyTodoList.controller;

import com.springboot.MyTodoList.model.ProjectItem;
import com.springboot.MyTodoList.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProjectController {
    @Autowired
    private ProjectService projectService;

    @GetMapping(value = "/todolist")
    public List<ProjectItem> getAllProjectItems() {
        return projectService.findAll();
    }

    @GetMapping(value = "/todolist/{id}")
    public ResponseEntity<ProjectItem> getProjectItemById(@PathVariable int id) {
        try {
            ResponseEntity<ProjectItem> responseEntity = projectService.getProjectItemById(id);
            return new ResponseEntity<>(responseEntity.getBody(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(value = "/todolist")
    public ResponseEntity addProjectItem(@RequestBody ProjectItem projectItem) throws Exception {
        ProjectItem td = projectService.addProjectItem(projectItem);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("location", "" + td.getID());
        responseHeaders.set("Access-Control-Expose-Headers", "location");
        return ResponseEntity.ok().headers(responseHeaders).build();
    }

    @PutMapping(value = "todolist/{id}")
    public ResponseEntity updateProjectItem(@RequestBody ProjectItem projectItem, @PathVariable int id) {
        try {
            ProjectItem projectItem1 = projectService.updateProjectItem(id, projectItem);
            return new ResponseEntity<>(projectItem1, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
}
