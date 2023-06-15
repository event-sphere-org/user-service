package com.eventsphere.user.service.client;

import com.eventsphere.user.exception.EventNotFoundException;
import com.eventsphere.user.model.dto.EventDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class EventClientService {

    private final WebClient webClient;

    public EventClientService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("lb://event-service/v1/events").build();
    }

    public EventDto findEvent(Long id) {
        log.info("Find event by id: {} from event-service", id);

        Mono<EventDto> eventDtoMono = webClient.get()
                .uri("/" + id)
                .retrieve()
                .bodyToMono(EventDto.class)
                .onErrorMap(error -> {
                    throw new EventNotFoundException(id);
                });

        return eventDtoMono.block();
    }
}
