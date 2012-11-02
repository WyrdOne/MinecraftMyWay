package net.minecraft.src;

import java.util.List;
import java.util.Random;

public class MMWChunkProvider extends ChunkProviderGenerate {
    /** RNG. */
    private Random rand;

    /** Reference to the World object. */
    private World worldObj;

    /** are map structures going to be generated (e.g. strongholds) */
    private final boolean mapFeaturesEnabled;

    /** Holds the overall noise array used in chunk generation */
    private MapGenBase caveGenerator = new MapGenCaves();

    /** Holds Stronghold Generator */
    private MapGenStronghold strongholdGenerator = new MapGenStronghold();

    /** Holds Village Generator */
    private MapGenVillage villageGenerator = new MapGenVillage();

    /** Holds Mineshaft Generator */
    private MapGenMineshaft mineshaftGenerator = new MapGenMineshaft();
    private MapGenScatteredFeature scatteredFeatureGenerator = new MapGenScatteredFeature();

    /** Holds ravine generator */
    private MapGenBase ravineGenerator = new MapGenRavine();

    /** The biomes that are used to generate the chunk */
    private BiomeGenBase[] biomesForGeneration;

    public MMWChunkProvider(World par1World, long par2, boolean par4) {
        super(par1World, par2, par4);
        worldObj = par1World;
        mapFeaturesEnabled = par4;
        rand = new Random(par2);
        mod_MyWay.processWorldGen(); // Make sure gen options are set.
    }

    /**
     * Will return back a chunk, if it doesn't exist and its not a MP client it will generates all the blocks for the
     * specified chunk from the map seed and chunk seed
     */
    public Chunk provideChunk(int par1, int par2)
    {
        this.rand.setSeed((long)par1 * 341873128712L + (long)par2 * 132897987541L);
        byte[] var3 = new byte[32768];
        this.generateTerrain(par1, par2, var3);
        this.biomesForGeneration = this.worldObj.getWorldChunkManager().loadBlockGeneratorData(this.biomesForGeneration, par1 * 16, par2 * 16, 16, 16);
        this.replaceBlocksForBiome(par1, par2, var3, this.biomesForGeneration);
        if (mod_MyWay.genCaves) {
            this.caveGenerator.generate(this, this.worldObj, par1, par2, var3);
        }
        if (mod_MyWay.genRavines) {
            this.ravineGenerator.generate(this, this.worldObj, par1, par2, var3);
        }
        if (this.mapFeaturesEnabled)
        {
            if (mod_MyWay.genMineshafts) {
                this.mineshaftGenerator.generate(this, this.worldObj, par1, par2, var3);
            }
            if (mod_MyWay.genVillages) {
                this.villageGenerator.generate(this, this.worldObj, par1, par2, var3);
            }
            if (mod_MyWay.genStrongholds) {
                this.strongholdGenerator.generate(this, this.worldObj, par1, par2, var3);
            }
            if (mod_MyWay.genScatteredFeatures) {
                this.scatteredFeatureGenerator.generate(this, this.worldObj, par1, par2, var3);
            }
        }

        Chunk var4 = new Chunk(this.worldObj, var3, par1, par2);
        byte[] var5 = var4.getBiomeArray();

        for (int var6 = 0; var6 < var5.length; ++var6)
        {
            var5[var6] = (byte)this.biomesForGeneration[var6].biomeID;
        }

        var4.generateSkylightMap();
        return var4;
    }

