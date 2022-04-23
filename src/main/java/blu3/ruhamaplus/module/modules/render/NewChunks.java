package blu3.ruhamaplus.module.modules.render;

import blu3.ruhamaplus.module.Category;
import blu3.ruhamaplus.module.Module;
import blu3.ruhamaplus.utils.RenderUtils;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.init.Biomes;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketChunkData;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.Chunk;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class NewChunks extends Module {
    public NewChunks() {
        super("NewChunks", 0, Category.RENDER, "really?", null);
    }

    public ChunkPos c;
    private List<ChunkData> chunkDataList = new ArrayList<>();

    private List<ChunkPos> bruh = new ArrayList<>();

    public boolean onPacketRead(Packet<?> packet) {
        if (packet instanceof SPacketChunkData) {
            final SPacketChunkData yoooooooooo = (SPacketChunkData) packet;



            if (!yoooooooooo.isFullChunk())
            {
                final ChunkData chunk = new ChunkData(yoooooooooo.getChunkX() * 16, yoooooooooo.getChunkZ() * 16);

                ChunkPos chonk = new ChunkPos(yoooooooooo.getChunkX(), yoooooooooo.getChunkZ());

                if (!this.chunkDataList.contains(chunk))
                {
                    this.chunkDataList.add(chunk);
                    this.bruh.add(chonk);
                }
            }
        }
        return false;
    }


    public void onRender() {
        Iterator chonkIter = this.bruh.iterator();
        ChunkPos c;
        while (chonkIter.hasNext())
        {
            c = (ChunkPos) chonkIter.next();
            RenderUtils.drawFilledBlockBox(new AxisAlignedBB(c.getBlock(0, 0, 0), c.getBlock(16, 0, 16)), 1.0F, 0.0F, 0.0F, 0.3F);
        }
    }

    public static class ChunkData
    {
        private int x;
        private int z;

        public ChunkData(int x, int z)
        {
            this.x = x;
            this.z = z;
        }

        public int getX()
        {
            return x;
        }

        public void setX(int x)
        {
            this.x = x;
        }

        public int getZ()
        {
            return z;
        }

        public void setZ(int z)
        {
            this.z = z;
        }
    }
}

