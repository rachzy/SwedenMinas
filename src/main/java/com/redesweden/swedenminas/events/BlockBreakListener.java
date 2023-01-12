package com.redesweden.swedenminas.events;

import com.redesweden.swedenminas.SwedenMinas;
import com.redesweden.swedenminas.data.Nevascas;
import com.redesweden.swedenminas.data.Players;
import com.redesweden.swedenminas.functions.GetBlocosPorPerto;
import com.redesweden.swedenminas.models.Nevasca;
import com.redesweden.swedenminas.models.PlayerMina;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class BlockBreakListener implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBreak(BlockBreakEvent e) {
        Player player = e.getPlayer();
        PlayerMina playerMina = Players.getPlayerPorNickname(player.getName());

        if(e.getBlock().getType() == Material.SNOW) {
            List<Block> blocosAoRedor = new GetBlocosPorPerto(e.getBlock().getLocation(), 2, true).getBlocos();

            AtomicReference<Nevasca> nevasca = new AtomicReference<>(null);
            blocosAoRedor.forEach((bloco) -> {
                if(nevasca.get() != null) return;
                Nevasca nevascaBloco = Nevascas.getNevascaPorLocal(bloco.getLocation());

                if(nevascaBloco == null) return;
                nevasca.set(nevascaBloco);
            });

            if(nevasca.get() != null) {
                e.setCancelled(true);
                if(!nevasca.get().getDono().equals(player.getName()) && !nevasca.get().getAmigos().contains(player.getName())) {
                    player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 3.0F, 0.5F);
                    player.sendMessage("§f§lNEVASCAS §e>> §cVocê não tem permissão para utilizar esta nevasca.");
                } else {
                    nevasca.get().quebrarNeve(player);
                }
            }
        }

        if(playerMina == null) return;

        Block blocoQuebrado = e.getBlock();
        if(player.getItemInHand().getType() != Material.DIAMOND_PICKAXE) return;
        if(playerMina.getMina().getBloco() == null) return;
        if((blocoQuebrado.getType() != playerMina.getMina().getBloco().getType() || blocoQuebrado.getData() != playerMina.getMina().getBloco().getData().getData())
                && blocoQuebrado.getType() != Material.GOLD_BLOCK) return;
        e.setCancelled(true);

        BukkitScheduler scheduler = Bukkit.getScheduler();
        scheduler.runTaskLater(SwedenMinas.getPlugin(SwedenMinas.class), () -> {
            blocoQuebrado.setType(Material.AIR);
        }, 1L);

        playerMina.quebrarBloco(blocoQuebrado.getType() == Material.GOLD_BLOCK, false, blocoQuebrado.getLocation());
    }
}
