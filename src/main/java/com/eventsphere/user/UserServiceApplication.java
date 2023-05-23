package com.eventsphere.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;

@SpringBootApplication
@EnableDiscoveryClient
@RefreshScope
public class UserServiceApplication {

    /**
     * Entry point for the User Service microservice.
     * <p>
     * The User Service is responsible for managing user-related operations, such as creating,
     * updating, and deleting user accounts, as well as retrieving user information.
     * It serves as a part of the larger Event Sphere system, allowing users to interact
     * with the platform and participate in various events.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }

}
