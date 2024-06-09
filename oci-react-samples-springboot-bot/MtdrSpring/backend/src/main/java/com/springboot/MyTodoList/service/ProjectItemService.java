package com.springboot.MyTodoList.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.springboot.MyTodoList.model.ProjectItem;
import com.springboot.MyTodoList.repository.ProjectItemRepository;

@Service
public class ProjectItemService {

    @Autowired
    private ProjectItemRepository projectItemRepository;

    public List<ProjectItem> findAll() {
        List<ProjectItem> projectItems = projectItemRepository.findAll();
        return projectItems;
    }

    public ResponseEntity<ProjectItem> getProjectItemById(int id) {
        Optional<ProjectItem> projectData = projectItemRepository.findById(id);
        if (projectData.isPresent()) {
            return new ResponseEntity<>(projectData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    public ProjectItem addProjectItem(ProjectItem projectItem) {
        return projectItemRepository.save(projectItem);
    }

    public boolean deleteProjectItem(int id) {
        try {
            projectItemRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public ProjectItem updateProjectItem(int id, ProjectItem td) {
        Optional<ProjectItem> projectItemData = projectItemRepository.findById(id);
        if (projectItemData.isPresent()) {
            ProjectItem projectItem = projectItemData.get();
            projectItem.setID(id);
            projectItem.setName(td.getName());
            projectItem.setDateStart(td.getDateStart());
            projectItem.setDateEnd(td.getDateEnd());
            projectItem.setStatus(td.getStatus());
            projectItem.setDepartamentID(td.getDepartamentID());

            return projectItemRepository.save(projectItem);
        } else {
            return null;
        }
    }

}
