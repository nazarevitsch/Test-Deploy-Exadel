package com.exadel.discount.platform.web;

import com.exadel.discount.platform.Application;
import com.exadel.discount.platform.model.dto.SubCategoryDto;
import com.exadel.discount.platform.model.dto.SubCategoryResponseDto;
import com.exadel.discount.platform.repository.SubCategoryRepository;
import com.exadel.discount.platform.service.SubCategoryServiceImpl;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;
import java.util.UUID;

@SpringBootTest(classes = Application.class)
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class SubCategoryControllerTest {

    @Autowired
    private SubCategoryServiceImpl subCategoryService;
    @Autowired
    SubCategoryRepository subCategoryRepository;
    @Autowired
    private WebApplicationContext context;
    private MockMvc mockMvc;
    private static final String testCategoryId = "e84858db-1b05-45da-bd31-e48aaab43a33";
    private static final String testSubCategoryId = "35f97267-404e-4d95-98f6-9a399bc3a9de";
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
            .setSerializationInclusion(JsonInclude.Include.NON_NULL);

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @WithMockUser(username = "testuser", authorities = "ADMINISTRATOR")
    @Test
    @Sql(value = {"/insertSubCategoryTest.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/deleteSubCategoryTest.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getAllSubCategories() throws Exception {
        byte[] contentAsByteArray = mockMvc.perform(MockMvcRequestBuilders.get("/sub_category"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsByteArray();
        List<SubCategoryResponseDto> subCategoryResponseDtos = OBJECT_MAPPER.readValue(contentAsByteArray,
                new TypeReference<List<SubCategoryResponseDto>>() {
                });
        SubCategoryResponseDto subCategoryResponseDto = new SubCategoryResponseDto();
        subCategoryResponseDto.setCategoryId(UUID.fromString(testCategoryId));
        subCategoryResponseDto.setId(UUID.fromString(testSubCategoryId));
        subCategoryResponseDto.setName("test_cake");
        subCategoryResponseDto.setDeleted(false);
        Assertions.assertEquals(1, subCategoryResponseDtos.size());
        Assertions.assertTrue(subCategoryResponseDtos.contains(subCategoryResponseDto));
    }

    @WithMockUser(username = "testuser", authorities = "ADMINISTRATOR")
    @Sql(value = {"/insertSubCategoryTest.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/deleteSubCategoryTest.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    public void createSubCategory() throws Exception {
        SubCategoryDto subCategoryDto = new SubCategoryDto();
        subCategoryDto.setName("baseball9");
        subCategoryDto.setCategoryId(UUID.fromString(testCategoryId));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/sub_category")
                .contentType(MediaType.APPLICATION_JSON)
                .content(OBJECT_MAPPER.writeValueAsString(subCategoryDto));
        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name")
                        .value("baseball9"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.categoryId").value(testCategoryId));
    }

    @WithMockUser(username = "testuser", authorities = "ADMINISTRATOR")
    @Test
    @Sql(value = {"/insertSubCategoryTest.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/deleteSubCategoryTest.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getSubCategoryById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/sub_category/{subCategoryId}",testSubCategoryId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name")
                        .value("test_cake"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id")
                        .value(testSubCategoryId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.categoryId")
                        .value(testCategoryId));
    }

    @WithMockUser(username = "testuser", authorities = "ADMINISTRATOR")
    @Test
    @Sql(value = {"/insertSubCategoryTest.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/deleteSubCategoryTest.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void updateSubCategory() throws Exception {
        SubCategoryDto subCategoryDto = new SubCategoryDto();
        subCategoryDto.setName("test_cake2");
        mockMvc.perform(MockMvcRequestBuilders.put("/sub_category/{subCategoryId}",testSubCategoryId)
                .contentType(MediaType.APPLICATION_JSON).content(OBJECT_MAPPER.writeValueAsString(subCategoryDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name")
                        .value("test_cake2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id")
                        .value(testSubCategoryId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.categoryId")
                        .value(testCategoryId));
    }

    @WithMockUser(username = "testuser", authorities = "ADMINISTRATOR")
    @Test
    @Sql(value = {"/insertSubCategoryTest.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/deleteSubCategoryTest.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void sendSubCategoryToArchive() throws Exception {
        MockMvcRequestBuilders
                .delete("/sub_category/{subCategoryId}",testSubCategoryId)
                .contentType(MediaType.APPLICATION_JSON);
        subCategoryService.toArchive(UUID.fromString("35f97267-404e-4d95-98f6-9a399bc3a9de"));
        Assertions.assertTrue(subCategoryService.getById(UUID.fromString("35f97267-404e-4d95-98f6-9a399bc3a9de")).isDeleted());
    }
}