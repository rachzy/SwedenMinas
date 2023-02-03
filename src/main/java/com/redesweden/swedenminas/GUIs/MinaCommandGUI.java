package com.redesweden.swedenminas.GUIs;

import com.redesweden.swedenminas.data.Minas;
import com.redesweden.swedenminas.models.Mina;
import com.redesweden.swedenminas.models.PlayerMina;
import com.redesweden.swedenranks.data.Players;
import com.redesweden.swedenranks.models.PlayerRank;
import dev.dbassett.skullcreator.SkullCreator;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class MinaCommandGUI {
    private final Inventory inventario = Bukkit.createInventory(null, 27, "§8Mina");

    public MinaCommandGUI(Player player) {
        ItemStack vidroAmarelo = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 4);
        ItemMeta vidroMeta = vidroAmarelo.getItemMeta();
        vidroMeta.setDisplayName("§eMina");
        vidroAmarelo.setItemMeta(vidroMeta);

        for(int i = 0; i < 27; i++) {
            inventario.setItem(i, vidroAmarelo.clone());
        }

        PlayerMina playerMina = com.redesweden.swedenminas.data.Players.getPlayerPorNickname(player.getName());
        PlayerRank playerRank = Players.getPlayerPorNickname(player.getName());

        ItemStack topItem = new ItemStack(Material.ARMOR_STAND, 1);
        ItemMeta topMeta = topItem.getItemMeta();
        topMeta.setDisplayName("§eTop 10");

        List<String> loreTop = new ArrayList<>();
        loreTop.add("§7Clique para ver o TOP 10 de");
        loreTop.add("§7jogadores que mais quebraram blocos");
        topMeta.setLore(loreTop);

        topItem.setItemMeta(topMeta);

        ItemStack locaisDeMina = SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTI5ZmE4Y2Q4NGFjMTI1MDIxZDYyMDFhMTQ4YTllODYyYzBiNzcyYzQ4NDc3YjA2YzE5MTQ1YzRhNjczYWEyNCJ9fX0=");
        ItemMeta locaisMeta = locaisDeMina.getItemMeta();
        locaisMeta.setDisplayName("§eLocais de Mineração");

        List<String> loreLocais = new ArrayList<>();
        loreLocais.add("§7Clique para acessar");
        loreLocais.add("§7os locais de mineração");
        locaisMeta.setLore(loreLocais);

        locaisDeMina.setItemMeta(locaisMeta);

        ItemStack encantamentos = new ItemStack(Material.ENCHANTED_BOOK, 1);
        ItemMeta encantamentosMeta = encantamentos.getItemMeta();
        encantamentosMeta.setDisplayName("§eEncantamentos");

        List<String> loreEncantamentos = new ArrayList<>();
        loreEncantamentos.add("§7Clique para abrir o");
        loreEncantamentos.add("§7menu de encantamentos");
        encantamentosMeta.setLore(loreEncantamentos);

        encantamentos.setItemMeta(encantamentosMeta);

        ItemStack beneficios = SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDY3ZGNlNDY0NTM0OWU0MWE3ZjM1Nzk3ZTJiOTI3OWUzNWE2NWY1ZTgxYTM0NDk2ODg1ZDI3MjY4ZjM2OTEzOSJ9fX0=");
        ItemMeta beneficiosMeta = beneficios.getItemMeta();
        beneficiosMeta.setDisplayName("§eBenefícios §aVIPs");

        if(playerMina == null) {
            ItemStack irMinerar = new ItemStack(Material.DIAMOND_PICKAXE, 1);
            ItemMeta irMinerarMeta = irMinerar.getItemMeta();
            irMinerarMeta.setDisplayName("§eIr Minerar");

            irMinerarMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            irMinerarMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

            int boostDeMoney = 1;
            int boostDeFlocos = 1;

            if(player.hasPermission("swedenminas.money3x")) {
                boostDeMoney = 3;
            } else if (player.hasPermission("swedenminas.money2x")) {
                boostDeMoney = 2;
            }

            if(player.hasPermission("swedenminas.flocos3x")) {
                boostDeFlocos = 3;
            } else if (player.hasPermission("swedenminas.flocos2x")) {
                boostDeFlocos = 2;
            }

            List<String> loreMinerar = new ArrayList<>();
            Mina mina;
            if (player.hasPermission("swedenminas.mina.vip") && Minas.getMinaPorId("VIP") != null) {
                mina = Minas.getMinaPorId("VIP");
            } else {
                mina = Minas.getMinaPorId(playerRank.getRank().getId());
            }

            loreMinerar.add("§7Sua mina: " + mina.getTitulo());
            if(boostDeMoney != 1) {
                loreMinerar.add("");
                loreMinerar.add(String.format(" §a■ §fBoost de §aMoney§f: §6%sx", boostDeMoney));
            }

            if(boostDeFlocos != 1) {
                loreMinerar.add(String.format(" §b■ §fBoost de §bFlocos§f: §6%sx", boostDeFlocos));
            }

            loreMinerar.add("");
            loreMinerar.add("§eClique para ir minerar");
            irMinerarMeta.setLore(loreMinerar);

            irMinerar.setItemMeta(irMinerarMeta);
            irMinerar.addEnchantment(Enchantment.DURABILITY, 1);
            inventario.setItem(16, irMinerar);
        } else {
            ItemStack sairItem = SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMWVhNmFmNzBlZWVjNmZiMTkwMjJjODFlNTZmZjcyYmNjNWY4ZjBmM2UwODcyMWEyM2UzMGRkMWMxZjllMGNmMiJ9fX0=");
            ItemMeta sairMeta = sairItem.getItemMeta();
            sairMeta.setDisplayName("§cSair da Mina");

            List<String> loreSair = new ArrayList<>();
            loreSair.add("§7Clique para sair da mina");
            sairMeta.setLore(loreSair);

            sairItem.setItemMeta(sairMeta);
            inventario.setItem(16, sairItem);
        }

        ItemStack boostersHead = SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDU2OGI5NjgxMDhlOTRjZWU0OWEzOTBkMGY4NDQ5YTNiZGMxYjFlNTg1NTVmMDlhZWRhNGNmYjZjYTcxMGMxMSJ9fX0=");
        ItemMeta boosterMeta = boostersHead.getItemMeta();
        boosterMeta.setDisplayName("§eBoosters");

        List<String> loreBoosters = new ArrayList<>();
        loreBoosters.add("§7Clique para acessar");
        loreBoosters.add("§7o menu de boosters");
        boosterMeta.setLore(loreBoosters);

        boostersHead.setItemMeta(boosterMeta);

        inventario.setItem(10, topItem);
        inventario.setItem(11, encantamentos);
        inventario.setItem(12, locaisDeMina);
        inventario.setItem(13, boostersHead);
    }

    public Inventory get() {
        return inventario;
    }
}
