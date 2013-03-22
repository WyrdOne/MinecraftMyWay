package net.minecraft.src;

import java.util.*;
import java.lang.reflect.*;
import java.io.PrintWriter;
import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import moapi.*;
import mmw.*;

public class mod_MyWay extends BaseMod {
	// Copyright/license info
	private static final String Name = "Minecraft My Way";
	private static final String Version = "0.83 (For use with Minecraft 1.5)";
	private static final String Copyright = "All original code and images (C) 2011-2013, Jonathan \"Wyrd\" Brazell";
	private static final String License = "This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 3.0 Unported License.";
	// Options
	private static ModOptions optionsMain;
	private static ModOptions optionsMobs;
	private static ModOptions optionsHostileMobs;
	private static ModOptions optionsPeacefulMobs;
	private static ModOptions optionsRecipes;
	private static ModOptions optionsSpecial;
	private static ModOptions optionsWorldGen;
	private static ModOptions optionsAdventureMode;
	// Save states
	private static boolean removeDiamondTools = false;
	private static boolean removeDiamondArmor = false;
	private static boolean removeWoodAxes = false;
	private static boolean removeCobblestoneTools = false;
	private static boolean removeTNT = false;
	private static boolean craftFlint = false;
	private static boolean craftCobblestone = false;
	private static boolean flintTools = false;
	private static boolean extendedCrafting = false;
	private static boolean timeFlow = false;
	// World Generation Options
	public static int genCaves = 2; // 0=off, 1=rare, 2=normal, 3=common
	public static int genDungeons = 2;
	public static int genLakes = 2;
	public static int genLargeCaves = 2;
	public static int genLavaLakes = 2;
	public static int genMineshafts = 2;
	public static int genRavines = 2;
	public static int genScatteredFeatures = 2;
	public static int genStrongholds = 2;
	public static int genVillages = 2;
	// Mob Spawns
	public static ModOptionBoolean optBats;
	public static boolean enabledBats = true;
	public static ModOptionBoolean optChicken;
	public static boolean enabledChicken = true;
	public static ModOptionBoolean optCow;
	public static boolean enabledCow = true;
	public static ModOptionBoolean optIronGolem;
	public static boolean enabledIronGolem = true;
	public static ModOptionBoolean optMooshroom;
	public static boolean enabledMooshroom = true;
	public static ModOptionBoolean optNPC;
	public static boolean enabledNPC = true;
	public static ModOptionBoolean optOcelot;
	public static boolean enabledOcelot = true;
	public static ModOptionBoolean optPeaceful;
	public static boolean enabled = true;
	public static ModOptionBoolean optPig;
	public static boolean enabledPig = true;
	public static ModOptionBoolean optSheep;
	public static boolean enabledSheep = true;
	public static ModOptionBoolean optSnowman;
	public static boolean enabledSnowman = true;
	public static ModOptionBoolean optSquid;
	public static boolean enabledSquid = true;
	public static ModOptionBoolean optWolf;
	public static boolean enabledWolf = true;
	public static ModOptionBoolean optBlaze;
	public static boolean enabledBlaze = true;
	public static ModOptionBoolean optCaveSpider;
	public static boolean enabledCaveSpider = true;
	public static ModOptionBoolean optCreeper;
	public static boolean enabledCreeper = true;
	public static ModOptionBoolean optDragon;
	public static boolean enabledDragon = true;
	public static ModOptionBoolean optEnderman;
	public static boolean enabledEnderman = true;
	public static ModOptionBoolean optGhast;
	public static boolean enabledGhast = true;
	public static ModOptionBoolean optGiantZombie;
	public static boolean enabledGiantZombie = true;
	public static ModOptionBoolean optHostile;
	public static ModOptionBoolean optMagmaCube;
	public static boolean enabledMagmaCube = true;
	public static ModOptionBoolean optPigZombie;
	public static boolean enabledPigZombie = true;
	public static ModOptionBoolean optSilverfish;
	public static boolean enabledSilverfish = true;
	public static ModOptionBoolean optSkeleton;
	public static boolean enabledSkeleton = true;
	public static ModOptionBoolean optSlime;
	public static boolean enabledSlime = true;
	public static ModOptionBoolean optSpider;
	public static boolean enabledSpider = true;
	public static ModOptionBoolean optWitch;
	public static boolean enabledWitch = true;
	public static ModOptionBoolean optWither;
	public static boolean enabledWither = true;
	public static ModOptionBoolean optZombie;
	public static boolean enabledZombie = true;
	// Peaceful mob adjustments
	public static boolean peacefulDropBones = false;
	public static boolean peacefulAdjustDrops = false;
	// Items for flint tools
	public static Item hoeFlint;
	public static Item axeFlint;
	public static Item pickaxeFlint;
	public static Item spearFlint;  
	// Items for hatchets
	public static Item hatchetFlint;
	public static Item hatchetWood;
	public static Item hatchetStone;
	public static Item hatchetIron;
	public static Item hatchetDiamond;
	public static Item hatchetGold;
	// Other variables
	private static Minecraft mc = Minecraft.getMinecraft();
	private static MMWWorldType mmwWorldType = new MMWWorldType();
	private static MMWFoodStats mmwFoodStats = new MMWFoodStats();
	private static Block mmwStone;
	private static Block mmwSand;
	private static Block mmwGravel;
	private static Block mmwDirt;
	private static Item saveItemDye = Item.dyePowder;
	private static int tickCounter = 0;
	private static int guiTickCounter = 0;
	private static boolean firstTime = true;
	private static int baseItemID = 12120;
      	
