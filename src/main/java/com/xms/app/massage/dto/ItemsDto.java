package com.xms.app.massage.dto;

import com.xms.app.massage.model.Item;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ItemsDto {

    private List<Item> items = new ArrayList<>();

    public void addItem(Item item) {
        this.items.add(item);
    }
}
