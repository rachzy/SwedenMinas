package com.redesweden.swedenminas.files;

import com.redesweden.swedenminas.data.Nevascas;
import com.redesweden.swedenminas.models.Nevasca;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class NevascasFile {
    private static File file;
    private static FileConfiguration nevascasFile;

    public static void setup() {
        file = new File(Bukkit.getPluginManager().getPlugin("SwedenMinas").getDataFolder(), "nevascas.yml");

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (Exception e) {
                System.out.println("Não foi possível criar o arquivo nevascas.yml..." + e.getMessage());
                return;
            }
        }

        nevascasFile = YamlConfiguration.loadConfiguration(file);

        if (nevascasFile.getConfigurationSection("nevascas") != null) {
            for (String id : nevascasFile.getConfigurationSection("nevascas").getKeys(false)) {
                String dono = nevascasFile.getString(String.format("nevascas.%s.dono", id));
                int level = nevascasFile.getInt(String.format("nevascas.%s.level", id));

                List<String> amigos = new ArrayList<>();

                if(nevascasFile.getStringList(String.format("nevascas.%s.amigos", id)) != null) {
                    amigos = nevascasFile.getStringList(String.format("nevascas.%s.amigos", id));
                }
                
                double x = nevascasFile.getDouble(String.format("nevascas.%s.local.x", id));
                double y = nevascasFile.getDouble(String.format("nevascas.%s.local.y", id));
                double z = nevascasFile.getDouble(String.format("nevascas.%s.local.z", id));
                String mundo = nevascasFile.getString(String.format("nevascas.%s.local.mundo", id));

                Location local = new Location(Bukkit.getWorld(mundo), x, y, z);

                Nevasca nevasca = new Nevasca(id, dono, level, amigos, local);
                Nevascas.addNevasca(nevasca);
                nevasca.iniciar();
            }
        }
        
        Nevascas.iniciarSalvamentoAutomatico();
        save();
    }

    public static void novaNevasca(Player player, int level, Location local) {
        String uuid = player.getUniqueId().toString();
        nevascasFile.set(String.format("nevascas.%s.dono", uuid), player.getName());
        nevascasFile.set(String.format("nevascas.%s.level", uuid), level);
        nevascasFile.set(String.format("nevascas.%s.local.x", uuid), local.getX());
        nevascasFile.set(String.format("nevascas.%s.local.y", uuid), local.getY());
        nevascasFile.set(String.format("nevascas.%s.local.z", uuid), local.getZ());
        nevascasFile.set(String.format("nevascas.%s.local.mundo", uuid), local.getWorld().getName());

        Nevasca nevasca = new Nevasca(uuid, player.getName(), level, new ArrayList<>(), local);
        Nevascas.addNevasca(nevasca);
        nevasca.iniciar();

        save();

        System.out.println("[SwedenMinas] Nova nevasca criada: " + uuid);
    }

    public static void removerNevasca(Nevasca nevasca) {
        nevascasFile.set(String.format("nevascas.%s", nevasca.getId()), null);
        Nevascas.removerNevasca(nevasca);
        Nevascas.removerNevascaAlterada(nevasca);
        save();
    }

    public static FileConfiguration get() {
        return nevascasFile;
    }

    public static void save() {
        try {
            nevascasFile.save(file);
        } catch (Exception e) {
            System.out.println("Não foi possível salvar o arquivo nevascas.yml... " + e.getMessage());
        }
    }
}
