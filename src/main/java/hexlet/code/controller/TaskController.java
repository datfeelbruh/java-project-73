package hexlet.code.controller;

import hexlet.code.dto.TaskDtoRequest;
import hexlet.code.model.Task;
import hexlet.code.service.TaskService;
import hexlet.code.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController()
@RequestMapping(value = "${base-url}" + "/tasks")
public class TaskController {
    public static final String TASK_CONTROLLER_PATH = "/api/tasks";
    public static final String ID = "/{id}";
    @Autowired
    private TaskService taskService;
    @Autowired
    private UserService userService;

    @GetMapping(ID)
    public Task getTaskById(@PathVariable Long id) {
        return taskService.getTask(id);
    }
    @GetMapping("")
    public List<Task> getAllTasks() {
        return taskService.getAllTasks();
    }


    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public Task createTask(@RequestBody TaskDtoRequest taskDtoRequest) {
        return taskService.createTask(taskDtoRequest);
    }

    @PutMapping(ID)
    public Task updateTask(@RequestBody TaskDtoRequest taskDtoRequest, @PathVariable Long id) {
        return taskService.updateTask(taskDtoRequest, id);
    }

    @DeleteMapping(ID)
    public void deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
    }
}
