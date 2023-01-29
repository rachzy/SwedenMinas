package com.redesweden.swedenminas.models;

import com.connorlinfoot.actionbarapi.ActionBarAPI;
import com.redesweden.swedenminas.SwedenMinas;
import com.redesweden.swedenminas.data.Nevascas;
import com.redesweden.swedenminas.files.NevascasFile;
import com.redesweden.swedenminas.functions.GetBlocosPorPerto;
import com.redesweden.swedenminas.types.RecompensaTipo;
import eu.decentsoftware.holograms.api.DHAPI;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import io.github.bananapuncher714.nbteditor.NBTEditor;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Nevasca {
    private final String id;
    private final String dono;
    private final List<String> amigos;
    private final Location local;
    private int level;
    private Hologram holograma;

    public Nevasca(String id, String dono, int level, List<String> amigos, Location local) {
        this.id = id;
        this.dono = dono;
        this.level = level;
        this.amigos = amigos;
        this.local = local;
    }

    public void salvarDados() {
        NevascasFile.get().set(String.format("nevascas.%s.amigos", id), amigos);
        NevascasFile.get().set(String.format("nevascas.%s.level", id), level);
        NevascasFile.save();
    }

    public String getId() {
        return id;
    }

    public String getDono() {
        return dono;
    }

    public int getLevel() {
        return level;
    }

    public void addLevel() {
        this.level += 1;
        Nevascas.addNevascaAlterada(this);
        DHAPI.setHologramLine(holograma, 0, 1, String.format("§7Level: §e%s", level));
    }

    public List<String> getAmigos() {
        return amigos;
    }

    public void addAmigo(String nickname) {
        amigos.add(nickname);
        Nevascas.addNevascaAlterada(this);
    }

    public void removerAmigo(String nickname) {
        amigos.remove(nickname);
        Nevascas.addNevascaAlterada(this);
    }

    public Location getLocal() {
        return local;
    }

    public ItemStack getNevasca() {
        ItemStack neve = new ItemStack(Material.SNOW_BLOCK, 1);
        ItemMeta neveMeta = neve.getItemMeta();
        neveMeta.setDisplayName("§b§lNEVASCA");

        List<String> loreNeve = new ArrayList<>();
        loreNeve.add("§aLevel: §e" + level);
        loreNeve.add("");
        loreNeve.add("§eA Nevasca é um item §c§lLENDÁRIO");
        loreNeve.add("");
        loreNeve.add("§7Coloque essa nevasca em um terreno");
        loreNeve.add("§7e tenha acesso à sua própria escavação");
        loreNeve.add("§7privada, que gera MUITOS itens §braros§7.");
        neveMeta.setLore(loreNeve);

        neve.setItemMeta(neveMeta);

        return neve;
    }

    public void setarHolograma() {
        holograma = DHAPI.getHologram(id);

        if (holograma != null) {
            holograma.delete();
        }

        holograma = DHAPI.createHologram(id, local.clone().add(0.5, 3.2, 0.5));
        DHAPI.addHologramLine(holograma, "§b§lNEVASCA");
        DHAPI.addHologramLine(holograma, "§7Level: §e" + level);
        DHAPI.addHologramLine(holograma, "§7Dono: §6" + dono);
        DHAPI.addHologramLine(holograma, "§aQuebre as neves para ganhar recompensas");
    }

    public void quebrarNeve(Player player) {
        ItemStack recompensa = new Recompensa(level, RecompensaTipo.NEVASCA).gerar();
        String itemTitulo;

        if (recompensa.hasItemMeta()) {
            itemTitulo = recompensa.getItemMeta().getDisplayName();
        } else {
            itemTitulo = "§enada :(";
        }

        ActionBarAPI.sendActionBar(player, String.format("§f§lNEVASCAS §e>> §aVocê quebrou uma neve e ganhou %s", itemTitulo));

        HashMap<Integer, ItemStack> itemNaoCoube = player.getInventory().addItem(recompensa);

        if (itemNaoCoube != null && itemNaoCoube.size() > 0) {
            player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 3.0F, 0.5F);
            player.sendMessage("§f§lNEVASCAS §e>> §cVocê não tem mais espaço suficiente em seu inventário, esvazie-o.");
        }
    }

    public void iniciar() {
        setarHolograma();

        List<Block> blocosAoRedor = new GetBlocosPorPerto(local, 2, true, false).getBlocos();
        blocosAoRedor.forEach(bloco -> {
            if (bloco.getLocation().getY() != local.getY()) return;

            Bukkit.getScheduler().runTaskLater(SwedenMinas.getPlugin(SwedenMinas.class), () -> {
                bloco.getLocation().clone().subtract(0, 1, 0).getBlock().setType(Material.SNOW_BLOCK);
                bloco.setType(Material.SNOW);
            }, 1L);
        });

        Bukkit.getScheduler().runTaskLater(SwedenMinas.getPlugin(SwedenMinas.class), () -> {
            boolean snowManSpawnado = false;

            for (Entity entidade : Bukkit.getWorld(local.getWorld().getName()).getNearbyEntities(local, 1, 1, 1)) {
                if (entidade.getType() == EntityType.SNOWMAN) {
                    snowManSpawnado = true;
                    break;
                }
            }

            if (!snowManSpawnado) {
                Entity snowGolem = Bukkit.getWorld(local.getWorld().getName()).spawnEntity(local.clone().add(0.5, 0, 0.5), EntityType.SNOWMAN);
                NBTEditor.set(snowGolem, true, "NoAI");
                NBTEditor.set(snowGolem, true, "Invulnerable");
            }
        }, 5L);
    }

    public void desativar(Player dono) {
        if (this.holograma != null) {
            this.holograma.delete();
        }

        for (Entity entidade : dono.getNearbyEntities(3, 2, 3)) {
            if (entidade.getType() == EntityType.SNOWMAN) {
                entidade.remove();
                break;
            }
        }

        dono.getInventory().addItem(this.getNevasca());
        dono.playSound(dono.getLocation(), Sound.NOTE_PLING, 3.0F, 2F);
        dono.sendMessage("§f§lNEVASCAS §e>> §aVocê removeu sua Nevasca.");

        List<Block> blocosAoRedor = new GetBlocosPorPerto(local, 2, true, false).getBlocos();
        blocosAoRedor.forEach(bloco -> {
            if (bloco.getLocation().getY() != local.getY()) return;

            Bukkit.getScheduler().runTaskLater(SwedenMinas.getPlugin(SwedenMinas.class), () -> {
                bloco.setType(Material.AIR);
            }, 1L);
        });
    }
}
