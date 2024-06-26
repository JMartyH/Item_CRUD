package com.crud.app1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.crud.app1.model.Item;

@Repository
public interface ItemRepository extends JpaRepository <Item, Long> {

}
