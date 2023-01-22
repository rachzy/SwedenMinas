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
    private final Inventory inventario = Bukkit.createInventory(null, 27, "§9Mina");

    public MinaCommandGUI(Player player) {
        PlayerMina playerMina = com.redesweden.swedenminas.data.Players.getPlayerPorNickname(player.getName());
        PlayerRank playerRank = Players.getPlayerPorNickname(player.getName());
        ItemStack locaisDeMina = new ItemStack(Material.ARMOR_STAND, 1);
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

        List<String> loreBeneficios = new ArrayList<>();
        loreBeneficios.add("");
        loreBeneficios.add(" §a■ §a§lVIP§7: Fly");
        loreBeneficios.add(" §b■ §b§lICE§7: Fly + 2x Money");
        loreBeneficios.add(" §9■ §9§lPOLAR§7: Fly + 2x Money + 2x Flocos");
        loreBeneficios.add(" §f■ §f§lBLIZZARD§7: Fly + 3x Money + 3x Flocos");
        loreBeneficios.add("");
        beneficiosMeta.setLore(loreBeneficios);

        beneficios.setItemMeta(beneficiosMeta);

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
            inventario.setItem(15, irMinerar);
        } else {
            ItemStack sairItem = SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMWVhNmFmNzBlZWVjNmZiMTkwMjJjODFlNTZmZjcyYmNjNWY4ZjBmM2UwODcyMWEyM2UzMGRkMWMxZjllMGNmMiJ9fX0=");
            ItemMeta sairMeta = sairItem.getItemMeta();
            sairMeta.setDisplayName("§cSair da Mina");

            List<String> loreSair = new ArrayList<>();
            loreSair.add("§7Clique para sair da mina");
            sairMeta.setLore(loreSair);

            sairItem.setItemMeta(sairMeta);
            inventario.setItem(15, sairItem);
        }

        ItemStack boostersHead = SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDU2OGI5NjgxMDhlOTRjZWU0OWEzOTBkMGY4NDQ5YTNiZGMxYjFlNTg1NTVmMDlhZWRhNGNmYjZjYTcxMGMxMSJ9fX0=");
        ItemMeta boosterMeta = boostersHead.getItemMeta();
        boosterMeta.setDisplayName("§eBoosters");

        List<String> loreBoosters = new ArrayList<>();
        loreBoosters.add("§7Clique para acessar");
        loreBoosters.add("§7o menu de boosters");
        boosterMeta.setLore(loreBoosters);

        boostersHead.setItemMeta(boosterMeta);

        inventario.setItem(10, locaisDeMina);
        inventario.setItem(11, encantamentos);
        inventario.setItem(12, beneficios);
        inventario.setItem(16, boostersHead);
    }

    public Inventory get() {
        return inventario;
    }
}
