package com.crud.app1.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crud.app1.model.Item;
import com.crud.app1.service.ItemService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;

@RestController
@RequestMapping("/items")
public class ItemController {

	private static final Logger logger = LoggerFactory.getLogger(ItemController.class);

	@Autowired
	ItemService itemService;

	@GetMapping
	public ResponseEntity<?> getAllItems() {
		logger.info("GET request received for all items");
		
		List<Item> allItems = itemService.findAll();
		return ResponseEntity.ok(allItems);

	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getItemById(@Valid @PathVariable @Min(1) Long id) {
		logger.info("GET request received for item with ID: {}", id);

		Item item = itemService.findById(id);
		return ResponseEntity.ok(item);

	}

	@PostMapping
	public ResponseEntity<Item> createItem(@Valid @RequestBody Item item) {
		logger.info("POST request received to create item: {}", item);
		System.out.println(item.getId());
		Item savedItem = itemService.save(item);
		return ResponseEntity.status(HttpStatus.CREATED).body(savedItem);

	}

	@PutMapping("/{id}")
	public ResponseEntity<?> updateItem(@PathVariable Long id, @RequestBody Item item) {
		logger.info("PUT request received to update item with ID: {}", id);
		
		Item itemToUpdate = itemService.findById(id);
		itemToUpdate.setName(item.getName());
		itemToUpdate.setDescription(item.getDescription());
		itemService.save(itemToUpdate);
		return ResponseEntity.ok(itemToUpdate);

	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteItemById(@PathVariable Long id) {
		logger.info("DELETE request received to delete ID: {}", id);
		itemService.deleteById(id); // This might throw ItemNotFoundException, will be handled by GlobalExceptionHandler
		return ResponseEntity.ok("Successfully deleted item " + id);

	}
}
