package com.magnesify.magnesifydungeons.boss.events;

import com.magnesify.magnesifydungeons.MagnesifyDungeons;
import com.magnesify.magnesifydungeons.boss.MagnesifyBoss;
import com.magnesify.magnesifydungeons.dungeon.entitys.DungeonPlayer;
import com.magnesify.magnesifydungeons.languages.LanguageFile;
import com.magnesify.magnesifydungeons.modules.Defaults;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.HashMap;
import java.util.UUID;

public class BossCreateEvent implements Listener {

    public static HashMap<UUID, Integer> bossSystemLevel = new HashMap<>();
    public BossCreateEvent(MagnesifyDungeons magnesifyDungeons) {}
    public static HashMap<String, String> bossdata = new HashMap<>();

    @EventHandler
    public void onDungeonCreateEvent(AsyncPlayerChatEvent event) {
        if(bossSystemLevel.get(event.getPlayer().getUniqueId()) != null) {
            DungeonPlayer dungeonPlayer = new DungeonPlayer(event.getPlayer());
            int level = bossSystemLevel.get(event.getPlayer().getUniqueId());
            switch (level) {
                case 1:
                    bossdata.put("Name", event.getMessage());
                    event.setCancelled(true);
                    dungeonPlayer.messageManager().chat(new LanguageFile().getLanguage().getString("messages.boss.creation").replace("#done", Defaults.name).replace("#next", Defaults.boss_id));
                    bossSystemLevel.put(event.getPlayer().getUniqueId(), level + 1);
                    break;
                case 2:
                    bossdata.put("ID", event.getMessage());
                    event.setCancelled(true);
                    bossSystemLevel.remove(event.getPlayer().getUniqueId());
                    MagnesifyBoss create_boss = new MagnesifyBoss(bossdata.get("Name"), bossdata.get("ID"));
                    if(create_boss.create()) {
                        dungeonPlayer.messageManager().chat(new LanguageFile().getLanguage().getString("messages.boss.created").replace("#id", bossdata.get("ID")).replace("#name", bossdata.get("Name")));
                    } else {
                        dungeonPlayer.messageManager().chat(new LanguageFile().getLanguage().getString("messages.error.cannot-created").replace("#id", bossdata.get("ID")).replace("#name", bossdata.get("Name")));
                    }
                    bossdata.clear();
                    break;
            }
        }
    }
}
