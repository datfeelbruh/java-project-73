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

/**
 * Rest controller to process a request for an endpoint /statuses.
 * @author sobadxx
 */
@RestController
@RequestMapping(value = "${base-url}" + "/statuses")
public class TaskStatusController {
    public static final String TASK_STATUS_CONTROLLER_PATH = "/api/statuses";
    public static final String ID = "/{id}";
    @Autowired
    private TaskStatusService taskStatusService;

    /**
     * GET request handler on endpoint "api/statuses".
     *
     * @return All entities {@link TaskStatus} from {@link TaskStatusService#getListOfAllStatuses()}
     */
    @Operation(summary = "Get a list of all task statuses")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", content =
            @Content(schema =
            @Schema(implementation = TaskStatus.class)))
    })
    @GetMapping("")
    public List<TaskStatus> getAllStatuses() {
        return taskStatusService.getListOfAllStatuses();
    }

    /**
     * GET request handler on endpoint "api/statuses/{id}".
     *
     * @param id path variable from request
     * @return {@link TaskStatus} from {@link TaskStatusService#getTaskStatusById(Long)}
     */
    @Operation(summary = "Get task status by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200"),
    })
    @GetMapping(ID)
    public TaskStatus getTaskStatusById(@PathVariable Long id) {
        return taskStatusService.getTaskStatusById(id);
    }

    /**
     * POST request handler on endpoint "api/statuses".
     *
     * @param taskStatusDtoRequest JSON request body
     * @return Created {@link TaskStatus} from {@link TaskStatusService#createTaskStatus(TaskStatusDtoRequest)}
     */
    @Operation(summary = "Create a new task status")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Task status created"),
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("")
    public TaskStatus createTaskStatus(@RequestBody TaskStatusDtoRequest taskStatusDtoRequest) {
        return taskStatusService.createTaskStatus(taskStatusDtoRequest);
    }

    /**
     * PUT request handler on endpoint "api/statuses/{id}".
     *
     * @param taskStatusDtoRequest JSON request body
     * @param id path variable from request
     * @return Created {@link TaskStatus} from {@link TaskStatusService#updateTaskStatus(TaskStatusDtoRequest, Long)}
     */
    @Operation(summary = "Update task status by ID")
    @PutMapping(ID)
    public TaskStatus updateTaskStatusById(
        @RequestBody TaskStatusDtoRequest taskStatusDtoRequest,
        @PathVariable Long id
    ) {
        return taskStatusService.updateTaskStatus(taskStatusDtoRequest, id);
    }

    /**
     * DELETE request handler on endpoint "api/statuses/{id}".
     *
     * @param id path variable from request
     * @see TaskStatusService#deleteTaskStatus(Long)
     */
    @Operation(summary = "Delete task status by ID")
    @DeleteMapping(ID)
    public void deleteTaskStatusById(@PathVariable Long id) {
        taskStatusService.deleteTaskStatus(id);
    }
}
