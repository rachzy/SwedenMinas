package com.redesweden.swedenminas.data;

import com.redesweden.swedenminas.models.PlayerMina;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class Players {
    private static final List<PlayerMina> playersNaMina = new ArrayList<>();

    public static void addPlayer(PlayerMina player) throws Exception {
        if (playersNaMina.stream().anyMatch(playerIn -> playerIn.getNickname().equals(player.getNickname())))
            throw new Exception("Jogador já está minerando");
        playersNaMina.add(player);
    }

    public static PlayerMina getPlayerPorNickname(String nickname) {
        return playersNaMina
                .stream()
                .filter(player -> player.getNickname().equals(nickname))
                .findFirst()
                .orElse(null);
    }

    public static List<PlayerMina> getPlayers() {
        return playersNaMina;
    }

    public static void removerPlayer(PlayerMina player) {
        playersNaMina.remove(player);
    }

    public static void removerTodos() {
        playersNaMina.forEach((player) -> {
            player.sairDaMina(true, true);
        });
    }
}
