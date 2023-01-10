package com.redesweden.swedenminas.events;

import com.redesweden.swedenminas.data.Picaretas;
import com.redesweden.swedenminas.files.PicaretasFile;
import com.redesweden.swedenminas.models.Picareta;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        Picareta picareta = Picaretas.getPicaretaPorDono(player.getName());

        if(picareta != null) return;
        PicaretasFile.novaPicareta(player);
    }
}
