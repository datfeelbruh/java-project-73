package hexlet.code.service;

import hexlet.code.dto.TaskStatusDtoRequest;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.model.TaskStatus;
import hexlet.code.repository.TaskStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TaskStatusService {
    @Autowired
    private TaskStatusRepository taskStatusRepository;

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
        taskStatusRepository.deleteById(id);
    }

    private TaskStatus findById(Long id) {
        return taskStatusRepository.findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException("TaskStatus with id " + id + " not found"));
    }
}
