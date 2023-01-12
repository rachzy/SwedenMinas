package com.redesweden.swedenminas.GUIs;

import com.redesweden.swedenminas.data.Nevascas;
import com.redesweden.swedenminas.models.Nevasca;
import dev.dbassett.skullcreator.SkullCreator;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class GerenciarAmigosGUI {
    private final Inventory inventario = Bukkit.createInventory(null, 27, "§3Nevasca §8- §2Amigos");

    public GerenciarAmigosGUI(Player player) {
        String nickname = player.getName();
        Nevasca nevasca = Nevascas.getNevascaPorDono(nickname);

        if(nevasca == null) return;

        for(int i = 0; i <= 4; i++) {
            if(i <= 1 || player.hasPermission(String.format("swedenminas.amigos%s", i + 1))) {
                try {
                    String amigo = nevasca.getAmigos().get(i);

                    ItemStack headPlayer = SkullCreator.itemFromName(amigo);
                    ItemMeta headMeta = headPlayer.getItemMeta();
                    headMeta.setDisplayName("§e" + amigo);

                    List<String> loreHead = new ArrayList<>();
                    loreHead.add("§7Clique para remover este amigo");
                    headMeta.setLore(loreHead);

                    headPlayer.setItemMeta(headMeta);

                    inventario.setItem(i + 10, headPlayer);
                } catch (Exception e) {
                    ItemStack vidro = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15);
                    ItemMeta vidroMeta = vidro.getItemMeta();
                    vidroMeta.setDisplayName("§eVaga");

                    List<String> loreVidro = new ArrayList<>();
                    loreVidro.add("§7Você pode adicionar um jogador");
                    loreVidro.add("§7para preencher essa vaga");
                    vidroMeta.setLore(loreVidro);

                    vidro.setItemMeta(vidroMeta);

                    inventario.setItem(i + 10, vidro);
                }
            }
        }

        ItemStack adicionarAmigo = SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWZmMzE0MzFkNjQ1ODdmZjZlZjk4YzA2NzU4MTA2ODFmOGMxM2JmOTZmNTFkOWNiMDdlZDc4NTJiMmZmZDEifX19");
        ItemMeta adicionarMeta = adicionarAmigo.getItemMeta();
        adicionarMeta.setDisplayName("§aAdicionar Amigo");

        List<String> loreAdicionar = new ArrayList<>();
        loreAdicionar.add("§7Clique para adicionar um amigo");
        adicionarMeta.setLore(loreAdicionar);

        adicionarAmigo.setItemMeta(adicionarMeta);

        ItemStack voltarFlecha = new ItemStack(Material.ARROW, 1);
        ItemMeta flechaMeta = voltarFlecha.getItemMeta();
        flechaMeta.setDisplayName("§eVoltar");

        List<String> loreVoltar = new ArrayList<>();
        loreVoltar.add("§7Clique para voltar ao menu");
        flechaMeta.setLore(loreVoltar);

        voltarFlecha.setItemMeta(flechaMeta);

        inventario.setItem(16, adicionarAmigo);
        inventario.setItem(22, voltarFlecha);
    }

    public Inventory get() {
        return inventario;
    }
}
