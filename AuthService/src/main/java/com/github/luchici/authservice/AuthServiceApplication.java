package com.github.luchici.authservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AuthServiceApplication {
    // TODO: Auth Manager vs Auth Provider

    // TODO: Implement JWT token
    // TODO: FormLogin and SessionId - how it is generate and how to use it

    // To do - in this project
    // TODO: Use Decorate Pattern password to split UserDetail from Entity
    // TODO: Add salt and pepper to the login password
    // TODO: Use redis to store session ID
    // TODO: Refactoring gradle file
    // TODO: Make dto avalable in other modules
    // TODO: Do some more refactoring and more testing Global Exception Controller
    // TODO: Some group annotation for the validate of the entities and dto. I dont like to repeat the annotation on each filed.
    // TODO: Internationalization

    // Read for more
    // TODO: Serialize and deserialize manula an objects
    // TODO: Test method generator/factory
    // TODO: Elasticsearch tutorial

    public static void main(final String[] args) {
        SpringApplication.run(AuthServiceApplication.class, args);
    }
}
