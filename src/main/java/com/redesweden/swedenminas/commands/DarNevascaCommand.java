package com.redesweden.swedenminas.commands;

import com.redesweden.swedenminas.functions.NevascaFactory;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class DarNevascaCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!sender.hasPermission("swedenminas.admin")) {
            if(sender instanceof Player) {
                Player player = (Player) sender;
                player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 3.0F, 0.5F);
            }
            sender.sendMessage("§f§lNEVASCAS §e>> §cVocê não tem permissão para executar este comando.");
            return true;
        }

        if(args.length != 2) {
            if(sender instanceof Player) {
                Player player = (Player) sender;
                player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 3.0F, 0.5F);
            }
            sender.sendMessage("§f§lNEVASCAS §e>> §cUso: /darnevasca <player> <level>.");
            return true;
        }

        Player alvo = Bukkit.getServer().getPlayer(args[0]);

        if(alvo == null || !alvo.isOnline()) {
            if(sender instanceof Player) {
                Player player = (Player) sender;
                player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 3.0F, 0.5F);
            }
            sender.sendMessage(String.format("§f§lNEVASCAS §e>> §cNão foi possível encontrar um jogador online com o nick '%s'.", args[0]));
            return true;
        }

        int level;
        try {
            level = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            if(sender instanceof Player) {
                Player player = (Player) sender;
                player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 3.0F, 0.5F);
            }
            sender.sendMessage("§f§lNEVASCAS §e>> §cO level precisa ser um número de 1 a 5.");
            return true;
        }

        if(level < 1 || level > 5) {
            if(sender instanceof Player) {
                Player player = (Player) sender;
                player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 3.0F, 0.5F);
            }
            sender.sendMessage("§f§lNEVASCAS §e>> §cO level precisa ser um número de 1 a 5.");
            return true;
        }

        ItemStack nevascaItem = new NevascaFactory(level).gerarItem();
        alvo.getInventory().addItem(nevascaItem);

        if(sender instanceof Player) {
            Player player = (Player) sender;
            player.playSound(player.getLocation(), Sound.NOTE_PLING, 3.0F, 2F);
        }
        sender.sendMessage(String.format("§f§lNEVASCAS §e>> §aVocê deu uma Nevasca de Level §e%s §apara o jogador §b%s§a.", level, alvo.getDisplayName()));
        return true;
    }
}
