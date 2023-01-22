package com.redesweden.swedenminas.commands;

import com.redesweden.swedenminas.data.Minas;
import com.redesweden.swedenminas.models.Mina;
import com.redesweden.swedenminas.models.PlayerMina;
import com.redesweden.swedenranks.data.Players;
import com.redesweden.swedenranks.models.PlayerRank;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MinerarCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage("§2§lMINAS §e>> §cApenas players podem executar este comando.");
            return true;
        }

        Player player = (Player) sender;

        if(player.getWorld().getName().equals("Pescador")) {
            player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 3.0F, 0.5F);
            player.sendMessage("§2§lMINAS §e>> §cVocê não pode ir minerar enquanto está no mundo de pescaria.");
            return true;
        }

        Mina mina;
        if (player.hasPermission("swedenminas.mina.vip") && Minas.getMinaPorId("VIP") != null) {
            mina = Minas.getMinaPorId("VIP");
        } else {
            PlayerRank playerRank = Players.getPlayerPorNickname(player.getName());
            mina = Minas.getMinaPorId(playerRank.getRank().getId());
        }

        PlayerMina playerMina = new PlayerMina(player.getName(), player.getInventory().getContents(), player.getLocation(), mina);

        try {
            com.redesweden.swedenminas.data.Players.addPlayer(playerMina);
            playerMina.teleportar();
        } catch (Exception e) {
            player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 3.0F, 0.5F);
            player.sendMessage("§2§lMINAS §e>> §cVocê já está minerando.");
        }
        return true;
    }
}