  public void load() {
    // Set up options menu for mod options
    initModOptionsAPI();
    baseItemID = Integer.parseInt(optionsRecipes.getOptionValue("Base Item ID"));

    // Replace Stone
    Block.blocksList[Block.stone.blockID] = null;
    mmwStone = (new MMWBlockStone(1)).setHardness(1.5F).setResistance(10.0F).setStepSound(Block.soundStoneFootstep).setUnlocalizedName("stone");

    // Replace Sand
    Block.blocksList[Block.sand.blockID] = null;
    mmwSand = (new MMWBlockSand(12)).setHardness(0.5F).setStepSound(Block.soundSandFootstep).setUnlocalizedName("sand");

    // Replace Gravel
    Block.blocksList[Block.gravel.blockID] = null;
    mmwGravel = (new MMWBlockGravel(13)).setHardness(0.6F).setStepSound(Block.soundGravelFootstep).setUnlocalizedName("gravel");

    // Replace Dirt
    Block.blocksList[Block.dirt.blockID] = null;
    mmwDirt = (new MMWBlockDirt(3)).setHardness(0.5F).setStepSound(Block.soundGravelFootstep).setUnlocalizedName("dirt");

    // Add MMW versions to shovel
    Block[] blocksEffectiveAgainst = new Block[] {Block.grass, mmwDirt, mmwSand, mmwGravel, Block.snow, Block.blockSnow, Block.blockClay, Block.tilledField, Block.slowSand, Block.mycelium};
    MMWReflection.setPrivateValue(ItemSpade.class, null, "blocksEffectiveAgainst", blocksEffectiveAgainst);
    
    // Replace dye to allow bonemeal to be disabled.
    Item.itemsList[Item.dyePowder.itemID] = null;
    Item.dyePowder = (new MMWItemDye(95)).setUnlocalizedName("dyePowder");

    // Remove spawns for vanilla mobs, add custom mob spawns to replace them
    MMWUtil.removeSpawn(EntityCow.class, EnumCreatureType.creature);
    MMWUtil.removeSpawn(EntityPig.class, EnumCreatureType.creature);
    MMWUtil.removeSpawn(EntitySheep.class, EnumCreatureType.creature);
    MMWUtil.registerEntityID(MMWEntityCow.class, "Cow", 92);
    MMWUtil.registerEntityID(MMWEntityPig.class, "Pig", 90);
    MMWUtil.registerEntityID(MMWEntitySheep.class, "Sheep", 91);
    MMWUtil.addSpawn(MMWEntityCow.class, 8, 4, 4, EnumCreatureType.creature);
    MMWUtil.addSpawn(MMWEntityPig.class, 10, 4, 4, EnumCreatureType.creature);
    MMWUtil.addSpawn(MMWEntitySheep.class, 12, 4, 4, EnumCreatureType.creature);

    // Add flint tool items
    hoeFlint = new MMWItemHoe(baseItemID+0, MMWEnumToolMaterial.FLINT).setUnlocalizedName("hoeFlint");
    axeFlint = new MMWItemAxe(baseItemID+1, MMWEnumToolMaterial.FLINT).setUnlocalizedName("axeFlint");
    pickaxeFlint = new MMWItemPickaxe(baseItemID+2, MMWEnumToolMaterial.FLINT).setUnlocalizedName("pickaxeFlint");
    spearFlint = new MMWItemSpear(baseItemID+3, MMWEnumToolMaterial.FLINT).setUnlocalizedName("spearFlint");
    MMWUtil.addLocalization(pickaxeFlint, "Flint Pickaxe");
    MMWUtil.addLocalization(spearFlint, "Flint Spear");      
    MMWUtil.addLocalization(axeFlint, "Flint Axe");
    MMWUtil.addLocalization(hoeFlint, "Flint Hoe");
    if (optionsRecipes.getBooleanValue("Flint Tools")) {
      flintTools = true;
      MMWUtil.addRecipe(new ItemStack(spearFlint, 1), new Object[] {"F", "S", "S", 'F', Item.flint, 'S', Item.stick}); 
      MMWUtil.addRecipe(new ItemStack(pickaxeFlint, 1), new Object[] {"FFF", " S ", " S ", 'F', Item.flint, 'S', Item.stick}); 
      MMWUtil.addRecipe(new ItemStack(axeFlint, 1), new Object[] {"FF", "FS", " S", 'F', Item.flint, 'S', Item.stick}); 
      MMWUtil.addRecipe(new ItemStack(hoeFlint, 1), new Object[] {"FF", " S", " S", 'F', Item.flint, 'S', Item.stick});
    }

    // Add hatchets
    hatchetFlint = new MMWItemHatchetFlint(baseItemID+4, MMWEnumToolMaterial.FLINT).setUnlocalizedName("hatchetFlint");
    hatchetWood = new MMWItemHatchet(baseItemID+5, EnumToolMaterial.WOOD).setUnlocalizedName("hatchetWood");
    hatchetStone = new MMWItemHatchet(baseItemID+6, EnumToolMaterial.STONE).setUnlocalizedName("hatchetStone");
    hatchetIron = new MMWItemHatchet(baseItemID+7, EnumToolMaterial.IRON).setUnlocalizedName("hatchetIron");
    hatchetDiamond = new MMWItemHatchet(baseItemID+8, EnumToolMaterial.EMERALD).setUnlocalizedName("hatchetDiamond");
    hatchetGold = new MMWItemHatchet(baseItemID+9, EnumToolMaterial.GOLD).setUnlocalizedName("hatchetGold");
    MMWUtil.addLocalization(hatchetFlint, "Flint Hatchet");
    MMWUtil.addLocalization(hatchetWood, "Wood Hatchet");
    MMWUtil.addLocalization(hatchetStone, "Stone Hatchet");
    MMWUtil.addLocalization(hatchetIron, "Iron Hatchet");
    MMWUtil.addLocalization(hatchetDiamond, "Diamond Hatchet");
    MMWUtil.addLocalization(hatchetGold, "Gold Hatchet");
	  if (optionsRecipes.getBooleanValue("Extended 2x2 Crafting") && optionsRecipes.getBooleanValue("Flint Tools")) {
      MMWUtil.addRecipe(new ItemStack(hatchetFlint, 1), new Object[] {"FS", "S ", 'F', Item.flint, 'S', Item.stick}); 
    } 
	  if (optionsRecipes.getBooleanValue("Extended 2x2 Crafting") && optionsRecipes.getBooleanValue("Wood Axes")) {
      MMWUtil.addRecipe(new ItemStack(hatchetWood, 1), new Object[] {"WS", "S ", 'W', Block.planks, 'S', Item.stick}); 
    } 
	  if (optionsRecipes.getBooleanValue("Extended 2x2 Crafting") && optionsRecipes.getBooleanValue("Cobblestone Tools")) {
      MMWUtil.addRecipe(new ItemStack(hatchetStone, 1), new Object[] {"CS", "S ", 'C', Block.cobblestone, 'S', Item.stick}); 
    } 
	  if (optionsRecipes.getBooleanValue("Extended 2x2 Crafting")) {
      MMWUtil.addRecipe(new ItemStack(hatchetIron, 1), new Object[] {"IS", "S ", 'I', Item.ingotIron, 'S', Item.stick}); 
    }
	  if (optionsRecipes.getBooleanValue("Extended 2x2 Crafting") && optionsRecipes.getBooleanValue("Diamond Tools")) {
      MMWUtil.addRecipe(new ItemStack(hatchetDiamond, 1), new Object[] {"DS", "S ", 'D', Item.diamond, 'S', Item.stick}); 
    } 
	  if (optionsRecipes.getBooleanValue("Extended 2x2 Crafting")) {
      MMWUtil.addRecipe(new ItemStack(hatchetGold, 1), new Object[] {"GS", "S ", 'G', Item.ingotGold, 'S', Item.stick}); 
    } 

    // Check for option changes every tick
    ModLoader.setInGUIHook(this, true, false);
    ModLoader.setInGameHook(this, true, false);
  }

  public boolean onTickInGUI(float ticks, Minecraft mc, GuiScreen screen) {
    if (screen instanceof GuiCreateWorld && optionsAdventureMode.getBooleanValue("Allow on Creation")) {
      GuiButton buttonGameMode;
      try {
        buttonGameMode = (GuiButton)MMWReflection.getPrivateValue(GuiCreateWorld.class, screen, "buttonGameMode");
      } catch (Exception ignored) {
        buttonGameMode = null;
      }
      if (!(buttonGameMode instanceof MMWModeButton)) {
        buttonGameMode = new MMWModeButton(screen);
        try {
          MMWReflection.setPrivateValue(GuiCreateWorld.class, screen, "buttonGameMode", buttonGameMode);
        } catch (Exception ignored) { }
        MMWReflection.getControlList(screen).set(2, buttonGameMode);
      }
    } else if ((screen instanceof moapi.client.ModMenu) || (screen instanceof GuiMainMenu && firstTime)) {
      firstTime = false;
      processWorldGen();
      processHostileSpawns();
      processPeacefulSpawns();
      processAdventureMode();  
      processSpecial();
      processRecipes();
    }
    return true;
  }

  public boolean onTickInGame(float f, Minecraft paramMinecraft) {
    processTimeFlow();
    processXP();
    killCritters();
    // Rain
    if (!mc.thePlayer.worldObj.isRemote) {
	    WorldInfo worldInfo = MinecraftServer.getServer().worldServers[0].getWorldInfo();
	    if (optionsSpecial.getOptionValue("Rain")=="Always") {
	      worldInfo.setRainTime(1000);
	      worldInfo.setRaining(true);
	    } else if (optionsSpecial.getOptionValue("Rain")=="Never") {
	      worldInfo.setRainTime(1000);
	      worldInfo.setRaining(false);
	    }
    }
    if (mc.thePlayer!=null && !optionsSpecial.getBooleanValue("Sprinting")) {
      mc.thePlayer.setSprinting(false);
    }
    return true;
  }

