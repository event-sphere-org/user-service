package com.eventsphere.user.service.client;

import com.eventsphere.user.exception.CategoryNotFoundException;
import com.eventsphere.user.model.dto.CategoryDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class CategoryClientService {

    private final WebClient webClient;

    public CategoryClientService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("lb://event-service/v1/categories").build();
    }

    public CategoryDto findCategory(Long id) {
        log.info("Find category by id: {} from event-service", id);

        Mono<CategoryDto> categoryDtoMono = webClient.get()
                .uri("/" + id)
                .retrieve()
                .bodyToMono(CategoryDto.class)
                .onErrorMap(error -> {
                    throw new CategoryNotFoundException(id);
                });

        return categoryDtoMono.block();
    }
}
