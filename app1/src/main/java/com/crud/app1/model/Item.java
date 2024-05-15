package com.crud.app1.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;

@Entity
public class Item {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@NotBlank(message = "Name is required")
	private String name;
	@NotBlank(message = "Description is required")
	private String description;

	// No-argument constructor
	public Item() {
	}

	public Item(Long id, @NotBlank(message = "Name is required") String name,
			@NotBlank(message = "Description is required") String description) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
	}

	public Item(@NotBlank(message = "Name is required") String name,
			@NotBlank(message = "Description is required") String description) {
		super();
		this.name = name;
		this.description = description;
	}

	// Copy constructor (Optional)
	public Item(Item other) {
		this(other.id, other.name, other.description);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "Item [id=" + id + ", name=" + name + ", description=" + description + "]";
	}

}