  public static void processTimeFlow() {
    if (mc.thePlayer.worldObj.isRemote) {
    	return;
    }
    if (optionsSpecial.getOptionValue("Time Flow")=="Always Day") {
      for (int idx=0; idx<MinecraftServer.getServer().worldServers.length; idx++) {
        long time = MinecraftServer.getServer().worldServers[idx].getWorldTime();
        if ((time%24000)>12000) {
          time /= 24000;
          time *= 24000;
          MinecraftServer.getServer().worldServers[idx].setWorldTime(time);
        }
      }
    } else if (optionsSpecial.getOptionValue("Time Flow")=="Always Night") {
      for (int idx=0; idx<MinecraftServer.getServer().worldServers.length; idx++) {
        long time = MinecraftServer.getServer().worldServers[idx].getWorldTime();
        if (((time%24000)<13800) || ((time%24000)>22200)) {
          time /= 24000;
          time *= 24000;
          MinecraftServer.getServer().worldServers[idx].setWorldTime(time);
        }
      }
    } else if (optionsSpecial.getOptionValue("Time Flow")=="Rapid") {
      for (int idx=0; idx<MinecraftServer.getServer().worldServers.length; idx++) {
        long time = MinecraftServer.getServer().worldServers[idx].getWorldTime();
        MinecraftServer.getServer().worldServers[idx].setWorldTime(time + 1);
      }
    } else if (optionsSpecial.getOptionValue("Time Flow")=="Slow") {
      if (timeFlow) {
        for (int idx=0; idx<MinecraftServer.getServer().worldServers.length; idx++) {
          long time = MinecraftServer.getServer().worldServers[idx].getWorldTime();
          MinecraftServer.getServer().worldServers[idx].setWorldTime(time - 1);
        }
      }
      timeFlow = !timeFlow;
    } else if (optionsSpecial.getOptionValue("Time Flow")=="Single Day") {
      for (int idx=0; idx<MinecraftServer.getServer().worldServers.length; idx++) {
        long totaltime = MinecraftServer.getServer().worldServers[idx].worldInfo.getWorldTotalTime();
        long time = MinecraftServer.getServer().worldServers[idx].getWorldTime();
        if ((totaltime>13800) && (((time%24000)<13800) || ((time%24000)>22200))) {
          time /= 24000;
          time *= 24000;
          time += 13800;
          MinecraftServer.getServer().worldServers[idx].setWorldTime(time);
        }
      }
    } 
  }

  public static void processAdventureMode() {
    if (optionsAdventureMode.getBooleanValue("Hands break sand/gravel")) {
      Material.sand.func_85158_p();
    } else {
      MMWReflection.setPrivateValue(Material.class, Material.sand, "field_85159_M", Boolean.FALSE);
    }
    if (optionsAdventureMode.getBooleanValue("Hands break leaves")) {
      Material.leaves.func_85158_p();
    } else {
      MMWReflection.setPrivateValue(Material.class, Material.leaves, "field_85159_M", Boolean.FALSE);
    }
    if (optionsAdventureMode.getBooleanValue("Hands break dirt")) {
      Material.ground.func_85158_p();
      Material.grass.func_85158_p();
    } else {
      MMWReflection.setPrivateValue(Material.class, Material.ground, "field_85159_M", Boolean.FALSE);
      MMWReflection.setPrivateValue(Material.class, Material.grass, "field_85159_M", Boolean.FALSE);
    }
    if (optionsAdventureMode.getBooleanValue("Hands break clay")) {
      Material.clay.func_85158_p();
    } else {
      MMWReflection.setPrivateValue(Material.class, Material.clay, "field_85159_M", Boolean.FALSE);
    }
    if (optionsAdventureMode.getBooleanValue("Hands break cactus")) {
      Material.cactus.func_85158_p();
    } else {
      MMWReflection.setPrivateValue(Material.class, Material.cactus, "field_85159_M", Boolean.FALSE);
    }
  }

  public static void processXP() {
    if (mc.thePlayer.worldObj.isRemote) {
    	return;
    }
    if (!optionsSpecial.getBooleanValue("XP")) {
      MMWUtil.killAll(EntityXPOrb.class);
      mc.thePlayer.experience = 0;
      mc.thePlayer.experienceLevel = 0;
      if (mc.thePlayer.worldObj.isRemote) {
        for (int idx=0; idx<mc.getIntegratedServer().getConfigurationManager().playerEntityList.size(); idx++) {
          ((EntityPlayerMP)mc.getIntegratedServer().getConfigurationManager().playerEntityList.get(idx)).experience = 0;
          ((EntityPlayerMP)mc.getIntegratedServer().getConfigurationManager().playerEntityList.get(idx)).experienceLevel = 0;
        }
      }
    }
  }

  public static void processWorldGen() {
    // World Gen Options - Called from MMWChunkProvider
    int forgeCompat = 0;
    if (optionsWorldGen.getBooleanValue("Biome - Desert")) {
      ModLoader.addBiome(BiomeGenBase.desert);
      forgeCompat++;
    } else
      ModLoader.removeBiome(BiomeGenBase.desert);
    if (optionsWorldGen.getBooleanValue("Biome - Extreme Hills")) {
      ModLoader.addBiome(BiomeGenBase.extremeHills);
      forgeCompat++;
    } else
      ModLoader.removeBiome(BiomeGenBase.extremeHills);
    if (optionsWorldGen.getBooleanValue("Biome - Forest")) {
      ModLoader.addBiome(BiomeGenBase.forest);
      forgeCompat++;
    } else
      ModLoader.removeBiome(BiomeGenBase.forest);
    if (optionsWorldGen.getBooleanValue("Biome - Jungle")) {
      ModLoader.addBiome(BiomeGenBase.jungle);
      forgeCompat++;
    } else
      ModLoader.removeBiome(BiomeGenBase.jungle);
    if (optionsWorldGen.getBooleanValue("Biome - Plains")) {
      ModLoader.addBiome(BiomeGenBase.plains);
      forgeCompat++;
    } else
      ModLoader.removeBiome(BiomeGenBase.plains);
    if (optionsWorldGen.getBooleanValue("Biome - Swampland")) {
      ModLoader.addBiome(BiomeGenBase.swampland);
      forgeCompat++;
    } else
      ModLoader.removeBiome(BiomeGenBase.swampland);
    if (optionsWorldGen.getBooleanValue("Biome - Taiga")) {
      ModLoader.addBiome(BiomeGenBase.taiga);
      forgeCompat++;
    } else
      ModLoader.removeBiome(BiomeGenBase.taiga);
    //Originally -> if (GenLayerBiome.biomeArray.length==0) {
    if (forgeCompat==0) {
      ModLoader.addBiome(BiomeGenBase.plains);
      optionsWorldGen.getOption("Biome - Plains").setValue(true);
    }
    genCaves = optionsWorldGen.getMappedValue("Caves");
    genDungeons = optionsWorldGen.getMappedValue("Dungeons");
    genLakes = optionsWorldGen.getMappedValue("Lakes");
    genLargeCaves = optionsWorldGen.getMappedValue("Large Caves");
    genLavaLakes = optionsWorldGen.getMappedValue("Lava Lakes");
    genMineshafts = optionsWorldGen.getMappedValue("Mineshafts");
    genRavines = optionsWorldGen.getMappedValue("Ravines");
    genScatteredFeatures = optionsWorldGen.getMappedValue("Scattered Features");
    genStrongholds = optionsWorldGen.getMappedValue("Strongholds");
    genVillages = optionsWorldGen.getMappedValue("Villages");
  }

