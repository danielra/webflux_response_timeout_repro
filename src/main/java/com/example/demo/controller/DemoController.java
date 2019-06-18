package com.example.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping(value = "/")
public class DemoController {

    static final Logger logger = LoggerFactory.getLogger(DemoController.class);

    private static final List<Integer> INTEGER_LIST = new ArrayList<>();
    static {
        for(int i = 0; i < 1000; ++i) {
            INTEGER_LIST.add(i);
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/get", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public Flux<Integer> get(final ServerWebExchange serverWebExchange) {
        final String logPrefix = serverWebExchange.getLogPrefix();
        logger.info(String.format("%sController called.", logPrefix));
        return Mono.fromFuture(CompletableFuture.supplyAsync(this::getIntList))
            .flatMapMany(Flux::fromIterable)
            .doOnComplete(() -> {
                logger.info(String.format("%sFlux returned from Controller doOnComplete called.", logPrefix));
            });
    }

    private List<Integer> getIntList() {
        return INTEGER_LIST;
    }
}
