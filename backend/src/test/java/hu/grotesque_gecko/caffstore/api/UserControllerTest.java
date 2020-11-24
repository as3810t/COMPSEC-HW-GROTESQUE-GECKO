package hu.grotesque_gecko.caffstore.api;

import hu.grotesque_gecko.caffstore.user.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.security.Principal;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ContextConfiguration
public class UserControllerTest {

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

    //TODO: @BeforeAll
    @BeforeEach
    public void init() {
        user = new User();
        user.setUsername("Kukor Ica");
        user.setEmail("ica99@email.com");
        user.setPassword("SuperS3cr3t");
        user.setAdmin(false);

        principal = () -> user.getUsername();
    }

    /*
    Covers: FU3, FU3.1, FU3.2, FU3.3
    Implemented by: editOne
     */
    @Test
    void FU3_Test() {
        //Users must be able to modify their profile (username, email, password)


    }

    /*
    Covers: FU4
    Implemented by: getAll
     */
    @Test
    void FU4_Test() {
        //Administrators must be able to list all users of the service


    }

    /*
    Covers: FU4.1
    Implemented by: getMe, getOne
     */
    @Test
    void FU4_1_Test() {
        //Administrators must be able to view the profile of any user


    }

    /*
    Covers: FU5, FU5.1
    Implemented by: editOne
     */
    @Test
    void FU5_1_Test() {
        //Administrators must be able to modify the username of any user


    }

    /*
    Covers: FU5.3
    Implemented by: deleteOne
     */
    @Test
    void FU5_3_Test() {
        // 	Administrators must be able to delete any user


    }
}
