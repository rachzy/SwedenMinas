package com.redesweden.swedenminas.commands;

import com.redesweden.swedenminas.data.Nevascas;
import com.redesweden.swedenminas.models.Nevasca;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class NevascaCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player) {
            Player player = (Player) sender;
            player.playSound(player.getLocation(), Sound.CLICK, 3.0F, 2F);
        }

        if(args.length == 0) {
            sender.sendMessage("§f§lNEVASCAS §e>> §cUso: /nevasca <sub-comando>");
            return true;
        }

        if(args[0].equals("reload")) {
            if(!sender.hasPermission("swedenminas.admin")) {
                sender.sendMessage("§f§lNEVASCAS §e>> §cVocê não tem permissão para executar este comando.");
                return true;
            }
            Nevascas.getNevascas().forEach(Nevasca::iniciar);
            sender.sendMessage("§f§lNEVASCAS §e>> §aNevascas recarregadas!");
            return true;
        }

        sender.sendMessage("§f§lNEVASCAS §e>> §cSub-comando não encontrado.");
        return true;
    }
}
