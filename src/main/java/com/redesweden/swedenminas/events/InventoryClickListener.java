package com.redesweden.swedenminas.events;

import com.redesweden.swedencash.models.PlayerCash;
import com.redesweden.swedenflocos.models.PlayerFlocos;
import com.redesweden.swedenminas.GUIs.*;
import com.redesweden.swedenminas.data.EventosEspeciais;
import com.redesweden.swedenminas.data.Minas;
import com.redesweden.swedenminas.data.Nevascas;
import com.redesweden.swedenminas.data.Picaretas;
import com.redesweden.swedenminas.files.NevascasFile;
import com.redesweden.swedenminas.functions.InstantFirework;
import com.redesweden.swedenminas.models.*;
import com.redesweden.swedenminas.types.MinaTipo;
import com.redesweden.swedenranks.data.Players;
import com.redesweden.swedenranks.data.Ranks;
import com.redesweden.swedenranks.models.PlayerRank;
import org.bukkit.*;
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

        if (tituloInv.equalsIgnoreCase("suas caixas")) {
            if (tituloItem != null && tituloItem.equals("§eArmazém de Recompensas")) {
                PlayerMina playerMina = com.redesweden.swedenminas.data.Players.getPlayerPorNickname(player.getName());
                if (playerMina == null) return;

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

            if (tituloItem.equals("§eLocais de Mineração")) {
                player.playSound(player.getLocation(), Sound.CLICK, 3.0F, 2F);
                player.openInventory(new LocaisGUI(player.getName()).get());
                return;
            }

            if (tituloItem.equals("§eEncantamentos")) {
                player.playSound(player.getLocation(), Sound.CLICK, 3.0F, 2F);
                player.openInventory(new PicaretaGUI(player.getName()).get());
                return;
            }

            if (tituloItem.startsWith("§eBenefícios")) {
                player.performCommand("site");
                player.closeInventory();
                return;
            }

            if (tituloItem.equals("§eIr Minerar")) {
                PlayerRank playerRank = Players.getPlayerPorNickname(player.getName());

                Mina mina;
                if (player.hasPermission("swedenminas.mina.vip") && Minas.getMinaPorId("VIP") != null) {
                    mina = Minas.getMinaPorId("VIP");
                } else {
                    mina = Minas.getMinaPorId(playerRank.getRank().getId());
                }


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
                return;
            }

            if (tituloItem.equals("§cSair da Mina")) {
                player.closeInventory();
                player.performCommand("mina sair");
                return;
            }

            if (tituloItem.equals("§eBoosters")) {
                player.performCommand("cash boosters mina");
                return;
            }
            return;
        }

        if (tituloInv.equalsIgnoreCase("locais de mineração")) {
            e.setCancelled(true);

            if (tituloItem == null) return;

            if (tituloItem.startsWith("§b§lMINA")) {
                Mina minaAlvo = Minas.getMinaPorTitulo(Arrays.stream(tituloItem.split(" ")).toArray()[1].toString());
                PlayerMina playerMina = new PlayerMina(player.getName(), player.getInventory().getContents(), player.getLocation(), minaAlvo);

                if (minaAlvo.getTipo() == MinaTipo.RANK) {
                    PlayerRank playerRank = Players.getPlayerPorNickname(player.getName());

                    if (Ranks.getPosicaoPorRank(playerRank.getRank()) >= Ranks.getPosicaoPorRank(Ranks.getRankPorId(minaAlvo.getId()))) {
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
                }

                if ((minaAlvo.getTipo() == MinaTipo.VIP && player.hasPermission("swedenminas.mina.vip")) || minaAlvo.getTipo() == MinaTipo.PVP) {
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

            if (tituloItem.equals("§eVoltar")) {
                player.playSound(player.getLocation(), Sound.CLICK, 3.0F, 2F);
                player.openInventory(new MinaCommandGUI(player).get());
            }
            return;
        }

        if (tituloInv.equalsIgnoreCase("picareta")) {
            e.setCancelled(true);

            if (tituloItem == null) return;

            if (tituloItem.equals("§eVoltar")) {
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
            if (playerMina != null) {
                playerMina.setarScoreboard(false);
                player.getInventory().setItem(0, picareta.pegarPicareta(true));
            }

            player.openInventory(new PicaretaGUI(player.getName()).get());
            player.playSound(player.getLocation(), Sound.LEVEL_UP, 3.0F, 1F);
            player.sendMessage(String.format("§2§lMINAS §e>> §aVocê evoluiu o encantamento §e%s §apara o level §b%s§a.", levelSelecionado.getMeta().getTitle(), levelSelecionado.getLevelAtual()));
        }

        if (tituloInv.equalsIgnoreCase("nevasca")) {
            e.setCancelled(true);

            if (tituloItem == null) return;

            Nevasca nevasca = Nevascas.getNevascaPorDono(player.getName());

            if (tituloItem.equals("§eDar Upgrade")) {
                int proximoLevel = nevasca.getLevel() + 1;

                if (proximoLevel > 5) {
                    player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 3.0F, 0.5F);
                    player.sendMessage("§f§lNEVASCAS §e>> §cSua Nevasca já está no level máximo (5).");
                    player.closeInventory();
                    return;
                }

                BigDecimal custoProximoLevel = new BigDecimal("1500000").multiply(BigDecimal.valueOf(proximoLevel));
                PlayerCash playerCash = com.redesweden.swedencash.data.Players.getPlayerPorNickname(player.getName());

                if (playerCash.getCash().compareTo(custoProximoLevel) < 0) {
                    player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 3.0F, 0.5F);
                    player.sendMessage("§f§lNEVASCAS §e>> §cVocê não tem CASH suficiente para evoluir sua Nevasca.");
                    return;
                }

                playerCash.subCash(custoProximoLevel);
                nevasca.addLevel();

                new InstantFirework(FireworkEffect.builder().withColor(Color.WHITE, Color.YELLOW).withFlicker().withFade(Color.ORANGE).build(), nevasca.getLocal().clone().add(0.5, 1, 0.5));
                player.closeInventory();
                player.playSound(player.getLocation(), Sound.LEVEL_UP, 3.0F, 1F);
                player.sendMessage(String.format("§f§lNEVASCAS §e>> §aVocê evoluiu sua Nevasca para o level §e%s§a.", nevasca.getLevel()));
                return;
            }

            if (tituloItem.equals("§eAmigos")) {
                player.playSound(player.getLocation(), Sound.CLICK, 3.0F, 2F);
                player.openInventory(new GerenciarAmigosGUI(player).get());
                return;
            }

            if (tituloItem.equals("§eResetar Neve")) {
                nevasca.iniciar();
                player.playSound(player.getLocation(), Sound.LEVEL_UP, 3.0F, 2F);
                player.sendMessage("§f§lNEVASCAS §e>> §aNeves resetadas com sucesso.");
                player.closeInventory();
                return;
            }

            if (tituloItem.equals("§cRemover Nevasca")) {
                if (player.getInventory().firstEmpty() == -1) {
                    player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 3.0F, 0.5F);
                    player.sendMessage("§f§lNEVASCAS §e>> §cVocê precisa ter pelo menos 1 slot vazio em seu inventário para remover sua Nevasca.");
                    return;
                }

                player.closeInventory();
                nevasca.desativar(player);
                NevascasFile.removerNevasca(nevasca);
            }
        }

        if (tituloInv.equalsIgnoreCase("nevasca §8- §2amigos")) {
            e.setCancelled(true);

            if (tituloItem == null) return;

            if (tituloItem.equals("§eVoltar")) {
                player.playSound(player.getLocation(), Sound.CLICK, 3.0F, 2F);
                player.openInventory(new GerenciarNevascaGUI(player.getName()).get());
                return;
            }

            Nevasca nevasca = Nevascas.getNevascaPorDono(player.getName());

            if (tituloItem.equals("§aAdicionar Amigo")) {
                if (nevasca.getAmigos().toArray().length >= 5) {
                    player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 3.0F, 0.5F);
                    player.sendMessage("§f§lNEVASCAS §e>> §cVocê já tem o limite máximo de amigos permitidos por Nevasca (5).");
                    return;
                }

                for (int i = 4; i >= 1; i--) {
                    if (nevasca.getAmigos().toArray().length == i && !player.hasPermission(String.format("swedenminas.nevasca.amigos.%s", i + 1))) {
                        player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 3.0F, 0.5F);
                        player.sendMessage(String.format("§f§lNEVASCAS §e>> §cVocê não tem permissão para adicionar mais que %s amigo%s à sua Nevasca.", i, i == 1 ? "" : "s"));
                        return;
                    }
                }

                EventosEspeciais.addPlayerAdicionandoAmigo(player);
                player.closeInventory();
                player.playSound(player.getLocation(), Sound.CLICK, 3.0F, 2F);
                player.sendMessage("");
                player.sendMessage(" §aDigite o nome do jogador que deseja adicionar como amigo:");
                player.sendMessage(" §7(Ou digite 'CANCELAR' para cancelar esta operação)");
                player.sendMessage("");

                return;
            }

            String finalTituloItem1 = tituloItem;
            String alvo = nevasca
                    .getAmigos()
                    .stream()
                    .filter((playerNick) -> playerNick.equals(finalTituloItem1.substring(2)))
                    .findFirst()
                    .orElse(null);

            if (alvo == null) return;

            Player playerAlvo = Bukkit.getPlayer(alvo);
            nevasca.removerAmigo(alvo);

            player.openInventory(new GerenciarAmigosGUI(player).get());
            player.playSound(player.getLocation(), Sound.NOTE_PLING, 3.0F, 2F);
            player.sendMessage(String.format("§f§lNEVASCAS §7>> §eVocê removeu o jogador §b%s §ecomo amigo de sua Nevasca.", alvo));

            if (playerAlvo != null && playerAlvo.isOnline()) {
                playerAlvo.playSound(playerAlvo.getLocation(), Sound.NOTE_BASS_GUITAR, 3.0F, 0.5F);
                playerAlvo.sendMessage(String.format("§f§lNEVASCAS §e>> §cO jogador §b%s §clhe removeu como amigo de sua Nevasca.", player.getName()));
            }
        }
    }
}
