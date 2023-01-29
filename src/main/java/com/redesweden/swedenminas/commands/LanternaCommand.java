package com.redesweden.swedenminas.commands;

import com.redesweden.swedenminas.SwedenMinas;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class LanternaCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage("§cApenas players podem executar este comando.");
            return true;
        }

        Player player = (Player) sender;

        Bukkit.getScheduler().runTaskLater(SwedenMinas.getPlugin(SwedenMinas.class), () -> {
            if(player.getActivePotionEffects().stream().anyMatch((efeito) -> efeito.getType().equals(PotionEffectType.NIGHT_VISION))) {
                player.removePotionEffect(PotionEffectType.NIGHT_VISION);
                player.playSound(player.getLocation(), Sound.CLICK, 3.0F, 2F);
                player.sendMessage("§cVocê desativou sua lanterna");
            } else {
                player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 99999, 2, false, false), true);
                player.playSound(player.getLocation(), Sound.FIRE_IGNITE, 3.0F, 1F);
                player.sendMessage("§aVocê ativou sua lanterna");
            }
        }, 1L);
        return true;
    }
}