  public static void processSpecial() {
    // Bonemeal
    ((MMWItemDye)Item.dyePowder).disableBonemeal = !optionsSpecial.getBooleanValue("Bonemeal Instagrowth");
    // Sand
    ((MMWBlockSand)mmwSand).gravityWorks = optionsSpecial.getBooleanValue("Sand Gravity"); 
    // Gravel
    ((MMWBlockGravel)mmwGravel).gravityWorks = optionsSpecial.getBooleanValue("Gravel Gravity"); 
    // Dirt
    ((MMWBlockDirt)mmwDirt).gravityWorks = optionsSpecial.getBooleanValue("Dirt Gravity"); 
    // Stone
    ((MMWBlockStone)Block.blocksList[Block.stone.blockID]).drop = optionsSpecial.getBooleanValue("Stone Drops Gravel") ? Block.gravel.blockID : Block.cobblestone.blockID;
    // Set custom FoodStats
    if (mc.thePlayer!=null) {
      mc.thePlayer.foodStats = mmwFoodStats;
      if (mc.thePlayer.worldObj.isRemote) {
        for (int idx=0; idx<mc.getIntegratedServer().getConfigurationManager().playerEntityList.size(); idx++) {
          ((EntityPlayerMP)mc.getIntegratedServer().getConfigurationManager().playerEntityList.get(idx)).foodStats = mmwFoodStats;
        }
      }
    }
    // Hunger
    mmwFoodStats.hunger = optionsSpecial.getBooleanValue("Hunger");
    // Healing
    mmwFoodStats.healing = optionsSpecial.getBooleanValue("Auto Healing");
  }

