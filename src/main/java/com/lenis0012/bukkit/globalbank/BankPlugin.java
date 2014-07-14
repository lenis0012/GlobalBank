package com.lenis0012.bukkit.globalbank;

import com.lenis0012.bukkit.globalbank.banker.Banker;
import com.lenis0012.bukkit.globalbank.banker.BankerManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by Lenny on 14-7-2014.
 */
public class BankPlugin extends JavaPlugin {
    private BankerManager bankerManager;

    @Override
    public void onEnable() {
        //Load config
        saveDefaultConfig();

        //Load bankers
        this.bankerManager = new BankerManager(this);

        //Register command
        getCommand("globalbank").setExecutor(new BankCommandExecutor(bankerManager));
    }

    @Override
    public void onDisable() {
        //Save all bankers
        bankerManager.save();

        //Kill all bankers
        for(Banker banker : bankerManager.getBankers()) {
            banker.getNpc().getBukkitEntity().remove();
        }
    }

    public BankerManager getBankerManager() {
        return bankerManager;
    }
}
