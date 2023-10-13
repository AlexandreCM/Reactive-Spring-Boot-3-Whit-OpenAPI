package com.example.demo.controller;


import com.example.demo.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Optional;

@RestController
public class UsersController implements UsersApi {

	private final ArrayList<User> users = new ArrayList<>();

	@Override
	public Mono<ResponseEntity<Void>> addUser(Mono<User> user, ServerWebExchange exchange) {
		return user.flatMap(u -> {
			users.add(u);
			return Mono.just(ResponseEntity.ok().build());
		});
	}

	@Override
	public Mono<ResponseEntity<Flux<User>>> getUsers(ServerWebExchange exchange) {
		return Mono.just(ResponseEntity.ok().body(Flux.fromIterable(users)));
	}

	@Override
	public Mono<ResponseEntity<User>> getUser(Long userId, ServerWebExchange exchange) {
		return Optional.of(users)
				.flatMap(list -> list.stream()
						.filter(user -> user.getId().equals(userId.intValue()))
						.findAny()
						.map(user -> Mono.just(ResponseEntity.ok(user)))
				)
				.orElseThrow();
	}
}
