package hu.grotesque_gecko.caffstore.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import hu.grotesque_gecko.caffstore.authentication.exceptions.AuthInvalidCredentialsException;
import hu.grotesque_gecko.caffstore.authentication.services.AuthService;
import hu.grotesque_gecko.caffstore.user.models.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.util.NestedServletException;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ContextConfiguration
public class AuthControllerTest {

    //////////////////
    //End-to-end tests
    //////////////////////////////////////////////////////////////
    //The list of requirements are:
    //Functional requirements:  https://github.com/as3810t/COMPSEC-HW-GROTESQUE-GECKO/wiki/Functional-Requirements-and-Use-Cases
    //Security requirements:    https://github.com/as3810t/COMPSEC-HW-GROTESQUE-GECKO/wiki/Security-Requirements-and-Objectives
    //////////////////////////////////////////////////////////////

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Mock
    private Principal principal;

    private User user;
    private final static String badUsername = "Script Kiddie";
    private final static String badEmail = "doReply@email.com";
    private final static String invalidEmail = "doReply*at*.com";
    private final static String badPassword = "P455w0rd";
    private final static String invalidPassword = "42"; //Every password should be at least 8 characters long

    //TODO: @BeforeAll
    @BeforeEach
    public void init() {
        user = new User();
        user.setUsername("Kukor Ica");
        user.setEmail("ica99@email.com");
        user.setPassword("SuperS3cr3t");
        user.setAdmin(false);

        principal = () -> user.getUsername();

        mockMvc = MockMvcBuilders.standaloneSetup(authController)
                .build();
    }

