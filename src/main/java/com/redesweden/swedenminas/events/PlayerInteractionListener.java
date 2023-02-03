package com.redesweden.swedenminas.events;

import com.redesweden.swedenminas.GUIs.PicaretaGUI;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerInteractionListener implements Listener {
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        if(e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        Player player = e.getPlayer();

        if(!player.getItemInHand().hasItemMeta() || !player.getItemInHand().getItemMeta().hasDisplayName() || !player.getItemInHand().getItemMeta().getDisplayName().startsWith("§e§lPICARETA")) return;
        if(player.getName().startsWith(".") && !player.isSneaking()) return;
        player.playSound(player.getLocation(), Sound.CLICK, 3.0F, 2F);
        player.openInventory(new PicaretaGUI(player.getName()).get());
    }
}
