package com.redesweden.swedenminas.GUIs;

import com.redesweden.swedeneconomia.data.Players;
import com.redesweden.swedeneconomia.functions.ConverterQuantia;
import com.redesweden.swedeneconomia.models.PlayerSaldo;
import com.redesweden.swedenflocos.models.PlayerFlocos;
import com.redesweden.swedenminas.data.Minas;
import com.redesweden.swedenminas.data.Picaretas;
import com.redesweden.swedenminas.models.Picareta;
import com.redesweden.swedenranks.data.Ranks;
import com.redesweden.swedenranks.models.PlayerRank;
import dev.dbassett.skullcreator.SkullCreator;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class LocaisGUI {
    private Inventory inventario = Bukkit.createInventory(null, 54, "§2Locais de Mineração");

    public LocaisGUI(String nickname) {
        PlayerSaldo playerSaldo = Players.getPlayer(nickname);
        PlayerFlocos playerFlocos = com.redesweden.swedenflocos.data.Players.getPlayerPorNickname(nickname);
        PlayerRank playerRank = com.redesweden.swedenranks.data.Players.getPlayerPorNickname(nickname);
        Picareta picareta = Picaretas.getPicaretaPorDono(nickname);

        ItemStack playerHead = SkullCreator.itemFromName(nickname);
        ItemMeta playerMeta = playerHead.getItemMeta();
        playerMeta.setDisplayName("§eSeu perfil");

        List<String> lorePlayer = new ArrayList<>();
        lorePlayer.add("");
        lorePlayer.add(String.format(" §a■ §7Saldo: §a$%s", playerSaldo.getSaldo(true)));
        lorePlayer.add(String.format(" §b■ §7Flocos: §b❄%s", new ConverterQuantia(playerFlocos.getFlocos()).emLetras()));
        lorePlayer.add("");
        lorePlayer.add(String.format("§7Seu rank: %s", playerRank.getRank().getTituloPorLevel(playerRank.getLevel())));
        playerMeta.setLore(lorePlayer);

        playerHead.setItemMeta(playerMeta);

        Minas.getMinas().forEach(mina -> {
            int index = Minas.getMinas().indexOf(mina);
            ItemStack minaHead = mina.getRank().getHead();
            ItemMeta minaMeta = minaHead.getItemMeta();
            minaMeta.setDisplayName("§b§lMINA " + mina.getRank().getTitulo());

            List<String> loreMina = new ArrayList<>();
            loreMina.add(String.format("§7Valor p/ bloco: §a$%s", mina.getValorBloco().toString()));
            loreMina.add("");

            if(Ranks.getPosicaoPorRank(playerRank.getRank()) >= Ranks.getPosicaoPorRank(mina.getRank())) {
                loreMina.add("§eClique para teleportar");
            } else {
                loreMina.add("§cVocê não tem acesso à essa mina");
            }
            minaMeta.setLore(loreMina);

            minaHead.setItemMeta(minaMeta);

            inventario.setItem(index + 28, minaHead);
        });

        ItemStack flechaVoltar = new ItemStack(Material.ARROW, 1);
        ItemMeta flechaMeta = flechaVoltar.getItemMeta();
        flechaMeta.setDisplayName("§eVoltar");

        List<String> loreVoltar = new ArrayList<>();
        loreVoltar.add("§7Clique para voltar ao menu");
        flechaMeta.setLore(loreVoltar);

        flechaVoltar.setItemMeta(flechaMeta);

        inventario.setItem(12, playerHead);
        inventario.setItem(14, picareta.pegarPicareta(false));
        inventario.setItem(49, flechaVoltar);
    }

    public Inventory get() {
        return inventario;
    }
}
