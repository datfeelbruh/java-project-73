package hexlet.code.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.config.SpringConfigForIT;
import hexlet.code.dto.LabelDtoRequest;
import hexlet.code.dto.TaskDtoRequest;
import hexlet.code.dto.TaskStatusDtoRequest;
import hexlet.code.dto.UserDtoRequest;
import hexlet.code.model.Label;
import hexlet.code.model.Task;
import hexlet.code.model.TaskStatus;
import hexlet.code.model.User;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.UserRepository;

import static hexlet.code.controller.TaskController.ID;
import static hexlet.code.utils.TestUtils.asJson;
import static hexlet.code.utils.TestUtils.fromJson;
import static org.assertj.core.api.Assertions.assertThat;

import hexlet.code.utils.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;


import static hexlet.code.controller.UserController.USER_CONTROLLER_PATH;
import static hexlet.code.controller.TaskStatusController.TASK_STATUS_CONTROLLER_PATH;
import static hexlet.code.controller.LabelController.LABEL_CONTROLLER_PATH;
import static hexlet.code.controller.TaskController.TASK_CONTROLLER_PATH;

import static hexlet.code.config.SpringConfigForIT.TEST_PROFILE;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ActiveProfiles(TEST_PROFILE)
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = RANDOM_PORT, classes = SpringConfigForIT.class)
@Transactional
public class TaskControllerIT {
    private static final Logger LOGGER = LoggerFactory.getLogger(TaskControllerIT.class);
    private final UserDtoRequest sampleUser = UserControllerIT.getSampleUser();
    private final UserDtoRequest anotherUser = UserControllerIT.getAnotherUser();
    private final TaskStatusDtoRequest firstTaskStatusDto = TaskStatusesControllerIT.getSampleTaskStatus();
    private final TaskStatusDtoRequest secondTaskStatusDto = TaskStatusesControllerIT.getAnotherTaskStatus();
    private final LabelDtoRequest firstLabelDto = LabelControllerIT.getSampleLabel();
    private final LabelDtoRequest secondLabelDto = LabelControllerIT.getAnotherLabel();
    private TaskDtoRequest firstTask;
    private TaskDtoRequest secondTask;
    private TaskDtoRequest thirdTask;
    private TaskStatus secondTaskStatus;
    private Label secondLabel;
    @Autowired
    private TestUtils utils;
    @Autowired
    private LabelRepository labelRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @BeforeEach
    public void beforeEach() throws Exception {
        utils.setUp();

        utils.regEntity(sampleUser, USER_CONTROLLER_PATH).andExpect(status().isCreated());
        utils.regEntity(anotherUser, USER_CONTROLLER_PATH).andExpect(status().isCreated());

        utils.regEntity(
                firstTaskStatusDto,
                sampleUser.getEmail(),
                TASK_STATUS_CONTROLLER_PATH
        );

        utils.regEntity(
                firstLabelDto,
                sampleUser.getEmail(),
                LABEL_CONTROLLER_PATH
        );

        utils.regEntity(
                secondTaskStatusDto,
                anotherUser.getEmail(),
                TASK_STATUS_CONTROLLER_PATH
        );

        utils.regEntity(
                secondLabelDto,
                anotherUser.getEmail(),
                LABEL_CONTROLLER_PATH
        );

        TaskStatus firstTaskStatus = taskStatusRepository.findAll().get(0);
        secondTaskStatus = taskStatusRepository.findAll().get(1);
        User firstExecutor = userRepository.findAll().get(0);
        Label firstLabel = labelRepository.findAll().get(0);
        secondLabel = labelRepository.findAll().get(1);

        firstTask = TaskDtoRequest.builder()
                .name("Sample task name")
                .description("Sample task desc")
                .taskStatusId(firstTaskStatus.getId())
                .executorId(firstExecutor.getId())
                .labels(Set.of(firstLabel.getId()))
                .build();

        secondTask = TaskDtoRequest.builder()
                .name("Another task name")
                .description("Another task desc")
                .taskStatusId(secondTaskStatus.getId())
                .executorId(firstExecutor.getId())
                .labels(Set.of(secondLabel.getId()))
                .build();

        thirdTask = TaskDtoRequest.builder()
                .name("Third task name")
                .description("Another task desc")
                .taskStatusId(secondTaskStatus.getId())
                .executorId(firstExecutor.getId())
                .labels(Set.of(firstLabel.getId()))
                .build();
    }


