package com.redesweden.swedenminas.commands;

import com.redesweden.swedenminas.GUIs.MinaCommandGUI;
import com.redesweden.swedenminas.data.Players;
import com.redesweden.swedenminas.models.PlayerMina;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MinaCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage("§2§lMINAS §e>> §cApenas players podem executar este comando.");
            return true;
        }

        Player player = (Player) sender;

        if(args.length == 0) {
            player.playSound(player.getLocation(), Sound.NOTE_PLING, 3.0F, 2F);
            player.openInventory(new MinaCommandGUI(player).get());
            return true;
        }

        if(args[0].equals("sair")) {
            PlayerMina playerMina = Players.getPlayerPorNickname(player.getName());

            if(playerMina == null) {
                player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 3.0F, 0.5F);
                player.sendMessage("§2§lMINAS §e>> §cVocê não está em uma área de mineração.");
                return true;
            }

            playerMina.sairDaMina(true, false);
            return true;
        }

        player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 3.0F, 0.5F);
        player.sendMessage("§2§lMINAS §e>> §cSub-comando não encontrado.");
        return true;
    }
}
