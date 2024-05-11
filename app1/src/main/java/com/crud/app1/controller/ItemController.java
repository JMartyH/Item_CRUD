package com.crud.app1.controller;

import java.time.LocalDateTime;
import java.util.List;

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

import exceptionHandler.ItemErrorResponse;
import exceptionHandler.ItemNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;

@RestController
@RequestMapping("/items")
public class ItemController {

	@Autowired
	ItemService itemService;

	@GetMapping
	public ResponseEntity<List<Item>> getAllItems() {

		// TODO: add validation if there are no items in the list
		List<Item> allItems = itemService.findAll();

		return ResponseEntity.ok(allItems);

	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getItemById(@Valid @PathVariable @Min(1) Long id) {

		try {
			Item item = itemService.findById(id);
			return ResponseEntity.ok(item);
		} catch (ItemNotFoundException e) {
			ItemErrorResponse errorResponse = new ItemErrorResponse(HttpStatus.NOT_FOUND.value(), e.getMessage(),
					LocalDateTime.now());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
		}

	}

	@PostMapping
	public ResponseEntity<Item> createItem(@Valid @RequestBody Item item) {

		Item savedItem = itemService.save(item);
		return ResponseEntity.status(HttpStatus.CREATED).body(savedItem);

	}

	@PutMapping("/{id}")
	public ResponseEntity<?> updateItem(@PathVariable Long id, @RequestBody Item item) {

		try {
			Item itemToUpdate = itemService.findById(id);
			itemToUpdate.setName(item.getName());
			itemToUpdate.setDescription(item.getDescription());
			itemService.save(itemToUpdate);
			return ResponseEntity.ok(itemToUpdate);
		} catch (ItemNotFoundException e) {
			ItemErrorResponse errorResponse = new ItemErrorResponse(HttpStatus.NOT_FOUND.value(), e.getMessage(),
					LocalDateTime.now());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
		}

	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteItemById(@PathVariable Long id) {

		try {
			itemService.findById(id);
			itemService.deleteById(id);
			return ResponseEntity.ok("Successfully deleted item " + id);

		} catch (ItemNotFoundException e) {
			ItemErrorResponse errorResponse = new ItemErrorResponse(HttpStatus.NOT_FOUND.value(), e.getMessage(),
					LocalDateTime.now());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
		}

	}
}
