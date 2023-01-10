package com.redesweden.swedenminas.events;

import com.redesweden.swedenflocos.models.PlayerFlocos;
import com.redesweden.swedenminas.GUIs.LocaisGUI;
import com.redesweden.swedenminas.GUIs.MinaCommandGUI;
import com.redesweden.swedenminas.GUIs.PicaretaGUI;
import com.redesweden.swedenminas.data.Minas;
import com.redesweden.swedenminas.data.Picaretas;
import com.redesweden.swedenminas.models.LevelPicareta;
import com.redesweden.swedenminas.models.Mina;
import com.redesweden.swedenminas.models.Picareta;
import com.redesweden.swedenminas.models.PlayerMina;
import com.redesweden.swedenranks.data.Players;
import com.redesweden.swedenranks.data.Ranks;
import com.redesweden.swedenranks.models.PlayerRank;
import com.redesweden.swedenranks.models.Rank;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.math.BigDecimal;
import java.util.Arrays;

public class InventoryClickListener implements Listener {
    @EventHandler(priority = EventPriority.HIGH)
    public void onInventoryClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        String tituloInv = e.getView().getTitle().substring(2);
        String tituloItem = null;

        if (e.getClick() == ClickType.NUMBER_KEY) {
            ItemStack itemSlot0 = player.getInventory().getItem(0);
            if (itemSlot0.hasItemMeta() && itemSlot0.getItemMeta().hasDisplayName() && itemSlot0.getItemMeta().getDisplayName().startsWith("§e§lPICARETA")) {
                e.setCancelled(true);
                return;
            }
        }

        if (e.getCurrentItem() != null && e.getCurrentItem().hasItemMeta() && e.getCurrentItem().getItemMeta().hasDisplayName()) {
            tituloItem = e.getCurrentItem().getItemMeta().getDisplayName();
        }

        if (tituloItem != null && tituloItem.startsWith("§e§lPICARETA") && e.getCurrentItem().getType() == Material.DIAMOND_PICKAXE) {
            e.setCancelled(true);
            return;
        }

