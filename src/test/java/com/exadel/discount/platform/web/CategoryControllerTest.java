package com.exadel.discount.platform.web;

import com.exadel.discount.platform.model.dto.CategoryDto;
import com.exadel.discount.platform.model.dto.CategoryResponseDto;
import com.exadel.discount.platform.service.CategoryService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.shaded.com.fasterxml.jackson.annotation.JsonInclude;
import org.testcontainers.shaded.com.fasterxml.jackson.core.type.TypeReference;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.UUID;

@SpringBootTest
@WebAppConfiguration
public class CategoryControllerTest {

    private final WebApplicationContext context;
    private static final UUID deletedCategory = UUID.fromString("7c90994b-06f4-4e42-aafb-c9b07eeb9815");
    private static final UUID actualCategory = UUID.fromString("a55b8b0c-cd2b-4896-a074-bafb558266a9");
    private static final UUID actualSubCategory = UUID.fromString("35f97267-404e-4d95-98f6-9a399bc3a9de");

    private MockMvc mockMvc;
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
            .setSerializationInclusion(JsonInclude.Include.NON_NULL);

    @Autowired
    public CategoryControllerTest(WebApplicationContext context) {
        this.context = context;
    }

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    @WithMockUser(username = "testAdmin", authorities = "ADMINISTRATOR")
    @Sql(value = {"/insertDeletedCategoryTest.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/deleteCategoryTest.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void shouldGetAllDeletedCategoryForAdmin() throws Exception {
        byte[] contentAsByte = mockMvc.perform(MockMvcRequestBuilders.get("/category")
                .param("isDeleted", "true"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsByteArray();

        List<CategoryResponseDto> categories = OBJECT_MAPPER.readValue(contentAsByte
                , new TypeReference<List<CategoryResponseDto>>() {
                });

        Assertions.assertEquals(2, categories.size());
        Assertions.assertTrue(categories.get(0).isDeleted());
        Assertions.assertEquals(categories.get(0).getName(), "tourism");
        Assertions.assertEquals(categories.get(0).getId(), deletedCategory);

    }

    @Test
    @WithMockUser(username = "testUser", authorities = "USER")
    @Sql(value = {"/insertDeletedCategoryTest.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/deleteCategoryTest.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void shouldNotGetAllDeletedCategoryForUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/category")
                .param("isDeleted", "true")).andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithMockUser(username = "testAdmin", authorities = "ADMINISTRATOR")
    @Sql(value = {"/deleteCategoryTest.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void shouldCreateCategory() throws Exception {
        CategoryDto category = new CategoryDto();
        category.setName("RestTest");

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/category")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(OBJECT_MAPPER.writeValueAsString(category));

        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("RestTest"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                .andReturn();
    }

    @Test
    @WithMockUser(username = "testAdmin", authorities = "ADMINISTRATOR")
    @Sql(value = {"/insertDeletedCategoryTest.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/insertActualCategory.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/deleteCategoryTest.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void shouldGetDeletedCategoryByIdForAdmin() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/category/{id}", deletedCategory)
                .param("isDeleted", "true")).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(deletedCategory.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("tourism"));
    }

    @Test
    @WithMockUser(username = "testUser", authorities = "USER")
    @Sql(value = {"/insertDeletedCategoryTest.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/insertActualCategory.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/deleteCategoryTest.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void shouldGetDeletedAndActualCategoryForUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/category/{id}", deletedCategory)
                .param("isDeleted", "true")).andExpect(MockMvcResultMatchers.status().isForbidden());

        mockMvc.perform(MockMvcRequestBuilders.get("/category/{id}", actualCategory))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(actualCategory.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("education"));
    }

    @Test
    @WithMockUser(username = "testAdmin", authorities = "ADMINISTRATOR")
    @Sql(value = {"/insertActualCategory.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/deleteCategoryTest.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void shouldUpdateCategoryById() throws Exception {
        CategoryDto category = new CategoryDto();
        category.setName("EducationTest");

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/category/{id}", actualCategory)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(OBJECT_MAPPER.writeValueAsString(category));

        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(actualCategory.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("EducationTest"))
                .andReturn();
    }

    @Test
    @WithMockUser(username = "testAdmin", authorities = "ADMINISTRATOR")
    @Sql(value = {"/insertActualCategory.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/deleteCategoryTest.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void shouldDeleteCategoryById() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.delete("/category/{id}", actualCategory))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(actualCategory.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.deleted").value(true));

    }

    @Test
    @WithMockUser(username = "testAdmin", authorities = "ADMINISTRATOR")
    @Sql(value = {"/insertActualCategory.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/deleteCategoryTest.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void shouldDeleteSubCategoryIfDeletedCategory() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.delete("/category/{id}", actualCategory))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(actualCategory.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.deleted").value(true));

        mockMvc.perform(MockMvcRequestBuilders
                .get("/category/{id}/sub_category/{sub_id}", actualCategory, actualSubCategory))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(actualSubCategory.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.deleted").value(true));
    }

}