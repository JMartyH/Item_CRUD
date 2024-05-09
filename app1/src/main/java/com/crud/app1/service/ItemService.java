package com.crud.app1.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.crud.app1.model.Item;
import com.crud.app1.repository.ItemRepository;

@Service
public class ItemService {

	@Autowired
	private ItemRepository itemRepository;

	public List<Item> findAll() {

		return itemRepository.findAll();
	}

	public Optional<Item> findById(Long id) {

		return itemRepository.findById(id);

	}

	public Item save(Item item) {

		return itemRepository.save(item);

	}

	public void deleteById(Long id) {

		itemRepository.deleteById(id);

	}

}
