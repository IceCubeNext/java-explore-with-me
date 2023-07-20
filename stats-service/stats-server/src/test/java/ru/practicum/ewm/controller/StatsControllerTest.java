package ru.practicum.ewm.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import ru.practicum.ewm.dto.ResponseParametersDto;
import ru.practicum.ewm.dto.StatRequestDto;
import ru.practicum.ewm.utility.Constants;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@AutoConfigureMockMvc
@RequiredArgsConstructor
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class StatsControllerTest {
    @Autowired
    private MockMvc mockMvc;
    private final ObjectMapper mapper = new ObjectMapper();
    private StatRequestDto requestDto;

    @BeforeEach
    public void setUp() {
        requestDto = new StatRequestDto();
        requestDto.setApp("app");
        requestDto.setUri("event/1");
        requestDto.setIp("192.168.0.1");
        requestDto.setTimestamp(LocalDateTime.now());
        mapper.registerModule(new JavaTimeModule());
    }

    @Test
    public void addNormalHit() throws Exception {
        postHit(requestDto).andExpect(status().isOk());
    }

    @Test
    public void addHitWithoutUri() throws Exception {
        requestDto.setUri(null);
        postHit(requestDto).andExpect(status().isBadRequest());
    }

    @Test
    public void addHitWithoutApp() throws Exception {
        requestDto.setApp(null);
        postHit(requestDto).andExpect(status().isBadRequest());
    }

    @Test
    public void addHitWithoutIp() throws Exception {
        requestDto.setIp(null);
        postHit(requestDto).andExpect(status().isBadRequest());
    }

    @Test
    public void getStat() throws Exception {
        mapper.registerModule(new JavaTimeModule());
        StatRequestDto request1 = new StatRequestDto("app", "event/1", "192.168.0.1", LocalDateTime.now());
        StatRequestDto request2 = new StatRequestDto("app", "event/1", "192.168.0.1", LocalDateTime.now().plusHours(1));
        StatRequestDto request3 = new StatRequestDto("app", "event/1", "192.168.0.1", LocalDateTime.now().plusHours(2));

        postHit(request1).andExpect(status().isOk());
        postHit(request2).andExpect(status().isOk());
        postHit(request3).andExpect(status().isOk());

        getStat(new ResponseParametersDto(
                        LocalDateTime.now().minusHours(1).format(Constants.TIME_FORMATTER),
                        LocalDateTime.now().plusHours(2).format(Constants.TIME_FORMATTER),
                        "event/1",
                        false
                )
        ).andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$.[0].hits", is(3)));
    }

    @Test
    public void getStatUnique() throws Exception {
        mapper.registerModule(new JavaTimeModule());
        StatRequestDto request1 = new StatRequestDto("app", "event/1", "192.168.0.1", LocalDateTime.now());
        StatRequestDto request2 = new StatRequestDto("app", "event/1", "192.168.0.1", LocalDateTime.now().plusHours(1));
        StatRequestDto request3 = new StatRequestDto("app", "event/1", "192.168.0.1", LocalDateTime.now().plusHours(2));

        postHit(request1).andExpect(status().isOk());
        postHit(request2).andExpect(status().isOk());
        postHit(request3).andExpect(status().isOk());

        getStat(new ResponseParametersDto(
                        LocalDateTime.now().minusHours(1).format(Constants.TIME_FORMATTER),
                        LocalDateTime.now().plusHours(2).format(Constants.TIME_FORMATTER),
                        "event/1",
                        true
                )
        ).andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$.[0].hits", is(1)));
    }

    private ResultActions postHit(StatRequestDto requestDto) throws Exception {
        return mockMvc.perform(
                post("/hit")
                        .content(mapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON)
        );
    }

    private ResultActions getStat(ResponseParametersDto parameters) throws Exception {
        StringBuilder path = new StringBuilder();
        path.append("http://localhost:9090/stats?start=")
                .append(parameters.getStart())
                .append("&end=")
                .append(parameters.getEnd());
        if (!parameters.getUris().isEmpty()) {
            path.append("&uris=")
                    .append(String.join(",", parameters.getUris()));
        }
        if (parameters.getUnique()) {
            path.append("&unique=true");
        }
        return mockMvc.perform(
                get(path.toString())
        );
    }
}