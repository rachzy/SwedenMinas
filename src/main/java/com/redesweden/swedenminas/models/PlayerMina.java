package com.redesweden.swedenminas.models;

import com.connorlinfoot.actionbarapi.ActionBarAPI;
import com.redesweden.swedencash.models.PlayerCash;
import com.redesweden.swedeneconomia.data.Players;
import com.redesweden.swedeneconomia.functions.ConverterQuantia;
import com.redesweden.swedeneconomia.models.PlayerSaldo;
import com.redesweden.swedenflocos.models.PlayerFlocos;
import com.redesweden.swedenminas.SwedenMinas;
import com.redesweden.swedenminas.data.Picaretas;
import com.redesweden.swedenminas.files.ConfigFile;
import com.redesweden.swedenminas.functions.GetBlocosPorPerto;
import com.redesweden.swedenminas.functions.InstantFirework;
import com.redesweden.swedenminas.functions.PastedBlock;
import com.redesweden.swedenminas.functions.SerializeToScoreboard;
import com.redesweden.swedenminas.types.RecompensaTipo;
import com.redesweden.swedenranks.models.PlayerRank;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.*;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class PlayerMina {
    private final String nickname;
    private ItemStack[] inventarioPassado;
    private final Location localPassado;
    private final Mina mina;

    public PlayerMina(String nickname, ItemStack[] inventarioPassado, Location localPassado, Mina mina) {
        this.nickname = nickname;
        this.inventarioPassado = inventarioPassado;
        this.localPassado = localPassado;
        this.mina = mina;
    }

    public String getNickname() {
        return nickname;
    }

    public Mina getMina() {
        return mina;
    }

    public ItemStack[] getInventarioPassado() {
        return inventarioPassado;
    }

    public Location getLocalPassado() {
        return localPassado;
    }

    public void setarScoreboard(Boolean iniciarLoop) {
        Player player = Bukkit.getServer().getPlayer(nickname);
        PlayerRank playerRank = com.redesweden.swedenranks.data.Players.getPlayerPorNickname(player.getName());
        PlayerSaldo playerSaldo = Players.getPlayer(player.getName());
        PlayerCash playerCash = com.redesweden.swedencash.data.Players.getPlayerPorNickname(player.getName());
        PlayerFlocos playerFlocos = com.redesweden.swedenflocos.data.Players.getPlayerPorNickname(player.getName());
        Picareta picareta = Picaretas.getPicaretaPorDono(player.getName());

        int corChance = new Random().nextInt(2);
        String corTitulo = "§e";
        String corIP = "§6";

        if (corChance > 0) {
            corTitulo = "§6";
            corIP = "§e";
        }

        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard scoreboard = manager.getNewScoreboard();

        Objective objective = scoreboard.registerNewObjective("minaScoreboard", "dummy");
        objective.setDisplayName(String.format("%s§lMINA", corTitulo));
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        Score blank1 = objective.getScore("\u0020");
        blank1.setScore(13);

        Score rank = objective.getScore(String.format(" §7Seu rank: %s", playerRank.getRank().getTituloPorLevel(playerRank.getLevel())));
        rank.setScore(12);

        Score blank2 = objective.getScore("\u0020\u0020");
        blank2.setScore(11);

        Score money = objective.getScore(String.format(" §7| §fSaldo: §a$%s", playerSaldo.getSaldo(true)));
        money.setScore(10);

        Score cash = objective.getScore(String.format(" §7| §fCash: §6✰%s", new ConverterQuantia(playerCash.getCash()).emLetras()));
        cash.setScore(9);

        Score flocos = objective.getScore(String.format(" §7| §fFlocos: §b❄%s", new ConverterQuantia(playerFlocos.getFlocos()).emLetras()));
        flocos.setScore(8);

        Score blank3 = objective.getScore("\u0020\u0020\u0020");
        blank3.setScore(7);

        Score picaretaScore = objective.getScore(" §bPicareta:");
        picaretaScore.setScore(6);

        Score nivel = objective.getScore(String.format("\u0020\u0020§a■ §fNível: §a%s§7/§a100", picareta.getNivel()));
        nivel.setScore(5);

        Score energia = objective.getScore(String.format("\u0020\u0020§6■ §fEnergia: §e%s§7/§e%s", new ConverterQuantia(picareta.getEnergia()).emLetras(), new ConverterQuantia(BigDecimal.valueOf((picareta.getNivel() + 1) * 2000L)).emLetras()));
        energia.setScore(4);

        Score blank4 = objective.getScore("\u0020\u0020\u0020\u0020");
        blank4.setScore(3);

        Score mina = objective.getScore(String.format(" §7Mina atual: %s", this.mina.getTitulo()));
        mina.setScore(2);

        Score blank5 = objective.getScore("\u0020\u0020\u0020\u0020\u0020");
        blank5.setScore(1);

        Score ip = objective.getScore(new SerializeToScoreboard(String.format("\u0020\u0020%s%s", corIP, ConfigFile.getIP()), 32).generate());
        ip.setScore(0);

        player.setScoreboard(scoreboard);
        if(!iniciarLoop) return;
        Bukkit.getScheduler().runTaskLater(SwedenMinas.getPlugin(SwedenMinas.class), () -> {
            if(com.redesweden.swedenminas.data.Players.getPlayerPorNickname(nickname) == null) return;
            setarScoreboard(true);
        }, 20L);
    }

    public void teleportar() {
        Player player = Bukkit.getServer().getPlayer(nickname);
        Picareta picareta = Picaretas.getPicaretaPorDono(player.getName());

        if (player.hasPermission("swedenminas.fly")) {
            player.setAllowFlight(true);
            player.setFlying(true);
        }

        player.getInventory().clear();
        player.getInventory().setItem(0, picareta.pegarPicareta(true));

        player.teleport(mina.getSpawn());

        player.playSound(player.getLocation(), Sound.LEVEL_UP, 3.0F, 1F);
        player.sendMessage(String.format("§2§lMINAS §e>> §aVocê foi teleportado para a mina §e%s§a.", mina.getTitulo()));

        player.sendTitle("§e§lMINA", "§fVocê está na Mina " + mina.getTitulo());

        setarScoreboard(true);
    }

    public void quebrarBloco(Boolean luckyBlock, Boolean quebradoComEncantamento, Location localBloco) {
        Player player = Bukkit.getServer().getPlayer(nickname);
        PlayerSaldo playerSaldo = Players.getPlayer(nickname);
        PlayerFlocos playerFlocos = com.redesweden.swedenflocos.data.Players.getPlayerPorNickname(nickname);
        Picareta picareta = Picaretas.getPicaretaPorDono(nickname);

        final BigDecimal[] moneyFinal = {mina.getValorBloco()};
        final BigDecimal[] flocosFinal = {new BigDecimal(1)};
        final Boolean[] efeitoLuckyBlock = {luckyBlock};
        AtomicReference<ItemStack> recompensa = new AtomicReference<>(null);

        picareta.getLeveis().forEach((level) -> {
            switch (level.getLevel()) {
                case FORTUNA:
                    if (level.getLevelAtual() != 0) {
                        moneyFinal[0] = moneyFinal[0].add(BigDecimal.valueOf((level.getLevelAtual()) / 0.5));
                    }
                    break;
                case MULTPLICADOR:
                    if (level.getLevelAtual() != 0) {
                        flocosFinal[0] = flocosFinal[0].add(BigDecimal.valueOf((level.getLevelAtual())));
                    }
                    break;
                case INVOCADOR:
                    if (level.getLevelAtual() != 0) {
                        int chanceLuckyBlock = new Random().nextInt(1801);
                        if (chanceLuckyBlock <= level.getLevelAtual() / 2) {
                            efeitoLuckyBlock[0] = true;
                        }
                    }
                    break;
                case SORTUDO:
                    if (efeitoLuckyBlock[0]) {
                        if (level.getLevelAtual() > 0) {
                            recompensa.set(new Recompensa(level.getLevelAtual() / 10, RecompensaTipo.MINA).gerar());
                        } else {
                            recompensa.set(new Recompensa(1 / 10, RecompensaTipo.MINA).gerar());
                        }

                    }
                    break;
                case EXPLOSAO:
                    if (level.getLevelAtual() > 0 && !quebradoComEncantamento) {
                        int chanceExplosao = new Random().nextInt(15000);
                        if (chanceExplosao <= level.getLevelAtual() / 4) {
                            List<Block> getBlocosAoRedor = new GetBlocosPorPerto(player.getLocation(), 3, false, false).getBlocos();

                            getBlocosAoRedor.forEach((bloco) -> {
                                if ((bloco.getType() != mina.getBloco().getType() || bloco.getData() != mina.getBloco().getData().getData()) && bloco.getType() != Material.GOLD_BLOCK)
                                    return;
                                PastedBlock pastedBlock = new PastedBlock(bloco.getX(), bloco.getY(), bloco.getZ(), 0, (byte) 0);
                                PastedBlock.BlockQueue.getQueue(bloco.getWorld()).add(pastedBlock);
                                this.quebrarBloco(bloco.getType() == Material.GOLD_BLOCK, true, null);
                            });
                            player.playSound(player.getLocation(), Sound.FIREWORK_BLAST, 1.5F, 1F);
                            player.sendMessage("§2§lMINAS §e>> §aSua picareta usou §e§lEXPLOSÃO§a.");
                        }
                    }
                    break;
                case LASER:
                    if (level.getLevelAtual() > 0 && !quebradoComEncantamento) {
                        int chanceLaser = new Random().nextInt(60000);
                        if (chanceLaser <= level.getLevelAtual() / 6) {
                            double xMaior = Math.max(mina.getPos1().getX(), mina.getPos2().getX());
                            double xMenor = Math.min(mina.getPos1().getX(), mina.getPos2().getX());

                            double zMaior = Math.max(mina.getPos1().getZ(), mina.getPos2().getZ());
                            double zMenor = Math.min(mina.getPos1().getZ(), mina.getPos2().getZ());

                            for (double x = xMenor; x <= xMaior; x++) {
                                for (double z = zMenor; z <= zMaior; z++) {
                                    Block blocoMina = mina.getPos1().getWorld().getBlockAt(new Location(mina.getPos1().getWorld(), x, localBloco.getY(), z));
                                    if (blocoMina.getType() != Material.AIR) {
                                        PastedBlock pastedBlock = new PastedBlock(blocoMina.getX(), blocoMina.getY(), blocoMina.getZ(), 0, (byte) 0);
                                        PastedBlock.BlockQueue.getQueue(blocoMina.getWorld()).add(pastedBlock);
                                        this.quebrarBloco(blocoMina.getType() == Material.GOLD_BLOCK, true, null);
                                    }
                                }
                            }
                            player.playSound(player.getLocation(), Sound.GHAST_FIREBALL, 2F, 1F);
                            player.sendMessage("§2§lMINAS §e>> §aSua picareta usou §c§lLASER§a.");
                        }
                    }
                    break;
                case SUPER:
                    if (level.getLevelAtual() > 0 && !quebradoComEncantamento) {
                        int chanceSuper = new Random().nextInt(60000);
                        if (chanceSuper <= level.getLevelAtual() / 45) {
                            double xMaior = Math.max(mina.getPos1().getX(), mina.getPos2().getX());
                            double xMenor = Math.min(mina.getPos1().getX(), mina.getPos2().getX());

                            double yMaior = Math.max(mina.getPos1().getY(), mina.getPos2().getY());
                            double yMenor = Math.min(mina.getPos1().getY(), mina.getPos2().getY());

                            double zMaior = Math.max(mina.getPos1().getZ(), mina.getPos2().getZ());
                            double zMenor = Math.min(mina.getPos1().getZ(), mina.getPos2().getZ());

                            for (double x = xMenor; x <= xMaior; x++) {
                                for (double y = yMenor; y <= yMaior; y++) {
                                    for (double z = zMenor; z <= zMaior; z++) {
                                        Block blocoMina = mina.getPos1().getWorld().getBlockAt(new Location(mina.getPos1().getWorld(), x, y, z));
                                        if (blocoMina.getType() != Material.AIR) {
                                            PastedBlock pastedBlock = new PastedBlock(blocoMina.getX(), blocoMina.getY(), blocoMina.getZ(), 0, (byte) 0);
                                            PastedBlock.BlockQueue.getQueue(blocoMina.getWorld()).add(pastedBlock);
                                            this.quebrarBloco(blocoMina.getType() == Material.GOLD_BLOCK, true, null);
                                        }
                                    }
                                }
                            }

                            player.playSound(player.getLocation(), Sound.ENDERDRAGON_GROWL, 2F, 1F);
                            player.sendMessage("§2§lMINAS §e>> §aSua picareta usou §6§lEXPLOSÃO§a.");
                        }
                    }
                    break;
                default:
                    break;
            }
        });

        String mensagemBoost = "";

        if (player.hasPermission("swedenminas.money3x")) {
            moneyFinal[0] = moneyFinal[0].multiply(BigDecimal.valueOf(3));
            mensagemBoost = "§a(Boost §63x§a)";
        } else if (player.hasPermission("swedenminas.money2x")) {
            moneyFinal[0] = moneyFinal[0].multiply(BigDecimal.valueOf(2));
            mensagemBoost = "§a(Boost §62x§a)";
        }

        if (player.hasPermission("swedenminas.flocos3x")) {
            flocosFinal[0] = flocosFinal[0].multiply(BigDecimal.valueOf(3));
        } else if (player.hasPermission("swedenminas.flocos2x")) {
            flocosFinal[0] = flocosFinal[0].multiply(BigDecimal.valueOf(2));
        }

        if (efeitoLuckyBlock[0]) {
            moneyFinal[0] = moneyFinal[0].multiply(BigDecimal.valueOf(2));
            ActionBarAPI.sendActionBar(player, String.format("§2§lMINAS §e>> §fVocê quebrou uma §6Lucky Block §fe ganhou §a$%s§f. %s", new ConverterQuantia(moneyFinal[0]).emLetras(), mensagemBoost));

            if (player.getInventory().addItem(recompensa.get()).size() != 0 && !quebradoComEncantamento) {
                player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 3.0F, 0.5F);
                player.sendMessage("§2§lMINAS §e>> §cSeu inventário está cheio! Esvazi-o para receber recompensas.");
            }
        } else {
            ActionBarAPI.sendActionBar(player, String.format("§2§lMINAS §e>> §fVocê ganhou §a$%s §fpor ter quebrado este bloco. %s", new ConverterQuantia(moneyFinal[0]).emLetras(), mensagemBoost));
        }

        picareta.addEnergia();

        if (picareta.getEnergia().compareTo(BigDecimal.valueOf((picareta.getNivel() + 1) * 2000L)) >= 0) {
            picareta.addNivel();
            picareta.setEnergia(BigDecimal.ZERO);

            List<LevelPicareta> leveisNaoMaximos = picareta.getLeveis().stream().filter(level -> level.getLevelAtual() != level.getLevelMaximo()).collect(Collectors.toList());
            int encantamentoAleatorio = new Random().nextInt(leveisNaoMaximos.toArray().length);
            LevelPicareta level = leveisNaoMaximos.get(encantamentoAleatorio);

            level.evoluirLevel();

            new InstantFirework(FireworkEffect.builder().withColor(Color.LIME, Color.YELLOW).withTrail().build(), player.getLocation());
            player.sendMessage(String.format("§2§lMINAS §e>> §aSua picareta evoluiu para o nível §b%s§a! Como recompensa, você ganhou §e+1 §ade §e%s§a.", picareta.getNivel(), level.getMeta().getTitle()));
        }

        playerSaldo.addSaldo(moneyFinal[0]);
        playerFlocos.addFlocos(flocosFinal[0]);
        player.getInventory().setItemInHand(picareta.pegarPicareta(true));
    }

    public void sairDaMina(Boolean teleportarParaLocalPassado, Boolean sairSemRemover) {
        Player player = Bukkit.getServer().getPlayer(nickname);

        List<ItemStack> itensNoInventario = new ArrayList<>();

        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null && (!item.hasItemMeta() || !item.getItemMeta().hasDisplayName() || !item.getItemMeta().getDisplayName().startsWith("§e§lPICARETA"))) {
                itensNoInventario.add(item);
            }
        }

        for(ItemStack item : inventarioPassado) {
            if(item != null && item.hasItemMeta() && item.getItemMeta().hasDisplayName() && item.getItemMeta().getDisplayName().startsWith("§e§lVARA DE PESCA")) {
                item.setType(Material.AIR);
            }
        }

        if (itensNoInventario.toArray().length > 0) {
            ItemStack[] inventarioMina = Arrays.stream(player.getInventory().getContents()).filter((item) -> item != null && (!item.hasItemMeta() || !item.getItemMeta().hasDisplayName() || !item.getItemMeta().getDisplayName().startsWith("§e§lPICARETA"))).toArray(ItemStack[]::new);

            player.getInventory().setContents(inventarioPassado);

            HashMap<Integer, ItemStack> itensQueNaoCouberam = player.getInventory().addItem(inventarioMina);

            if (itensQueNaoCouberam.size() > 0) {
                itensQueNaoCouberam.values().forEach(item -> {
                    if (item != null && item.getType() != Material.AIR && item.getType() != Material.DIAMOND_PICKAXE) {
                        player.getEnderChest().addItem(item);
                    }
                });
                player.sendMessage("§2§lMINAS §e>> §cSeu inventário não tinha espaço suficiente para armazenar todos os itens recebidos, por isso, alguns de seus itens foram salvos em seu /ec.");
            } else {
                player.sendMessage("§2§lMINAS §e>> §aTodos os itens recebidos na mina foram adicionados ao seu inventário.");
            }
        } else {
            player.getInventory().setContents(inventarioPassado);
        }

        player.setFlying(false);
        player.setAllowFlight(false);

        player.removePotionEffect(PotionEffectType.SPEED);
        player.removePotionEffect(PotionEffectType.FAST_DIGGING);

        if (sairSemRemover) {
            player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
            player.sendMessage("§2§lMINAS §e>> §cVocê foi retirado da mina, pois o servidor está reiniciando.");

            if (teleportarParaLocalPassado) {
                if(localPassado.getWorld().getName().equalsIgnoreCase("Pescador")) {
                    player.performCommand("spawn");
                } else {
                    player.teleport(localPassado);
                }
            }

            player.playSound(player.getLocation(), Sound.CLICK, 3.0F, 2F);
        } else {
            player.sendMessage("§2§lMINA §e>> §aVocê saiu da área de mineração.");
            Bukkit.getScheduler().runTaskLater(SwedenMinas.getPlugin(SwedenMinas.class), () -> {
                player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
                com.redesweden.swedenminas.data.Players.removerPlayer(this);

                if (teleportarParaLocalPassado) {
                    if(localPassado.getWorld().getName().equalsIgnoreCase("Pescador")) {
                        player.performCommand("spawn");
                    } else {
                        player.teleport(localPassado);
                    }
                }
            }, 1L);
        }
    }
}
