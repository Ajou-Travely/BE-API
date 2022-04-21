package com.ajou.travely.controller.travel;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.assertj.core.api.Assertions.assertThat;

import com.ajou.travely.controller.travel.dto.TravelSaveRequestDto;
import com.ajou.travely.domain.Travel;
import com.ajou.travely.repository.TravelRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class TravelApiControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private TravelRepository travelRepository;

//    @Autowired
//    private WebApplicationContext context;
//
//    private MockMvc mvc;
//
//    @BeforeEach
//    public void setup() {
//        mvc = MockMvcBuilders
//            .webAppContextSetup(context)
//            .apply(springSecurity())
//            .build();
//    }

    @AfterEach
    public void tearDown() throws Exception{
        travelRepository.deleteAll();
    }

    @DisplayName("travel 생성")
//    @WithMockUser(roles="USER")
    @Test
    public void CreateTravel() throws Exception {
        // given
        String title = "title";
        LocalDate now = LocalDate.now();
        TravelSaveRequestDto requestDto = TravelSaveRequestDto.builder()
            .title(title)
            .memo("memo")
            .startDate(LocalDate.now())
            .endDate(LocalDate.now())
            .build();

        String url = "http://localhost:" + port + "/api/v1/travel";

        // when
        ResponseEntity<Long> responseEntity = restTemplate.postForEntity(url, requestDto, Long.class);
//        mvc.perform(post(url)
//            .contentType(MediaType.APPLICATION_JSON_UTF8)
//            .content(new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(requestDto))
//        ).andExpect(status().isOk());

        // then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isGreaterThan(0L);
        List<Travel> all = travelRepository.findAll();
        assertThat(all.get(0).getTitle()).isEqualTo(title);
        assertThat(all.get(0).getStartDate()).isEqualTo(now);
        assertThat(all.get(0).getEndDate()).isEqualTo(now);

    }
}
