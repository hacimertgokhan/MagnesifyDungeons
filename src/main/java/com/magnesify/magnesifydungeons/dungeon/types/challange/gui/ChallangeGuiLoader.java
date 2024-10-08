package com.magnesify.magnesifydungeons.dungeon.types.challange.gui;

import com.magnesify.magnesifydungeons.boss.MagnesifyBoss;
import com.magnesify.magnesifydungeons.dungeon.types.challange.Challange;
import com.magnesify.magnesifydungeons.modules.managers.DatabaseManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

import static com.magnesify.magnesifydungeons.MagnesifyDungeons.get;
import static com.magnesify.magnesifydungeons.dungeon.entitys.DungeonPlayer.parseHexColors;
import static dev.lone.itemsadder.api.ItemsAdder.getCustomItem;

public class ChallangeGuiLoader {

    private static Inventory inv = null;

    @Deprecated
    public static void openInventory(Player ent) {
        inv = Bukkit.createInventory(null,  54, parseHexColors(get().getConfig().getString("settings.challange.title")));
        loadItems(ent);
        ent.openInventory(inv);
    }

    @Deprecated
    public static void loadItems(Player player) {
        DatabaseManager databaseManager = new DatabaseManager(get());
        int i = 0;
        for(String ranks : databaseManager.getChallangeNames()) {
            String boss = databaseManager.getBoss(ranks.replace("challange_", ""));
            Challange challange = new Challange(ranks);
            MagnesifyBoss magnesifyBoss = new MagnesifyBoss(boss);
            boolean is_material_set = get().getConfig().isSet("settings.challange.defaults.material");
            boolean is_custom_material_set = get().getConfig().isSet("settings.challange.defaults.custom-material");
            ItemStack itemStack = null;
            if(is_material_set) {
                itemStack = new ItemStack(Material.getMaterial(get().getConfig().getString("settings.challange.defaults.material")));
            } else if(is_custom_material_set) {
                itemStack = getCustomItem(get().getConfig().getString("settings.challange.defaults.custom-material"));
            } else {
                itemStack = new ItemStack(Material.PAPER);
            }
            ItemMeta meta = itemStack.getItemMeta();
            meta.setDisplayName(parseHexColors(get().getConfig().getString("settings.challange.defaults.display").replace("#dungeon", ranks.replace("challange_", ""))));
            List<String> main_lores = get().getConfig().getStringList("settings.challange.defaults.lore");
            List<String> sub_lore = new ArrayList<>();
            for(int a = 0; a<main_lores.size();a++) {
                if(magnesifyBoss.exists()) {
                    sub_lore.add(parseHexColors(main_lores.get(a).replace("#boss_name", magnesifyBoss.name())
                            .replace("#boss_health", String.valueOf(magnesifyBoss.health()))
                                    .replace("#play_time", String.valueOf(databaseManager.getPlayTime(ranks))).replace("#status", challange.getStatus() == true ? get().getConfig().getString("settings.holders.full") : get().getConfig().getString("settings.holders.empty"))));
                }
            }
            meta.setLore(sub_lore);
            itemStack.setItemMeta(meta);
            inv.setItem(i, itemStack);
            i++;
        }

    }

}
