package hexlet.code.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import hexlet.code.config.SpringConfigForIT;
import hexlet.code.dto.TaskStatusDtoRequest;
import hexlet.code.dto.UserDtoRequest;
import hexlet.code.model.TaskStatus;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
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
    @Autowired
    private TestUtils utils;
    @Autowired
    private TaskStatusRepository taskStatusRepository;
    @Autowired
    private UserRepository userRepository;
    private final UserDtoRequest sampleUser = UserControllerIT.getSampleUser();
    private static final TaskStatusDtoRequest SAMPLE_TASK_STATUS = new TaskStatusDtoRequest("Sample task status");
    private static final TaskStatusDtoRequest ANOTHER_TASK_STATUS = new TaskStatusDtoRequest("Another task status");

    public static TaskStatusDtoRequest getSampleTaskStatus() {
        return SAMPLE_TASK_STATUS;
    }

    public static TaskStatusDtoRequest getAnotherTaskStatus() {
        return ANOTHER_TASK_STATUS;
    }

    @BeforeEach
    public void beforeEach() throws Exception {
        utils.setUp();
        regDefaultStatus();
    }

    @Test
    public void createTaskStatus() throws Exception {
        utils.regEntity(ANOTHER_TASK_STATUS, sampleUser.getEmail(), TASK_STATUS_CONTROLLER_PATH)
                .andExpect(status().isCreated());

        assertThat(taskStatusRepository.findAll().size()).isEqualTo(2);
    }

    @Test
    public void createTaskStatusAnUnauthorized() throws Exception {
        utils.regEntity(ANOTHER_TASK_STATUS, TASK_STATUS_CONTROLLER_PATH).andExpect(status().isForbidden());
        assertThat(taskStatusRepository.findAll().size()).isEqualTo(1);
    }

    @Test
    public void updateTask() throws Exception {
        TaskStatus taskStatusToUpdate = taskStatusRepository.findAll().get(0);

        MockHttpServletRequestBuilder request =
                put(TASK_STATUS_CONTROLLER_PATH + ID, taskStatusToUpdate.getId())
                        .content(asJson(ANOTHER_TASK_STATUS))
                        .contentType(MediaType.APPLICATION_JSON);

        utils.perform(request, sampleUser.getEmail()).andExpect(status().isOk());

        assertThat(taskStatusRepository
                .findById(taskStatusToUpdate.getId()).get().getName())
                .isEqualTo(ANOTHER_TASK_STATUS.getName());
    }

    @Test
    public void updateTaskAnUnauthorized() throws Exception {
        TaskStatus taskStatusToUpdate = taskStatusRepository.findAll().get(0);

        MockHttpServletRequestBuilder request =
                put(TASK_STATUS_CONTROLLER_PATH + ID, taskStatusToUpdate.getId())
                        .content(asJson(ANOTHER_TASK_STATUS))
                        .contentType(MediaType.APPLICATION_JSON);

        utils.perform(request).andExpect(status().isForbidden());

        assertThat(taskStatusRepository
                .findById(taskStatusToUpdate.getId()).get().getName())
                .isEqualTo(SAMPLE_TASK_STATUS.getName());
    }

    @Test
    public void getTaskStatus() throws Exception {
        TaskStatus taskStatus = taskStatusRepository.findAll().get(0);

        MockHttpServletRequestBuilder request =
                get(TASK_STATUS_CONTROLLER_PATH + ID, taskStatus.getId());

        MockHttpServletResponse response = utils
                .perform(request, sampleUser.getEmail())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        TaskStatus actualStatus = fromJson(response.getContentAsString(), new TypeReference<>() {
        });

        assertThat(actualStatus.getId()).isEqualTo(taskStatus.getId());
        assertThat(actualStatus.getName()).isEqualTo(taskStatus.getName());
    }

    @Test
    public void getAllTaskStatuses() throws Exception {
        utils.regEntity(ANOTHER_TASK_STATUS, sampleUser.getEmail(), TASK_STATUS_CONTROLLER_PATH);
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
    public void deleteStatus() throws Exception {
        TaskStatus taskStatusToDelete = taskStatusRepository.findAll().get(0);

        MockHttpServletRequestBuilder request =
                delete(TASK_STATUS_CONTROLLER_PATH + ID, taskStatusToDelete.getId());

        utils
                .perform(request, sampleUser.getEmail())
                .andExpect(status().isOk());

        assertThat(taskStatusRepository.findAll().size()).isEqualTo(0);
    }

    @Test
    public void deleteStatusAnUnauthorized() throws Exception {
        TaskStatus taskStatusToDelete = taskStatusRepository.findAll().get(0);

        MockHttpServletRequestBuilder request =
                delete(TASK_STATUS_CONTROLLER_PATH + ID, taskStatusToDelete.getId());

        utils.perform(request).andExpect(status().isForbidden());

        assertThat(taskStatusRepository.findAll().size()).isEqualTo(1);
    }

    private ResultActions regDefaultStatus() throws Exception {
        return utils.regEntity(SAMPLE_TASK_STATUS, sampleUser.getEmail(), TASK_STATUS_CONTROLLER_PATH);
    }
}
