package hexlet.code.service;

import hexlet.code.dto.TaskStatusDtoRequest;
import hexlet.code.exception.CustomConstraintException;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.model.Task;
import hexlet.code.model.TaskStatus;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.TaskStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskStatusService {
    @Autowired
    private TaskStatusRepository taskStatusRepository;
    @Autowired
    private TaskRepository taskRepository;

    public List<TaskStatus> getListOfAllStatuses() {
        return taskStatusRepository.findAll();
    }

    public TaskStatus getTaskStatusById(Long id) {
        return findById(id);
    }

    public TaskStatus createTaskStatus(TaskStatusDtoRequest taskStatusDtoRequest) {
        TaskStatus taskStatus = new TaskStatus();
        taskStatus.setName(taskStatusDtoRequest.getName());
        return taskStatusRepository.save(taskStatus);
    }

    public TaskStatus updateTaskStatus(TaskStatusDtoRequest taskStatusDtoRequest, Long id) {
        TaskStatus taskStatus = findById(id);
        taskStatus.setName(taskStatusDtoRequest.getName());
        return taskStatusRepository.save(taskStatus);
    }

    public void deleteTaskStatus(Long id) {
        TaskStatus taskStatus = findById(id);

        if (checkTaskStatusAndTaskAssociations(taskStatus.getId())) {
            throw new CustomConstraintException("Unable to delete the task status associated with the any task");
        }

        taskStatusRepository.delete(taskStatus);
    }

    private TaskStatus findById(Long id) {
        return taskStatusRepository.findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException("TaskStatus with id " + id + " not found"));
    }

    private boolean checkTaskStatusAndTaskAssociations(Long taskStatusId) {
        long countTaskStatusSameId = taskRepository.findAll()
                .stream()
                .map(Task::getTaskStatus)
                .filter(e -> e.getId() == taskStatusId)
                .count();


        return countTaskStatusSameId != 0;
    }
}
