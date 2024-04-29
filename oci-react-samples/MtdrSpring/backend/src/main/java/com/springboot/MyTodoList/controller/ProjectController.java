package com.springboot.MyTodoList.controller;

import com.springboot.MyTodoList.model.ProjectItem;
import com.springboot.MyTodoList.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
public class ProjectController {
    @Autowired
    private ProjectService projectService;

    // @CrossOrigin
    @GetMapping(value = "/todolist")
    public List<ProjectItem> getAllProjectItems() {
        return projectService.findAll();
    }

    // @CrossOrigin
    @GetMapping(value = "/todolist/{id}")
    public ResponseEntity<ProjectItem> getProjectItemById(@PathVariable int id) {
        try {
            ResponseEntity<ProjectItem> responseEntity = projectService.getProjectItemById(id);
            return new ResponseEntity<ProjectItem>(responseEntity.getBody(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // @CrossOrigin
    @PostMapping(value = "/todolist")
    public ResponseEntity addProjectItem(@RequestBody ProjectItem projectItem) throws Exception {
        ProjectItem td = projectService.addProjectItem(projectItem);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("location", "" + td.getID());
        responseHeaders.set("Access-Control-Expose-Headers", "location");
        // URI location = URI.create(""+td.getID())

        return ResponseEntity.ok()
                .headers(responseHeaders).build();
    }

    // @CrossOrigin
    @PutMapping(value = "todolist/{id}")
    public ResponseEntity updateProjectItem(@RequestBody ProjectItem projectItem, @PathVariable int id) {
        try {
            ProjectItem projectItem1 = projectService.updateProjectItem(id, projectItem);
            System.out.println(projectItem.toString());
            return new ResponseEntity<>(projectItem1, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    // @CrossOrigin
    @DeleteMapping(value = "todolist/{id}")
    public ResponseEntity<Boolean> deleteProjectItem(@PathVariable("id") int id) {
        Boolean flag = false;
        try {
            flag = projectService.deleteProjectItem(id);
            return new ResponseEntity<>(flag, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(flag, HttpStatus.NOT_FOUND);
        }
    }

}