  public static void processRecipes() {
    // Diamond Tools
    if (!optionsRecipes.getBooleanValue("Diamond Tools")) {
      if (!removeDiamondTools) {
        removeDiamondTools = true;
        MMWUtil.removeRecipe(new ItemStack(Item.swordDiamond, 1), new Object[] {"D", "D", "S", 'D', Item.diamond, 'S', Item.stick}); 
        MMWUtil.removeRecipe(new ItemStack(Item.shovelDiamond, 1), new Object[] {"D", "S", "S", 'D', Item.diamond, 'S', Item.stick}); 
        MMWUtil.removeRecipe(new ItemStack(Item.pickaxeDiamond, 1), new Object[] {"DDD", " S ", " S ", 'D', Item.diamond, 'S', Item.stick}); 
        MMWUtil.removeRecipe(new ItemStack(Item.axeDiamond, 1), new Object[] {"DD", "DS", " S", 'D', Item.diamond, 'S', Item.stick}); 
        MMWUtil.removeRecipe(new ItemStack(Item.hoeDiamond, 1), new Object[] {"DD", " S", " S", 'D', Item.diamond, 'S', Item.stick}); 
      }
    } else if (removeDiamondTools) {
      removeDiamondTools = false;
      MMWUtil.addRecipe(new ItemStack(Item.swordDiamond, 1), new Object[] {"D", "D", "S", 'D', Item.diamond, 'S', Item.stick}); 
      MMWUtil.addRecipe(new ItemStack(Item.shovelDiamond, 1), new Object[] {"D", "S", "S", 'D', Item.diamond, 'S', Item.stick}); 
      MMWUtil.addRecipe(new ItemStack(Item.pickaxeDiamond, 1), new Object[] {"DDD", " S ", " S ", 'D', Item.diamond, 'S', Item.stick}); 
      MMWUtil.addRecipe(new ItemStack(Item.axeDiamond, 1), new Object[] {"DD", "DS", " S", 'D', Item.diamond, 'S', Item.stick}); 
      MMWUtil.addRecipe(new ItemStack(Item.hoeDiamond, 1), new Object[] {"DD", " S", " S", 'D', Item.diamond, 'S', Item.stick}); 
    }
    // Diamond Armor
    if (!optionsRecipes.getBooleanValue("Diamond Armor")) {
      if (!removeDiamondArmor) {
        removeDiamondArmor = true;
        MMWUtil.removeRecipe(new ItemStack(Item.helmetDiamond, 1), new Object[] {"DDD", "D D", 'D', Item.diamond}); 
        MMWUtil.removeRecipe(new ItemStack(Item.plateDiamond, 1), new Object[] {"D D", "DDD", "DDD", 'D', Item.diamond}); 
        MMWUtil.removeRecipe(new ItemStack(Item.legsDiamond, 1), new Object[] {"DDD", "D D", "D D", 'D', Item.diamond}); 
        MMWUtil.removeRecipe(new ItemStack(Item.bootsDiamond, 1), new Object[] {"D D", "D D", 'D', Item.diamond}); 
      }
    } else if (removeDiamondArmor) {
      removeDiamondArmor = false;
      MMWUtil.addRecipe(new ItemStack(Item.helmetDiamond, 1), new Object[] {"DDD", "D D", 'D', Item.diamond}); 
      MMWUtil.addRecipe(new ItemStack(Item.plateDiamond, 1), new Object[] {"D D", "DDD", "DDD", 'D', Item.diamond}); 
      MMWUtil.addRecipe(new ItemStack(Item.legsDiamond, 1), new Object[] {"DDD", "D D", "D D", 'D', Item.diamond}); 
      MMWUtil.addRecipe(new ItemStack(Item.bootsDiamond, 1), new Object[] {"D D", "D D", 'D', Item.diamond}); 
    }
    // Wood Axes
    if (!optionsRecipes.getBooleanValue("Wood Axes")) {
      if (!removeWoodAxes) {
        removeWoodAxes = true;
        MMWUtil.removeRecipe(new ItemStack(Item.pickaxeWood, 1), new Object[] {"WWW", " S ", " S ", 'W', Block.planks, 'S', Item.stick}); 
        MMWUtil.removeRecipe(new ItemStack(Item.axeWood, 1), new Object[] {"WW", "WS", " S", 'W', Block.planks, 'S', Item.stick}); 
      }
    } else if (removeWoodAxes) {
      removeWoodAxes = false;
      MMWUtil.addRecipe(new ItemStack(Item.pickaxeWood, 1), new Object[] {"WWW", " S ", " S ", 'W', Block.planks, 'S', Item.stick}); 
      MMWUtil.addRecipe(new ItemStack(Item.axeWood, 1), new Object[] {"WW", "WS", " S", 'W', Block.planks, 'S', Item.stick}); 
    }
    // Cobblestone Tools
    if (!optionsRecipes.getBooleanValue("Cobblestone Tools")) {
      if (!removeCobblestoneTools) {
        removeCobblestoneTools = true;
        MMWUtil.removeRecipe(new ItemStack(Item.swordStone, 1), new Object[] {"C", "C", "S", 'C', Block.cobblestone, 'S', Item.stick}); 
        MMWUtil.removeRecipe(new ItemStack(Item.shovelStone, 1), new Object[] {"C", "S", "S", 'C', Block.cobblestone, 'S', Item.stick}); 
        MMWUtil.removeRecipe(new ItemStack(Item.pickaxeStone, 1), new Object[] {"CCC", " S ", " S ", 'C', Block.cobblestone, 'S', Item.stick}); 
        MMWUtil.removeRecipe(new ItemStack(Item.axeStone, 1), new Object[] {"CC", "CS", " S", 'C', Block.cobblestone, 'S', Item.stick}); 
        MMWUtil.removeRecipe(new ItemStack(Item.hoeStone, 1), new Object[] {"CC", " S", " S", 'C', Block.cobblestone, 'S', Item.stick}); 
      }
    } else if (removeCobblestoneTools) {
      removeCobblestoneTools = false;
      MMWUtil.addRecipe(new ItemStack(Item.swordStone, 1), new Object[] {"C", "C", "S", 'C', Block.cobblestone, 'S', Item.stick}); 
      MMWUtil.addRecipe(new ItemStack(Item.shovelStone, 1), new Object[] {"C", "S", "S", 'C', Block.cobblestone, 'S', Item.stick}); 
      MMWUtil.addRecipe(new ItemStack(Item.pickaxeStone, 1), new Object[] {"CCC", " S ", " S ", 'C', Block.cobblestone, 'S', Item.stick}); 
      MMWUtil.addRecipe(new ItemStack(Item.axeStone, 1), new Object[] {"CC", "CS", " S", 'C', Block.cobblestone, 'S', Item.stick}); 
      MMWUtil.addRecipe(new ItemStack(Item.hoeStone, 1), new Object[] {"CC", " S", " S", 'C', Block.cobblestone, 'S', Item.stick}); 
    }
    // TNT
    if (optionsRecipes.getBooleanValue("TNT")) {
      if (!removeTNT) {
        removeTNT = true;
        MMWUtil.removeRecipe(new ItemStack(Block.tnt, 1), new Object[] {"X#X", "#X#", "X#X", 'X', Item.gunpowder, '#', Block.sand});
      }
    } else if (removeTNT) {
      removeTNT = false;
      MMWUtil.addRecipe(new ItemStack(Block.tnt, 1), new Object[] {"X#X", "#X#", "X#X", 'X', Item.gunpowder, '#', Block.sand});
    }
    // Craft Flint
    if (optionsRecipes.getBooleanValue("Craft Flint")) {
      if (!craftFlint) {
        craftFlint = true;
        MMWUtil.addRecipe(new ItemStack(Item.flint, 1), new Object[] {"#", '#', Block.gravel});
      }
    } else if (craftFlint) {
      craftFlint = false;
      MMWUtil.removeRecipe(new ItemStack(Item.flint, 1), new Object[] {"#", '#', Block.gravel});
    }
    // Craft Cobblestone
    if (optionsRecipes.getBooleanValue("Craft Cobblestone")) {
      if (!craftCobblestone) {
        craftCobblestone = true;
        MMWUtil.addRecipe(new ItemStack(Block.cobblestone, 2), new Object[] {"#c", "c#", '#', Block.gravel, 'c', Item.clay});
      }
    } else if (craftCobblestone) {
      craftCobblestone = false;
      MMWUtil.removeRecipe(new ItemStack(Block.cobblestone, 2), new Object[] {"#c", "c#", '#', Block.gravel, 'c', Item.clay});
    }
    // Flint Tools
    if (optionsRecipes.getBooleanValue("Flint Tools")) {
      if (!flintTools) {
        flintTools = true;
        MMWUtil.addRecipe(new ItemStack(spearFlint, 1), new Object[] {"F", "S", "S", 'F', Item.flint, 'S', Item.stick}); 
        MMWUtil.addRecipe(new ItemStack(pickaxeFlint, 1), new Object[] {"FFF", " S ", " S ", 'F', Item.flint, 'S', Item.stick}); 
        MMWUtil.addRecipe(new ItemStack(axeFlint, 1), new Object[] {"FF", "FS", " S", 'F', Item.flint, 'S', Item.stick}); 
        MMWUtil.addRecipe(new ItemStack(hoeFlint, 1), new Object[] {"FF", " S", " S", 'F', Item.flint, 'S', Item.stick});
      }
    } else if (flintTools) {
      flintTools = false;
      MMWUtil.removeRecipe(new ItemStack(spearFlint, 1), new Object[] {"F", "S", "S", 'F', Item.flint, 'S', Item.stick}); 
      MMWUtil.removeRecipe(new ItemStack(pickaxeFlint, 1), new Object[] {"FFF", " S ", " S ", 'F', Item.flint, 'S', Item.stick}); 
      MMWUtil.removeRecipe(new ItemStack(axeFlint, 1), new Object[] {"FF", "FS", " S", 'F', Item.flint, 'S', Item.stick}); 
      MMWUtil.removeRecipe(new ItemStack(hoeFlint, 1), new Object[] {"FF", " S", " S", 'F', Item.flint, 'S', Item.stick});
    }
    // Extended 2x2 Crafting
	  if (optionsRecipes.getBooleanValue("Extended 2x2 Crafting")) {
      if (!extendedCrafting) {
        extendedCrafting = true;
        MMWUtil.addRecipe(new ItemStack(Item.stick, 2), new Object[] {"#", "#", '#', Block.sapling}); 
        MMWUtil.addRecipe(new ItemStack(Item.stick, 2), new Object[] {"#", "#", '#', Block.cactus}); 
        MMWUtil.addRecipe(new ItemStack(Item.stick, 2), new Object[] {"/", "/", '/', Item.bone});
        MMWUtil.addRecipe(new ItemStack(Item.silk, 2), new Object[] {"#", '#', Block.cloth});
        if (optionsRecipes.getBooleanValue("Flint Tools")) {
          MMWUtil.addRecipe(new ItemStack(hatchetFlint, 1), new Object[] {"FS", "S ", 'F', Item.flint, 'S', Item.stick});
        }
    	  if (optionsRecipes.getBooleanValue("Wood Axes")) {
          MMWUtil.addRecipe(new ItemStack(hatchetWood, 1), new Object[] {"WS", "S ", 'W', Block.planks, 'S', Item.stick}); 
        } 
    	  if (optionsRecipes.getBooleanValue("Cobblestone Tools")) {
          MMWUtil.addRecipe(new ItemStack(hatchetStone, 1), new Object[] {"CS", "S ", 'C', Block.cobblestone, 'S', Item.stick}); 
        } 
        MMWUtil.addRecipe(new ItemStack(hatchetIron, 1), new Object[] {"IS", "S ", 'I', Item.ingotIron, 'S', Item.stick}); 
    	  if (optionsRecipes.getBooleanValue("Diamond Tools")) {
          MMWUtil.addRecipe(new ItemStack(hatchetDiamond, 1), new Object[] {"DS", "S ", 'D', Item.diamond, 'S', Item.stick}); 
        } 
        MMWUtil.addRecipe(new ItemStack(hatchetGold, 1), new Object[] {"GS", "S ", 'G', Item.ingotGold, 'S', Item.stick}); 
        // Dagger (" #", "/ ", #=Material, /=stick)
        // Sling ("#", "@", #=Leather, @=string)
        // Sling stones? gravel -> stones?
        // Trowel ("#", "/", #=Material, /=stick) Shovel/hoe
        // Hand Pick ("##", "/ ", #=Material, /=stick)
        // Campfire ("//", "//", /=stick)
        // Bow Drill ("/@", "/#", /=stick, @=string, #=planks)
      }
    } else if (extendedCrafting) {
      extendedCrafting = false;
      MMWUtil.removeRecipe(new ItemStack(Item.stick, 1), new Object[] {"#", "#", '#', Block.sapling}); 
      MMWUtil.removeRecipe(new ItemStack(Item.stick, 2), new Object[] {"#", "#", '#', Block.cactus}); 
      MMWUtil.removeRecipe(new ItemStack(Item.stick, 2), new Object[] {"/", "/", '/', Item.bone});
      MMWUtil.removeRecipe(new ItemStack(Item.silk, 2), new Object[] {"#", '#', Block.cloth});
      MMWUtil.removeRecipe(new ItemStack(hatchetFlint, 1), new Object[] {"FS", "S ", 'F', Item.flint, 'S', Item.stick});
      MMWUtil.removeRecipe(new ItemStack(hatchetWood, 1), new Object[] {"WS", "S ", 'W', Block.planks, 'S', Item.stick}); 
      MMWUtil.removeRecipe(new ItemStack(hatchetStone, 1), new Object[] {"CS", "S ", 'C', Block.cobblestone, 'S', Item.stick}); 
      MMWUtil.removeRecipe(new ItemStack(hatchetIron, 1), new Object[] {"IS", "S ", 'I', Item.ingotIron, 'S', Item.stick}); 
      MMWUtil.removeRecipe(new ItemStack(hatchetDiamond, 1), new Object[] {"DS", "S ", 'D', Item.diamond, 'S', Item.stick}); 
      MMWUtil.removeRecipe(new ItemStack(hatchetGold, 1), new Object[] {"GS", "S ", 'G', Item.ingotGold, 'S', Item.stick}); 
    }
  }  

