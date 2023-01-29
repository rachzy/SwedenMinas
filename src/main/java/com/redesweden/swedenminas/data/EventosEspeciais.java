package com.redesweden.swedenminas.data;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EventosEspeciais {
    private static List<Player> playersAdicionandoAmigo = new ArrayList<>();

    public static void addPlayerAdicionandoAmigo(Player player) {
        removerPlayerAdicionandoAmigo(player);
        playersAdicionandoAmigo.add(player);
    }

    public static Player getPlayerAdicionandoAmigo(String nickname) {
        return playersAdicionandoAmigo.stream().filter(player -> player.getName().equals(nickname)).findFirst().orElse(null);
    }

    public static void removerPlayerAdicionandoAmigo(Player player) {
        playersAdicionandoAmigo = playersAdicionandoAmigo.stream().filter(playerIn -> !playerIn.getName().equals(player.getName())).collect(Collectors.toList());
    }
}
