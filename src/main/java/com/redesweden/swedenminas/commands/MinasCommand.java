package com.redesweden.swedenminas.commands;

import com.redesweden.swedenminas.GUIs.LocaisGUI;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MinasCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage("§2§lMINAS §e>> §cApenas players podem executar este comando.");
            return true;
        }

        Player player = (Player) sender;
        player.playSound(player.getLocation(), Sound.CLICK, 3.0F, 2F);
        player.openInventory(new LocaisGUI(player.getName()).get());
        return true;
    }
}