  public static void killCritters() {
    boolean allowHostile = optHostile.getValue();
    boolean allowPeaceful = optPeaceful.getValue(); 
      
    // Hostile & Peaceful Spawn overrides
    mc.theWorld.setAllowedSpawnTypes(allowHostile, allowPeaceful);
    IntegratedServer server = mc.getIntegratedServer();
    if (server!=null) {
      for (int idx=0;idx<server.worldServers.length;idx++) {
        server.worldServers[idx].setAllowedSpawnTypes(allowHostile, allowPeaceful);
      }    
    }
    if (!allowHostile) {
      MMWUtil.killAll(EntityBlaze.class);
      MMWUtil.killAll(EntityCaveSpider.class);
      MMWUtil.killAll(EntityCreeper.class);
      MMWUtil.killAll(EntityDragon.class);
      MMWUtil.killAll(EntityEnderman.class);
      MMWUtil.killAll(EntityGhast.class);
      MMWUtil.killAll(EntityGiantZombie.class);
      MMWUtil.killAll(EntityMagmaCube.class);
      MMWUtil.killAll(EntityPigZombie.class);
      MMWUtil.killAll(EntitySilverfish.class);
      MMWUtil.killAll(EntitySkeleton.class);
      MMWUtil.killAll(EntitySlime.class);
      MMWUtil.killAll(EntitySpider.class);
      MMWUtil.killAll(EntityWitch.class);
      MMWUtil.killAll(EntityWither.class);
      MMWUtil.killAll(EntityZombie.class);
    }    
    if (!allowPeaceful) {
      MMWUtil.killAll(EntityVillager.class);
      MMWUtil.killAll(EntityBat.class);
      MMWUtil.killAll(EntityChicken.class);
      MMWUtil.killAll(EntityCow.class);
      MMWUtil.killAll(EntityIronGolem.class);
      MMWUtil.killAll(EntityMooshroom.class);
      MMWUtil.killAll(EntityOcelot.class);
      MMWUtil.killAll(EntityPig.class);
      MMWUtil.killAll(EntitySheep.class);
      MMWUtil.killAll(EntitySnowman.class);
      MMWUtil.killAll(EntityWolf.class);
      MMWUtil.killAll(EntitySquid.class);
    }
    // NPCs
    mc.getIntegratedServer().setCanSpawnNPCs(optNPC.getValue());
    if (!optNPC.getValue()) { 
      MMWUtil.killAll(EntityVillager.class);
    }
    if (!allowHostile && !allowPeaceful) {
      return;
    }
    if (allowPeaceful) {
      if (!optBats.getValue()) {
        MMWUtil.killAll(EntityBat.class);
      }
      if (!optChicken.getValue()) {
        MMWUtil.killAll(EntityChicken.class);
      }
      if (!optCow.getValue()) {
        MMWUtil.killAll(EntityCow.class);
        MMWUtil.killAll(MMWEntityCow.class);
      }
      if (!optIronGolem.getValue()) {
        MMWUtil.killAll(EntityIronGolem.class);
      }
      if (!optMooshroom.getValue()) {
        MMWUtil.killAll(EntityMooshroom.class);
      }
      if (!optOcelot.getValue()) {
        MMWUtil.killAll(EntityOcelot.class);
      }
      if (!optPig.getValue()) {
        MMWUtil.killAll(EntityPig.class);
        MMWUtil.killAll(MMWEntityPig.class);
      } 
      if (!optSheep.getValue()) {
        MMWUtil.killAll(EntitySheep.class);
        MMWUtil.killAll(MMWEntitySheep.class);
      }
      if (!optSnowman.getValue()) {
        MMWUtil.killAll(EntitySnowman.class);
      }
      if (!optWolf.getValue()) {
        MMWUtil.killAll(EntityWolf.class);
      }
      if (!optSquid.getValue()) {
        MMWUtil.killAll(EntitySquid.class);
      }
    }
    if (allowHostile) {
      if (!optBlaze.getValue()) {
        MMWUtil.killAll(EntityBlaze.class);
      }
      if (!optCaveSpider.getValue()) {
        MMWUtil.killAll(EntityCaveSpider.class);
      }
      if (!optCreeper.getValue()) {
        MMWUtil.killAll(EntityCreeper.class);
      }
      if (!optDragon.getValue()) {
        MMWUtil.killAll(EntityDragon.class);
      }
      if (!optEnderman.getValue()) {
        MMWUtil.killAll(EntityEnderman.class);
      }
      if (!optGhast.getValue()) {
        MMWUtil.killAll(EntityGhast.class);
      }
      if (!optGiantZombie.getValue()) {
        MMWUtil.killAll(EntityGiantZombie.class);
      }
      if (!optMagmaCube.getValue()) {
        MMWUtil.killAll(EntityMagmaCube.class);
      }
      if (!optPigZombie.getValue()) {
        MMWUtil.killAll(EntityPigZombie.class);
      }
      if (!optSilverfish.getValue()) {
        MMWUtil.killAll(EntitySilverfish.class);
      }
      if (!optSkeleton.getValue()) {
        MMWUtil.killAll(EntitySkeleton.class);
      }
      if (!optSlime.getValue()) {
        MMWUtil.killAll(EntitySlime.class);
      }
      if (!optSpider.getValue()) {
        MMWUtil.killAll(EntitySpider.class);
      }
      if (!optWitch.getValue()) {
        MMWUtil.killAll(EntityWitch.class);
      }
      if (!optWither.getValue()) {
        MMWUtil.killAll(EntityWither.class);
      }
      if (!optZombie.getValue()) {
        MMWUtil.killAll(EntityZombie.class);
      }
    }
  }
  
  public static void processPeacefulSpawns() {
    boolean allowPeaceful = optPeaceful.getValue(); 
      
    // Set peaceful mob adjustments
    peacefulDropBones = optionsPeacefulMobs.getBooleanValue("Drop bones");
    peacefulAdjustDrops = optionsPeacefulMobs.getBooleanValue("Adjust drops");
    if (allowPeaceful && !enabledBats && (enabledBats=optBats.getValue())) {
      MMWUtil.addSpawn(EntityBat.class, 10, 8, 8, EnumCreatureType.ambient);
    } else if (enabledBats && !(enabledBats=optBats.getValue())) {
      MMWUtil.removeSpawn(EntityBat.class, EnumCreatureType.ambient);
    }
    if (allowPeaceful && !enabledChicken && (enabledChicken=optChicken.getValue())) {
      MMWUtil.addSpawn(EntityChicken.class, 10, 4, 4, EnumCreatureType.creature);
    } else if (enabledChicken && !(enabledChicken=optChicken.getValue())) {
      MMWUtil.removeSpawn(EntityChicken.class, EnumCreatureType.creature);
    }
    if (allowPeaceful && !enabledCow && (enabledCow=optCow.getValue())) {
      MMWUtil.addSpawn(MMWEntityCow.class, 8, 4, 4, EnumCreatureType.creature);
    } else if (enabledCow && !(enabledCow=optCow.getValue())) {
      MMWUtil.removeSpawn(EntityCow.class, EnumCreatureType.creature);
      MMWUtil.removeSpawn(MMWEntityCow.class, EnumCreatureType.creature);
    }
    if (allowPeaceful && !enabledMooshroom && (enabledMooshroom=optMooshroom.getValue())) {
      MMWUtil.addSpawn(EntityMooshroom.class, 8, 4, 8, EnumCreatureType.creature, new BiomeGenBase[] {BiomeGenBase.mushroomIsland});
    } else if (enabledMooshroom && !(enabledMooshroom=optMooshroom.getValue())) {
      MMWUtil.removeSpawn(EntityMooshroom.class, EnumCreatureType.creature);
    }
    if (allowPeaceful && !enabledOcelot && (enabledOcelot=optOcelot.getValue())) {
      MMWUtil.addSpawn(EntityOcelot.class, 2, 1, 1, EnumCreatureType.creature, new BiomeGenBase[] {BiomeGenBase.jungle});
    } else if (enabledOcelot && !(enabledOcelot=optOcelot.getValue())) {
      MMWUtil.removeSpawn(EntityOcelot.class, EnumCreatureType.creature);
    }
    if (allowPeaceful && !enabledPig && (enabledPig=optPig.getValue())) {
      MMWUtil.addSpawn(MMWEntityPig.class, 10, 4, 4, EnumCreatureType.creature);
    } else if (enabledPig && !(enabledPig=optPig.getValue())) {
      MMWUtil.removeSpawn(EntityPig.class, EnumCreatureType.creature);
      MMWUtil.removeSpawn(MMWEntityPig.class, EnumCreatureType.creature);
    } 
    if (allowPeaceful && !enabledSheep && (enabledSheep=optSheep.getValue())) {
      MMWUtil.addSpawn(MMWEntitySheep.class, 12, 4, 4, EnumCreatureType.creature);
    } else if (enabledSheep && !(enabledSheep=optSheep.getValue())) {
      MMWUtil.removeSpawn(EntitySheep.class, EnumCreatureType.creature);
      MMWUtil.removeSpawn(MMWEntitySheep.class, EnumCreatureType.creature);
    }
    if (allowPeaceful && !enabledWolf && (enabledWolf=optWolf.getValue())) {
      MMWUtil.addSpawn(EntityWolf.class, 5, 4, 4, EnumCreatureType.creature, new BiomeGenBase[] {BiomeGenBase.forest});
      MMWUtil.addSpawn(EntityWolf.class, 8, 4, 4, EnumCreatureType.creature, new BiomeGenBase[] {BiomeGenBase.taiga});
    } else if (enabledWolf && !(enabledWolf=optWolf.getValue())) {
      MMWUtil.removeSpawn(EntityWolf.class, EnumCreatureType.creature);
    }
    if (allowPeaceful && !enabledSquid && (enabledSquid=optSquid.getValue())) {
      MMWUtil.addSpawn(EntitySquid.class, 10, 4, 4, EnumCreatureType.waterCreature);
    } else if (enabledSquid && !(enabledSquid=optSquid.getValue())) {
      MMWUtil.removeSpawn(EntitySquid.class, EnumCreatureType.waterCreature);
    }
  }
    
