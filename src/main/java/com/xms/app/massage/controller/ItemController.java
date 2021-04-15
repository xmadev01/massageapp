package com.xms.app.massage.controller;

import com.xms.app.massage.dto.ItemsDto;
import com.xms.app.massage.model.Item;
import com.xms.app.massage.service.ItemService;
import com.xms.app.massage.utils.MessageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@SessionAttributes("activeItem")
public class ItemController {

    @Autowired
    private ItemService itemService;

    @GetMapping("/listAllItems")
    public String listAllItems(Model model) {
        ItemsDto itemsForm = new ItemsDto();
        final List<Item> items = itemService.getAllItems();
        items.forEach(item -> itemsForm.addItem(item));
        model.addAttribute("activeItem", "settings");
        model.addAttribute("itemsForm", itemsForm);
        return "items";
    }

    @PostMapping("/saveItems")
    public String saveItems(@ModelAttribute ItemsDto itemsForm, RedirectAttributes redirectAttributes, Model model) {
        itemsForm.getItems().forEach(item -> {
            final Item loadedItem = itemService.findById(item.getId()).orElse(null);
            if (loadedItem != null) {
                loadedItem.setDuration(item.getDuration());
                loadedItem.setPrice(item.getPrice());
            }
        });
        MessageUtils.addSuccessMessage(redirectAttributes, "The service items have been saved successfully.");
        return "redirect:/listAllItems";
    }

}
