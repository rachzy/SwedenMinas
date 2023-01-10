package com.redesweden.swedenminas.commands;

import com.redesweden.swedeneconomia.functions.ConverterQuantia;
import com.redesweden.swedenminas.data.Minas;
import com.redesweden.swedenminas.files.ConfigFile;
import com.redesweden.swedenminas.files.MinasFile;
import com.redesweden.swedenminas.models.Mina;
import com.redesweden.swedenranks.data.Ranks;
import com.redesweden.swedenranks.models.Rank;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.math.BigDecimal;

public class AdminaCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§c§lADMINA §e>> §cApenas players podem executar este comando.");
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("swedenminas.admin")) {
            player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 3.0F, 0.5F);
            player.sendMessage("§c§lADMINA §e>> §cVocê não tem permissão para executar este comando.");
            return true;
        }

        if (args.length == 0 || args[0] == null) {
            player.playSound(player.getLocation(), Sound.NOTE_PLING, 3.0F, 2F);
            player.sendMessage(" ");
            player.sendMessage(" §2§lMINAS §e- §c§lADMINA");
            player.sendMessage(" §7Comandos de administração e configuração de áreas de mineração");
            player.sendMessage("");
            player.sendMessage(" §e- §a/admina setmina <rank> <valorPorBloco>");
            player.sendMessage(" §e- §a/admina setspawn <mina>");
            player.sendMessage(" §e- §a/admina setpos1 <mina>");
            player.sendMessage(" §e- §a/admina setpos2 <mina>");
            player.sendMessage(" §e- §a/admina resetar <mina>");
            player.sendMessage(" §e- §a/admina reload");
            player.sendMessage("");
            return true;
        }

        if (args[0].equals("setmina")) {
            if (args.length != 3) {
                player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 3.0F, 0.5F);
                player.sendMessage("§c§lADMINA §e>> §cUso: /admina setmina <rank> <valorPorBloco>");
                return true;
            }

            Rank rank = Ranks.getRankPorId(args[1]);
            if (rank == null) {
                player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 3.0F, 0.5F);
                player.sendMessage("§c§lADMINA §e>> §cRank inválido.");
                return true;
            }

            if(Minas.getMinaPorRank(rank) != null) {
                player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 3.0F, 0.5F);
                player.sendMessage(String.format("§c§lADMINA §e>> §cJá existe uma mina para este rank, use '/admina deletar %s' para removê-la.", rank.getId()));
                return true;
            }

            BigDecimal valorPorBloco;

            try {
                valorPorBloco = new ConverterQuantia(args[2]).emNumeros();
            } catch (Exception e) {
                player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 3.0F, 0.5F);
                player.sendMessage("§c§lADMINA §e>> §cO valor por bloco precisa ser um número maior que 0.");
                return true;
            }

            MinasFile.novaMina(rank, valorPorBloco, player.getLocation());
            player.playSound(player.getLocation(), Sound.NOTE_PLING, 3.0F, 2F);
            player.sendMessage("§c§lADMINA §e>> §aVocê criou com sucesso a mina do rank §e" + rank.getId());
            return true;
        }

        if(args[0].equals("setspawn")) {
            if(args.length != 2) {
                player.playSound(player.getLocation(), Sound.NOTE_PLING, 3.0F, 2F);
                player.sendMessage("§2§lMINAS §e>> §cUso: /admina setspawn <mina>");
                return true;
            }

            Rank rank = Ranks.getRankPorId(args[1]);
            if (rank == null) {
                player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 3.0F, 0.5F);
                player.sendMessage("§c§lADMINA §e>> §cMina inválida.");
                return true;
            }

            Mina mina = Minas.getMinaPorRank(rank);

            if(mina == null) {
                player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 3.0F, 0.5F);
                player.sendMessage("§c§lADMINA §e>> §cNão existe uma mina deste rank.");
                return true;
            }

            mina.setSpawn(player.getLocation());

            player.playSound(player.getLocation(), Sound.NOTE_PLING, 3.0F, 2F);
            player.sendMessage(String.format("§c§lADMINA §e>> §aVocê setou o spawn da mina §e%s§a.", rank.getId()));
            return true;
        }

        if(args[0].equals("setpos1") || args[0].equals("setpos2")) {
            if(args.length != 2) {
                player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 3.0F, 0.5F);
                player.sendMessage(String.format("§2§lMINAS §e>> §cUso: /admina %s <mina>", args[9]));
                return true;
            }

            Rank rank = Ranks.getRankPorId(args[1]);
            if (rank == null) {
                player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 3.0F, 0.5F);
                player.sendMessage("§c§lADMINA §e>> §cMina inválida.");
                return true;
            }

            Mina mina = Minas.getMinaPorRank(rank);

            if(mina == null) {
                player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 3.0F, 0.5F);
                player.sendMessage("§c§lADMINA §e>> §cNão existe uma mina deste rank.");
                return true;
            }

            if(args[0].equals("setpos1")) {
                mina.setPos1(player.getLocation());
            } else {
                mina.setPos2(player.getLocation());

                if(mina.getPos1() != null) {
                    mina.resetar();
                }
            }

            player.playSound(player.getLocation(), Sound.NOTE_PLING, 3.0F, 2F);
            player.sendMessage(String.format("§c§lADMINA §e>> §aVocê setou a posição %s da Mina %s", args[0].charAt(6), rank.getId()));
        }

        if(args[0].equals("resetar") || args[0].equals("reset")) {
            if(args.length != 2) {
                player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 3.0F, 0.5F);
                player.sendMessage("§2§lMINAS §e>> §cUso: /admina reset <mina>");
                return true;
            }

            Rank rank = Ranks.getRankPorId(args[1]);
            if (rank == null) {
                player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 3.0F, 0.5F);
                player.sendMessage("§c§lADMINA §e>> §cMina inválida.");
                return true;
            }

            Mina mina = Minas.getMinaPorRank(rank);

            if(mina == null) {
                player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 3.0F, 0.5F);
                player.sendMessage("§c§lADMINA §e>> §cNão existe uma mina deste rank.");
                return true;
            }

            if(mina.getPos1() == null || mina.getPos2() == null) {
                player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 3.0F, 0.5F);
                player.sendMessage("§c§lADMINA §e>> §cEssa mina ainda não teve suas posições setadas.");
                return true;
            }

            mina.resetar();
            player.playSound(player.getLocation(), Sound.NOTE_PLING, 3.0F, 2F);
            player.sendMessage(String.format("§c§lADMINA §e>> §aVocê resetou a mina §e%s§a.", rank.getId()));
            return true;
        }

        if(args[0].equals("reload")) {
            ConfigFile.reload();
            player.playSound(player.getLocation(), Sound.NOTE_PLING, 3.0F, 2F);
            player.sendMessage("§c§lADMINA §e>> §aArquivo config.yml recarregado com sucesso.");
            return true;
        }

        player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 3.0F, 0.5F);
        player.sendMessage("§c§lADMINA §e>> §cSub-comando inválido.");
        return true;
    }
}
