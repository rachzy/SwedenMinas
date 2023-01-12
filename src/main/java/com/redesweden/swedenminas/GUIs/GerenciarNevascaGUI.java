package com.redesweden.swedenminas.GUIs;

import com.redesweden.swedencash.data.Players;
import com.redesweden.swedencash.models.PlayerCash;
import com.redesweden.swedeneconomia.functions.ConverterQuantia;
import com.redesweden.swedenminas.data.Nevascas;
import com.redesweden.swedenminas.models.Nevasca;
import dev.dbassett.skullcreator.SkullCreator;
import org.bukkit.Bukkit;
import org.bukkit.block.Skull;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class GerenciarNevascaGUI {
    private final Inventory inventario = Bukkit.createInventory(null, 27, "§3Nevasca");

    public GerenciarNevascaGUI(String nickname) {
        Nevasca nevasca = Nevascas.getNevascaPorDono(nickname);
        PlayerCash playerCash = Players.getPlayerPorNickname(nickname);

        if(nevasca == null) return;

        ItemStack darUpgrade = SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjQxZjFlZjQzOWY5MTA2OWE0MzY3OGQyMjdhZDQ1OGQ2NjNlYzA0MzYzYmNlOWM3YzAxOWM1Njc5ZThjZjAwNCJ9fX0=");
        ItemMeta upgradeMeta = darUpgrade.getItemMeta();
        upgradeMeta.setDisplayName("§eDar Upgrade");

        List<String> loreUpgrade = new ArrayList<>();

        int proximoLevel = nevasca.getLevel() + 1;
        BigDecimal custoProximoLevel = new BigDecimal("1500000").multiply(BigDecimal.valueOf(proximoLevel));

        if(nevasca.getLevel() >= 5) {
            loreUpgrade.add("§7Sua nevasca já está no level máximo (5).");
        } else {
            loreUpgrade.add("§7Próximo level: §e" + proximoLevel);
            loreUpgrade.add("§7Preço: §6✰" + new ConverterQuantia(custoProximoLevel).emLetras());
            loreUpgrade.add("");

            if(playerCash.getCash().compareTo(custoProximoLevel) >= 0) {
                loreUpgrade.add("§aClique para evoluir");
            } else {
                loreUpgrade.add("§cVocê não possui CASH suficiente");
                loreUpgrade.add("§cpara evoluir para o próximo level");
            }
        }
        upgradeMeta.setLore(loreUpgrade);

        darUpgrade.setItemMeta(upgradeMeta);

        ItemStack amigos = SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzZjYmFlNzI0NmNjMmM2ZTg4ODU4NzE5OGM3OTU5OTc5NjY2YjRmNWE0MDg4ZjI0ZTI2ZTA3NWYxNDBhZTZjMyJ9fX0=");
        ItemMeta amigosMeta = amigos.getItemMeta();
        amigosMeta.setDisplayName("§eAmigos");

        List<String> loreAmigos = new ArrayList<>();
        if(nevasca.getAmigos().toArray().length == 0) {
            loreAmigos.add("§7Você não tem amigos na Nevasca");
        } else {
            loreAmigos.add("");
            nevasca.getAmigos().forEach((amigo) -> {
                loreAmigos.add(" §e- §b" + amigo);
            } );
        }
        loreAmigos.add("");
        loreAmigos.add("§aClique para gerenciar amigos");
        amigosMeta.setLore(loreAmigos);

        amigos.setItemMeta(amigosMeta);

        ItemStack resetar = SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWM4NDM2MGM4N2RhOTY2ZTI3NWUzZWU2MmQ4Y2RlOTI2MzBiNTA5YTNmMzNlOWIzM2FmOGNhN2Y4NTMyNzg3OSJ9fX0=");
        ItemMeta resetarMeta = resetar.getItemMeta();
        resetarMeta.setDisplayName("§eResetar Neve");

        List<String> loreResetar = new ArrayList<>();
        loreResetar.add("§7Reseta todas as neves da sua");
        loreResetar.add("§7nevasca, caso alguma tenha sumido");
        resetarMeta.setLore(loreResetar);

        resetar.setItemMeta(resetarMeta);

        ItemStack removerNevasca = SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2VkMWFiYTczZjYzOWY0YmM0MmJkNDgxOTZjNzE1MTk3YmUyNzEyYzNiOTYyYzk3ZWJmOWU5ZWQ4ZWZhMDI1In19fQ==");
        ItemMeta removerMeta = removerNevasca.getItemMeta();
        removerMeta.setDisplayName("§cRemover Nevasca");

        List<String> loreRemover = new ArrayList<>();
        loreRemover.add("§7Clique para remover sua nevasca");
        removerMeta.setLore(loreRemover);

        removerNevasca.setItemMeta(removerMeta);

        inventario.setItem(11, darUpgrade);
        inventario.setItem(12, amigos);
        inventario.setItem(13, resetar);
        inventario.setItem(15, removerNevasca);
    }

    public Inventory get() {
        return inventario;
    }
}
