package com.tortoise.ms.util.regen;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Set;
import com.tortoise.ms.coremod.MSCoremod;
import net.minecraft.util.LongHashMap;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.ChunkProviderServer;

public class RegenUtil {
    /* 我IDE已经炸了,这是我IDE炸前留下来的遗产 ,复写一个区块,恢复最初的样子*/
    @SuppressWarnings("unchecked")
    public static void Regen(World world, int x, int z) {
        IChunkProvider Provider = world.getChunkProvider();//获取世界区块供应者
        if (Provider instanceof ChunkProviderServer) {
            ChunkProviderServer ProviderServer = (ChunkProviderServer) Provider;
            try {
                //MSCoremod.Debug = true;
                /* MSCoremod.Debug说明运行时混淆是否开启
                需要Coremod,如果你没有Coremod,你可以写两个同样的代码,
                其中一个报错说明运行时混淆有或没有开启,则按照相反的字段名反射获取这些字段
                 */
                Field ChunksToUnload = ChunkProviderServer.class
                        .getDeclaredField(MSCoremod.Debug ? "chunksToUnload" : "field_73248_b");
                Field LoadedChunkHashMap = ChunkProviderServer.class
                        .getDeclaredField(MSCoremod.Debug ? "loadedChunkHashMap" : "field_73244_f");
                Field LoadedChunks = ChunkProviderServer.class
                        .getDeclaredField(MSCoremod.Debug ? "loadedChunks" : "field_73245_g");
                        /* 上面三个用于卸载、加载、刷新区块 */
                Field CurrentChunkProvider = ChunkProviderServer.class
                        .getDeclaredField(MSCoremod.Debug ? "currentChunkProvider" : "field_73246_d");
                        /* 原理:但凡你知道种子是什么你也不会问原理。。 */
                ChunksToUnload.setAccessible(true);
                LoadedChunkHashMap.setAccessible(true);
                LoadedChunks.setAccessible(true);
                CurrentChunkProvider.setAccessible(true);
                Set<?> UnloadQueue = (Set<?>) ChunksToUnload.get(ProviderServer);
                LongHashMap LoadedHashMap = (LongHashMap) LoadedChunkHashMap.get(ProviderServer);
                List<Chunk> Loaded = (List<Chunk>) LoadedChunks.get(ProviderServer);
                IChunkProvider ChunkProvider = (IChunkProvider) CurrentChunkProvider.get(ProviderServer);
                Chunk chunk = world.getChunkFromBlockCoords(x, z);//根据x和z轴获取一个区块
                if (ProviderServer.chunkExists(chunk.xPosition, chunk.zPosition)) {
                    //如果这个区块,则从区块供应者获取这个区块并卸载
                    Chunk c = ProviderServer.loadChunk(chunk.xPosition, chunk.zPosition);
                    c.onChunkUnload();
                }
                //区块位置在UnloadQueue的表示方法
                long Pos = ChunkCoordIntPair.chunkXZ2Int(chunk.xPosition, chunk.zPosition);
                //Long.valueOf(long)不可省略,因为可能不等于传入的参数
                UnloadQueue.remove(Long.valueOf(Pos));//卸载区块
                LoadedHashMap.remove(Pos);
                //调用区块供应者生成地形(一定是最初的地形,因为种子不变)
                Chunk RegeneratedChunk = ChunkProvider.provideChunk(chunk.xPosition, chunk.zPosition);
                /* 加载区块 */
                LoadedHashMap.add(Pos, RegeneratedChunk);
                Loaded.add(RegeneratedChunk);
                /* 加载并填充区块 */
                RegeneratedChunk.onChunkLoad();
                RegeneratedChunk.populateChunk(ChunkProvider, ChunkProvider, chunk.xPosition, chunk.zPosition);
            } catch (Exception exp) {

            }
        }
    }
}
