package com.redesweden.swedenminas.events;

import com.redesweden.swedenminas.data.EventosEspeciais;
import com.redesweden.swedenminas.data.Players;
import com.redesweden.swedenminas.models.PlayerMina;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();

        EventosEspeciais.removerPlayerAdicionandoAmigo(player);

        PlayerMina playerMina = Players.getPlayerPorNickname(player.getName());

        if(playerMina == null) return;
        playerMina.sairDaMina(true, false);
    }
}
