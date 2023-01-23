package com.redesweden.swedenminas.models;

import com.redesweden.swedencash.data.Players;
import com.redesweden.swedencash.models.PlayerCash;
import com.redesweden.swedeneconomia.functions.ConverterQuantia;
import com.redesweden.swedenminas.types.BoosterTipo;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Booster {
    private final String titulo;
    private final BoosterTipo tipo;
    private final int valor;
    private final BigDecimal preco;
    private final String permission;
    private final String comando;

    public Booster(String titulo, BoosterTipo tipo, int valor, BigDecimal preco, String permission, String comando) {
        this.titulo = titulo;
        this.tipo = tipo;
        this.valor = valor;
        this.preco = preco;
        this.permission = permission;
        this.comando = comando;
    }

    public String getTitulo() {
        return titulo;
    }

    public BoosterTipo getTipo() {
        return tipo;
    }

    public int getValor() {
        return valor;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public String getPermission() {
        return permission;
    }

    public String getComando(String alvo) {
        return comando.replace("{player}", alvo).replace("{permission}", permission);
    }

    public ItemStack getItem(Player player) {
        PlayerCash playerCash = Players.getPlayerPorNickname(player.getName());
        ItemStack itemBoost = new ItemStack(Material.PAPER, 1);
        String tipoTitulo = "§a§lMONEY";

        if(tipo == BoosterTipo.FLOCOS) {
            tipoTitulo = "§b§lFLOCOS";
            itemBoost = new ItemStack(Material.getMaterial(395), 1);
        }

        ItemMeta itemMeta = itemBoost.getItemMeta();
        itemMeta.setDisplayName(String.format("§e§lBOOSTER %s §e%sx §7- §a%s", tipoTitulo, valor, titulo));

        List<String> loreItem = new ArrayList<>();
        loreItem.add(String.format("§7Bônus de %sx na venda de peixes.", valor));
        loreItem.add(String.format("§7Válido por %s", titulo));
        loreItem.add("");
        loreItem.add(" §6■ §fPreço: §6✰" + new ConverterQuantia(preco).emLetras());
        loreItem.add("");

        String permissaoMaxima = "swedenminas.money3x";

        if(tipo == BoosterTipo.FLOCOS) {
            permissaoMaxima = "swedenminas.flocos3x";
        }

        if(player.hasPermission(permission) || player.hasPermission(permissaoMaxima)) {
            loreItem.add(" §cVocê já tem esse boost");
        } else {
            if(playerCash.getCash().compareTo(preco) < 0) {
                loreItem.add(" §cVocê não tem cash suficiente");
            } else {
                loreItem.add(" §aClique para comprar");
            }
        }
        itemMeta.setLore(loreItem);

        itemBoost.setItemMeta(itemMeta);
        return itemBoost;
    }
}