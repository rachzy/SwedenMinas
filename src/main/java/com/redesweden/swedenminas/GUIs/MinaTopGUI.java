package com.redesweden.swedenminas.GUIs;

import com.redesweden.swedeneconomia.functions.ConverterQuantia;
import com.redesweden.swedenflocos.data.Players;
import com.redesweden.swedenflocos.models.PlayerFlocos;
import com.redesweden.swedenminas.data.Picaretas;
import com.redesweden.swedenminas.models.Picareta;
import dev.dbassett.skullcreator.SkullCreator;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;

public class MinaTopGUI {
    private final Inventory inventario;
    private final Integer posicaoInicial;

    public MinaTopGUI(Player player, int posicaoInicial) {
        PlayerFlocos playerFlocos = Players.getPlayerPorNickname(player.getName());
        Picareta picareta = Picaretas.getPicaretaPorDono(player.getName());
        inventario = Bukkit.createInventory(null, 54, String.format("§8TOP Players - Mina - #%s", (posicaoInicial / 10 + 1)));

        this.posicaoInicial = posicaoInicial;

        ItemStack vidroAmarelo = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 4);
        ItemMeta vidroMeta = vidroAmarelo.getItemMeta();
        vidroMeta.setDisplayName("§eTOP Mina");
        vidroAmarelo.setItemMeta(vidroMeta);

        for (int i = 0; i < 54; i++) {
            if (i <= 9 || i >= 45 || i % 9 == 0 || (i + 1) % 9 == 0) {
                inventario.setItem(i, vidroAmarelo.clone());
            }
        }

        int posicao = Picaretas.getTopPicaretas().indexOf(Picaretas.getTopPicaretas().stream().filter(picaretaIn -> picaretaIn.getDono().equals(player.getName())).findFirst().orElse(null));

        if(posicao != -1) {
            ItemStack playerHead = SkullCreator.itemFromName(player.getName());
            ItemMeta playerMeta = playerHead.getItemMeta();
            playerMeta.setDisplayName("§eSeu perfil");

            List<String> lore = new ArrayList<>();
            lore.add("");
            lore.add(" §3■ §7Blocos quebrados: §3" + new ConverterQuantia(picareta.getBlocosQuebrados()).emLetras());
            lore.add(" §b■ §7Flocos: §b❄" + new ConverterQuantia(playerFlocos.getFlocos()).emLetras());
            lore.add(" §e■ §7Posição: §e#" + (posicao + 1));
            lore.add("");
            playerMeta.setLore(lore);

            playerHead.setItemMeta(playerMeta);
            inventario.setItem(4, playerHead);
        }

        setTop10();

        // Bloco verde que avança uma página
        ItemStack proximaPaginaItem = SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDA2NzJiODJmMGQxZjhjNDBjNTZiNDJkMzY5YWMyOTk0Yzk0ZGE0NzQ5MTAxMGMyY2U0MzAzZTM0NjViOTJhNyJ9fX0=");
        SkullMeta metaProximaPaginaItem = (SkullMeta) proximaPaginaItem.getItemMeta();

        metaProximaPaginaItem.setDisplayName("§ePróxima página");

        List<String> loreProximaPaginaItem = new ArrayList<>();
        loreProximaPaginaItem.add("§7Ir para a página " + Math.round(posicaoInicial / 10 + 2));
        metaProximaPaginaItem.setLore(loreProximaPaginaItem);

        proximaPaginaItem.setItemMeta(metaProximaPaginaItem);

        if (posicaoInicial < 73) {
            inventario.setItem(50, proximaPaginaItem);
        }

        // livro que mostra informações sobre o TOP Flocos
        ItemStack placaInfoItem = new ItemStack(Material.SIGN, 1);
        ItemMeta placaInfoItemMeta = placaInfoItem.getItemMeta();
        placaInfoItemMeta.setDisplayName("§eSobre o TOP Mina");

