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
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.util.NestedServletException;

import java.security.Principal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    Principal principal;

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

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
    
    ///////////////
    //Mapping tests
    ///////////////

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
        /*this.mockMvc.perform(post("/auth/logout")
                .principal(principal)
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .content(new ObjectMapper().writeValueAsString("")))
                .andExpect(status().isOk());*/
        assertThat(true).isFalse();
    }

    @Test
    public void logout_invalidToken_mappingTest() throws Exception {
        /*this.mockMvc.perform(post("/auth/logout")
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .content(new ObjectMapper().writeValueAsString("")))
                .andExpect(status().isOk());*/
        assertThat(true).isFalse();
    }
}