  public static void processHostileSpawns() {
    boolean allowHostile = optHostile.getValue();
    
    if (allowHostile && !enabledCreeper && (enabledCreeper=optCreeper.getValue())) {
      MMWUtil.addSpawn(EntityCreeper.class, 10, 4, 4, EnumCreatureType.monster);
    } else if (enabledCreeper && !(enabledCreeper=optCreeper.getValue())) {
      MMWUtil.removeSpawn(EntityCreeper.class, EnumCreatureType.monster);
    }
    if (allowHostile && !enabledEnderman && (enabledEnderman=optEnderman.getValue())) {
      MMWUtil.addSpawn(EntityEnderman.class, 1, 1, 4, EnumCreatureType.monster);
    } else if (enabledEnderman && !(enabledEnderman=optEnderman.getValue())) {
      MMWUtil.removeSpawn(EntityEnderman.class, EnumCreatureType.monster);
    }
    if (allowHostile && !enabledGhast && (enabledGhast=optGhast.getValue())) {
      MMWUtil.addSpawn(EntityGhast.class, 50, 4, 4, EnumCreatureType.monster, new BiomeGenBase[] {BiomeGenBase.hell});
    } else if (enabledGhast && !(enabledGhast=optGhast.getValue())) {
      MMWUtil.removeSpawn(EntityGhast.class, EnumCreatureType.monster);
    }
    if (allowHostile && !enabledMagmaCube && (enabledMagmaCube=optMagmaCube.getValue())) {
      MMWUtil.addSpawn(EntityMagmaCube.class, 1, 4, 4, EnumCreatureType.monster, new BiomeGenBase[] {BiomeGenBase.hell});
    } else if (enabledMagmaCube && !(enabledMagmaCube=optMagmaCube.getValue())) {
      MMWUtil.removeSpawn(EntityMagmaCube.class, EnumCreatureType.monster);
    }
    if (allowHostile && !enabledPigZombie && (enabledPigZombie=optPigZombie.getValue())) {
      MMWUtil.addSpawn(EntityPigZombie.class, 100, 4, 4, EnumCreatureType.monster, new BiomeGenBase[] {BiomeGenBase.hell});
    } else if (enabledPigZombie && !(enabledPigZombie=optPigZombie.getValue())) {
      MMWUtil.removeSpawn(EntityPigZombie.class, EnumCreatureType.monster);
    }
    if (allowHostile && !enabledSkeleton && (enabledSkeleton=optSkeleton.getValue())) {
      MMWUtil.addSpawn(EntitySkeleton.class, 10, 4, 4, EnumCreatureType.monster);
    } else if (enabledSkeleton && !(enabledSkeleton=optSkeleton.getValue())) {
      MMWUtil.removeSpawn(EntitySkeleton.class, EnumCreatureType.monster);
    }
    if (allowHostile && !enabledSlime && (enabledSlime=optSlime.getValue())) {
      MMWUtil.addSpawn(EntitySlime.class, 10, 4, 4, EnumCreatureType.monster);
    } else if (enabledSlime && !(enabledSlime=optSlime.getValue())) {
      MMWUtil.removeSpawn(EntitySlime.class, EnumCreatureType.monster);
    }
    if (allowHostile && !enabledSpider && (enabledSpider=optSpider.getValue())) {
      MMWUtil.addSpawn(EntitySpider.class, 10, 4, 4, EnumCreatureType.monster);
    } else if (enabledSpider && !(enabledSpider=optSpider.getValue())) {
      MMWUtil.removeSpawn(EntitySpider.class, EnumCreatureType.monster);
    }
    if (allowHostile && !enabledZombie && (enabledZombie=optZombie.getValue())) {
      MMWUtil.addSpawn(EntityZombie.class, 10, 4, 4, EnumCreatureType.monster);
    } else if (enabledZombie && !(enabledZombie=optZombie.getValue())) {
      MMWUtil.removeSpawn(EntityZombie.class, EnumCreatureType.monster);
    }
  }

