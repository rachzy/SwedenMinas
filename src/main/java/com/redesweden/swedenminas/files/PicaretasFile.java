package com.redesweden.swedenminas.files;

import com.redesweden.swedenminas.data.Picaretas;
import com.redesweden.swedenminas.models.Picareta;
import com.redesweden.swedenminas.types.Level;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PicaretasFile {
    private static File file;
    private static FileConfiguration picaretasFile;

    public static void setup() {
        file = new File(Bukkit.getPluginManager().getPlugin("SwedenMinas").getDataFolder(), "picaretas.yml");

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (Exception e) {
                System.out.println("Não foi possível criar o arquivo picaretas.yml... " + e.getMessage());
                return;
            }
        }

        picaretasFile = YamlConfiguration.loadConfiguration(file);

        if (picaretasFile.getConfigurationSection("picaretas") != null) {
            for (String uuid : picaretasFile.getConfigurationSection("picaretas").getKeys(false)) {
                String dono = picaretasFile.getString(String.format("picaretas.%s.dono", uuid));
                int nivel = picaretasFile.getInt(String.format("picaretas.%s.nivel", uuid));
                BigDecimal energia = new BigDecimal(picaretasFile.getString(String.format("picaretas.%s.energia", uuid)));
                List<String> leveis = picaretasFile.getStringList(String.format("picaretas.%s.leveis", uuid));

                Picareta picareta = new Picareta(uuid, dono, nivel, energia);

                leveis.forEach((level) -> {
                    String[] levelSplit = level.split(",");
                    Level levelTipo = Level.valueOf(levelSplit[0]);
                    int levelValor = Integer.parseInt(levelSplit[1]);

                    picareta.setarLevel(levelTipo, levelValor);
                });

                Picaretas.addPicareta(picareta);
            }
        }

        save();
        Picaretas.iniciarSalvamentoAutomatico();
    }

    public static void novaPicareta(Player player) {
        System.out.println("[SwedenMinas] Registrando nova picareta do jogador " + player.getName());
        String uuid = player.getUniqueId().toString();
        picaretasFile.set(String.format("picaretas.%s.dono", uuid), player.getName());
        picaretasFile.set(String.format("picaretas.%s.nivel", uuid), 0);
        picaretasFile.set(String.format("picaretas.%s.energia", uuid), 0);

        Picareta picareta = new Picareta(player.getUniqueId().toString(), player.getName(), 0, new BigDecimal("0"));

        List<String> leveis = new ArrayList<>();

        Arrays.stream(Level.values()).forEach((level) -> {
            int valor = 0;
            if (level == Level.EFICIENCIA) {
                valor = 20;
            }

            picareta.setarLevel(level, valor);
            leveis.add(String.format("%s,%s", level, valor));
        });

        Picaretas.addPicareta(picareta);
        picaretasFile.set(String.format("picaretas.%s.leveis", uuid), leveis);
        save();
    }

    public static FileConfiguration get() {
        return picaretasFile;
    }

    public static void save() {
        try {
            picaretasFile.save(file);
        } catch (Exception e) {
            System.out.println("Não foi possível salvar o arquivo picaretas.yml... " + e.getMessage());
        }
    }
}
