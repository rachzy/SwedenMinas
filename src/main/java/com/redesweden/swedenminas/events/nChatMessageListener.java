package com.redesweden.swedenminas.events;

import com.nickuc.chat.api.events.PublicMessageEvent;
import com.redesweden.swedenminas.data.Players;
import com.redesweden.swedenminas.models.PlayerMina;
import net.md_5.bungee.api.chat.*;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.awt.*;

public class nChatMessageListener implements Listener {
    @EventHandler
    public void onPublicMessage(PublicMessageEvent e) {
        Player player = e.getSender();

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
