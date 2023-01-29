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

        boolean blocoProximo = false;
        List<Block> blocosPorPerto = new GetBlocosPorPerto(e.getBlockPlaced().getLocation().clone(), 3, false, true).getBlocos();

        for(Block bloco : blocosPorPerto) {
            if(bloco != null
                    && bloco.getType() != Material.AIR
                    && (bloco.getLocation().getX() != e.getBlockPlaced().getLocation().getX()
                    || bloco.getLocation().getY() != e.getBlockPlaced().getLocation().getY()
                    || bloco.getLocation().getZ() != e.getBlockPlaced().getLocation().getZ())
            ) {
                System.out.println(bloco.getType());
                blocoProximo = true;
                break;
            }
        }

        if(blocoProximo) {
            e.setCancelled(true);
            player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 3.0F, 0.5F);
            player.sendMessage("§f§lNEVASCAS §e>> §cVocê está muito próximo de um outro bloco ou do limite da sua plot. Por favor, coloque sua Nevasca em um lugar mais vazio.");
            return;
        }

        Bukkit.getScheduler().runTaskLater(SwedenMinas.getPlugin(SwedenMinas.class), () -> {
            e.getBlockPlaced().setType(Material.AIR);
        }, 1L);

        NevascasFile.novaNevasca(player, level, local);

        player.playSound(player.getLocation(), Sound.LEVEL_UP, 3.0F, 2F);
        player.sendMessage("§f§lNEVASCAS §e>> §aVocê colocou uma nova Nevasca. Aproveite!");
    }
}
