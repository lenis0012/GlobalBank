package com.lenis0012.bukkit.globalbank;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by Lenny on 14-7-2014.
 */
public class BankPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        //Load config
        saveDefaultConfig();

        //Register command
        getCommand("globalbank").setExecutor(new BankCommandExecutor());
    }
}