    /*
    Covers: FU1, FU1.1
    Implemented by: register
     */
    @Test
    void FU1_1_Test() {
        //Users must be able to register to the service with a valid email, globally unique username, and a password.

        /*GIVEN
        - globally unique username
        - valid email
        - valid password
         */
        Map<String, String> params = new HashMap<>();
        params.put("username", user.getUsername());
        params.put("email", user.getEmail());
        params.put("password", user.getPassword());

        /*WHEN
        - request is register
         */
        ResponseEntity responseEntity = testRestTemplate.postForEntity("/auth/register", null, AuthController.class, params);

        /*THEN
        - status code is OK
        TODO: - response LoginDTO is valid
         */
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    /*
    Covers: FU2, FU2.1
    Implemented by: login
    */
    @Test
    void FU2_1_Test() {
        //Users must be able to login to the service with a valid email or username and the matching password


    }

    /*
    Covers: FU2.2
    Implemented by: passwordReset
    */
    @Test
    void FU2_2_Test() {
        //Users must be able to reset their password with their email address


    }

    /*
    Covers: FU5.2
    Implemented by: passwordReset
    */
    @Test
    void FU5_2_Test() {
        //Administrators must be able to reset the password of any user with their email address


    }

    ///////////////
    //Mapping tests
    ///////////////

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    /* login */

    @Test
    public void loginWith_GoodPassword_GoodUsername_mappingTest() throws Exception {
        Mockito.when(authService.authWithUsernameAndPassword(user.getUsername(), user.getPassword())).thenReturn(user);
        Mockito.when(authService.generateToken(user)).thenReturn("mostsecuretoken");

        this.mockMvc.perform(post("/auth/login")
                .param("username", user.getUsername())
                .param("password", user.getPassword())
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .content(new ObjectMapper().writeValueAsString("")))
                .andExpect(status().isOk());
    }

    @Test
    public void loginWith_GoodPassword_GoodEmail_mappingTest() throws Exception {
        Mockito.when(authService.authWithEmailAndPassword(user.getEmail(), user.getPassword())).thenReturn(user);
        Mockito.when(authService.generateToken(user)).thenReturn("mostsecuretoken");

        this.mockMvc.perform(post("/auth/login")
                .param("email", user.getEmail())
                .param("password", user.getPassword())
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .content(new ObjectMapper().writeValueAsString("")))
                .andExpect(status().isOk());
    }

    @Test
    public void loginWith_GoodPassword_BadUsername_mappingTest() throws Exception {
        /*Mockito.when(authService.authWithUsernameAndPassword(user.getUsername(), user.getPassword())).thenReturn(user);
        Mockito.when(authService.generateToken(user)).thenReturn("mostsecuretoken");

        this.mockMvc.perform(post("/auth/login")
                .param("password", user.getPassword())
                .param("username", badUsername)
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .content(new ObjectMapper().writeValueAsString("")))
                .andExpect(status().isOk());*/
        assertThat(true).isFalse();
    }

    @Test
    public void loginWith_GoodPassword_BadEmail_mappingTest() throws Exception {
        /*Mockito.when(authService.authWithEmailAndPassword(user.getEmail(), user.getPassword())).thenReturn(user);
        Mockito.when(authService.generateToken(user)).thenReturn("mostsecuretoken");

        this.mockMvc.perform(post("/auth/login")
                .param("password", user.getPassword())
                .param("email", badEmail)
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .content(new ObjectMapper().writeValueAsString("")))
                .andExpect(status().isOk());*/
        assertThat(true).isFalse();
    }

    @Test
    public void loginWith_GoodPassword_NoUsernameAndNoEmail_mappingTest() throws Exception {
        try {
            this.mockMvc.perform(post("/auth/login")
                    .param("password", user.getPassword())
                    .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                    .content(new ObjectMapper().writeValueAsString("")));
        }
        catch(NestedServletException nse) {
            assertThat(nse.getCause() instanceof AuthInvalidCredentialsException).isTrue();
        }
    }

    @Test
    public void loginWith_BadPassword_GoodUsername_mappingTest() throws Exception {
        /*Mockito.when(authService.authWithUsernameAndPassword(user.getUsername(), user.getPassword())).thenReturn(user);
        Mockito.when(authService.generateToken(user)).thenReturn("mostsecuretoken");

        this.mockMvc.perform(post("/auth/login")
                .param("password", badPassword)
                .param("username", user.getUsername())
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .content(new ObjectMapper().writeValueAsString("")))
                .andExpect(status().isOk());*/
        assertThat(true).isFalse();
    }

    @Test
    public void loginWith_BadPassword_GoodEmail_mappingTest() throws Exception {
        /*Mockito.when(authService.authWithEmailAndPassword(user.getEmail(), user.getPassword())).thenReturn(user);
        Mockito.when(authService.generateToken(user)).thenReturn("mostsecuretoken");

        this.mockMvc.perform(post("/auth/login")
                .param("password", badPassword)
                .param("email", user.getEmail())
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .content(new ObjectMapper().writeValueAsString("")))
                .andExpect(status().isOk());*/
        assertThat(true).isFalse();
    }

    @Test
    public void loginWith_BadPassword_BadUsername_mappingTest() throws Exception {
        /*Mockito.when(authService.authWithUsernameAndPassword(user.getUsername(), user.getPassword())).thenReturn(user);
        Mockito.when(authService.generateToken(user)).thenReturn("mostsecuretoken");

        this.mockMvc.perform(post("/auth/login")
                .param("password", badPassword)
                .param("username", badUsername)
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .content(new ObjectMapper().writeValueAsString("")))
                .andExpect(status().isOk());*/
        assertThat(true).isFalse();
    }

    @Test
    public void loginWith_BadPassword_BadEmail_mappingTest() throws Exception {
        /*Mockito.when(authService.authWithEmailAndPassword(user.getEmail(), user.getPassword())).thenReturn(user);
        Mockito.when(authService.generateToken(user)).thenReturn("mostsecuretoken");

        this.mockMvc.perform(post("/auth/login")
                .param("password", badPassword)
                .param("email", badEmail)
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .content(new ObjectMapper().writeValueAsString("")))
                .andExpect(status().isOk());*/
        assertThat(true).isFalse();
    }

    @Test
    public void loginWith_BadPassword_NoUsernameAndNoEmail_mappingTest() throws Exception {
        try {
            this.mockMvc.perform(post("/auth/login")
                    .param("password", badPassword)
                    .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                    .content(new ObjectMapper().writeValueAsString("")));
        }
        catch(NestedServletException nse) {
            assertThat(nse.getCause() instanceof AuthInvalidCredentialsException).isTrue();
        }
    }

    @Test
    public void loginWith_NoPassword_GoodUsername_mappingTest() throws Exception {
        try {
            this.mockMvc.perform(post("/auth/login")
                    .param("username", user.getUsername())
                    .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                    .content(new ObjectMapper().writeValueAsString("")));
        }
        catch(NestedServletException nse) {
            assertThat(nse.getCause() instanceof MissingServletRequestParameterException).isTrue();
        }
    }

    @Test
    public void loginWith_NoPassword_GoodEmail_mappingTest() throws Exception {
        try {
            this.mockMvc.perform(post("/auth/login")
                    .param("email", user.getEmail())
                    .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                    .content(new ObjectMapper().writeValueAsString("")));
        }
        catch(NestedServletException nse) {
            assertThat(nse.getCause() instanceof MissingServletRequestParameterException).isTrue();
        }
    }

    @Test
    public void loginWith_NoPassword_BadUsername_mappingTest() throws Exception {
        try {
            this.mockMvc.perform(post("/auth/login")
                    .param("username", badUsername)
                    .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                    .content(new ObjectMapper().writeValueAsString("")));
        }
        catch(NestedServletException nse) {
            assertThat(nse.getCause() instanceof MissingServletRequestParameterException).isTrue();
        }
    }

    @Test
    public void loginWith_NoPassword_BadEmail_mappingTest() throws Exception {
        try {
            this.mockMvc.perform(post("/auth/login")
                    .param("email", badEmail)
                    .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                    .content(new ObjectMapper().writeValueAsString("")));
        }
        catch(NestedServletException nse) {
            assertThat(nse.getCause() instanceof MissingServletRequestParameterException).isTrue();
        }
    }

    @Test
    public void loginWith_NoPassword_NoUsernameAndNoEmail_mappingTest() throws Exception {
        try {
            this.mockMvc.perform(post("/auth/login")
                    .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                    .content(new ObjectMapper().writeValueAsString("")));
        }
        catch(NestedServletException nse) {
            assertThat(nse.getCause() instanceof MissingServletRequestParameterException).isTrue();
        }
    }

    /* register */

    @Test
    public void register_GoodPassword_GoodUsername_GoodEmail_mappingTest() throws Exception {
        Mockito.when(authService.register(user.getUsername(), user.getEmail(), user.getPassword())).thenReturn(user);
        Mockito.when(authService.generateToken(user)).thenReturn("mostsecuretoken");

        this.mockMvc.perform(post("/auth/register")
                .param("password", user.getPassword())
                .param("username", user.getUsername())
                .param("email", user.getEmail())
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .content(new ObjectMapper().writeValueAsString("")))
                .andExpect(status().isOk());
    }

    @Test
    public void register_GoodPassword_GoodUsername_InvalidEmail_mappingTest() throws Exception {
        /*Mockito.when(authService.register(user.getUsername(), user.getEmail(), user.getPassword())).thenReturn(user);
        Mockito.when(authService.generateToken(user)).thenReturn("mostsecuretoken");

        this.mockMvc.perform(post("/auth/register")
                .param("password", user.getPassword())
                .param("username", user.getUsername())
                .param("email", user.getEmail())
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .content(new ObjectMapper().writeValueAsString("")))
                .andExpect(status().isOk());*/
        assertThat(true).isFalse();
    }

    @Test
    public void register_GoodPassword_GoodUsername_NoEmail_mappingTest() throws Exception {
        /*Mockito.when(authService.register(user.getUsername(), user.getEmail(), user.getPassword())).thenReturn(user);
        Mockito.when(authService.generateToken(user)).thenReturn("mostsecuretoken");

        this.mockMvc.perform(post("/auth/register")
                .param("password", user.getPassword())
                .param("username", user.getUsername())
                .param("email", "")
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .content(new ObjectMapper().writeValueAsString("")))
                .andExpect(status().isOk());*/
        assertThat(true).isFalse();
    }

    @Test
    public void register_GoodPassword_UsedUsername_GoodEmail_mappingTest() throws Exception {
        /*Mockito.when(authService.register(user.getUsername(), user.getEmail(), user.getPassword())).thenReturn(user);
        Mockito.when(authService.generateToken(user)).thenReturn("mostsecuretoken");

        this.mockMvc.perform(post("/auth/register")
                .param("password", user.getPassword())
                .param("username", user.getUsername())
                .param("email", user.getEmail())
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .content(new ObjectMapper().writeValueAsString("")))
                .andExpect(status().isOk());*/
        assertThat(true).isFalse();
    }

    @Test
    public void register_GoodPassword_UsedUsername_InvalidEmail_mappingTest() throws Exception {
        /*Mockito.when(authService.register(user.getUsername(), user.getEmail(), user.getPassword())).thenReturn(user);
        Mockito.when(authService.generateToken(user)).thenReturn("mostsecuretoken");

        this.mockMvc.perform(post("/auth/register")
                .param("password", user.getPassword())
                .param("username", user.getUsername())
                .param("email", user.getEmail())
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .content(new ObjectMapper().writeValueAsString("")))
                .andExpect(status().isOk());*/
        assertThat(true).isFalse();
    }

    @Test
    public void register_GoodPassword_UsedUsername_NoEmail_mappingTest() throws Exception {
        /*Mockito.when(authService.register(user.getUsername(), user.getEmail(), user.getPassword())).thenReturn(user);
        Mockito.when(authService.generateToken(user)).thenReturn("mostsecuretoken");

        this.mockMvc.perform(post("/auth/register")
                .param("password", user.getPassword())
                .param("username", ???)
                .param("email", "")
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .content(new ObjectMapper().writeValueAsString("")))
                .andExpect(status().isOk());*/
        assertThat(true).isFalse();
    }

    @Test
    public void register_GoodPassword_NoUsername_GoodEmail_mappingTest() throws Exception {
        /*Mockito.when(authService.register(user.getUsername(), user.getEmail(), user.getPassword())).thenReturn(user);
        Mockito.when(authService.generateToken(user)).thenReturn("mostsecuretoken");

        this.mockMvc.perform(post("/auth/register")
                .param("password", user.getPassword())
                .param("username", user.getUsername())
                .param("email", user.getEmail())
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .content(new ObjectMapper().writeValueAsString("")))
                .andExpect(status().isOk());*/
        assertThat(true).isFalse();
    }

    @Test
    public void register_GoodPassword_NoUsername_InvalidEmail_mappingTest() throws Exception {
        /*Mockito.when(authService.register(user.getUsername(), user.getEmail(), user.getPassword())).thenReturn(user);
        Mockito.when(authService.generateToken(user)).thenReturn("mostsecuretoken");

        this.mockMvc.perform(post("/auth/register")
                .param("password", user.getPassword())
                .param("username", user.getUsername())
                .param("email", user.getEmail())
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .content(new ObjectMapper().writeValueAsString("")))
                .andExpect(status().isOk());*/
        assertThat(true).isFalse();
    }

    @Test
    public void register_GoodPassword_NoUsername_NoEmail_mappingTest() throws Exception {
        /*Mockito.when(authService.register(user.getUsername(), user.getEmail(), user.getPassword())).thenReturn(user);
        Mockito.when(authService.generateToken(user)).thenReturn("mostsecuretoken");

        this.mockMvc.perform(post("/auth/register")
                .param("password", user.getPassword())
                .param("username", "")
                .param("email", "")
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .content(new ObjectMapper().writeValueAsString("")))
                .andExpect(status().isOk());*/
        assertThat(true).isFalse();
    }

    @Test
    public void register_InvalidPassword_GoodUsername_GoodEmail_mappingTest() throws Exception {
        /*Mockito.when(authService.register(user.getUsername(), user.getEmail(), user.getPassword())).thenReturn(user);
        Mockito.when(authService.generateToken(user)).thenReturn("mostsecuretoken");

        this.mockMvc.perform(post("/auth/register")
                .param("password", user.getPassword())
                .param("username", user.getUsername())
                .param("email", user.getEmail())
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .content(new ObjectMapper().writeValueAsString("")))
                .andExpect(status().isOk());*/
        assertThat(true).isFalse();
    }

    @Test
    public void register_InvalidPassword_GoodUsername_InvalidEmail_mappingTest() throws Exception {
        /*Mockito.when(authService.register(user.getUsername(), user.getEmail(), user.getPassword())).thenReturn(user);
        Mockito.when(authService.generateToken(user)).thenReturn("mostsecuretoken");

        this.mockMvc.perform(post("/auth/register")
                .param("password", user.getPassword())
                .param("username", user.getUsername())
                .param("email", user.getEmail())
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .content(new ObjectMapper().writeValueAsString("")))
                .andExpect(status().isOk());*/
        assertThat(true).isFalse();
    }

    @Test
    public void register_InvalidPassword_GoodUsername_NoEmail_mappingTest() throws Exception {
        /*Mockito.when(authService.register(user.getUsername(), user.getEmail(), user.getPassword())).thenReturn(user);
        Mockito.when(authService.generateToken(user)).thenReturn("mostsecuretoken");

        this.mockMvc.perform(post("/auth/register")
                .param("password", user.getPassword())
                .param("username", user.getUsername())
                .param("email", "")
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .content(new ObjectMapper().writeValueAsString("")))
                .andExpect(status().isOk());*/
        assertThat(true).isFalse();
    }

    @Test
    public void register_InvalidPassword_UsedUsername_GoodEmail_mappingTest() throws Exception {
        /*Mockito.when(authService.register(user.getUsername(), user.getEmail(), user.getPassword())).thenReturn(user);
        Mockito.when(authService.generateToken(user)).thenReturn("mostsecuretoken");

        this.mockMvc.perform(post("/auth/register")
                .param("password", user.getPassword())
                .param("username", user.getUsername())
                .param("email", user.getEmail())
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .content(new ObjectMapper().writeValueAsString("")))
                .andExpect(status().isOk());*/
        assertThat(true).isFalse();
    }

    @Test
    public void register_Invalidassword_UsedUsername_InvalidEmail_mappingTest() throws Exception {
        /*Mockito.when(authService.register(user.getUsername(), user.getEmail(), user.getPassword())).thenReturn(user);
        Mockito.when(authService.generateToken(user)).thenReturn("mostsecuretoken");

        this.mockMvc.perform(post("/auth/register")
                .param("password", user.getPassword())
                .param("username", user.getUsername())
                .param("email", user.getEmail())
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .content(new ObjectMapper().writeValueAsString("")))
                .andExpect(status().isOk());*/
        assertThat(true).isFalse();
    }

    @Test
    public void register_InvalidPassword_UsedUsername_NoEmail_mappingTest() throws Exception {
        /*Mockito.when(authService.register(user.getUsername(), user.getEmail(), user.getPassword())).thenReturn(user);
        Mockito.when(authService.generateToken(user)).thenReturn("mostsecuretoken");

        this.mockMvc.perform(post("/auth/register")
                .param("password", user.getPassword())
                .param("username", ???)
                .param("email", "")
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .content(new ObjectMapper().writeValueAsString("")))
                .andExpect(status().isOk());*/
        assertThat(true).isFalse();
    }

    @Test
    public void register_InvalidPassword_NoUsername_GoodEmail_mappingTest() throws Exception {
        /*Mockito.when(authService.register(user.getUsername(), user.getEmail(), user.getPassword())).thenReturn(user);
        Mockito.when(authService.generateToken(user)).thenReturn("mostsecuretoken");

        this.mockMvc.perform(post("/auth/register")
                .param("password", user.getPassword())
                .param("username", user.getUsername())
                .param("email", user.getEmail())
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .content(new ObjectMapper().writeValueAsString("")))
                .andExpect(status().isOk());*/
        assertThat(true).isFalse();
    }

    @Test
    public void register_InvalidPassword_NoUsername_InvalidEmail_mappingTest() throws Exception {
        /*Mockito.when(authService.register(user.getUsername(), user.getEmail(), user.getPassword())).thenReturn(user);
        Mockito.when(authService.generateToken(user)).thenReturn("mostsecuretoken");

        this.mockMvc.perform(post("/auth/register")
                .param("password", user.getPassword())
                .param("username", user.getUsername())
                .param("email", user.getEmail())
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .content(new ObjectMapper().writeValueAsString("")))
                .andExpect(status().isOk());*/
        assertThat(true).isFalse();
    }

    @Test
    public void register_InvalidPassword_NoUsername_NoEmail_mappingTest() throws Exception {
        /*Mockito.when(authService.register(user.getUsername(), user.getEmail(), user.getPassword())).thenReturn(user);
        Mockito.when(authService.generateToken(user)).thenReturn("mostsecuretoken");

        this.mockMvc.perform(post("/auth/register")
                .param("password", user.getPassword())
                .param("username", "")
                .param("email", "")
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .content(new ObjectMapper().writeValueAsString("")))
                .andExpect(status().isOk());*/
        assertThat(true).isFalse();
    }

    /* passwordReset */
    //TODO: AuthController "/passwordReset" is called "register" (line 63)?

    @Test
    public void passwordReset_Self_mappingTest() throws Exception {
        this.mockMvc.perform(post("/auth/passwordReset")
                .param("username", user.getUsername())
                .param("email", user.getEmail())
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .content(new ObjectMapper().writeValueAsString("")))
                .andExpect(status().isOk());
    }

    @Test
    public void passwordReset_OtherUser_mappingTest() throws Exception {
        /*this.mockMvc.perform(post("/auth/passwordReset")
                .param("username", "")
                .param("email", "")
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .content(new ObjectMapper().writeValueAsString("")))
                .andExpect(status().isOk());*/
        assertThat(true).isFalse();
    }

    @Test
    public void passwordReset_Admin_mappingTest() throws Exception {
        /*this.mockMvc.perform(post("/auth/passwordReset")
                .param("username", "")
                .param("email", "")
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .content(new ObjectMapper().writeValueAsString("")))
                .andExpect(status().isOk());*/
        assertThat(true).isFalse();
    }

    /* logout */

    @Test
    public void logout_validToken_mappingTest() throws Exception {
        this.mockMvc.perform(post("/auth/logout")
                .principal(principal)
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .content(new ObjectMapper().writeValueAsString("")))
                .andExpect(status().isOk());
    }

    @Test
    public void logout_invalidToken_mappingTest() throws Exception {
        this.mockMvc.perform(post("/auth/logout")
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .content(new ObjectMapper().writeValueAsString("")))
                .andExpect(status().is4xxClientError());
    }
}
