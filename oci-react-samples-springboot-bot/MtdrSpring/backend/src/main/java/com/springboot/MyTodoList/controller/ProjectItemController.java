package com.springboot.MyTodoList.controller;

import java.net.http.HttpHeaders;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.MyTodoList.model.ProjectItem;
import com.springboot.MyTodoList.service.ProjectItemService;

@RestController
public class ProjectItemController {
    @Autowired
    private ProjectItemService projectItemService;

    // @CrossOrigin
    @GetMapping(value = "/projectlist")
    public List<ProjectItem> getAllProjectItems() {
        return projectItemService.findAll();
    }

    // @CrossOrigin
    @GetMapping(value = "/projectlist/{id}")
    public ResponseEntity<ProjectItem> getProjectItemById(@PathVariable int id) {
        try {
            ResponseEntity<ProjectItem> responseEntity = projectItemService.getProjectItemById(id);
            return new ResponseEntity<ProjectItem>(responseEntity.getBody(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // @CrossOrigin
    @PostMapping(value = "/projectlist")
    public ResponseEntity addProjectItem(@RequestBody ProjectItem projectItem) throws Exception {
        ProjectItem td = projectItemService.addProjectItem(projectItem);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("location", "" + td.getID());
        responseHeaders.set("Access-Control-Expose-Headers", "location");
        // URI location = URI.create(""+td.getID())

        return ResponseEntity.ok()
                .headers(responseHeaders).build();
    }

    // @CrossOrigin
    @PutMapping(value = "project/{id}")
    public ResponseEntity updateToDoItem(@RequestBody ProjectItem projectItem, @PathVariable int id) {
        try {
            ProjectItem projectItem1 = projectItemService.updateProjectItem(id, projectItem);
            System.out.println(projectItem1.toString());
            return new ResponseEntity<>(projectItem1, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    // @CrossOrigin
    @DeleteMapping(value = "project/{id}")
    public ResponseEntity<Boolean> deleteProjectItem(@PathVariable("id") int id) {
        Boolean flag = false;
        try {
            flag = projectItemService.deleteProjectItem(id);
            return new ResponseEntity<>(flag, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(flag, HttpStatus.NOT_FOUND);
        }
    }

}
