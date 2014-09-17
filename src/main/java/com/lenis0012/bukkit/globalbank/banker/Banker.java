package com.lenis0012.bukkit.globalbank.banker;

import com.lenis0012.bukkit.globalbank.BankPlugin;
import com.lenis0012.bukkit.npc.NPC;
import com.lenis0012.bukkit.npc.NPCFactory;
import com.lenis0012.bukkit.npc.NPCProfile;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by Lenny on 14-7-2014.
 */
public class Banker {
    private final NPC npc;
    private final String name;

    public Banker(NPC npc, String name) {
        this.npc = npc;
        this.name = name;
    }

    public NPC getNpc() {
        return npc;
    }

    public String getName() {
        return name;
    }

    public void save(FileConfiguration config) {
        Location location = npc.getBukkitEntity().getLocation();
        config.set("bankers." + name + ".world", location.getWorld().getName());
        config.set("bankers." + name + ".x", location.getX());
        config.set("bankers." + name + ".y", location.getY());
        config.set("bankers." + name + ".z", location.getZ());
        config.set("bankers." + name + ".yaw", (double) location.getYaw());
    }

    public static Banker loadFromConfig(NPCFactory npcFactory, ConfigurationSection section) {
        BankPlugin plugin = JavaPlugin.getPlugin(BankPlugin.class);
        FileConfiguration config = plugin.getConfig();
        String tag = config.getString("settings.banker-nametag");
//        String skin = config.getString("settings.banker-skin");

        String name = section.getName();
        World world = Bukkit.getWorld(section.getString("world"));
        double x = section.getDouble("x");
        double y = section.getDouble("y");
        double z = section.getDouble("z");
        float yaw = (float) section.getDouble("yaw");

//        NPCProfile profile = new NPCProfile(tag, skin);
        NPCProfile profile = new NPCProfile(tag);
        NPC npc = npcFactory.spawnHumanNPC(new Location(world, x, y, z, yaw, 0F), profile);
        npc.setEntityCollision(false);
        npc.setYaw(yaw);
        npc.setGodmode(true);
        npc.setGravity(true);

        return new Banker(npc, name);
    }
}
