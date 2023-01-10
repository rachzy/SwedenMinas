package com.redesweden.swedenminas.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class PlayerDropListener implements Listener {
    @EventHandler
    public void onPlayerDrop(PlayerDropItemEvent e) {
        if(!e.getItemDrop().getItemStack().hasItemMeta()) return;
        if(!e.getItemDrop().getItemStack().getItemMeta().hasDisplayName()) return;
        if(!e.getItemDrop().getItemStack().getItemMeta().getDisplayName().startsWith("§e§lPICARETA")) return;
        e.setCancelled(true);
    }
}
