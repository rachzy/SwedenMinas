package com.redesweden.swedenminas.events;

import com.redesweden.swedenminas.SwedenMinas;
import com.redesweden.swedenminas.data.Players;
import com.redesweden.swedenminas.models.PlayerMina;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.scheduler.BukkitScheduler;

public class BlockBreakListener implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBreak(BlockBreakEvent e) {
        Player player = e.getPlayer();
        PlayerMina playerMina = Players.getPlayerPorNickname(player.getName());

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
