package com.redesweden.swedenminas.events;

import com.redesweden.swedenminas.SwedenMinas;
import com.redesweden.swedenminas.data.Nevascas;
import com.redesweden.swedenminas.files.NevascasFile;
import com.redesweden.swedenminas.functions.GetBlocosPorPerto;
import com.redesweden.swedenminas.models.Nevasca;
import io.github.bananapuncher714.nbteditor.NBTEditor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class BlockPlaceListener implements Listener {
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        if(e.isCancelled()) return;

        Player player = e.getPlayer();
        if(!player.getItemInHand().hasItemMeta()
                || !player.getItemInHand().getItemMeta().hasDisplayName()
                || !player.getItemInHand().getItemMeta().getDisplayName().equals("§b§lNEVASCA"))
            return;

        Location local = e.getBlockPlaced().getLocation();

        int level = Integer.parseInt(Arrays.stream(player.getItemInHand().getItemMeta().getLore().get(0).split(" ")).toArray()[1].toString().substring(2));

        if(Nevascas.getNevascaPorDono(player.getName()) != null) {
            e.setCancelled(true);
            player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 3.0F, 0.5F);
            player.sendMessage("§f§lNEVASCAS §e>> §cVocê só pode ter 1 nevasca!");
            return;
        }

        Nevasca nevascaProxima = null;
        List<Block> blocosPorPerto = new GetBlocosPorPerto(player.getLocation(), 7, true).getBlocos();

        for(Block bloco : blocosPorPerto) {
            Nevasca nevascaLocal = Nevascas.getNevascaPorLocal(bloco.getLocation());
            if(nevascaLocal != null) {
                nevascaProxima = nevascaLocal;
                break;
            }
        }

        if(nevascaProxima != null) {
            e.setCancelled(true);
            player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 3.0F, 0.5F);
            player.sendMessage("§f§lNEVASCAS §e>> §cExiste uma nevasca muito próxima desta.");
            return;
        }

        Bukkit.getScheduler().runTaskLater(SwedenMinas.getPlugin(SwedenMinas.class), () -> {
            e.getBlockPlaced().setType(Material.AIR);
        }, 1L);

        NevascasFile.novaNevasca(player, level, local);

        Entity snowGolem = Bukkit.getWorld(local.getWorld().getName()).spawnEntity(local.clone().add(0.5, 0, 0.5), EntityType.SNOWMAN);
        NBTEditor.set(snowGolem, true, "NoAI");
        NBTEditor.set(snowGolem, true, "Invulnerable");

        player.playSound(player.getLocation(), Sound.LEVEL_UP, 3.0F, 2F);
        player.sendMessage("§f§lNEVASCAS §e>> §aVocê colocou uma nova Nevasca. Aproveite!");
    }
}
