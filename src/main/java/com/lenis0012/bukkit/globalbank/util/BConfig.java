package com.lenis0012.bukkit.globalbank.util;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

/**
 * Created by Lenny on 14-7-2014.
 *
 * Configuration made easy.
 */
public class BConfig extends YamlConfiguration {
    private final File file;

    public BConfig(File file) {
        this.file = file;
        reload();
    }

    public void reload() {
        try {
            load(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void save() {
        try {
            save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
