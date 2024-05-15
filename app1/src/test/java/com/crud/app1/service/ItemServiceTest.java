package com.crud.app1.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.AccessibleObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.aopalliance.intercept.Invocation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import com.crud.app1.errorhandling.ItemIsEmpty;
import com.crud.app1.errorhandling.ItemNotFoundException;
import com.crud.app1.model.Item;
import com.crud.app1.repository.ItemRepository;

@ExtendWith(MockitoExtension.class)
class ItemServiceTest {

	@Mock
	private ItemRepository itemRepository;

	@InjectMocks
	private ItemService itemService;

	private Item item;

	@BeforeEach
	void setUp() {
		item = new Item();
		item.setId(1L);
		item.setName("first_item");
		item.setDescription("First Item Description");
	}

	@Test
	void testFindById_Success() {
		when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
		Item foundItem = itemService.findById(1L);
		assertEquals(item, foundItem, "The found item should match the expected item: " + foundItem);

	}

	@Test
	void testFindById_NotFound() {
		when(itemRepository.findById(anyLong())).thenReturn(Optional.empty());
		assertThrows(ItemNotFoundException.class, () -> itemService.findById(1L));

	}

	@Test
	void testFindAll_Success() {
		List<Item> expectedItems = new ArrayList<>();
		expectedItems.add(item); // Add the item created in setUp to the list

		when(itemRepository.findAll()).thenReturn(expectedItems);

		List<Item> actualItems = itemService.findAll();

		assertEquals(expectedItems, actualItems);

	}

	@Test
	void testFindAll_IsEmpty() {
		when(itemRepository.findAll()).thenReturn(Collections.emptyList());
		assertThrows(ItemIsEmpty.class, () -> itemService.findAll()); // Expect ItemIsEmpty
	}

	@Test
	void testDeleteById_Success() {
		when(itemRepository.existsById(1L)).thenReturn(true);
		itemService.deleteById(1L);
		verify(itemRepository, times(1)).deleteById(1L); // Check if deleteById is called once
	}

	@Test
	void testDeleteById_NotFound() {
		when(itemRepository.existsById(1L)).thenReturn(false);
		assertThrows(ItemNotFoundException.class, () -> itemService.deleteById(1L)); // Expect ItemNotFoundException
	}

	@Test
	void testSave_Success() {
		// Arrange
		Item newItem = new Item();
		newItem.setId(2L); // Set a different ID
		newItem.setName("new item");
		newItem.setDescription("New item description");

		when(itemRepository.save(any(Item.class))).thenAnswer(invocation -> invocation.getArgument(0));
		// Act
		Item savedItem = itemService.save(newItem);
		System.out.println(" Save " + newItem);
		// Assert
		verify(itemRepository, times(1)).save(newItem); // Verify save was called
		assertEquals(newItem, savedItem); // Ensure the saved item is the same as the input
	}

	@Test
	void testSave_DataIntegrityViolation() {
		Item newItem = new Item();
		newItem.setId(2L);
		newItem.setName(" ");
		newItem.setDescription("New item description");
		// Simulate a database constraint violation -- Name is set to @NotBlank
		when(itemRepository.save(newItem)).thenThrow(new DataIntegrityViolationException("Constraint violation"));

		// Assert that the service throws a RuntimeException (or a more specific custom
		// exception)
		assertThrows(RuntimeException.class, () -> itemService.save(item));
	}

}
