package com.redesweden.swedenminas.GUIs;

import com.redesweden.swedeneconomia.functions.ConverterQuantia;
import com.redesweden.swedenflocos.data.Players;
import com.redesweden.swedenflocos.models.PlayerFlocos;
import com.redesweden.swedenminas.data.Picaretas;
import com.redesweden.swedenminas.models.Picareta;
import com.redesweden.swedenminas.types.Level;
import dev.dbassett.skullcreator.SkullCreator;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PicaretaGUI {
    private final Inventory inventario = Bukkit.createInventory(null, 54, "§8Picareta");

    public PicaretaGUI(String nickname) {
        ItemStack vidro = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 4);
        ItemMeta vidroMeta = vidro.getItemMeta();
        vidroMeta.setDisplayName("§ePicareta");
        vidro.setItemMeta(vidroMeta);

        for(int i = 0; i < 54; i++) {
            if(i <= 9 || i >= 45 || i % 9 == 0 || (i + 1) % 9 == 0) {
                inventario.setItem(i, vidro.clone());
            }
        }

        Picareta picareta = Picaretas.getPicaretaPorDono(nickname);
        PlayerFlocos playerFlocos = Players.getPlayerPorNickname(nickname);

        ItemStack multiplicadorDeLevel = SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmExNDc0ZDg5MWQyYTY0N2VlMWFhNzliNTg5NDRhNTZlN2I4M2FiZDliN2NjMTM0ZTQyMGMxYjNhN2M0OTk4In19fQ==");
        ItemMeta multiplicadorMeta = multiplicadorDeLevel.getItemMeta();
        multiplicadorMeta.setDisplayName("§eMultiplicador de Level");

        List<String> loreMultiplicador = new ArrayList<>();
        loreMultiplicador.add("§7Este valor define a quantidade de leveis");
        loreMultiplicador.add("§7de encantamento que serão evoluídos de");
        loreMultiplicador.add("§7uma só vez. O valor padrão é 1, o que");
        loreMultiplicador.add("§7significa que os leveis serão evoluídos de");
        loreMultiplicador.add("§7um em um.");
        loreMultiplicador.add("");
        loreMultiplicador.add(" §fMultiplicador atual: §e" + picareta.getMultiplicadorDeLevel());
        loreMultiplicador.add("");
        loreMultiplicador.add("§aClique para alterar");
        multiplicadorMeta.setLore(loreMultiplicador);

        multiplicadorDeLevel.setItemMeta(multiplicadorMeta);

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

            if(level.getMeta().getLevel() == Level.INVOCADOR
                    || level.getMeta().getLevel() == Level.SORTUDO
                    || level.getMeta().getLevel() == Level.EXPLOSAO
                    || level.getMeta().getLevel() == Level.LASER
                    || level.getMeta().getLevel() == Level.SUPER
            ) {
                String chance = BigDecimal.valueOf(level.getLevelAtual() + 1).divide(BigDecimal.valueOf(level.getLevelMaximo()), 6, RoundingMode.DOWN).toPlainString();
                levelLore.add(String.format(" §fChance atual: §7%s%s", chance, "%"));
            }

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

        inventario.setItem(4, multiplicadorDeLevel);
        inventario.setItem(39, playerHead);
        inventario.setItem(41, picareta.pegarPicareta(false));
        inventario.setItem(49, flechaVoltar);
    }

    public Inventory get() {
        return inventario;
    }
}
