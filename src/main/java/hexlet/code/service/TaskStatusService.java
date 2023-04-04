package hexlet.code.service;

import hexlet.code.dto.TaskStatusDtoRequest;
import hexlet.code.model.TaskStatus;
import hexlet.code.repository.TaskStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * A class that implements the application logic for processing requests from the
 * {@link hexlet.code.controller.TaskStatusController}.
 * Contains a {@link TaskStatusRepository} bean for interacting with the taskStatuses table in the database.
 *
 * @author sobadxx
 * @see hexlet.code.controller.TaskStatusController
 */
@Service
public class TaskStatusService {
    @Autowired
    private TaskStatusRepository taskStatusRepository;

    /**
     * Returns all entities of type {@link TaskStatus} from the database.
     *
     * @return all entities of type {@link TaskStatus}
     */
    public List<TaskStatus> getListOfAllStatuses() {
        return taskStatusRepository.findAll();
    }

    /**
     * Retrieves an entity by its id.
     *
     * @param id entity id
     * @return result of the findById method
     */
    public TaskStatus getTaskStatusById(Long id) {
        return findById(id);
    }

    /**
     * Creates an entity type {@link TaskStatus} and saves it to the database.
     *
     * @param taskStatusDtoRequest
     * {@link TaskStatusDtoRequest} DTO-object with the fields necessary to create a TaskStatus
     * @return created {@link TaskStatus}
     */
    public TaskStatus createTaskStatus(TaskStatusDtoRequest taskStatusDtoRequest) {
        TaskStatus taskStatus = new TaskStatus();
        taskStatus.setName(taskStatusDtoRequest.getName());
        return taskStatusRepository.save(taskStatus);
    }

    /**
     * Updates the {@link TaskStatus} object and stores the updated {@link TaskStatus} in the database.
     *
     * @param taskStatusDtoRequest
     * {@link TaskStatusDtoRequest} DTO-object with the fields necessary to update a {@link TaskStatus}
     * @param id id of the taskStatus whose data is being updated
     * @return updated {@link TaskStatus}
     */
    public TaskStatus updateTaskStatus(TaskStatusDtoRequest taskStatusDtoRequest, Long id) {
        TaskStatus taskStatus = findById(id);
        taskStatus.setName(taskStatusDtoRequest.getName());
        return taskStatusRepository.save(taskStatus);
    }

    /**
     * Delete the {@link TaskStatus} object in the database.
     *
     * @param id id of the {@link TaskStatus} whose is being deleted
     */
    public void deleteTaskStatus(Long id) {
        TaskStatus taskStatus = findById(id);

        taskStatusRepository.delete(taskStatus);
    }

    /**
     * Retrieves an entity by its id.
     *
     * @param id entity id
     * @return the entity with given id
     * @throws NoSuchElementException if Entity with {@literal id} not found
     */
    private TaskStatus findById(Long id) {
        return taskStatusRepository.findById(id)
                .orElseThrow(
                        () -> new NoSuchElementException("TaskStatus with id " + id + " not found"));
    }
}
