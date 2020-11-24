package hu.grotesque_gecko.caffstore.api;

import hu.grotesque_gecko.caffstore.authentication.services.AuthService;
import hu.grotesque_gecko.caffstore.caff.services.CAFFService;
import hu.grotesque_gecko.caffstore.caff.services.CommentService;
import hu.grotesque_gecko.caffstore.user.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.security.Principal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration
public class CAFFControllerTest {

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

        /*mockMvc = MockMvcBuilders.standaloneSetup(authController)
                .build();*/
    }

    /*
    Covers: FCa1
    Implemented by: getAll
     */
    @Test
    void FCa1_Test() {
        //Users and Administrators must be able to view the list of CAFFs

    }

    /*
    Covers: FCa1.1, FCa1.1.1, FCa1.1.2
    Implemented by: getAll
     */
    @Test
    void FCa1_1_Test() {
        //Users and Administrators must be able to search the list of CAFFs (by their tags and by their names)


    }

    /*
    Covers: FCa2
    Implemented by: getOne
     */
    @Test
    void FCa2_Test() {
        //Users and Administrators must be able to view individual CAFFs


    }

    /*
    Covers: FCa2.1
    Implemented by: preview
     */
    @Test
    void FCa2_1_Test() {
        //Users and Administrators must be able to see a preview of the CAFFs


    }

    /*
    Covers: FCa3
    Implemented by: createOne
     */
    @Test
    void FCa3_Test() {
        //Users and Administrators must be able to upload CAFFs


    }

    /*
    Covers: FCa3_1, FCa3_1_1, FCa3_1_2
    Implemented by: editOne
     */
    @Test
    void FCa3_1_Test() {
        //The owner of the CAFF must be able to modify the CAFF's metadata (name, tags)


    }

    /*
    Covers: FCa3_1_3
    Implemented by: deleteOne
     */
    @Test
    void FCa3_1_3_Test() {
        //The owner of the CAFF must be able to delete the CAFF


    }

    /*
    Covers: FCa3_1_4
    Implemented by: editOne
     */
    @Test
    void FCa3_1_4_Test() {
        //Users that are not the owner of the CAFF and not Administrators must not be able to modify the CAFFs metadata


    }

    /*
    Covers: FCa3_2, FCa3_2_1, FCa3_2_2
    Implemented by: editOne
     */
    @Test
    void FCa3_2_Test() {
        //Administrators must be able to modify any CAFF's metadata (name, tag)


    }

    /*
    Covers: FCa3_2_3
    Implemented by: deleteOne
     */
    @Test
    void FCa3_2_3_Test() {
        //Administrators must be able to delete any CAFF


    }

    /*
    Covers: FCa4
    Implemented by: download
     */
    @Test
    void FCa4_Test() {
        //Users and Administrators must be able to download a CAFF


    }

    /*
    Covers: FCo1
    Implemented by: createOneComment
     */
    @Test
    void FCo1_Test() {
        //Users must be able to comment on CAFFs


    }

    /*
    Covers: FCo1_1
    Implemented by: editOneComment
     */
    @Test
    void FCo1_1_Test() {
        //Users must be able to edit their comments


    }

    /*
    Covers: FCa1_2
    Implemented by: deleteOneComment
     */
    @Test
    void FCa1_2_Test() {
        //Users must be able to delete their comments


    }

    /*
    Covers: FCo1_3
    Implemented by: editOneComment
     */
    @Test
    void FCo1_3_Test() {
        //Users must not be able to edit other users' comments


    }

    /*
    Covers: FCo1_4
    Implemented by: deleteOneComment
     */
    @Test
    void FCo1_4_Test() {
        //Users must not be able to delete other users' comments


    }

    /*
    Covers: FCo2
    Implemented by: createOneComment
     */
    @Test
    void FCo2_Test() {
        //Administrators must be able to comment on CAFFs


    }

    /*
    Covers: FCo2_1
    Implemented by: editOneComment
     */
    @Test
    void FCo2_1_Test() {
        //Administrators must be able to edit any comment


    }

    /*
    Covers: FCo2_2
    Implemented by: deleteOneComment
     */
    @Test
    void FCo2_2_Test() {
        //Administrators must be able to delete any comment


    }
    ///////////////
    //Mapping tests
    ///////////////

    @Autowired
    MockMvc mockMvc;

    @Mock
    private CAFFService caffService;
    @Mock
    private CommentService commentService;

    @InjectMocks
    private CAFFController caffController;

    /* getAll */

    @Test
    public void getAll_validToken_mappingTest() throws Exception {
        //Mockito.when(caffService.getAll(user, 0, 100, "", "", "")).thenReturn()

        this.mockMvc.perform(get("/caff"))
                .andExpect(status().isOk());
    }

    @Test
    public void getAll_invalidToken_mappingTest() throws Exception {
        /*this.mockMvc.perform(get("/caff"))
                .andExpect(status().isOk());*/
        assertThat(true).isFalse();
    }

    /* createOne */

    @Test
    public void createOne_validToken_mappingTest() throws Exception {
        /*this.mockMvc.perform(post("/caff"))
                .andExpect(status().isOk());*/
        assertThat(true).isFalse();
    }

    @Test
    public void createOne_invalidToken_mappingTest() throws Exception {
        /*this.mockMvc.perform(post("/caff"))
                .andExpect(status().isOk());*/
        assertThat(true).isFalse();
    }

    /* getOne */

    @Test
    public void getOne_validToken_mappingTest() throws Exception {
        /*this.mockMvc.perform(post("/caff/0"))
                .andExpect(status().isOk());*/
        assertThat(true).isFalse();
    }

    @Test
    public void getOne_invalidToken_mappingTest() throws Exception {
        /*this.mockMvc.perform(post("/caff/0"))
                .andExpect(status().isOk());*/
        assertThat(true).isFalse();
    }
}
