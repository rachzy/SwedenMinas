package com.redesweden.swedenminas.functions;

import com.redesweden.swedenminas.SwedenMinas;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.util.Deque;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * Created by avigh on 8/25/2016.
 */
public class PastedBlock {

    private int x, y, z, id;
    private byte data;

    public PastedBlock(int x, int y, int z, int id, byte data) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.id = id;
        this.data = data;
    }

    public static class BlockQueue {

        private final Deque<PastedBlock> queue = new ConcurrentLinkedDeque<>();
        private static final Map<World, BlockQueue> queueMap = new ConcurrentHashMap<>();

        public void add(PastedBlock block) {
            queue.add(block);
        }

        public BlockQueue(final World world) {
            Bukkit.getScheduler().scheduleSyncRepeatingTask(SwedenMinas.getPlugin(SwedenMinas.class), () -> {
                PastedBlock block = null;
                boolean hasTime = true;
                long start = System.currentTimeMillis();

                while ((block = queue.poll()) != null && hasTime) {

                    hasTime = System.currentTimeMillis() - start < 10;
                    world.getBlockAt(block.x, block.y, block.z).setTypeIdAndData(block.id, block.data, true);
                }
            }, 1, 1);
        }

        public static BlockQueue getQueue(World w) {
            if (!queueMap.containsKey(w)) {
                BlockQueue blockQueue = new BlockQueue(w);
                queueMap.put(w, blockQueue);

                return blockQueue;
            }
            return queueMap.get(w);
        }
    }
}