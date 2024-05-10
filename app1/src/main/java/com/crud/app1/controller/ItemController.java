package com.crud.app1.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;

@RestController
@RequestMapping("/items")
public class ItemController {

	@Autowired
	ItemService itemService;

	@GetMapping
	public ResponseEntity<?> getAllItems() {

		List<Item> allItems = new ArrayList<>();

		allItems = itemService.findAll();

		if (!itemService.findAll().isEmpty()) {
			return new ResponseEntity<>(allItems, HttpStatus.OK);
		} else {
			return ResponseEntity.status(404).body("There are currently no Items");
		}

	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getItemById(@Valid @PathVariable @Min(1) Long id) {

		  Optional<Item> item = itemService.findById(id);

		    if (!item.isPresent()) {
		    	System.out.println(item);
		        return ResponseEntity.status(HttpStatus.NOT_FOUND)
		                .body(new ItemErrorResponse(HttpStatus.NOT_FOUND.value(), "Item not found with ID: " + id, System.currentTimeMillis()));
		    }
		    return ResponseEntity.ok(item); 
		

	}

	@PostMapping
	public ResponseEntity<Item> createItem(@Valid @RequestBody Item item) {

		Item savedItem = itemService.save(item);
		return new ResponseEntity<>(savedItem, HttpStatus.CREATED);

	}

	@PutMapping("/{id}")
	public ResponseEntity<Item> updateItem(@PathVariable Long id, @RequestBody Item item) {
		// TODO: add validation
		return itemService.findById(id).map(existingItem -> {
			existingItem.setName(item.getName());
			existingItem.setDescription(item.getDescription());
			return ResponseEntity.ok(itemService.save(existingItem));
		}).orElse(ResponseEntity.notFound().build());

	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteItemById(@PathVariable Long id) {
		return itemService.findById(id).map(existingItem -> {
			itemService.deleteById(id);
			return ResponseEntity.ok("Successfully deleted item " + id);
		}).orElse(ResponseEntity.notFound().build());
	}

}
