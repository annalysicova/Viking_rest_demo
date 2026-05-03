package ru.mephi.vikingdemo.service;

import ru.mephi.vikingdemo.model.EquipmentItem;

import java.util.List;
import java.util.Random;

public class EquipmentFactory {

    private static final List<String> EQUIPMENT_NAMES = List.of(
            "Axe", "Sword", "Shield", "Helmet", "Spear", "Chainmail", "Hammer", "Knife"
    );

    private static final Random RANDOM = new Random();

    public static EquipmentItem createItem() {
        String name = EQUIPMENT_NAMES.get(RANDOM.nextInt(EQUIPMENT_NAMES.size()));
        String quality = generateQuality();
        return new EquipmentItem(name, quality);
    }

    private static String generateQuality() {
        int roll = RANDOM.nextInt(100);
        if (roll < 60) {
            return "Common";
        }
        if (roll < 85) {
            return "Uncommon";
        }
        if (roll < 97) {
            return "Rare";
        }
        return "Legendary";
    }
}
