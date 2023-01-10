package com.redesweden.swedenminas.files;

import com.redesweden.swedenminas.data.Minas;
import com.redesweden.swedenminas.models.Mina;
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
            for (String rankID : minasFile.getConfigurationSection("minas").getKeys(false)) {
                Rank rank = Ranks.getRankPorId(rankID);
                BigDecimal valorPorBloco = new BigDecimal(minasFile.getString(String.format("minas.%s.valorPorBloco", rankID)));

                double x = minasFile.getDouble(String.format("minas.%s.spawn.x", rankID));
                double y = minasFile.getDouble(String.format("minas.%s.spawn.y", rankID));
                double z = minasFile.getDouble(String.format("minas.%s.spawn.z", rankID));
                String mundo = minasFile.getString(String.format("minas.%s.spawn.mundo", rankID));

                Location spawn = new Location(Bukkit.getWorld(mundo), x, y, z);

                ItemStack bloco = new ItemStack(Material.AIR);

                if(minasFile.getString(String.format("minas.%s.blocoID", rankID)) != null) {
                    int blocoID = minasFile.getInt(String.format("minas.%s.blocoID", rankID));
                    Integer blocoSubID = minasFile.getInt(String.format("minas.%s.blocoSubID", rankID));

                    bloco = new ItemStack(blocoID, 1, blocoSubID.shortValue());
                }

                Location pos1 = null;

                if (minasFile.getConfigurationSection(String.format("minas.%s.pos1", rankID)) != null) {
                    double posX = minasFile.getDouble(String.format("minas.%s.pos1.x", rankID));
                    double posY = minasFile.getDouble(String.format("minas.%s.pos1.y", rankID));
                    double posZ = minasFile.getDouble(String.format("minas.%s.pos1.z", rankID));
                    String posMundo = minasFile.getString(String.format("minas.%s.pos1.mundo", rankID));

                    pos1 = new Location(Bukkit.getWorld(posMundo), posX, posY, posZ);
                }

                Location pos2 = null;
                if (minasFile.getConfigurationSection(String.format("minas.%s.pos2", rankID)) != null) {
                    double posX = minasFile.getDouble(String.format("minas.%s.pos2.x", rankID));
                    double posY = minasFile.getDouble(String.format("minas.%s.pos2.y", rankID));
                    double posZ = minasFile.getDouble(String.format("minas.%s.pos2.z", rankID));
                    String posMundo = minasFile.getString(String.format("minas.%s.pos2.mundo", rankID));

                    pos2 = new Location(Bukkit.getWorld(posMundo), posX, posY, posZ);
                }

                Mina mina = new Mina(rank, valorPorBloco, spawn, bloco, pos1, pos2);
                Minas.addMina(mina);
                mina.iniciarResetAutomatico();
            }
        }
        save();
    }

    public static void novaMina(Rank rank, BigDecimal valorDoBloco, Location local) {
        minasFile.set(String.format("minas.%s.valorPorBloco", rank.getId()), valorDoBloco);
        minasFile.set(String.format("minas.%s.spawn.x", rank.getId()), local.getX());
        minasFile.set(String.format("minas.%s.spawn.y", rank.getId()), local.getY());
        minasFile.set(String.format("minas.%s.spawn.z", rank.getId()), local.getY());
        minasFile.set(String.format("minas.%s.spawn.mundo", rank.getId()), local.getWorld().getName());

        Mina mina = new Mina(rank, valorDoBloco, local, null, null, null);
        Minas.addMina(mina);
        mina.iniciarResetAutomatico();

        save();

        System.out.println("[SwedenMinas] Nova mina criada: " + rank.getId());
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
