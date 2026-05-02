package com.celauro.SpendWise.integration_test.security;

import com.celauro.SpendWise.dtos.CategoryDTO;
import com.celauro.SpendWise.dtos.TransactionDTO;
import com.celauro.SpendWise.dtos.UserLoginDTO;
import com.celauro.SpendWise.dtos.UserRegisterDTO;
import com.celauro.SpendWise.utils.TransactionType;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class TransactionSecurityIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String userAToken;
    private String userBToken;

    // ========================
    // SETUP
    // ========================
    @BeforeEach
    void setup() throws Exception{
        objectMapper = new ObjectMapper();

        registerUser("userA@gmail.com", "password", "Nome", "Cognome");
        registerUser("userB@gmail.com", "password", "Nome", "Cognome");

        userAToken = login("userA@gmail.com", "password");
        userBToken = login("userB@gmail.com", "password");
    }

    // ========================
    // TEST: access without token
    // ========================
    @Test
    void shouldReturn403_whenNoToken() throws Exception{
        mockMvc.perform(get("/api/transactions"))
                .andExpect(status().isForbidden());
    }

    // ========================
    // TEST: access with token
    // ========================
    @Test
    void shouldReturn200_whenValidToken()throws Exception{
        mockMvc.perform(get("/api/transactions")
                    .header("Authorization", "Bearer " + userAToken))
                .andExpect(status().isOk());
    }

    // ========================
    // TEST: multi-user isolation
    // ========================
    @Test
    void userB_shouldNotAccess_userATransactions()throws Exception{
        CategoryDTO category = new CategoryDTO("Casa");

        TransactionDTO transaction = new TransactionDTO(
                TransactionType.INCOME,
                100.00,
                "Casa",
                LocalDate.now(),
                "descrizione",
                "paypal"
                );

        String jsonCategory = objectMapper.writeValueAsString(category);
        String jsonTransaction = objectMapper.writeValueAsString(transaction);

        mockMvc.perform(post("/api/category")
                        .header("Authorization", "Bearer " + userAToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonCategory))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/transaction")
                        .header("Authorization", "Bearer " + userAToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTransaction))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/transactions")
                    .header("Authorization", "Bearer " + userBToken))
                .andExpect((status().isOk()))
                .andExpect(jsonPath("$.length()").value(0));
    }

    // ========================
    // TEST: create transaction linked to user
    // ========================
    @Test
    void shouldCreateTransaction_forLoggedUser()throws Exception{
        CategoryDTO category = new CategoryDTO("Casa");

        TransactionDTO transaction = new TransactionDTO(
                TransactionType.INCOME,
                100.00,
                "Casa",
                LocalDate.now(),
                "descrizione",
                "paypal"
        );

        String jsonCategory = objectMapper.writeValueAsString(category);
        String jsonTransaction = objectMapper.writeValueAsString(transaction);

        mockMvc.perform(post("/api/category")
                        .header("Authorization", "Bearer " + userAToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonCategory))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/transaction")
                        .header("Authorization", "Bearer " + userAToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonTransaction))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("descrizione"));
    }
    // ========================
    // HELPER: register
    // ========================
    private void registerUser(String email, String password, String name, String surname) throws Exception{
        UserRegisterDTO register = new UserRegisterDTO(email, password, name, surname);

        String json = objectMapper.writeValueAsString(register);

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk());
    }

    // ========================
    // HELPER: login
    // ========================
    private String login(String email, String password)throws Exception{
        UserLoginDTO login = new UserLoginDTO(email, password);

        String json = objectMapper.writeValueAsString(login);

        return  mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
    }
}
