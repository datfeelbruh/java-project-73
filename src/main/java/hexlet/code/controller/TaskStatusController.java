package hexlet.code.controller;

import hexlet.code.dto.TaskStatusDtoRequest;
import hexlet.code.model.TaskStatus;
import hexlet.code.service.TaskStatusService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

@RestController
@RequestMapping(value = "${base-url}" + "/statuses")
public class TaskStatusController {
    public static final String TASK_STATUS_CONTROLLER_PATH = "/api/statuses";
    public static final String ID = "/{id}";
    @Autowired
    private TaskStatusService taskStatusService;

    @Operation(summary = "Get a list of all task statuses")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", content =
            @Content(schema =
            @Schema(implementation = TaskStatus.class)))
    })
    @GetMapping("")
    public List<TaskStatus> getAllStatuses() {
        return taskStatusService.getListOfAllStatuses();
    }

    @Operation(summary = "Get task status by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200"),
    })
    @GetMapping(ID)
    public TaskStatus getTaskStatusById(@PathVariable Long id) {
        return taskStatusService.getTaskStatusById(id);
    }

    @Operation(summary = "Create a new task status")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Task status created"),
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("")
    public TaskStatus createTaskStatus(@RequestBody TaskStatusDtoRequest taskStatusDtoRequest) {
        return taskStatusService.createTaskStatus(taskStatusDtoRequest);
    }

    @Operation(summary = "Update task status by ID")
    @PutMapping(ID)
    public TaskStatus updateTaskStatusById(
        @RequestBody TaskStatusDtoRequest taskStatusDtoRequest,
        @PathVariable Long id
    ) {
        return taskStatusService.updateTaskStatus(taskStatusDtoRequest, id);
    }

    @Operation(summary = "Delete task status by ID")
    @DeleteMapping(ID)
    public void deleteTaskStatusById(@PathVariable Long id) {
        taskStatusService.deleteTaskStatus(id);
    }
}
