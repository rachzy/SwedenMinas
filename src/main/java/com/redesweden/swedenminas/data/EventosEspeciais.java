package com.redesweden.swedenminas.data;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class EventosEspeciais {
    private static final List<Player> playersAdicionandoAmigo = new ArrayList<>();

    public static void addPlayerAdicionandoAmigo(Player player) {
        removerPlayerAdicionandoAmigo(player);
        playersAdicionandoAmigo.add(player);
    }

    public static Player getPlayerAdicionandoAmigo(String nickname) {
        return playersAdicionandoAmigo.stream().filter(player -> player.getName().equals(nickname)).findFirst().orElse(null);
    }

    public static void removerPlayerAdicionandoAmigo(Player player) {
        playersAdicionandoAmigo.remove(player);
    }
}
