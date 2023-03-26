package hexlet.code.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import hexlet.code.config.SpringConfigForIT;
import hexlet.code.dto.LabelDtoRequest;
import hexlet.code.dto.TaskDtoRequest;
import hexlet.code.dto.TaskStatusDtoRequest;
import hexlet.code.dto.UserDtoRequest;
import hexlet.code.model.Task;
import hexlet.code.model.TaskStatus;
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

import java.util.Set;

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
    private final TaskStatusDtoRequest sampleTaskStatus = TaskStatusesControllerIT.getSampleTaskStatus();
    private final LabelDtoRequest sampleLabel = LabelControllerIT.getSampleLabel();
    private TaskDtoRequest sampleTask;
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
                sampleTaskStatus,
                sampleUser.getEmail(),
                TASK_STATUS_CONTROLLER_PATH
        );
        utils.regEntity(
                sampleLabel,
                sampleUser.getEmail(),
                LABEL_CONTROLLER_PATH
        );

        sampleTask = TaskDtoRequest.builder()
                .name("Sample task name")
                .description("Sample task desc")
                .taskStatusId(taskStatusRepository.findAll().get(0).getId())
                .executorId(userRepository.findAll().get(0).getId())
                .labelsIds(Set.of(labelRepository.findAll().get(0).getId()))
                .build();
    }


    @Test
    public void createTask() throws Exception {
        utils.regEntity(sampleTask, sampleUser.getEmail(), TASK_CONTROLLER_PATH)
                .andExpect(status().isCreated());

        assertThat(taskRepository.findAll().size()).isEqualTo(1);
    }

    @Test
    public void createTaskAnUnauthorized() throws Exception {
        utils.regEntity(sampleTask, TASK_CONTROLLER_PATH).andExpect(status().isForbidden());
        assertThat(taskRepository.findAll().size()).isEqualTo(0);
    }

    @Test
    public void getTask() throws Exception {
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
        LOGGER.info("{}", taskToUpdate);
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

        assertThat(taskStatusRepository.findAll().size()).isEqualTo(1);
    }

    private void regDefaultTask() throws Exception {
        utils.regEntity(sampleTask, sampleUser.getEmail(), TASK_CONTROLLER_PATH);
    }

}
