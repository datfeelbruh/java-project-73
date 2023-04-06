package hexlet.code.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import hexlet.code.config.SpringConfigForIT;
import hexlet.code.dto.LabelDtoRequest;
import hexlet.code.dto.UserDtoRequest;
import hexlet.code.model.Label;
import hexlet.code.repository.LabelRepository;

import static hexlet.code.config.SpringConfigForIT.TEST_PROFILE;
import static hexlet.code.utils.TestUtils.asJson;
import static hexlet.code.utils.TestUtils.fromJson;
import static org.assertj.core.api.Assertions.assertThat;
import hexlet.code.repository.UserRepository;
import hexlet.code.utils.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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

import java.util.List;

import static hexlet.code.controller.UserController.USER_CONTROLLER_PATH;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static hexlet.code.controller.LabelController.LABEL_CONTROLLER_PATH;
import static hexlet.code.controller.LabelController.ID;

@AutoConfigureMockMvc
@ActiveProfiles(TEST_PROFILE)
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = RANDOM_PORT, classes = SpringConfigForIT.class)
@Transactional
public class LabelControllerIT {
    @Autowired
    private TestUtils utils;
    @Autowired
    private LabelRepository labelRepository;
    @Autowired
    private UserRepository userRepository;
    private final UserDtoRequest sampleUser = UserControllerIT.getSampleUser();
    private static final LabelDtoRequest SAMPLE_LABEL = new LabelDtoRequest("Sample label");
    private static final LabelDtoRequest ANOTHER_LABEL = new LabelDtoRequest("Another label");

    public static LabelDtoRequest getSampleLabel() {
        return SAMPLE_LABEL;
    }
    public static LabelDtoRequest getAnotherLabel() {
        return ANOTHER_LABEL;
    }

    @BeforeEach
    public void beforeEach() throws Exception {
        utils.setUp();
        utils.regEntity(sampleUser, USER_CONTROLLER_PATH);
        regDefaultLabel();
    }

    @Test
    public void createLabel() throws Exception {
        utils.regEntity(ANOTHER_LABEL, sampleUser.getEmail(), LABEL_CONTROLLER_PATH)
                        .andExpect(status().isCreated());

        assertThat(labelRepository.findAll().size()).isEqualTo(2);
    }
    @Test
    public void createLabelUnauthorized() throws Exception {
        utils.regEntity(ANOTHER_LABEL, LABEL_CONTROLLER_PATH)
                .andExpect(status().isForbidden());

        assertThat(labelRepository.findAll().size()).isEqualTo(1);
    }

    @Test
    public void getAllLabels() throws Exception {
        utils.regEntity(ANOTHER_LABEL, sampleUser.getEmail(), LABEL_CONTROLLER_PATH);

        MockHttpServletResponse response = utils.perform(
                get(LABEL_CONTROLLER_PATH), sampleUser.getEmail())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        List<Label> labelList = fromJson(response.getContentAsString(), new TypeReference<List<Label>>() {
        });


        assertThat(labelRepository.findAll().size()).isEqualTo(labelList.size());
    }

    @Test
    public void getLabel() throws Exception {
        Label expectedLabel = labelRepository.findAll().get(0);

        MockHttpServletResponse response = utils.perform(
                get(LABEL_CONTROLLER_PATH + ID, expectedLabel.getId()), sampleUser.getEmail())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        Label actualLabel = fromJson(response.getContentAsString(), new TypeReference<Label>() {
        });

        assertThat(expectedLabel.getName()).isEqualTo(actualLabel.getName());
        assertThat(expectedLabel.getCreatedAt()).isEqualTo(actualLabel.getCreatedAt());
        assertThat(expectedLabel.getId()).isEqualTo(actualLabel.getId());
    }

    @Test
    public void getLabelUnauthorized() throws Exception {
        Label expectedLabel = labelRepository.findAll().get(0);

        MockHttpServletResponse response = utils.perform(
                    get(LABEL_CONTROLLER_PATH + ID, expectedLabel.getId()))
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }

    @Test
    public void updateLabel() throws Exception {
        Label expectedLabel = labelRepository.findAll().get(0);

        MockHttpServletRequestBuilder request =
                put(LABEL_CONTROLLER_PATH + ID, expectedLabel.getId())
                        .content(asJson(ANOTHER_LABEL))
                        .contentType(MediaType.APPLICATION_JSON);

        utils.perform(request, sampleUser.getEmail()).andExpect(status().isOk());

        assertThat(labelRepository.findAll().get(0).getName()).isEqualTo(ANOTHER_LABEL.getName());
    }

    @Test
    public void updateLabelUnauthorized() throws Exception {
        Label expectedLabel = labelRepository.findAll().get(0);

        MockHttpServletRequestBuilder request =
                put(LABEL_CONTROLLER_PATH + ID, expectedLabel.getId())
                        .content(asJson(ANOTHER_LABEL))
                        .contentType(MediaType.APPLICATION_JSON);

        utils.perform(request).andExpect(status().isForbidden());

        assertThat(expectedLabel.getName()).isNotEqualTo(ANOTHER_LABEL.getName());
    }

    @Test
    public void deleteLabel() throws Exception {
        Label expectedLabel = labelRepository.findAll().get(0);

        MockHttpServletRequestBuilder request =
                delete(LABEL_CONTROLLER_PATH + ID, expectedLabel.getId());

        utils.perform(request, sampleUser.getEmail()).andExpect(status().isOk());

        assertThat(labelRepository.findAll().size()).isEqualTo(0);
    }

    @Test
    public void deleteLabelUnauthorized() throws Exception {
        Label expectedLabel = labelRepository.findAll().get(0);

        MockHttpServletRequestBuilder request =
                delete(LABEL_CONTROLLER_PATH + ID, expectedLabel.getId());

        utils.perform(request).andExpect(status().isForbidden());

        assertThat(labelRepository.findAll().size()).isEqualTo(1);
    }

    private ResultActions regDefaultLabel() throws Exception {
        return utils.regEntity(SAMPLE_LABEL, sampleUser.getEmail(), LABEL_CONTROLLER_PATH);
    }
}