        List<String> lorePlacaInfoItem = new ArrayList<>();
        lorePlacaInfoItem.add("§7O TOP Mina é atualizado de 10 em 10");
        lorePlacaInfoItem.add("§7minutos e é ordenado por jogadores que");
        lorePlacaInfoItem.add("§7quebraram a maior quantidade de blocos");
        lorePlacaInfoItem.add("§7na Mina até o momento da atualização");
        placaInfoItemMeta.setLore(lorePlacaInfoItem);

        placaInfoItem.setItemMeta(placaInfoItemMeta);
        inventario.setItem(8, placaInfoItem);

        // Bloco vermelho que volta uma página
        ItemStack paginaAnteriorItem = SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTVlZmQ5Njk3NGMwNDAzZjIyOWNmOTQxODVjZGQwZjcxOTczNjJhY2JkMDMxY2RmNTFmY2M4ZGFmYWM2Yjg1YSJ9fX0=");
        SkullMeta metaPaginaAnteriorItem = (SkullMeta) paginaAnteriorItem.getItemMeta();

        metaPaginaAnteriorItem.setDisplayName("§ePágina anterior");

        List<String> lorePaginaAnteriorItem = new ArrayList<>();
        lorePaginaAnteriorItem.add("§7Ir para a página " + Math.round(posicaoInicial / 10));
        metaPaginaAnteriorItem.setLore(lorePaginaAnteriorItem);

        paginaAnteriorItem.setItemMeta(metaPaginaAnteriorItem);

        if (posicaoInicial > 9) {
            inventario.setItem(48, paginaAnteriorItem);
        }

        ItemStack flechaVoltar = new ItemStack(Material.ARROW, 1);
        ItemMeta flechaMeta = flechaVoltar.getItemMeta();
        flechaMeta.setDisplayName("§eVoltar");

        List<String> loreFlecha = new ArrayList<>();
        loreFlecha.add("§7Clique para voltar ao Menu da Mina");
        flechaMeta.setLore(loreFlecha);

        flechaVoltar.setItemMeta(flechaMeta);
        inventario.setItem(49, flechaVoltar);
    }

    private void setTop10() {
        List<Picareta> playersTop = new ArrayList<>();
        for (int i = posicaoInicial; i <= posicaoInicial + 9; i++) {
            int finalI = i;
            Picaretas.getTopPicaretas()
                    .stream()
                    .filter(picaretaIn -> Picaretas.getTopPicaretas().indexOf(picaretaIn) == finalI)
                    .findFirst().ifPresent(playersTop::add);

        }

        for (int i = 0; i <= 9; i++) {
            int saltRounds = 20;
            if (i > 4) {
                saltRounds = 24;
            }
            int finalI = i;

            Picareta picareta = playersTop
                    .stream()
                    .filter(playerIn -> playersTop.indexOf(playerIn) == finalI)
                    .findFirst()
                    .orElse(null);

            ItemStack playerHead = null;

            if (picareta != null) {
                playerHead = SkullCreator.itemFromName(picareta.getDono());
            }


            if (playerHead != null) {
                PlayerFlocos playerFlocos = Players.getPlayerPorNickname(picareta.getDono());
                SkullMeta playerHeadMeta = (SkullMeta) playerHead.getItemMeta();

                List<String> lore = new ArrayList<>();
                playerHeadMeta.setDisplayName(String.format("§e%s. %s", i + (posicaoInicial + 1), picareta.getDono()));
                lore.add(String.format("§7Blocos quebrados: §3%s", new ConverterQuantia(picareta.getBlocosQuebrados()).emLetras()));
                lore.add(String.format("§7Flocos: §b❄%s", new ConverterQuantia(playerFlocos.getFlocos()).emLetras()));

                playerHeadMeta.setLore(lore);
                playerHead.setItemMeta(playerHeadMeta);

                inventario.setItem(i + saltRounds, playerHead);
            }
        }
    }

    public Inventory get() {
        return this.inventario;
    }
}
