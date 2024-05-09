package com.crud.app1.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

@RestController
@RequestMapping("/items")
public class ItemController {

	@Autowired
	ItemService itemService;

	@GetMapping
	public List<Item> getAllItems() {

		return itemService.findAll();

	}

	@GetMapping("/{id}")
	public ResponseEntity<Item> getItemById(@PathVariable Long id) {

		return itemService.findById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());

	}
	
	@PostMapping
	public Item createItem(@RequestBody Item item) {
		
		return itemService.save(item);
		
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Item> updateItem(@PathVariable Long id, @RequestBody Item item){
		
		return itemService.findById(id)
				.map(existingItem ->{
					existingItem.setName(item.getName());
					existingItem.setDescription(item.getDescription());
					return ResponseEntity.ok(itemService.save(existingItem));
				})
				.orElse(ResponseEntity.notFound().build());
		
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteItemById(@PathVariable Long id){
		return itemService.findById(id)
				.map(existingItem ->{
					itemService.deleteById(id);
					return ResponseEntity.ok().build();
				})
				.orElse(ResponseEntity.notFound().build());
	}
	

}
