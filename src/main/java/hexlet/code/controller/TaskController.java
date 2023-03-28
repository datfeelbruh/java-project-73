package hexlet.code.controller;

import com.querydsl.core.types.Predicate;
import hexlet.code.dto.TaskDtoRequest;
import hexlet.code.model.Task;
import hexlet.code.service.TaskService;
import hexlet.code.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
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

@RestController
@RequestMapping(value = "${base-url}" + "/tasks")
public class TaskController {
    public static final String TASK_CONTROLLER_PATH = "/api/tasks";
    public static final String ID = "/{id}";
    @Autowired
    private TaskService taskService;
    @Autowired
    private UserService userService;

    @Operation(summary = "Get task by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Task with that ID was found"),
    })
    @GetMapping(ID)
    public Task getTaskById(@PathVariable Long id) {
        return taskService.getTask(id);
    }

    @Operation(summary = "Get a list of all tasks")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", content =
            @Content(schema =
            @Schema(implementation = Task.class)))
    })
    @GetMapping("")
    public Iterable<Task> getFilteredTasks(@QuerydslPredicate(root = Task.class) Predicate predicate) {
        return taskService.getFilteredTasks(predicate);
    }

    @Operation(summary = "Create a new task")
    @ApiResponse(responseCode = "201", description = "Task created")
    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public Task createTask(@RequestBody TaskDtoRequest taskDtoRequest) {
        return taskService.createTask(taskDtoRequest);
    }

    @Operation(summary = "Update task by ID")
    @PutMapping(ID)
    public Task updateTask(@RequestBody TaskDtoRequest taskDtoRequest, @PathVariable Long id) {
        return taskService.updateTask(taskDtoRequest, id);
    }

    @Operation(summary = "Delete a task")
    @DeleteMapping(ID)
    public void deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
    }
}
