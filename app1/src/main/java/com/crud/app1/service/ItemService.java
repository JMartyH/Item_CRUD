package com.crud.app1.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.crud.app1.exceptionHandler.ItemIsEmpty;
import com.crud.app1.exceptionHandler.ItemNotFoundException;
import com.crud.app1.model.Item;
import com.crud.app1.repository.ItemRepository;

@Service
public class ItemService {

	private static final Logger logger = LoggerFactory.getLogger(ItemService.class);

	@Autowired
	private ItemRepository itemRepository;

	public List<Item> findAll() {

		try {

			if (itemRepository.findAll().isEmpty()) {
				throw new ItemIsEmpty("no items in the list");
			}
			logger.info("Fetched all items from the repository");
			return itemRepository.findAll();

		} catch (ItemIsEmpty e) {
			logger.error("There are no Items in the list.", e);
			throw new ItemIsEmpty("There are no Items in the list.");
		}
	}

	public Item findById(Long id) {

		try {
			if (!itemRepository.existsById(id)) {
				throw new ItemNotFoundException("No such Id exists: " + id);
			}
			logger.info("Fetching item with ID: {}", id);
			return itemRepository.findById(id).get();
		} catch (ItemNotFoundException e) {
			logger.error("Error fetching item with ID: {} - Item not found", id, e);
			throw new ItemNotFoundException("Item with ID " + id + " is not found.");
		}

	}

	public Item save(Item item) {
		logger.info("Saving item: \n" + item.toString());
		return itemRepository.save(item);

	}

	public void deleteById(Long id) throws EmptyResultDataAccessException {
		logger.info("Attempting to delete item with ID: {}", id);
		try {
			if (!itemRepository.existsById(id)) {
				throw new EmptyResultDataAccessException(1);
			}
			itemRepository.deleteById(id);
			logger.info("Item with ID: {} successfully deleted", id);

		} catch (EmptyResultDataAccessException e) { // Catch the specific exception
			logger.error("Error deleting item with ID: {} - Item not found", id, e);
			throw new ItemNotFoundException("Item with ID " + id + " is not found."); // Re-throw as
																						// ItemNotFoundException
		}

	}

}
