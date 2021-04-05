package com.xms.app.massage.repository;

import com.xms.app.massage.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {

}
