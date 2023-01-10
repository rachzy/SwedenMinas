package com.redesweden.swedenminas.GUIs;

import com.redesweden.swedeneconomia.functions.ConverterQuantia;
import com.redesweden.swedenflocos.data.Players;
import com.redesweden.swedenflocos.models.PlayerFlocos;
import com.redesweden.swedenminas.data.Picaretas;
import com.redesweden.swedenminas.models.Picareta;
import dev.dbassett.skullcreator.SkullCreator;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PicaretaGUI {
    private final Inventory inventario = Bukkit.createInventory(null, 54, "§5Picareta");

    public PicaretaGUI(String nickname) {
        Picareta picareta = Picaretas.getPicaretaPorDono(nickname);
        PlayerFlocos playerFlocos = Players.getPlayerPorNickname(nickname);

        picareta.getLeveis().forEach(level -> {
            int index = picareta.getLeveis().indexOf(level);
            ItemStack levelItem;
            String ultimoValorLore;

            if(playerFlocos.getFlocos().compareTo(level.getCustoProximoLevel()) < 0) {
                levelItem = SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMWVhNmFmNzBlZWVjNmZiMTkwMjJjODFlNTZmZjcyYmNjNWY4ZjBmM2UwODcyMWEyM2UzMGRkMWMxZjllMGNmMiJ9fX0=");
                ultimoValorLore = "§cVocê não tem flocos suficientes";
            } else {
                levelItem = SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTkwNzkzZjU2NjE2ZjEwMTUwMmRlMWQzNGViMjU0NGY2MDdkOTg5MDBlMzY5OTM2OTI5NTMxOWU2MzBkY2Y2ZCJ9fX0=");
                ultimoValorLore = "§eClique para evoluir";
            }

            if(level.getLevelAtual() == level.getLevelMaximo()) {
                levelItem = SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMWVhNmFmNzBlZWVjNmZiMTkwMjJjODFlNTZmZjcyYmNjNWY4ZjBmM2UwODcyMWEyM2UzMGRkMWMxZjllMGNmMiJ9fX0=");
                ultimoValorLore = "§cLevel máximo atingido";
            }

            ItemMeta levelMeta = levelItem.getItemMeta();
            levelMeta.setDisplayName("§a" + level.getMeta().getTitle());

            List<String> levelLore = new ArrayList<>();
            Arrays.stream(level.getMeta().getDescription().split("\n")).forEach(valor -> {
                levelLore.add("§7" + valor);
            });
            levelLore.add("");
            levelLore.add(String.format(" §fLevel atual: §7%s/%s", level.getLevelAtual(), level.getLevelMaximo()));
            levelLore.add(" §fCusto de evolução: §b❄" + new ConverterQuantia(level.getCustoProximoLevel()).emLetras());
            levelLore.add("");
            levelLore.add(ultimoValorLore);
            levelMeta.setLore(levelLore);

            levelItem.setItemMeta(levelMeta);

            if(index <= 4) {
                inventario.setItem(index + 11, levelItem);
            } else {
                inventario.setItem(index + 15, levelItem);
            }
        });

        ItemStack playerHead = SkullCreator.itemFromName(nickname);
        ItemMeta playerMeta = playerHead.getItemMeta();
        playerMeta.setDisplayName("§eSeu Perfil");

        List<String> lorePlayer = new ArrayList<>();
        lorePlayer.add("");
        lorePlayer.add(" §b■ §7Flocos: §b❄" + new ConverterQuantia(playerFlocos.getFlocos()).emLetras());
        lorePlayer.add("");
        playerMeta.setLore(lorePlayer);

        playerHead.setItemMeta(playerMeta);

        ItemStack flechaVoltar = new ItemStack(Material.ARROW, 1);
        ItemMeta flechaMeta = flechaVoltar.getItemMeta();
        flechaMeta.setDisplayName("§eVoltar");

        List<String> loreFlecha = new ArrayList<>();
        loreFlecha.add("§7Clique para voltar");
        flechaMeta.setLore(loreFlecha);

        flechaVoltar.setItemMeta(flechaMeta);

        inventario.setItem(39, playerHead);
        inventario.setItem(41, picareta.pegarPicareta(false));
        inventario.setItem(49, flechaVoltar);
    }

    public Inventory get() {
        return inventario;
    }
}
