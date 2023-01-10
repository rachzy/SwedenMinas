package com.redesweden.swedenminas.events;

import com.redesweden.swedenminas.data.Players;
import com.redesweden.swedenminas.models.PlayerMina;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

public class SwitchWorldListener implements Listener {
    @EventHandler
    public void onPlayerSwitchWorld(PlayerChangedWorldEvent e) {
        Player player = e.getPlayer();
        PlayerMina playerMina = Players.getPlayerPorNickname(player.getName());

        if(playerMina == null) return;
        playerMina.sairDaMina(false, false);
    }
}
