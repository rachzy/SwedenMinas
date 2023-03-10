package com.redesweden.swedenminas.functions;

import org.bukkit.Location;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.List;

public class GetBlocosPorPerto {
    private final List<Block> blocks = new ArrayList<>();

    public GetBlocosPorPerto(Location location, int radius, boolean mesmaCamada, boolean camdaSuperior) {
        if (mesmaCamada) {
            for (int x = location.getBlockX() - radius; x <= location.getBlockX() + radius; x++) {
                for (int z = location.getBlockZ() - radius; z <= location.getBlockZ() + radius; z++) {
                    blocks.add(location.getWorld().getBlockAt(x, (int) location.getY(), z));
                }
            }
            return;
        }
        if(camdaSuperior) {
            for (int x = location.getBlockX() - radius; x <= location.getBlockX() + radius; x++) {
                for (int y = location.getBlockY(); y <= location.getBlockY() + radius; y++) {
                    for (int z = location.getBlockZ() - radius; z <= location.getBlockZ() + radius; z++) {
                        blocks.add(location.getWorld().getBlockAt(x, y, z));
                    }
                }
            }
            return;
        }
        for (int x = location.getBlockX() - radius; x <= location.getBlockX() + radius; x++) {
            for (int y = location.getBlockY() - radius; y <= location.getBlockY() + radius; y++) {
                for (int z = location.getBlockZ() - radius; z <= location.getBlockZ() + radius; z++) {
                    blocks.add(location.getWorld().getBlockAt(x, y, z));
                }
            }
        }
    }

    public List<Block> getBlocos() {
        return blocks;
    }
}