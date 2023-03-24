package hexlet.code.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import hexlet.code.config.SpringConfigForIT;
import hexlet.code.dto.TaskStatusDtoRequest;
import hexlet.code.dto.UserDtoRequest;
import hexlet.code.model.TaskStatus;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.transaction.annotation.Transactional;
import hexlet.code.utils.TestUtils;
import java.util.List;

import static hexlet.code.controller.TaskStatusController.ID;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static hexlet.code.controller.UserController.USER_CONTROLLER_PATH;
import static hexlet.code.utils.TestUtils.fromJson;
import static hexlet.code.utils.TestUtils.asJson;
import static hexlet.code.controller.TaskStatusController.TASK_STATUS_CONTROLLER_PATH;
import static org.assertj.core.api.Assertions.assertThat;
import static hexlet.code.config.SpringConfigForIT.TEST_PROFILE;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@AutoConfigureMockMvc
@ActiveProfiles(TEST_PROFILE)
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = RANDOM_PORT, classes = SpringConfigForIT.class)
@Transactional
public class TaskStatusesControllerIT {
    private static final Logger LOGGER = LoggerFactory.getLogger(TaskStatusesControllerIT.class);
    @Autowired
    private TestUtils utils;
    @Autowired
    private TaskStatusRepository taskStatusRepository;
    @Autowired
    private UserRepository userRepository;
    private final UserDtoRequest sampleUser = UserControllerIT.getSampleUser();
    private final TaskStatusDtoRequest sampleTaskStatus = new TaskStatusDtoRequest("Sample task status");
    private final TaskStatusDtoRequest anotherTaskStatus = new TaskStatusDtoRequest("Another task status");

    @BeforeEach
    public void beforeEach() throws Exception {
        utils.regEntity(sampleUser, USER_CONTROLLER_PATH);
    }

    @Test
    @DisplayName(value = "Тест на создание нового статуса")
    public void createTaskStatuses() throws Exception {
        regDefaultStatus()
                .andExpect(status().isCreated());

        assertThat(taskStatusRepository.findAll().size()).isEqualTo(1);
    }

    @Test
    @DisplayName(value = "Тест на попытку создания нового статуса, от незалогиненного юзера")
    public void createTaskStatusesByUnauthenticatedUser() throws Exception {
        try {
            utils.regEntity(anotherTaskStatus, TASK_STATUS_CONTROLLER_PATH);
        } catch (Exception e) {
            assertThat(e.getMessage()).isEqualTo(HttpStatus.UNAUTHORIZED.toString());
        }
    }

    @Test
    @DisplayName(value = "Тест на обновление статуса")
    public void updateTask() throws Exception {
        regDefaultStatus();

        TaskStatus taskStatusToUpdate = taskStatusRepository.findAll().get(0);

        MockHttpServletRequestBuilder request =
                put(TASK_STATUS_CONTROLLER_PATH + ID, taskStatusToUpdate.getId())
                        .content(asJson(anotherTaskStatus))
                        .contentType(MediaType.APPLICATION_JSON);


        utils
                .perform(request, sampleUser.getEmail())
                .andExpect(status().isOk());



        assertThat(taskStatusRepository
                .findById(taskStatusToUpdate.getId()).get().getName())
                .isEqualTo(anotherTaskStatus.getName());
    }

    @Test
    @DisplayName(value = "Тест на получение статуса")
    public void getTaskStatus() throws Exception {
        regDefaultStatus();

        TaskStatus taskStatus = taskStatusRepository.findAll().get(0);

        MockHttpServletRequestBuilder request =
                get(TASK_STATUS_CONTROLLER_PATH + ID, taskStatus.getId());

        MockHttpServletResponse response = utils
                .perform(request, sampleUser.getEmail())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        TaskStatus actualStatus = fromJson(response.getContentAsString(), new TypeReference<TaskStatus>() {
        });

        assertThat(actualStatus.getId()).isEqualTo(taskStatus.getId());
        assertThat(actualStatus.getName()).isEqualTo(taskStatus.getName());
    }

    @Test
    @DisplayName(value = "Тест на получение всех статусов")
    public void getAllTaskStatuses() throws Exception {
        regDefaultStatus();
        utils.regEntity(anotherTaskStatus, sampleUser.getEmail(), TASK_STATUS_CONTROLLER_PATH);

        MockHttpServletResponse response = utils.perform(
                get(TASK_STATUS_CONTROLLER_PATH), sampleUser.getEmail())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        List<TaskStatus> taskStatuses = fromJson(response.getContentAsString(), new TypeReference<List<TaskStatus>>() {
        });

        assertThat(taskStatuses.size()).isEqualTo(taskStatusRepository.findAll().size());
    }

    @Test
    @DisplayName(value = "Тест на удаление статуса")
    public void deleteStatus() throws Exception {
        regDefaultStatus();

        TaskStatus taskStatusToDelete = taskStatusRepository.findAll().get(0);

        MockHttpServletRequestBuilder request =
                delete(TASK_STATUS_CONTROLLER_PATH + ID, taskStatusToDelete.getId());

        utils
                .perform(request, sampleUser.getEmail())
                .andExpect(status().isOk());

        assertThat(taskStatusRepository.findAll().size()).isEqualTo(0);
    }

    private ResultActions regDefaultStatus() throws Exception {
        return utils.regEntity(sampleTaskStatus, sampleUser.getEmail(), TASK_STATUS_CONTROLLER_PATH);
    }
}
