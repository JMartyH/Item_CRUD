package com.crud.app1.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.crud.app1.model.Item;
import com.crud.app1.repository.ItemRepository;

import exceptionHandler.ItemNotFoundException;

@Service
public class ItemService {

	@Autowired
	private ItemRepository itemRepository;

	public List<Item> findAll() {

		return itemRepository.findAll();
	}

	public Item findById(Long id) {

		return itemRepository.findById(id)
				.orElseThrow(() -> new ItemNotFoundException("Item with ID " + id + " is not found."));

	}

	public Item save(Item item) {

		return itemRepository.save(item);

	}

	public void deleteById(Long id) {

		try {
			itemRepository.deleteById(id);
		} catch (NoSuchElementException e) {
			throw new ItemNotFoundException("Item with ID " + id + " is not found.");
		}

	}

}
