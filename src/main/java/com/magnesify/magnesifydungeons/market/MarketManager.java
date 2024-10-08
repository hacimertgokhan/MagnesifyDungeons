package com.magnesify.magnesifydungeons.market;

import com.magnesify.magnesifydungeons.MagnesifyDungeons;
import com.magnesify.magnesifydungeons.dungeon.entitys.DungeonConsole;
import com.magnesify.magnesifydungeons.dungeon.entitys.DungeonEntity;
import com.magnesify.magnesifydungeons.dungeon.entitys.DungeonPlayer;
import com.magnesify.magnesifydungeons.languages.LanguageFile;
import com.magnesify.magnesifydungeons.market.file.MarketFile;
import com.magnesify.magnesifydungeons.market.gui.MarketGuiLoader;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static com.magnesify.magnesifydungeons.modules.Defaults.TEXT_PREFIX;

public class MarketManager implements CommandExecutor {

    public MarketManager(MagnesifyDungeons magnesifyDungeons) {}

    public void help(CommandSender sender) {
        if(sender instanceof Player) {
            Player player = (Player) sender;
            DungeonPlayer dungeonPlayer = new DungeonPlayer(player);
            for(String messages : new LanguageFile().getLanguage().getStringList("messages.helps.market")) {
                dungeonPlayer.messageManager().chat(messages);
            }
        } else {
            DungeonConsole dungeonConsole = new DungeonConsole(sender);
            for(String messages : new LanguageFile().getLanguage().getStringList("messages.helps.market")) {
                dungeonConsole.ConsoleOutputManager().write(messages);
            }
        }
    }


    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        DungeonEntity dungeonEntity = new DungeonEntity(commandSender);
        if (strings.length == 0) {
            if (commandSender instanceof Player) {
                Player player = (Player) commandSender;
                MarketGuiLoader.openInventory(player, 1);
            } else {
                dungeonEntity.EntityChatManager().send(new LanguageFile().getLanguage().getString("messages.in-game-command"));
            }
        } else if (strings.length == 1) {
            if (commandSender.hasPermission("mgd.admin")) {
                if (strings[0].equalsIgnoreCase("reload")) {
                    MarketFile marketFile = new MarketFile();
                    marketFile.saveKitsConfig();
                    dungeonEntity.EntityChatManager().send(String.format(new LanguageFile().getLanguage().getString("plugin.market-reloaded"), TEXT_PREFIX));
                } else {
                    if (commandSender instanceof Player) {
                        Player player = (Player) commandSender;
                        MarketGuiLoader.openInventory(player, 1);
                    } else {
                        dungeonEntity.EntityChatManager().send(new LanguageFile().getLanguage().getString("messages.in-game-command"));
                    }
                }
            } else {
                dungeonEntity.EntityChatManager().send(new LanguageFile().getLanguage().getString("messages.no-permission"));
            }
        } else {
            help(commandSender);
        }
        return false;
    }
}
