package net.backstube.bomblebee.items;

import net.backstube.bomblebee.Bomblebee;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ItemsRegistry {
    public static void register() {
        Registry.register(Registries.ITEM, BOMBLEBEE_ITEM_ID, BOMBLEBEE_ITEM);
    }

    public static final BomblebeeItem BOMBLEBEE_ITEM = new BomblebeeItem(new Item.Settings());
    public static final net.minecraft.util.Identifier BOMBLEBEE_ITEM_ID = new Identifier(Bomblebee.MOD_ID, "bomblebee_item");
}