        if(tituloInv.equalsIgnoreCase("suas caixas")) {
            if(tituloItem != null && tituloItem.equals("§eArmazém de Recompensas")) {
                PlayerMina playerMina = com.redesweden.swedenminas.data.Players.getPlayerPorNickname(player.getName());
                if(playerMina == null) return;

                e.setCancelled(true);
                player.closeInventory();
                player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 3.0F, 0.5F);
                player.sendMessage("§2§lMINAS §e>> §cVocê não pode acessar seu armazém enquanto está minerando, use o comando '/mina sair'");
                return;
            }
        }

        if (tituloInv.equalsIgnoreCase("mina")) {
            e.setCancelled(true);

            if (tituloItem == null) return;

            if (tituloItem.equals("§eÁreas de Mineração")) {
                player.playSound(player.getLocation(), Sound.CLICK, 3.0F, 2F);
                player.openInventory(new LocaisGUI(player.getName()).get());
                return;
            }

            if (tituloItem.equals("§eEncantamentos")) {
                player.playSound(player.getLocation(), Sound.CLICK, 3.0F, 2F);
                player.openInventory(new PicaretaGUI(player.getName()).get());
                return;
            }

            if(tituloItem.startsWith("§eBenefícios")) {
                player.performCommand("site");
                player.closeInventory();
                return;
            }

            if (tituloItem.equals("§eIr Minerar")) {
                PlayerRank playerRank = Players.getPlayerPorNickname(player.getName());
                Mina mina = Minas.getMinaPorRank(playerRank.getRank());

                if (mina == null) {
                    player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 3.0F, 0.5F);
                    player.sendMessage("§2§lMINAS §e>> §cSeu rank não possui uma mina.");
                    return;
                }

                PlayerMina playerMina = new PlayerMina(player.getName(), player.getInventory().getContents(), player.getLocation(), mina);

                try {
                    com.redesweden.swedenminas.data.Players.addPlayer(playerMina);
                } catch (Exception ex) {
                    player.closeInventory();
                    player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 3.0F, 0.5F);
                    player.sendMessage("§2§lMINAS >> §cVocê já está em uma área de mineração. Utilize '/mina sair' para sair.");
                    return;
                }


                playerMina.teleportar();
            }

            if (tituloItem.equals("§cSair da Mina")) {
                player.closeInventory();
                player.performCommand("mina sair");
                return;
            }

            if(tituloItem.equals("§eBoosters")) {
                player.performCommand("cash boosters mina");
                return;
            }
            return;
        }

        if (tituloInv.equalsIgnoreCase("locais de mineração")) {
            e.setCancelled(true);

            if (tituloItem == null) return;

            if (tituloItem.startsWith("§b§lMINA")) {
                Rank rankAlvo = Ranks.getRankPorId(Arrays.stream(tituloItem.split(" ")).toArray()[1].toString().substring(2));
                PlayerRank playerRank = Players.getPlayerPorNickname(player.getName());

                if (Ranks.getPosicaoPorRank(playerRank.getRank()) >= Ranks.getPosicaoPorRank(rankAlvo)) {
                    PlayerMina playerMina = new PlayerMina(player.getName(), player.getInventory().getContents(), player.getLocation(), Minas.getMinaPorRank(rankAlvo));

                    try {
                        com.redesweden.swedenminas.data.Players.addPlayer(playerMina);
                    } catch (Exception ex) {
                        player.closeInventory();
                        player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 3.0F, 0.5F);
                        player.sendMessage("§2§lMINAS >> §cVocê já está em uma área de mineração. Utilize '/mina sair' para sair.");
                        return;
                    }

                    playerMina.teleportar();
                    return;
                }
                player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 3.0F, 0.5F);
                player.sendMessage("§2§lMINAS >> §cVocê não tem acesso à esta mina.");
                return;
            }

            if(tituloItem.equals("§eVoltar")) {
                player.playSound(player.getLocation(), Sound.CLICK, 3.0F, 2F);
                player.openInventory(new MinaCommandGUI(player).get());
            }
            return;
        }

        if (tituloInv.equalsIgnoreCase("picareta")) {
            e.setCancelled(true);

            if (tituloItem == null) return;

            if(tituloItem.equals("§eVoltar")) {
                player.playSound(player.getLocation(), Sound.CLICK, 3.0F, 2F);
                player.openInventory(new MinaCommandGUI(player).get());
                return;
            }

            Picareta picareta = Picaretas.getPicaretaPorDono(player.getName());

            String finalTituloItem = tituloItem;
            LevelPicareta levelSelecionado = picareta
                    .getLeveis()
                    .stream()
                    .filter(level -> level.getMeta().getTitle().equals(finalTituloItem.substring(2)))
                    .findFirst()
                    .orElse(null);

            if (levelSelecionado == null) return;

            PlayerFlocos playerFlocos = com.redesweden.swedenflocos.data.Players.getPlayerPorNickname(player.getName());

            if (levelSelecionado.getLevelAtual() >= levelSelecionado.getLevelMaximo()) {
                player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 3.0F, 0.5F);
                player.sendMessage("§2§lMINAS §e>> §cSua picareta já está no level máximo neste encantamento.");
                return;
            }

            BigDecimal custo = levelSelecionado.getCustoProximoLevel();

            if (playerFlocos.getFlocos().compareTo(custo) < 0) {
                player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 3.0F, 0.5F);
                player.sendMessage("§2§lMINAS §e>> §cVocê não tem flocos o suficiente para evoluir sua picareta neste encantamento.");
                return;
            }

            levelSelecionado.evoluirLevel();
            playerFlocos.subFlocos(custo);

            PlayerMina playerMina = com.redesweden.swedenminas.data.Players.getPlayerPorNickname(player.getName());
            if(playerMina != null) {
                playerMina.setarScoreboard();
                player.getInventory().setItem(0, picareta.pegarPicareta(true));
            }

            player.openInventory(new PicaretaGUI(player.getName()).get());
            player.playSound(player.getLocation(), Sound.LEVEL_UP, 3.0F, 1F);
            player.sendMessage(String.format("§2§lMINAS §e>> §aVocê evoluiu o encantamento §e%s §apara o level §b%s§a.", levelSelecionado.getMeta().getTitle(), levelSelecionado.getLevelAtual()));
        }
    }
}
