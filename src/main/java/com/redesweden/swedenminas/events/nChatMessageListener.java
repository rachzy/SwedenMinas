package com.redesweden.swedenminas.events;

import com.nickuc.chat.api.events.PublicMessageEvent;
import com.redesweden.swedenminas.GUIs.GerenciarAmigosGUI;
import com.redesweden.swedenminas.GUIs.PicaretaGUI;
import com.redesweden.swedenminas.data.EventosEspeciais;
import com.redesweden.swedenminas.data.Nevascas;
import com.redesweden.swedenminas.data.Picaretas;
import com.redesweden.swedenminas.data.Players;
import com.redesweden.swedenminas.models.Nevasca;
import com.redesweden.swedenminas.models.Picareta;
import com.redesweden.swedenminas.models.PlayerMina;
import net.md_5.bungee.api.chat.*;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class nChatMessageListener implements Listener {
    @EventHandler
    public void onPublicMessage(PublicMessageEvent e) {
        Player player = e.getSender();

        String mensagem = e.getOriginalMessage();

        if(mensagem.equalsIgnoreCase("cancelar") 
                && (EventosEspeciais.getPlayerAdicionandoAmigo(player.getName()) != null || EventosEspeciais.getPlayerAlterandoMultiplicador(player.getName()) != null)
        ) {
            e.setCancelled(true);
            EventosEspeciais.removerPlayerAdicionandoAmigo(player);
            EventosEspeciais.removerPlayerAlterandoMultiplicador(player);
            player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 3.0F, 0.5F);
            player.sendMessage("§cVocê cancelou todas as operações atuais.");
            return;
        }

        if(EventosEspeciais.getPlayerAdicionandoAmigo(player.getName()) !=  null) {
            e.setCancelled(true);

            if(mensagem.equals(player.getName())) {
                player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 3.0F, 0.5F);
                player.sendMessage("§cVocê não pode se adicionar como amigo!");
                player.sendMessage("(§7Digite 'CANCELAR' para cancelar esta operação)");
                return;
            }

            Player alvo = Bukkit.getServer().getPlayer(mensagem);
            if(alvo == null || !alvo.isOnline()) {
                player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 3.0F, 0.5F);
                player.sendMessage(String.format("§cNão foi possível encontrar um jogador online com o nick '%s'", player.getName()));
                player.sendMessage("(§7Digite 'CANCELAR' para cancelar esta operação)");
                return;
            }

            Nevasca nevasca = Nevascas.getNevascaPorDono(player.getDisplayName());

            if(nevasca.getAmigos().contains(mensagem)) {
                player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 3.0F, 0.5F);
                player.sendMessage("§cEste jogador já é um amigo da sua Nevasca.");
                player.sendMessage("(§7Digite 'CANCELAR' para cancelar esta operação)");
                return;
            }

            EventosEspeciais.removerPlayerAdicionandoAmigo(player);

            nevasca.addAmigo(mensagem);

            player.openInventory(new GerenciarAmigosGUI(player).get());
            player.playSound(player.getLocation(), Sound.NOTE_PLING, 3.0F, 2F);
            player.sendMessage(String.format("§f§lNEVASCAS §e>> §aVocê adicionou o jogador §b%s §acomo Amigo de sua Nevasca.", player.getName()));

            alvo.playSound(alvo.getLocation(), Sound.LEVEL_UP, 3.0F, 2F);
            alvo.sendMessage(String.format("§f§lNEVASCAS §e>> §aO jogador §b%s §alhe adicionou como amigo em sua Nevasca!", player.getName()));
        }
        
        if(EventosEspeciais.getPlayerAlterandoMultiplicador(player.getName()) != null) {
            e.setCancelled(true);
            
            int valor;
            try {
                valor = Integer.parseInt(mensagem);
                if(valor < 1 || valor > 50) throw new Exception();
            } catch (Exception ex) {
                player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 3.0F, 0.5F);
                player.sendMessage("§cO valor do multiplicador precisa ser um número entre 1 e 50.");
                player.sendMessage("(§7Digite 'CANCELAR' para cancelar esta operação)");
                return;
            }

            EventosEspeciais.removerPlayerAlterandoMultiplicador(player);

            Picareta picareta = Picaretas.getPicaretaPorDono(player.getName());
            picareta.setMultiplicadorDeLevel(valor);

            player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 3.0F, 0.5F);
            player.sendMessage(String.format("§aVocê definiu o valor do seu multiplicador de leveis para §e%s§a.", valor));
            player.openInventory(new PicaretaGUI(player.getName()).get());
            return;
        }

        PlayerMina playerMina = Players.getPlayerPorNickname(player.getName());
        if(playerMina == null) return;

        TextComponent minerandoTag = new TextComponent(TextComponent.fromLegacyText("§e[Minerando]"));
        BaseComponent[] hoverTag = new ComponentBuilder("§aIr minerar").create();
        HoverEvent hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_TEXT, hoverTag);
        ClickEvent clickEvent = new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/mina");

        minerandoTag.setHoverEvent(hoverEvent);
        minerandoTag.setClickEvent(clickEvent);

        e.setTag("minerando", minerandoTag);
    }
}
