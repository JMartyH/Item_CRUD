package com.crud.app1.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.crud.app1.errorhandling.ItemNotFoundException;
import com.crud.app1.model.Item;
import com.crud.app1.service.ItemService;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ItemController.class)
class ItemControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private ItemService itemService;

	@Test
	void testGetAllItems_Success() throws Exception {
		// Arrange
		List<Item> items = new ArrayList<>();
		items.add(new Item(1L, "Item 1", "Description 1"));
		items.add(new Item(2L, "Item 2", "Description 2"));
		when(itemService.findAll()).thenReturn(items);
		System.out.println(items);
		// Act & Assert
		mockMvc.perform(get("/items").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(2))) // Check for 2 items
				.andExpect(jsonPath("$[0].id", is(1))).andExpect(jsonPath("$[0].name", is("Item 1")))
				.andExpect(jsonPath("$[1].id", is(2))).andExpect(jsonPath("$[1].name", is("Item 2")));
	}

	@Test
	void testGetAllItems_IsEmpty() throws Exception {

		when(itemService.findAll()).thenReturn(Collections.emptyList()); // Mock an empty list

		// Act & Assert
		mockMvc.perform(get("/items").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(0)));
	}

	@Test
	void testGetItemById_Sucess() throws Exception {
		// Set up
		Long itemId = 1L;
		Item expectedItem = new Item(itemId, "item", "item description");
		when(itemService.findById(1L)).thenReturn(expectedItem);

		mockMvc.perform(get("/items/{id}", itemId).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$.id", is(1)))
				.andExpect(jsonPath("$.name", is("item"))).andExpect(jsonPath("$.description", is("item description")));
	}

	@Test
	void testGetItemById_NotFound() throws Exception {
		// Arrange
		Long itemId = 1L;
		when(itemService.findById(itemId))
				.thenThrow(new ItemNotFoundException("Item with ID " + itemId + " is not found."));

		// Act & Assert
		mockMvc.perform(get("/items/{id}", itemId).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound()).andExpect(jsonPath("$.status", is("NOT_FOUND")))
				.andExpect(jsonPath("$.message", is("Item with ID " + itemId + " is not found.")))
				.andExpect(jsonPath("$.timestamp").exists());
	}

	@Test
	void testCreateItem_Success() throws Exception {
		// Set up
		Item newItem = new Item();
		newItem.setName("New Item");
		newItem.setDescription("Description of the new item");

		Item savedItem = new Item(); // Item returned after saving, potentially with an ID
		savedItem.setId(1L);
		savedItem.setName(newItem.getName());
		savedItem.setDescription(newItem.getDescription());

		when(itemService.save(any(Item.class))).thenReturn(savedItem);

		// Act & Assert
        mockMvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newItem))) 
                .andExpect(status().isCreated())   
                .andExpect(header().string("Location", Matchers.endsWith("/items/1"))) // Verify the 'Location' header
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("New Item")))
                .andExpect(jsonPath("$.description", is("Description of the new item")));

	}

}
