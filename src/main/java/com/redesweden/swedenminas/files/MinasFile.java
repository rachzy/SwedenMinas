package com.redesweden.swedenminas.files;

import com.redesweden.swedenminas.SwedenMinas;
import com.redesweden.swedenminas.data.Minas;
import com.redesweden.swedenminas.models.Mina;
import com.redesweden.swedenminas.types.MinaTipo;
import com.redesweden.swedenranks.data.Ranks;
import com.redesweden.swedenranks.models.Rank;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.math.BigDecimal;

public class MinasFile {
    private static File file;
    private static FileConfiguration minasFile;

    public static void setup() {
        file = new File(Bukkit.getPluginManager().getPlugin("SwedenMinas").getDataFolder(), "minas.yml");

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (Exception e) {
                System.out.println("Não foi possível criar o arquivo minas.yml..." + e.getMessage());
                return;
            }
        }

        minasFile = YamlConfiguration.loadConfiguration(file);

        if (minasFile.getConfigurationSection("minas") != null) {
            int index = 0;
            for (String id : minasFile.getConfigurationSection("minas").getKeys(false)) {
                String titulo = minasFile.getString(String.format("minas.%s.titulo", id));
                MinaTipo tipo = MinaTipo.valueOf(minasFile.getString(String.format("minas.%s.tipo", id)));
                BigDecimal valorPorBloco = new BigDecimal(minasFile.getString(String.format("minas.%s.valorPorBloco", id)));

                double x = minasFile.getDouble(String.format("minas.%s.spawn.x", id));
                double y = minasFile.getDouble(String.format("minas.%s.spawn.y", id));
                double z = minasFile.getDouble(String.format("minas.%s.spawn.z", id));
                String mundo = minasFile.getString(String.format("minas.%s.spawn.mundo", id));

                Location spawn = new Location(Bukkit.getWorld(mundo), x, y, z);

                ItemStack bloco = new ItemStack(Material.AIR);

                if(minasFile.getString(String.format("minas.%s.blocoID", id)) != null) {
                    int blocoID = minasFile.getInt(String.format("minas.%s.blocoID", id));
                    Integer blocoSubID = minasFile.getInt(String.format("minas.%s.blocoSubID", id));

                    bloco = new ItemStack(blocoID, 1, blocoSubID.shortValue());
                }

                Location pos1 = null;

                if (minasFile.getConfigurationSection(String.format("minas.%s.pos1", id)) != null) {
                    double posX = minasFile.getDouble(String.format("minas.%s.pos1.x", id));
                    double posY = minasFile.getDouble(String.format("minas.%s.pos1.y", id));
                    double posZ = minasFile.getDouble(String.format("minas.%s.pos1.z", id));
                    String posMundo = minasFile.getString(String.format("minas.%s.pos1.mundo", id));

                    pos1 = new Location(Bukkit.getWorld(posMundo), posX, posY, posZ);
                }

                Location pos2 = null;
                if (minasFile.getConfigurationSection(String.format("minas.%s.pos2", id)) != null) {
                    double posX = minasFile.getDouble(String.format("minas.%s.pos2.x", id));
                    double posY = minasFile.getDouble(String.format("minas.%s.pos2.y", id));
                    double posZ = minasFile.getDouble(String.format("minas.%s.pos2.z", id));
                    String posMundo = minasFile.getString(String.format("minas.%s.pos2.mundo", id));

                    pos2 = new Location(Bukkit.getWorld(posMundo), posX, posY, posZ);
                }

                Mina mina = new Mina(id, titulo, tipo, valorPorBloco, bloco, spawn, pos1, pos2);
                Minas.addMina(mina);

                mina.resetar();
                Bukkit.getScheduler().runTaskLater(SwedenMinas.getPlugin(SwedenMinas.class), mina::iniciarResetAutomatico, 20L * 20L * ((long) index));
                index++;
            }
        }
        save();
    }

    public static void novaMina(String id, String titulo, MinaTipo tipo, BigDecimal valorDoBloco, Location local) {
        minasFile.set(String.format("minas.%s.titulo", id), titulo);
        minasFile.set(String.format("minas.%s.tipo", id), tipo.toString());
        minasFile.set(String.format("minas.%s.valorPorBloco", id), valorDoBloco);
        minasFile.set(String.format("minas.%s.spawn.x", id), local.getX());
        minasFile.set(String.format("minas.%s.spawn.y", id), local.getY());
        minasFile.set(String.format("minas.%s.spawn.z", id), local.getZ());
        minasFile.set(String.format("minas.%s.spawn.mundo", id), local.getWorld().getName());

        Mina mina = new Mina(id, titulo, tipo, valorDoBloco, null, local, null, null);
        Minas.addMina(mina);
        mina.iniciarResetAutomatico();

        save();

        System.out.println("[SwedenMinas] Nova mina criada: " + id);
    }

    public static FileConfiguration get() {
        return minasFile;
    }

    public static void save() {
        try {
            minasFile.save(file);
        } catch (Exception e) {
            System.out.println("Não foi possível salvar o arquivo minas.yml... " + e.getMessage());
        }
    }
}
