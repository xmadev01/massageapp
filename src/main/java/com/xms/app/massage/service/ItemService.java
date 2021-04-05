package com.xms.app.massage.service;

import com.xms.app.massage.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemService {

    Optional<Item> findById(long id);

    List<Item> getAllItems();

    void saveItem(final Item item);
}
