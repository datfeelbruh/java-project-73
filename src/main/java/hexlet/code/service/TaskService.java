package hexlet.code.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.querydsl.core.types.Predicate;
import hexlet.code.dto.TaskDtoRequest;
import hexlet.code.model.Label;
import hexlet.code.model.Task;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TaskService {
    @Autowired
    private ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TaskStatusService taskStatusService;
    @Autowired
    private LabelService labelService;

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Task getTask(Long id) {
        return findById(id);
    }

    public Iterable<Task> getFilteredTasks(Predicate predicate) {
        return predicate == null ? taskRepository.findAll() : taskRepository.findAll(predicate);
    }

    public Task createTask(TaskDtoRequest taskDtoRequest) {
        Task task = new Task();

        task.setName(taskDtoRequest.getName());
        task.setDescription(taskDtoRequest.getDescription());
        task.setTaskStatus(taskStatusService.getTaskStatusById(taskDtoRequest.getTaskStatusId()));
        task.setAuthor(userService.getCurrentUser());
        task.setExecutor(userService.getUserById(taskDtoRequest.getExecutorId()));
        if (taskDtoRequest.getLabelIds() != null) {
            task.setLabels(getLabelsByIds(taskDtoRequest.getLabelIds()));
        }
        return taskRepository.save(task);
    }

    public Task updateTask(TaskDtoRequest taskDtoRequest, Long id) {
        Task taskToUpdate = findById(id);
        if (taskToUpdate.getAuthor().getId() != userService.getCurrentUser().getId()) {
            throw new AccessDeniedException("You cannot edit other users tasks");
        }
        taskToUpdate.setName(taskDtoRequest.getName());
        taskToUpdate.setDescription(taskDtoRequest.getDescription());
        taskToUpdate.setExecutor(userService.getUserById(taskDtoRequest.getExecutorId()));
        taskToUpdate.setTaskStatus(taskStatusService.getTaskStatusById(taskDtoRequest.getTaskStatusId()));
        if (taskDtoRequest.getLabelIds() != null) {
            taskToUpdate.setLabels(getLabelsByIds(taskDtoRequest.getLabelIds()));
        }
        return taskRepository.save(taskToUpdate);
    }

    public void deleteTask(Long id) {
        Task taskToDelete = findById(id);
        if (taskToDelete.getAuthor().getId() != userService.getCurrentUser().getId()) {
            throw new AccessDeniedException("You cannot delete other users tasks");
        }
        taskRepository.delete(taskToDelete);
    }

    private Task findById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(
                        () -> new NoSuchElementException("Task with " + id + " not found")
                );
    }

    private Set<Label> getLabelsByIds(Set<Long> labelsIds) {
        return labelsIds.stream()
                .map(id -> labelService.getLabel(id))
                .collect(Collectors.toSet());
    }
}