    @Test
    public void createTask() throws Exception {
        utils.regEntity(firstTask, sampleUser.getEmail(), TASK_CONTROLLER_PATH)
                .andExpect(status().isCreated());

        assertThat(taskRepository.findAll().size()).isEqualTo(1);
    }

    @Test
    public void createTaskAnUnauthorized() throws Exception {
        utils.regEntity(firstTask, TASK_CONTROLLER_PATH).andExpect(status().isForbidden());
        assertThat(taskRepository.findAll().size()).isEqualTo(0);
    }

    @Test
    public void getTasksWithoutFilter() throws Exception {
        regDefaultTask();
        Task expectedTask = taskRepository.findAll().get(0);
        LOGGER.info("{}", expectedTask);

        MockHttpServletRequestBuilder request =
                get(TASK_CONTROLLER_PATH + ID, expectedTask.getId());

        MockHttpServletResponse response = utils.perform(request, sampleUser.getEmail())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        Task actualTask = fromJson(response.getContentAsString(), new TypeReference<>() {
        });

        assertThat(expectedTask).isEqualTo(actualTask);
    }

    @Test
    public void getListTasks() throws Exception {
        regDefaultTask();
        utils.regEntity(secondTask, sampleUser.getEmail(), TASK_CONTROLLER_PATH);
        utils.regEntity(thirdTask, sampleUser.getEmail(), TASK_CONTROLLER_PATH);

        ObjectMapper objectMapper = new ObjectMapper();

        MockHttpServletResponse response1 = utils.perform(
                get("/api/tasks"), sampleUser.getEmail()
        ).andReturn().getResponse();

        String body1 = response1.getContentAsString();

        assertThat(response1.getStatus()).isEqualTo(200);

        assertDoesNotThrow(() -> objectMapper.readValue(body1, List.class));
        assertThat(body1).contains(firstTask.getName());
        assertThat(body1).contains(secondTask.getName());
        assertThat(body1).contains(thirdTask.getName());

        long taskStatusId = secondTaskStatus.getId();

        MockHttpServletResponse response2 = utils.perform(
                get("/api/tasks?taskStatus=" + taskStatusId),
                sampleUser.getEmail()
        ).andReturn().getResponse();

        String body2 = response2.getContentAsString();

        assertThat(response2 .getStatus()).isEqualTo(200);
        assertDoesNotThrow(() -> objectMapper.readValue(body2, List.class));
        assertThat(body2).doesNotContain(firstTask.getName());
        assertThat(body2).contains(secondTask.getName());
        assertThat(body2).contains(thirdTask.getName());

        long labelId = secondLabel.getId();

        MockHttpServletResponse response3 = utils.perform(
                get("/api/tasks?taskStatus=" + taskStatusId
                        + "&labels=" + labelId), sampleUser.getEmail()
        ).andReturn().getResponse();

        String body3 = response3.getContentAsString();

        assertThat(response3.getStatus()).isEqualTo(200);
        assertDoesNotThrow(() -> objectMapper.readValue(body1, List.class));
        assertThat(body3).doesNotContain(firstTask.getName());
        assertThat(body3).contains(secondTask.getName());
    }


    @Test
    public void getTaskAnUnauthorized() throws Exception {
        regDefaultTask();
        Task expectedTask = taskRepository.findAll().get(0);
        LOGGER.info("{}", expectedTask);

        MockHttpServletRequestBuilder request =
                get(TASK_CONTROLLER_PATH + ID, expectedTask.getId());

        utils.perform(request)
                .andExpect(status().isForbidden());
    }

    @Test
    public void updateTask() throws Exception {
        regDefaultTask();
        Task taskToUpdate = taskRepository.findAll().get(0);
        TaskDtoRequest anotherTask = new TaskDtoRequest();
        anotherTask.setName("Another task name");
        anotherTask.setDescription("Another task desc");
        anotherTask.setTaskStatusId(taskStatusRepository.findAll().get(0).getId());
        anotherTask.setExecutorId(userRepository.findAll().get(0).getId());

        MockHttpServletRequestBuilder request =
                put(TASK_CONTROLLER_PATH + ID, taskToUpdate.getId())
                        .content(asJson(anotherTask))
                        .contentType(MediaType.APPLICATION_JSON);

        utils.perform(request, taskToUpdate.getAuthor().getEmail())
                .andExpect(status().isOk());

        assertThat(taskRepository.findAll().get(0).getId()).isEqualTo(taskToUpdate.getId());
        assertThat(taskRepository.findAll().get(0).getName()).isEqualTo("Another task name");
    }

