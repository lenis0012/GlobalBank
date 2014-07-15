package com.lenis0012.bukkit.globalbank.storage;

import com.lenis0012.bukkit.globalbank.BankPlugin;
import com.lenis0012.bukkit.globalbank.util.BConfig;
import com.lenis0012.bukkit.globalbank.util.Simple;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Collection;
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
    private int ownedSlots;

    public BPlayer(UUID uuid) {
        BankPlugin plugin = JavaPlugin.getPlugin(BankPlugin.class);
        File dir = new File(plugin.getDataFolder(), "storage");
        dir.mkdirs();

        this.config = new BConfig(new File(dir, uuid.toString() + ".yml"));
        this.bankRows = plugin.getConfig().getInt("settings.bank-rows") * 9;
        this.slotRows = plugin.getConfig().getInt("settings.slot-rows") * 9 - 2;
        this.ownedSlots = config.getInt("owned-slots", plugin.getConfig().getInt("settings.default-slots"));
        this.status = PlayerStatus.NONE;
        this.banks = new ItemStack[bankRows][slotRows];
        load();
    }

    public Inventory openBank(Player player) {
        ItemStack[] contents = new ItemStack[bankRows];
        for(int i = 0; i < contents.length; i++) {
            if(i < ownedSlots) {
                contents[i] = Simple.item(Material.CHEST, 1, "Slot " + (i + 1));
            } else {
                contents[i] = Simple.item(Material.PAPER, 1, "Purchase slot");
            }
        }

        Inventory inventory = Bukkit.createInventory(player, bankRows, "Your bank");
        inventory.setContents(contents);
        player.openInventory(inventory);
        return inventory;
    }

    public Inventory openSlot(Player player, int id) {
        ItemStack[] contents = new ItemStack[slotRows + 2];
        contents[0] = Simple.item(Material.CHEST, 1, "Back to bank");
        contents[1] = Simple.item(Material.PAPER, 1, "Sort items");
        for(int i = 0; i < slotRows; i++) {
            contents[i + 2] = banks[id][i];
        }

        Inventory inventory = Bukkit.createInventory(player, slotRows + 2, "Slot " + (id + 1));
        inventory.setContents(contents);
        player.openInventory(inventory);
        return inventory;
    }

    public void closeSlot(Inventory inventory, int slot) {
        for(int i = 0; i < slotRows; i++) {
            banks[slot][i] = inventory.getItem(i + 2);
        }
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

    public PlayerStatus getStatus() {
        return status;
    }

    public void setStatus(PlayerStatus status) {
        this.status = status;
    }

    public void save() {
        int bankEntries = 0;
        for(int i = 0; i < bankRows; i++) {
            int slotEntries = 0;
            for(int j = 0; j < slotRows; j++) {
                ItemStack item = banks[i][j];
                config.set("banks." + i + "." + j, item);
                if(item != null) {
                    slotEntries += 1;
                }
            }
            
            if(slotEntries == 0) {
                config.set("banks." + i, null);
            } else {
                bankEntries += 1;
            }
        } if(bankEntries == 0) {
            config.set("banks", null);
        }

        config.set("ownedSlots", ownedSlots);
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

    public static Collection<BPlayer> all() {
        return players.values();
    }

    public static enum PlayerStatus {
        NONE,
        DELETE,
        FACE,
        IN_BANK,
        IN_SLOT;
    }
}
