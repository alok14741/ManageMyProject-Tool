package com.alok14741.ppmtool.services;

import com.alok14741.ppmtool.domain.Backlog;
import com.alok14741.ppmtool.domain.Project;
import com.alok14741.ppmtool.exceptions.ProjectIdException;
import com.alok14741.ppmtool.repositories.BacklogRepository;
import com.alok14741.ppmtool.repositories.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private BacklogRepository backlogRepository;

    public Project saveOrUpdateProject(Project project) {
        try {
            project.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());
            if(project.getId() == null) {
                Backlog backlog = new Backlog();
                project.setBacklog(backlog);
                backlog.setProject(project);
                backlog.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());
            }
            if(project.getId() != null)
            {
                project.setBacklog(backlogRepository.findByProjectIdentifier(project.getProjectIdentifier().toUpperCase()));
            }
            return projectRepository.save(project);
        } catch( Exception e ) {
            throw new ProjectIdException("Project ID '" + project.getProjectIdentifier().toUpperCase() + "' Already Exists");
        }
    }

    public Project findProjectByIdentifier(String projectId) {
        Project project =  projectRepository.findByProjectIdentifier(projectId.toUpperCase());
        if(project == null) {
            throw new ProjectIdException("Project Id '" + projectId.toUpperCase() + "' does not Exists");
        }
        return project;
    }

    public Iterable<Project> findAllProject() {
        return projectRepository.findAll();
    }

    public void deleteProjectByProjectIdentifier(String projectId) {
        Project project = projectRepository.findByProjectIdentifier(projectId.toUpperCase());
        if(project == null)
        {
            throw new ProjectIdException("Project ID '" + "' Does Not Exist to Delete.");
        }

        projectRepository.delete(project);
    }

}
