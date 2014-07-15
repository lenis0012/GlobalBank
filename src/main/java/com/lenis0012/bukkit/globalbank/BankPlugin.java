package com.lenis0012.bukkit.globalbank;

import com.lenis0012.bukkit.globalbank.banker.Banker;
import com.lenis0012.bukkit.globalbank.banker.BankerManager;
import com.lenis0012.bukkit.globalbank.util.sorting.Sort;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by Lenny on 14-7-2014.
 */
public class BankPlugin extends JavaPlugin {
    private BankerManager bankerManager;
    private Sort sort;

    @Override
    public void onEnable() {
        //Load config
        saveDefaultConfig();

        //Load sort
        this.sort = Sort.valueOf(getConfig().getString("settings.sorting").toUpperCase());

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

    public Sort getSort() {
        return sort;
    }
}