    /**
     * Populates chunk with ores etc etc
     */
    public void populate(IChunkProvider par1IChunkProvider, int par2, int par3)
    {
        BlockSand.fallInstantly = true;
        int var4 = par2 * 16;
        int var5 = par3 * 16;
        BiomeGenBase var6 = this.worldObj.getBiomeGenForCoords(var4 + 16, var5 + 16);
        this.rand.setSeed(this.worldObj.getSeed());
        long var7 = this.rand.nextLong() / 2L * 2L + 1L;
        long var9 = this.rand.nextLong() / 2L * 2L + 1L;
        this.rand.setSeed((long)par2 * var7 + (long)par3 * var9 ^ this.worldObj.getSeed());
        boolean var11 = false;

        if (this.mapFeaturesEnabled)
        {
            if (mod_MyWay.genMineshafts) {
                this.mineshaftGenerator.generateStructuresInChunk(this.worldObj, this.rand, par2, par3);
            }
            if (mod_MyWay.genVillages) {
                var11 = this.villageGenerator.generateStructuresInChunk(this.worldObj, this.rand, par2, par3);
            }
            if (mod_MyWay.genStrongholds) {
                this.strongholdGenerator.generateStructuresInChunk(this.worldObj, this.rand, par2, par3);
            }
            if (mod_MyWay.genScatteredFeatures) {
                this.scatteredFeatureGenerator.generateStructuresInChunk(this.worldObj, this.rand, par2, par3);
            }
        }

        int var12;
        int var13;
        int var14;

        if (!var11 && this.rand.nextInt(4) == 0)
        {
            var12 = var4 + this.rand.nextInt(16) + 8;
            var13 = this.rand.nextInt(128);
            var14 = var5 + this.rand.nextInt(16) + 8;
            (new WorldGenLakes(Block.waterStill.blockID)).generate(this.worldObj, this.rand, var12, var13, var14);
        }

        if (!var11 && this.rand.nextInt(8) == 0)
        {
            var12 = var4 + this.rand.nextInt(16) + 8;
            var13 = this.rand.nextInt(this.rand.nextInt(120) + 8);
            var14 = var5 + this.rand.nextInt(16) + 8;

            if (var13 < 63 || this.rand.nextInt(10) == 0)
            {
                (new WorldGenLakes(Block.lavaStill.blockID)).generate(this.worldObj, this.rand, var12, var13, var14);
            }
        }

        for (var12 = 0; var12 < 8; ++var12)
        {
            var13 = var4 + this.rand.nextInt(16) + 8;
            var14 = this.rand.nextInt(128);
            int var15 = var5 + this.rand.nextInt(16) + 8;

            if ((new WorldGenDungeons()).generate(this.worldObj, this.rand, var13, var14, var15))
            {
                ;
            }
        }

        var6.decorate(this.worldObj, this.rand, var4, var5);
        SpawnerAnimals.performWorldGenSpawning(this.worldObj, var6, var4 + 8, var5 + 8, 16, 16, this.rand);
        var4 += 8;
        var5 += 8;

        for (var12 = 0; var12 < 16; ++var12)
        {
            for (var13 = 0; var13 < 16; ++var13)
            {
                var14 = this.worldObj.getPrecipitationHeight(var4 + var12, var5 + var13);

                if (this.worldObj.isBlockFreezable(var12 + var4, var14 - 1, var13 + var5))
                {
                    this.worldObj.setBlockWithNotify(var12 + var4, var14 - 1, var13 + var5, Block.ice.blockID);
                }

                if (this.worldObj.canSnowAt(var12 + var4, var14, var13 + var5))
                {
                    this.worldObj.setBlockWithNotify(var12 + var4, var14, var13 + var5, Block.snow.blockID);
                }
            }
        }

        BlockSand.fallInstantly = false;
    }

   public void func_82695_e(int par1, int par2)
    {
        if (this.mapFeaturesEnabled)
        {
            if (mod_MyWay.genMineshafts) {
                this.mineshaftGenerator.generate(this, this.worldObj, par1, par2, (byte[])null);
            }
            if (mod_MyWay.genVillages) {
                this.villageGenerator.generate(this, this.worldObj, par1, par2, (byte[])null);
            }
            if (mod_MyWay.genStrongholds) {
                this.strongholdGenerator.generate(this, this.worldObj, par1, par2, (byte[])null);
            }
            if (mod_MyWay.genScatteredFeatures) {
                this.scatteredFeatureGenerator.generate(this, this.worldObj, par1, par2, (byte[])null);
            }
        }
    }
}
