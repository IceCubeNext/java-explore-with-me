package ru.practicum.ewm;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.ewm.dto.ResponseParametersDto;
import ru.practicum.ewm.dto.StatRequestDto;

import javax.validation.Valid;
import java.util.Map;

@Component
public class StatClient extends BaseClient {
    public StatClient(@Value("${stat.server.url}") String serverUrl) {
        super(
                new RestTemplateBuilder()
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> addHit(@Valid StatRequestDto statRequest) {
        return post("/hit", statRequest);
    }

    public ResponseEntity<Object> getStat(@Valid ResponseParametersDto statParameters) {
        Map<String, Object> parameters = Map.of(
                "start", statParameters.getStart(),
                "end", statParameters.getEnd(),
                "uris", statParameters.getUris(),
                "unique", statParameters.getUnique()
        );
        return get("/stats?start={start}&end={end}&uris={uris}&unique={unique}", parameters);
    }
}
