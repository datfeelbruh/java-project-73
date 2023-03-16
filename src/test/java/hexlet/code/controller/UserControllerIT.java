package hexlet.code.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import hexlet.code.config.SpringConfigForIT;
import hexlet.code.dto.AuthDto;
import hexlet.code.dto.UserDtoRq;
import hexlet.code.model.User;
import hexlet.code.repository.UserRepository;
import hexlet.code.security.JwtTokenUtil;
import hexlet.code.utils.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.http.MediaType;
import java.util.List;

import static hexlet.code.config.SpringConfigForIT.TEST_PROFILE;
import static hexlet.code.controller.UserController.ID;
import static hexlet.code.controller.UserController.USER_CONTROLLER_PATH;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static hexlet.code.utils.TestUtils.fromJson;
import static hexlet.code.utils.TestUtils.asJson;


@AutoConfigureMockMvc
@ActiveProfiles(TEST_PROFILE)
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = RANDOM_PORT, classes = SpringConfigForIT.class)
@Transactional
public final class UserControllerIT {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TestUtils utils;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    private static final UserDtoRq SAMPLE_USER = TestUtils.fromJson(
            TestUtils.readFixtureJson("sampleUser.json"),
            new TypeReference<UserDtoRq>() {
            }
    );

    private static final UserDtoRq ANOTHER_USER = TestUtils.fromJson(
            TestUtils.readFixtureJson("anotherUser.json"),
            new TypeReference<UserDtoRq>() {
            }
    );

    private static final AuthDto AUTH_DTO = TestUtils.fromJson(
            TestUtils.readFixtureJson("authDto.json"),
            new TypeReference<AuthDto>() {
            }
    );

    @BeforeEach
    public void beforeEach() throws Exception {
        userRepository.deleteAll();
        utils.regEntity(SAMPLE_USER, USER_CONTROLLER_PATH);
    }

    @Test
    @DisplayName(value = "Тест получения пользователя по id")
    public void getUserById() throws Exception {
        User expectedUser = userRepository.findAll().get(0);

        MockHttpServletResponse response = utils.perform(
                    get(USER_CONTROLLER_PATH + ID, expectedUser.getId())
                ).andExpect(status().isOk())
                .andReturn()
                .getResponse();

        final User actualUser = fromJson(response.getContentAsString(), new TypeReference<User>() {
        });

        assertEquals(expectedUser.getId(), actualUser.getId());
        assertEquals(expectedUser.getEmail(), actualUser.getEmail());
        assertEquals(expectedUser.getFirstName(), actualUser.getFirstName());
        assertEquals(expectedUser.getLastName(), actualUser.getLastName());
    }

    @Test
    @DisplayName(value = "Тест получения всех пользователей")
    public void getAllUsers() throws Exception {
        utils.regEntity(ANOTHER_USER, USER_CONTROLLER_PATH);


        MockHttpServletResponse response = utils.perform(
                    get(USER_CONTROLLER_PATH)
                ).andExpect(status().isOk())
                .andReturn()
                .getResponse();

        final List<User> users = fromJson(response.getContentAsString(), new TypeReference<>() { });

        assertThat(users.size()).isEqualTo(2);

    }

    @Test
    @DisplayName(value = "Тест создания пользователя")
    public void postUser() throws Exception {

        MockHttpServletResponse response = utils
                .regEntity(ANOTHER_USER, USER_CONTROLLER_PATH)
                .andReturn()
                .getResponse();

        assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON.toString());
        assertThat(response.getContentAsString()).contains("petr@yahoo.com");
    }

    @Test
    @DisplayName(value = "Тест изменение данных пользователя")
    public void putUser() throws Exception {
        User userToUpdate = userRepository.findAll().get(0);

        MockHttpServletRequestBuilder request =
                put(USER_CONTROLLER_PATH + ID, userToUpdate.getId())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + utils.getToken(SAMPLE_USER))
                        .content(asJson(ANOTHER_USER))
                        .contentType(MediaType.APPLICATION_JSON);

        utils.perform(request).andExpect(status().isOk());

        assertNull(userRepository.findByEmail(SAMPLE_USER.getEmail()).orElse(null));
        assertNotNull(userRepository.findByEmail(ANOTHER_USER.getEmail()).orElse(null));
    }

    @Test
    @DisplayName(value = "Тест на удаление пользователя")
    public void deleteUser() throws Exception {

        User userToDelete = userRepository.findAll().get(0);

        MockHttpServletRequestBuilder request =
                delete(USER_CONTROLLER_PATH + ID, userToDelete.getId())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + utils.getToken(SAMPLE_USER));

        utils.perform(request).andExpect(status().isOk());

        assertNull(userRepository.findByEmail(userToDelete.getEmail()).orElse(null));
    }

    @Test
    @DisplayName(value = "Тест на успешную аутентификацию пользователя")
    public void authUser() throws Exception {
        MockHttpServletResponse response = utils
                .regEntity(AUTH_DTO, "/api/login")
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(200);
        String token = response.getContentAsString();

        assertThat(jwtTokenUtil.getEmail(token)).isEqualTo(AUTH_DTO.getEmail());
    }
}
