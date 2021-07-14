package com.exadel.discount.platform.web;

import com.exadel.discount.platform.Application;
import com.exadel.discount.platform.model.dto.SubCategoryResponseDto;
import com.exadel.discount.platform.model.dto.update.SubCategoryBaseDto;
import com.exadel.discount.platform.service.SubCategoryService;
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
    private SubCategoryService subCategoryService;
    @Autowired
    private WebApplicationContext context;
    private MockMvc mockMvc;
    private static final UUID testCategoryId = UUID.fromString("e84858db-1b05-45da-bd31-e48aaab43a33");
    private static final UUID testSubCategoryId = UUID.fromString("35f97267-404e-4d95-98f6-9a399bc3a9de");
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
        byte[] contentAsByteArray = mockMvc.perform(MockMvcRequestBuilders.get("/category/{categoryId}/sub_category", testCategoryId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsByteArray();
        List<SubCategoryResponseDto> subCategoryResponseDtos = OBJECT_MAPPER.readValue(contentAsByteArray,
                new TypeReference<List<SubCategoryResponseDto>>() {
                });
        SubCategoryResponseDto subCategoryResponseDto = new SubCategoryResponseDto();
        subCategoryResponseDto.setCategoryId(testCategoryId);
        subCategoryResponseDto.setId(testSubCategoryId);
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
        SubCategoryBaseDto subCategoryBaseDto = new SubCategoryBaseDto();
        subCategoryBaseDto.setName("baseball9");
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/category/{categoryId}/sub_category", testCategoryId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(OBJECT_MAPPER.writeValueAsString(subCategoryBaseDto));
        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name")
                        .value("baseball9"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.categoryId").value(testCategoryId.toString()));
    }

    @WithMockUser(username = "testuser", authorities = "ADMINISTRATOR")
    @Test
    @Sql(value = {"/insertSubCategoryTest.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/deleteSubCategoryTest.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getSubCategoryById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/category/{categoryId}/sub_category/{subCategoryId}", testCategoryId, testSubCategoryId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name")
                        .value("test_cake"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id")
                        .value(testSubCategoryId.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.categoryId")
                        .value(testCategoryId.toString()));
    }

    @WithMockUser(username = "testuser", authorities = "ADMINISTRATOR")
    @Test
    @Sql(value = {"/insertSubCategoryTest.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/deleteSubCategoryTest.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void updateSubCategory() throws Exception {
        SubCategoryBaseDto subCategoryBaseDto = new SubCategoryBaseDto();
        subCategoryBaseDto.setName("test_cake2");
        mockMvc.perform(MockMvcRequestBuilders.put("/category/{categoryId}/sub_category/{subCategoryId}", testCategoryId, testSubCategoryId)
                .contentType(MediaType.APPLICATION_JSON).content(OBJECT_MAPPER.writeValueAsString(subCategoryBaseDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name")
                        .value("test_cake2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id")
                        .value(testSubCategoryId.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.categoryId")
                        .value(testCategoryId.toString()));
    }

    @WithMockUser(username = "testuser", authorities = "ADMINISTRATOR")
    @Test
    @Sql(value = {"/insertSubCategoryTest.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/deleteSubCategoryTest.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void sendSubCategoryToArchive() throws Exception {
        MockMvcRequestBuilders
                .delete("/category/{categoryId}/sub_category/{subCategoryId}", testCategoryId, testSubCategoryId)
                .contentType(MediaType.APPLICATION_JSON);
        subCategoryService.toArchive(testCategoryId,
                testSubCategoryId);
        Assertions.assertTrue(subCategoryService.getByCategoryIdAndId(testCategoryId,
                testSubCategoryId).isDeleted());
    }
}