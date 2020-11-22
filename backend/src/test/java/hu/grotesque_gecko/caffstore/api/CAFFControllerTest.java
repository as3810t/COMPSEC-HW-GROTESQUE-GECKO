package hu.grotesque_gecko.caffstore.api;

import org.junit.jupiter.api.Test;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration
public class CAFFControllerTest {

    @Autowired
    MockMvc mockMvc;

    ///////////////
    //Mapping tests
    ///////////////
    @Test
    public void getAllCAFFs_mappingTest() throws Exception {
        this.mockMvc.perform(get("/caff"))
                .andExpect(status().isOk());
    }
}
