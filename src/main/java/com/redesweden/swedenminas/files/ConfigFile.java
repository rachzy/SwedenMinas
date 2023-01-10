package com.redesweden.swedenminas.files;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class ConfigFile {
    private static File file;
    private static FileConfiguration configFile;
    private static String ip;

    public static void setup() {
        file = new File(Bukkit.getPluginManager().getPlugin("SwedenMinas").getDataFolder(), "config.yml");

        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch (Exception e) {
                System.out.println("Não foi possível criar o arquivo config.yml..." + e.getMessage());
                return;
            }
        }

        configFile = YamlConfiguration.loadConfiguration(file);
        ip = configFile.getString("ip");
    }

    public static FileConfiguration get() {
        return configFile;
    }

    public static String getIP() {
        return ip;
    }

    public static void save() {
        try {
            configFile.save(file);
        } catch (Exception e) {
            System.out.println("Não foi possível salvar o arquivo config.yml... " + e.getMessage());
        }
    }

    public static void reload() {
        configFile = YamlConfiguration.loadConfiguration(file);
        ip = configFile.getString("ip");
    }
}
