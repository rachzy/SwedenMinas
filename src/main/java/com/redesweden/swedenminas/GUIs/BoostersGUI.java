package com.redesweden.swedenminas.GUIs;

import com.redesweden.swedencash.data.Players;
import com.redesweden.swedencash.models.PlayerCash;
import com.redesweden.swedeneconomia.functions.ConverterQuantia;
import com.redesweden.swedenminas.data.Boosters;
import dev.dbassett.skullcreator.SkullCreator;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class BoostersGUI {
    public Inventory inventario = Bukkit.createInventory(null, 54, "§8Boosters de Mina");

    public BoostersGUI(Player player) {
        PlayerCash playerCash = Players.getPlayerPorNickname(player.getName());

        ItemStack vidroBranco = new ItemStack(Material.STAINED_GLASS_PANE, 1);
        ItemMeta vidroMeta = vidroBranco.getItemMeta();
        vidroMeta.setDisplayName("§f§lBOOSTERS");
        vidroBranco.setItemMeta(vidroMeta);

        for(int i = 0; i < 54; i++) {
            if(i <= 9 || i >= 44 || i % 9 == 0 || (i + 1) % 9 == 0) {
                inventario.setItem(i, vidroBranco.clone());
            }
        }

        ItemStack playerHead = SkullCreator.itemFromName(player.getName());
        ItemMeta playerMeta = playerHead.getItemMeta();
        playerMeta.setDisplayName("§eSeu Perfil");

        List<String> lorePlayer = new ArrayList<>();
        lorePlayer.add("");
        lorePlayer.add(" §6■ §fCash: §6✰" + new ConverterQuantia(playerCash.getCash()).emLetras());
        lorePlayer.add("");
        playerMeta.setLore(lorePlayer);

        playerHead.setItemMeta(playerMeta);
        inventario.setItem(4, playerHead);

        Boosters.getBoosters().forEach((booster) -> {
            int index = Boosters.getBoosters().indexOf(booster);

            int posicao = index + 19;
            if(index > 2 && index < 6) {
                posicao = index + 20;
            } else if(index >= 6 && index < 9) {
                posicao = index + 22;
            } else if (index >= 9 && index < 12) {
                posicao = index + 23;
            }

            inventario.setItem(posicao, booster.getItem(player));
        });

        ItemStack flechaVoltar = new ItemStack(Material.ARROW, 1);
        ItemMeta voltarMeta = flechaVoltar.getItemMeta();
        voltarMeta.setDisplayName("§eVoltar");

        List<String> loreVoltar = new ArrayList<>();
        loreVoltar.add("§7Clique para voltar ao menu");
        voltarMeta.setLore(loreVoltar);

        flechaVoltar.setItemMeta(voltarMeta);
        inventario.setItem(49, flechaVoltar);
    }

    public Inventory get() {
        return inventario;
    }
}
