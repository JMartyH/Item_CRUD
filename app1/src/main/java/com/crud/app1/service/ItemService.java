package com.crud.app1.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.crud.app1.errorhandling.ItemIsEmpty;
import com.crud.app1.errorhandling.ItemNotFoundException;
import com.crud.app1.model.Item;
import com.crud.app1.repository.ItemRepository;

@Service
public class ItemService {

	private static final Logger logger = LoggerFactory.getLogger(ItemService.class);

	@Autowired
	private ItemRepository itemRepository;

	public List<Item> findAll() {

		logger.info("Fetching all items from the repository");
		List<Item> items = itemRepository.findAll();
		if (items.isEmpty()) {
			throw new ItemIsEmpty("No items found in the repository.");
		}
		return items;

	}

	public Item findById(Long id) {

		logger.info("Fetching item with ID: {}", id);
		return itemRepository.findById(id)
				.orElseThrow(() -> new ItemNotFoundException("Item with ID " + id + " is not found."));

	}

	public Item save(Item item) {
		logger.info("Saving item: \n {}", item);
		return itemRepository.save(item);

	}

	public void deleteById(Long id) {

		logger.info("Attempting to delete item with ID: {}", id);

		if (!itemRepository.existsById(id)) { // If item not found, throw ItemNotFoundException
			logger.error("Error deleting item with ID: {} - Item not found", id);
			throw new ItemNotFoundException("Item with ID " + id + " is not found.");
		}

		itemRepository.deleteById(id);
		logger.info("Item with ID: {} successfully deleted", id);

	}

}
