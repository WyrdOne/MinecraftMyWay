package net.minecraft.src;

public class MMWWorldType extends WorldType {
    public MMWWorldType() {
        super(0, "default");
    }

    public WorldChunkManager getChunkManager(World world) {
      return new WorldChunkManager(world);
    }

    public IChunkProvider getChunkGenerator(World world, String generatorOptions) {
        return new MMWChunkProvider(world, world.getSeed(), world.getWorldInfo().isMapFeaturesEnabled());
    }
}