  private void initModOptionsAPI() {
	  String[] genOptions = new String[]{"Off", "Rare", "Normal", "Common"}; 
	  int[] genValues = {0, 1, 2, 3};
  
	  // Create option screens
	  optionsMain = ModOptionsAPI.addMod("Minecraft My Way");
	  optionsAdventureMode = optionsMain.addSubOption("Minecraft My Way - Adventure Mode");
	  optionsWorldGen = optionsMain.addSubOption("Minecraft My Way - Default World Gen");
	  optionsMobs = optionsMain.addSubOption("Minecraft My Way - Mobs");
	  optionsPeacefulMobs = optionsMobs.addSubOption("Minecraft My Way - Peaceful Mobs");
	  optionsHostileMobs = optionsMobs.addSubOption("Minecraft My Way - Hostile Mobs");
	  optionsRecipes = optionsMain.addSubOption("Minecraft My Way - Recipes");
	  optionsSpecial = optionsMain.addSubOption("Minecraft My Way - Special");
	  // Add options to Hostile Spawn screen
	  optHostile = (ModOptionBoolean)optionsHostileMobs.addBooleanOption("Allow Hostile Spawns").setValue(true).setWide(true);
	  optBlaze = (ModOptionBoolean)optionsHostileMobs.addBooleanOption("Blaze").setValue(true);
	  optCaveSpider = (ModOptionBoolean)optionsHostileMobs.addBooleanOption("Cave Spider").setValue(true);
	  optCreeper = (ModOptionBoolean)optionsHostileMobs.addBooleanOption("Creeper").setValue(true);
	  optDragon = (ModOptionBoolean)optionsHostileMobs.addBooleanOption("Dragon").setValue(true);
	  optEnderman = (ModOptionBoolean)optionsHostileMobs.addBooleanOption("Enderman").setValue(true);
	  optGhast = (ModOptionBoolean)optionsHostileMobs.addBooleanOption("Ghast").setValue(true);
	  optGiantZombie = (ModOptionBoolean)optionsHostileMobs.addBooleanOption("Giant Zombie").setValue(true);
	  optMagmaCube = (ModOptionBoolean)optionsHostileMobs.addBooleanOption("Magma Cube").setValue(true);
	  optPigZombie = (ModOptionBoolean)optionsHostileMobs.addBooleanOption("Pig Zombie").setValue(true);
	  optSilverfish = (ModOptionBoolean)optionsHostileMobs.addBooleanOption("Silverfish").setValue(true);
	  optSkeleton = (ModOptionBoolean)optionsHostileMobs.addBooleanOption("Skeleton").setValue(true);
	  optSlime = (ModOptionBoolean)optionsHostileMobs.addBooleanOption("Slime").setValue(true);
	  optSpider = (ModOptionBoolean)optionsHostileMobs.addBooleanOption("Spider").setValue(true);
	  optWitch = (ModOptionBoolean)optionsHostileMobs.addBooleanOption("Witch").setValue(true);
	  optWither = (ModOptionBoolean)optionsHostileMobs.addBooleanOption("Wither").setValue(true);
	  optZombie = (ModOptionBoolean)optionsHostileMobs.addBooleanOption("Zombie").setValue(true);
	  // Add options to Peaceful Spawn screen
	  optPeaceful = (ModOptionBoolean)optionsPeacefulMobs.addBooleanOption("Allow Peaceful Spawns").setValue(true).setWide(true);
	  optionsPeacefulMobs.addBooleanOption("Adjust drops").setValue(false);
	  optionsPeacefulMobs.addBooleanOption("Drop bones").setValue(false);
	  optBats = (ModOptionBoolean)optionsPeacefulMobs.addBooleanOption("Bats").setValue(true);
	  optChicken = (ModOptionBoolean)optionsPeacefulMobs.addBooleanOption("Chicken").setValue(true);
	  optCow = (ModOptionBoolean)optionsPeacefulMobs.addBooleanOption("Cow").setValue(true);
	  optIronGolem = (ModOptionBoolean)optionsPeacefulMobs.addBooleanOption("Iron Golem").setValue(true);
	  optMooshroom = (ModOptionBoolean)optionsPeacefulMobs.addBooleanOption("Mooshroom").setValue(true);
	  optNPC = (ModOptionBoolean)optionsPeacefulMobs.addBooleanOption("NPCs").setValue(true);
	  optOcelot = (ModOptionBoolean)optionsPeacefulMobs.addBooleanOption("Ocelot/Cat").setValue(true);
	  optPig = (ModOptionBoolean)optionsPeacefulMobs.addBooleanOption("Pig").setValue(true);
	  optSheep = (ModOptionBoolean)optionsPeacefulMobs.addBooleanOption("Sheep").setValue(true);
	  optSnowman = (ModOptionBoolean)optionsPeacefulMobs.addBooleanOption("Snowman").setValue(true);
	  optSquid = (ModOptionBoolean)optionsPeacefulMobs.addBooleanOption("Squid").setValue(true);
	  optWolf = (ModOptionBoolean)optionsPeacefulMobs.addBooleanOption("Wolf").setValue(true);
	  // Add options to Recipe screen
	  optionsRecipes.addTextOption("Base Item ID", 5, "12120").setWide(true);
	  optionsRecipes.addBooleanOption("Cobblestone Tools").setValue(true);
	  optionsRecipes.addBooleanOption("Craft Cobblestone").setValue(false);
	  optionsRecipes.addBooleanOption("Craft Flint").setValue(false);
	  optionsRecipes.addBooleanOption("Diamond Armor").setValue(true);
	  optionsRecipes.addBooleanOption("Diamond Tools").setValue(true);
	  optionsRecipes.addBooleanOption("Extended 2x2 Crafting").setValue(false);
	  optionsRecipes.addBooleanOption("Flint Tools").setValue(false);
	  optionsRecipes.addBooleanOption("TNT").setValue(true);
	  optionsRecipes.addBooleanOption("Wood Axes").setValue(true);
	  // Add options to Special screen
	  optionsSpecial.addBooleanOption("Auto Healing").setValue(true);
	  optionsSpecial.addBooleanOption("Bonemeal Instagrowth").setValue(true);
	  optionsSpecial.addBooleanOption("Dirt Gravity").setValue(false);
	  optionsSpecial.addBooleanOption("Gravel Gravity").setValue(true);
	  optionsSpecial.addBooleanOption("Hunger").setValue(true);
	  optionsSpecial.addMultiOption("Rain", new String[]{"Normal", "Always", "Never"});
	  optionsSpecial.addBooleanOption("Sand Gravity").setValue(true);
	  optionsSpecial.addBooleanOption("Sprinting").setValue(true);
	  optionsSpecial.addBooleanOption("Stone Drops Gravel").setValue(false);
	  optionsSpecial.addMultiOption("Time Flow", new String[]{"Normal", "Always Day", "Always Night", "Rapid", "Slow", "Single Day"});
	  optionsSpecial.addBooleanOption("XP").setValue(true);
	  // Add options to World Gen screen
	  optionsWorldGen.addBooleanOption("Biome - Desert").setValue(true);
	  optionsWorldGen.addBooleanOption("Biome - Extreme Hills").setValue(true);
	  optionsWorldGen.addBooleanOption("Biome - Forest").setValue(true);
	  optionsWorldGen.addBooleanOption("Biome - Jungle").setValue(true);
	  optionsWorldGen.addBooleanOption("Biome - Plains").setValue(true);
	  optionsWorldGen.addBooleanOption("Biome - Swampland").setValue(true);
	  optionsWorldGen.addBooleanOption("Biome - Taiga").setValue(true);
	  optionsWorldGen.addMappedOption("Caves", genOptions, genValues).setValue(2);
	  optionsWorldGen.addMappedOption("Dungeons", genOptions, genValues).setValue(2);
	  optionsWorldGen.addMappedOption("Lakes", genOptions, genValues).setValue(2);
	  optionsWorldGen.addMappedOption("Large Caves", genOptions, genValues).setValue(2);
	  optionsWorldGen.addMappedOption("Lava Lakes", genOptions, genValues).setValue(2);
	  optionsWorldGen.addMappedOption("Mineshafts", genOptions, genValues).setValue(2);
	  optionsWorldGen.addMappedOption("Ravines", genOptions, genValues).setValue(2);
	  optionsWorldGen.addMappedOption("Scattered Features", genOptions, genValues).setValue(2);
	  optionsWorldGen.addMappedOption("Strongholds", genOptions, genValues).setValue(2);
	  optionsWorldGen.addMappedOption("Villages", genOptions, genValues).setValue(2);
	  // Add options to Adventure Mode screen
	  optionsAdventureMode.addBooleanOption("Allow on Creation").setValue(false).setWide(true);
	  optionsAdventureMode.addBooleanOption("Hands break cactus").setValue(false);
	  optionsAdventureMode.addBooleanOption("Hands break clay").setValue(false);
	  optionsAdventureMode.addBooleanOption("Hands break dirt").setValue(false);
	  optionsAdventureMode.addBooleanOption("Hands break leaves").setValue(false);
	  optionsAdventureMode.addBooleanOption("Hands break sand/gravel").setValue(false).setWide(true);
	  // Load saved values over defaults (if any)
	  optionsMain.loadValues();
	  // Save current values
	  optionsMain.saveValues();
  }

  public String getPriorities() {
    return "required-after:mod_MOAPI";
  }

  public String getName() {
    return Name;
  }
	
  public String getVersion() {
    return Version;
  }
}
