package com.example.scheduler.Demo;

import com.example.scheduler.info.Task;
// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class TaskService {
    
    public List<Task> fetchTasksFromDatabase(){

        List<Task> tasks = new ArrayList<>();

        return tasks;
    }


    public void executeTask(Task task){

    }

    public void updateTaskInDatabase(Task task){

    }

}