    @Test
    public void updateTaskAnUnauthorized() throws Exception {
        regDefaultTask();
        Task taskToUpdate = taskRepository.findAll().get(0);

        TaskDtoRequest anotherTask = new TaskDtoRequest();
        anotherTask.setName("Another task name");
        anotherTask.setDescription("Another task desc");
        anotherTask.setTaskStatusId(taskStatusRepository.findAll().get(0).getId());
        anotherTask.setExecutorId(userRepository.findAll().get(0).getId());

        MockHttpServletRequestBuilder request =
                put(TASK_CONTROLLER_PATH + ID, taskToUpdate.getId())
                        .content(asJson(anotherTask))
                        .contentType(MediaType.APPLICATION_JSON);

        utils.perform(request)
                .andExpect(status().isForbidden());

        assertThat(taskRepository.findAll().get(0).getName()).isNotEqualTo("Another task name");
    }

    @Test
    public void updateTaskAnAnotherUser() throws Exception {
        regDefaultTask();
        Task taskToUpdate = taskRepository.findAll().get(0);

        TaskDtoRequest anotherTask = new TaskDtoRequest();
        anotherTask.setName("Another task name");
        anotherTask.setDescription("Another task desc");
        anotherTask.setTaskStatusId(taskStatusRepository.findAll().get(0).getId());
        anotherTask.setExecutorId(userRepository.findAll().get(0).getId());

        MockHttpServletRequestBuilder request =
                put(TASK_CONTROLLER_PATH + ID, taskToUpdate.getId())
                        .content(asJson(anotherTask))
                        .contentType(MediaType.APPLICATION_JSON);

        utils.perform(request, anotherUser.getEmail())
                .andExpect(status().isForbidden());

        assertThat(taskRepository.findAll().get(0).getName()).isNotEqualTo("Another task name");
    }


    @Test
    public void deleteTask() throws Exception {
        regDefaultTask();

        Task taskToDelete = taskRepository.findAll().get(0);

        MockHttpServletRequestBuilder request =
                delete(TASK_CONTROLLER_PATH + ID, taskToDelete.getId());

        utils.perform(request, taskToDelete.getAuthor().getEmail())
                .andExpect(status().isOk());

        assertNull(taskRepository.findById(taskToDelete.getId()).orElse(null));
    }

    @Test
    public void deleteTaskAnUnauthorized() throws Exception {
        regDefaultTask();

        Task taskToDelete = taskRepository.findAll().get(0);

        MockHttpServletRequestBuilder request =
                delete(TASK_CONTROLLER_PATH + ID, taskToDelete.getId());

        utils.perform(request)
                .andExpect(status().isForbidden());

        assertNotNull(taskRepository.findById(taskToDelete.getId()).orElse(null));
    }

    @Test
    public void deleteTaskAnAnotherUser() throws Exception {
        regDefaultTask();

        Task taskToDelete = taskRepository.findAll().get(0);

        MockHttpServletRequestBuilder request =
                delete(TASK_CONTROLLER_PATH + ID, taskToDelete.getId());


        utils.perform(request, anotherUser.getEmail()).andExpect(status().isForbidden());
        assertNotNull(taskRepository.findById(taskToDelete.getId()).orElse(null));
    }

    @Test
    public void deleteStatusWithBindToTask() throws Exception {
        regDefaultTask();

        TaskStatus taskStatusToDelete = taskStatusRepository.findAll().get(0);

        MockHttpServletRequestBuilder request =
                delete(TASK_STATUS_CONTROLLER_PATH + TaskStatusController.ID, taskStatusToDelete.getId());

        utils
                .perform(request, sampleUser.getEmail())
                .andExpect(status().isUnprocessableEntity());

        assertThat(taskStatusRepository.findAll().size()).isEqualTo(2);
    }

    private void regDefaultTask() throws Exception {
        utils.regEntity(firstTask, sampleUser.getEmail(), TASK_CONTROLLER_PATH);
    }

}
