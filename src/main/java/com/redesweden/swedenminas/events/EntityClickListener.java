package com.redesweden.swedenminas.events;

import com.redesweden.swedenminas.GUIs.GerenciarNevascaGUI;
import com.redesweden.swedenminas.data.Nevascas;
import com.redesweden.swedenminas.functions.GetBlocosPorPerto;
import com.redesweden.swedenminas.models.Nevasca;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import java.util.List;

public class EntityClickListener implements Listener {
    @EventHandler
    public void onEntityClick(PlayerInteractEntityEvent e) {
        if(e.getRightClicked().getType() != EntityType.SNOWMAN) return;
        List<Block> blocosPorPerto = new GetBlocosPorPerto(e.getRightClicked().getLocation(), 1, false, false).getBlocos();
        Nevasca nevasca = null;

        for(Block bloco : blocosPorPerto) {
            Nevasca nevascaBloco = Nevascas.getNevascaPorLocal(bloco.getLocation());
            if(nevascaBloco != null) {
                nevasca = nevascaBloco;
                break;
            }
        }

        if(nevasca == null) return;

        Player player = e.getPlayer();

        if(!nevasca.getDono().equals(player.getDisplayName())) {
            player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 3.0F, 0.5F);
            player.sendMessage("§f§lNEVASCAS §e>> §cApenas o dono da nevasca pode acessar o menu de gerenciamento.");
            return;
        }

        player.sendMessage("§f§lNEVASCAS §e>> §aVocê abriu o menu de gerenciamento da sua Nevasca.");
        player.playSound(player.getLocation(), Sound.CLICK, 3.0F, 2F);
        player.openInventory(new GerenciarNevascaGUI(player.getName()).get());
    }
}
