package com.lenis0012.bukkit.globalbank.storage;

import com.lenis0012.bukkit.globalbank.BankPlugin;
import com.lenis0012.bukkit.globalbank.util.BConfig;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Lenny on 7/14/2014.
 */
public class BPlayer {
    private final BConfig config;
    private final int bankRows, slotRows;
    private ItemStack[][] banks;
    private PlayerStatus status;

    public BPlayer(UUID uuid) {
        BankPlugin plugin = JavaPlugin.getPlugin(BankPlugin.class);
        File dir = new File(plugin.getDataFolder(), "storage");
        dir.mkdirs();

        this.config = new BConfig(new File(dir, uuid.toString() + ".yml"));
        this.bankRows = plugin.getConfig().getInt("settings.bank-rows") * 9;
        this.slotRows = plugin.getConfig().getInt("settings.slot-rows") * 9 - 2;
        this.status = PlayerStatus.NONE;
        this.banks = new ItemStack[bankRows][slotRows];
    }

    private void load() {
        if(config.contains("banks")) {
            for(String key : config.getConfigurationSection("banks").getKeys(false)) {
                ConfigurationSection section = config.getConfigurationSection("banks." + key);
                int bank = Integer.parseInt(key);
                for(String key2 : section.getKeys(false)) {
                    int slot = Integer.parseInt(key2);
                    ItemStack item = config.getItemStack("banks." + key + "." + key2);
                    if(bank < bankRows && slot < slotRows) {
                        banks[bank][slot] = item;
                    }
                }
            }
        }
    }

    public void save() {
        for(int i = 0; i < bankRows; i++) {
            for(int j = 0; j < slotRows; j++) {
                ItemStack item = banks[i][j];
                if(item != null) {
                    config.set("banks." + i + "." + j, item);
                }
            }
        }

        config.save();
    }

    private static final Map<UUID, BPlayer> players = new HashMap<>();

    public static BPlayer get(UUID uuid) {
        if(players.containsKey(uuid)) {
            return players.get(uuid);
        } else {
            BPlayer bPlayer = new BPlayer(uuid);
            players.put(uuid, bPlayer);
            return bPlayer;
        }
    }

    public static void save(UUID uuid) {
        BPlayer bPlayer = players.remove(uuid);
        if(bPlayer != null) {
            bPlayer.save();
        }
    }

    public static enum PlayerStatus {
        NONE,
        DELETE,
        FACE,
        IN_BANK;
    }
}
