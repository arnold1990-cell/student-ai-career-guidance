package com.edutech.controller;

import com.edutech.domain.Role;
import com.edutech.domain.RoleName;
import com.edutech.domain.User;
import com.edutech.repository.RoleRepository;
import com.edutech.repository.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthFlowIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setupRolesAndAdmin() {
        for (RoleName roleName : RoleName.values()) {
            roleRepository.findByName(roleName)
                    .orElseGet(() -> roleRepository.save(Role.builder().name(roleName).build()));
        }

        if (!userRepository.existsByEmail("admin@edutech.local")) {
            Role adminRole = roleRepository.findByName(RoleName.ADMIN).orElseThrow();
            userRepository.save(User.builder()
                    .email("admin@edutech.local")
                    .passwordHash(passwordEncoder.encode("Admin@12345"))
                    .enabled(true)
                    .roles(Set.of(adminRole))
                    .build());
        }
    }

    @Test
    void registerLoginAndRbacFlow() throws Exception {
        String registerPayload = """
                {
                  "email":"student1@example.com",
                  "password":"StrongPass123",
                  "firstName":"Stu",
                  "lastName":"Dent"
                }
                """;

        mockMvc.perform(post("/api/auth/register/student")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registerPayload))
                .andExpect(status().isCreated());

        String loginPayload = """
                {
                  "email":"student1@example.com",
                  "password":"StrongPass123"
                }
                """;

        String loginResponse = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginPayload))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        JsonNode studentTokenNode = objectMapper.readTree(loginResponse);
        String studentToken = studentTokenNode.get("accessToken").asText();

        mockMvc.perform(get("/api/admin/users")
                        .header("Authorization", "Bearer " + studentToken))
                .andExpect(status().isForbidden());

        String adminLoginPayload = """
                {
                  "email":"admin@edutech.local",
                  "password":"Admin@12345"
                }
                """;

        String adminResponse = mockMvc.perform(post("/api/admin/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(adminLoginPayload))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        String adminToken = objectMapper.readTree(adminResponse).get("accessToken").asText();

        mockMvc.perform(get("/api/admin/users")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk());
    }

    @Test
    void invalidCredentialsReturnUnauthorized() throws Exception {
        String invalidPayload = """
                {
                  "email":"missing@example.com",
                  "password":"WrongPass123"
                }
                """;

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidPayload))
                .andExpect(status().isUnauthorized());
    }
}
