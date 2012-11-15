package net.minecraft.src;

import java.util.*;
import java.lang.reflect.*;
import java.io.PrintWriter;
import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import moapi.api.*;

public class mod_MyWay extends BaseMod {
  // Copyright/license info
  private static final String Name = "Minecraft My Way";
  private static final String Version = "0.6 (For use with Minecraft 1.4.4)";
	private static final String Copyright = "All original code and images (C) 2011-2012, Jonathan \"Wyrd\" Brazell";
	private static final String License = "This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 3.0 Unported License.";
  // Options
  private static ModOptions optionsMain;
  private static ModOptions optionsHostileSpawns;
  private static ModOptions optionsPeacefulSpawns;
  private static ModOptions optionsRecipes;
  private static ModOptions optionsSpecial;
  private static ModOptions optionsWorldGen;
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
  public static boolean genCaves = true;
  public static boolean genStrongholds = true;
  public static boolean genVillages = true;
  public static boolean genMineshafts = true;
  public static boolean genScatteredFeatures = true;
  public static boolean genRavines = true;
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
  public static boolean isObfuscated;
  private static MMWWorldType mmwWorldType = new MMWWorldType();
  private static MMWFoodStats mmwFoodStats = new MMWFoodStats();
  private static Minecraft mc = ModLoader.getMinecraftInstance();
  private static Block saveStone = Block.stone;
  private static Block mmwStone;
  private static Block saveSand = Block.sand;
  private static Block mmwSand;
  private static Block saveGravel = Block.gravel;
  private static Block mmwGravel;
  private static Item saveItemDye = Item.dyePowder;
  private static int tickCounter = 0;
  private static int baseItemID = 12120;
      	
  public void load() {
    // Set up options menu for mod options
    initModOptionsAPI();
    baseItemID = Integer.parseInt(optionsRecipes.getOptionValue("Base Item ID"));

    // Replace Stone
    Block.blocksList[Block.stone.blockID] = null;
    mmwStone = (new MMWBlockStone(1, 1)).setHardness(1.5F).setResistance(10.0F).setStepSound(Block.soundStoneFootstep).setBlockName("stone");
    ModLoader.registerBlock(mmwStone);

    // Replace Sand
    Block.blocksList[Block.sand.blockID] = null;
    mmwSand = (new MMWBlockSand(12, 18)).setHardness(0.5F).setStepSound(Block.soundSandFootstep).setBlockName("sand");
    ModLoader.registerBlock(mmwSand);

    // Replace Gravel
    Block.blocksList[Block.gravel.blockID] = null;
    mmwGravel = (new MMWBlockGravel(13, 19)).setHardness(0.6F).setStepSound(Block.soundGravelFootstep).setBlockName("gravel");
    ModLoader.registerBlock(mmwGravel);

    // Replace dye to allow bonemeal to be disabled.
    Item.itemsList[Item.dyePowder.shiftedIndex] = null;
    Item.dyePowder = (new MMWItemDye(95)).setIconCoord(14, 4).setItemName("dyePowder");

    // Remove spawns for vanilla mobs, add custom mob spawns to replace them
    ModLoader.removeSpawn(EntityCow.class, EnumCreatureType.creature);
    ModLoader.removeSpawn(EntityPig.class, EnumCreatureType.creature);
    ModLoader.removeSpawn(EntitySheep.class, EnumCreatureType.creature);
    ModLoader.registerEntityID(MMWEntityCow.class, "Cow", 92);
    ModLoader.registerEntityID(MMWEntityPig.class, "Pig", 90);
    ModLoader.registerEntityID(MMWEntitySheep.class, "Sheep", 91);
    ModLoader.addSpawn(MMWEntityCow.class, 8, 4, 4, EnumCreatureType.creature);
    ModLoader.addSpawn(MMWEntityPig.class, 10, 4, 4, EnumCreatureType.creature);
    ModLoader.addSpawn(MMWEntitySheep.class, 12, 4, 4, EnumCreatureType.creature);

    // Add flint tool items
    hoeFlint = new MMWItemHoe(baseItemID+0, MMWEnumToolMaterial.FLINT).setItemName("hoeFlint").setIconCoord(1, 8);
    axeFlint = new MMWItemAxe(baseItemID+1, MMWEnumToolMaterial.FLINT).setItemName("axeFlint").setIconCoord(1, 7);
    pickaxeFlint = new MMWItemPickaxe(baseItemID+2, MMWEnumToolMaterial.FLINT).setItemName("pickaxeFlint").setIconCoord(1, 6);
    spearFlint = new MMWItemSpear(baseItemID+3, MMWEnumToolMaterial.FLINT).setItemName("spearFlint").setIconCoord(1, 4);  
    ModLoader.addName(pickaxeFlint, "Flint Pickaxe");
    ModLoader.addName(spearFlint, "Flint Spear");      
    ModLoader.addName(axeFlint, "Flint Axe");
    ModLoader.addName(hoeFlint, "Flint Hoe");
    if (optionsRecipes.getToggleValue("Flint Tools")) {
      flintTools = true;
      ModLoader.addRecipe(new ItemStack(spearFlint, 1), new Object[] {"F", "S", "S", 'F', Item.flint, 'S', Item.stick}); 
      ModLoader.addRecipe(new ItemStack(pickaxeFlint, 1), new Object[] {"FFF", " S ", " S ", 'F', Item.flint, 'S', Item.stick}); 
      ModLoader.addRecipe(new ItemStack(axeFlint, 1), new Object[] {"FF", "FS", " S", 'F', Item.flint, 'S', Item.stick}); 
      ModLoader.addRecipe(new ItemStack(hoeFlint, 1), new Object[] {"FF", " S", " S", 'F', Item.flint, 'S', Item.stick});
    }

    // Add hatchets
    hatchetFlint = new MMWItemHatchetFlint(baseItemID+4, MMWEnumToolMaterial.FLINT).setItemName("hatchetFlint").setIconCoord(1, 7);
    hatchetWood = new MMWItemHatchet(baseItemID+5, EnumToolMaterial.WOOD).setItemName("hatchetWood").setIconCoord(0, 7);
    hatchetStone = new MMWItemHatchet(baseItemID+6, EnumToolMaterial.STONE).setItemName("hatchetStone").setIconCoord(1, 7);
    hatchetIron = new MMWItemHatchet(baseItemID+7, EnumToolMaterial.IRON).setItemName("hatchetIron").setIconCoord(2, 7);
    hatchetDiamond = new MMWItemHatchet(baseItemID+8, EnumToolMaterial.EMERALD).setItemName("hatchetDiamond").setIconCoord(3, 7);
    hatchetGold = new MMWItemHatchet(baseItemID+9, EnumToolMaterial.GOLD).setItemName("hatchetGold").setIconCoord(4, 7);
    ModLoader.addName(hatchetFlint, "Flint Hatchet");
    ModLoader.addName(hatchetWood, "Wood Hatchet");
    ModLoader.addName(hatchetStone, "Stone Hatchet");
    ModLoader.addName(hatchetIron, "Iron Hatchet");
    ModLoader.addName(hatchetDiamond, "Diamond Hatchet");
    ModLoader.addName(hatchetGold, "Gold Hatchet");
	  if (optionsRecipes.getToggleValue("Extended 2x2 Crafting") && optionsRecipes.getToggleValue("Flint Tools")) {
      ModLoader.addRecipe(new ItemStack(hatchetFlint, 1), new Object[] {"FS", "S ", 'F', Item.flint, 'S', Item.stick}); 
    } 
	  if (optionsRecipes.getToggleValue("Extended 2x2 Crafting") && optionsRecipes.getToggleValue("Wood Axes")) {
      ModLoader.addRecipe(new ItemStack(hatchetWood, 1), new Object[] {"WS", "S ", 'W', Block.planks, 'S', Item.stick}); 
    } 
	  if (optionsRecipes.getToggleValue("Extended 2x2 Crafting") && optionsRecipes.getToggleValue("Cobblestone Tools")) {
      ModLoader.addRecipe(new ItemStack(hatchetStone, 1), new Object[] {"CS", "S ", 'C', Block.cobblestone, 'S', Item.stick}); 
    } 
	  if (optionsRecipes.getToggleValue("Extended 2x2 Crafting")) {
      ModLoader.addRecipe(new ItemStack(hatchetIron, 1), new Object[] {"IS", "S ", 'I', Item.ingotIron, 'S', Item.stick}); 
    }
	  if (optionsRecipes.getToggleValue("Extended 2x2 Crafting") && optionsRecipes.getToggleValue("Diamond Tools")) {
      ModLoader.addRecipe(new ItemStack(hatchetDiamond, 1), new Object[] {"DS", "S ", 'D', Item.diamond, 'S', Item.stick}); 
    } 
	  if (optionsRecipes.getToggleValue("Extended 2x2 Crafting")) {
      ModLoader.addRecipe(new ItemStack(hatchetGold, 1), new Object[] {"GS", "S ", 'G', Item.ingotGold, 'S', Item.stick}); 
    } 
     
    // Check for option changes every tick
    ModLoader.setInGameHook(this, true, true);
  }

  public boolean onTickInGame(float f, Minecraft paramMinecraft) {
    processSpawns();
    processTimeFlow();
    if (mc.thePlayer!=null && !optionsSpecial.getToggleValue("Sprinting")) {
      mc.thePlayer.setSprinting(false);
    }
    // Don't need to check other options every tick.
    tickCounter++;
    if ((tickCounter & 0x001f)!=0x0001)
      return true;
    processSpecial();
    processRecipes();
    return true;
  }

  public static void processTimeFlow() {
    if (optionsSpecial.getOptionValue("Time Flow")=="Always Day") {
      for (int idx=0; idx<MinecraftServer.getServer().worldServers.length; idx++) {
        long time = MinecraftServer.getServer().worldServers[idx].getWorldTime();
        if ((time%24000)>12000) {
          time = (time / 24000) * 24000; 
          MinecraftServer.getServer().worldServers[idx].setWorldTime(time);
        }
      }
    } else if (optionsSpecial.getOptionValue("Time Flow")=="Always Night") {
      for (int idx=0; idx<MinecraftServer.getServer().worldServers.length; idx++) {
        long time = MinecraftServer.getServer().worldServers[idx].getWorldTime();
        if (((time%24000)<13800) || ((time%24000)>22200)) {
          time = ((time / 24000) * 24000) + 13800; 
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
          time = ((time / 24000) * 24000) + 13800; 
          MinecraftServer.getServer().worldServers[idx].setWorldTime(time);
        }
      }
    } 
  }

  public static void processWorldGen() {
    // World Gen Options
    genCaves = optionsWorldGen.getToggleValue("Caves");
    genMineshafts = optionsWorldGen.getToggleValue("Mineshafts");
    genRavines = optionsWorldGen.getToggleValue("Ravines");
    genScatteredFeatures = optionsWorldGen.getToggleValue("Scattered Features");
    genStrongholds = optionsWorldGen.getToggleValue("Strongholds");
    genVillages = optionsWorldGen.getToggleValue("Villages");
  }

  public static void processSpecial() {
    // Bonemeal
    ((MMWItemDye)Item.dyePowder).disableBonemeal = !optionsSpecial.getToggleValue("Bonemeal Instagrowth");
    // Gravel/Sand
    ((MMWBlockSand)mmwSand).gravityWorks = optionsSpecial.getToggleValue("Gravel/Sand Gravity"); 
    ((MMWBlockGravel)mmwGravel).gravityWorks = optionsSpecial.getToggleValue("Gravel/Sand Gravity"); 
    // Stone
    ((MMWBlockStone)Block.blocksList[Block.stone.blockID]).drop = optionsSpecial.getToggleValue("Stone Drops Gravel") ? Block.gravel.blockID : Block.cobblestone.blockID;
    // Set custom FoodStats
    if (mc.thePlayer!=null) {
      mc.thePlayer.foodStats = mmwFoodStats;
      if (mc.thePlayer.worldObj.isRemote) {
        for (int idx=0; idx<mc.getIntegratedServer().getConfigurationManager().playerEntityList.size(); idx++) {
          ((EntityPlayerMP)mc.getIntegratedServer().getConfigurationManager().playerEntityList.get(idx)).foodStats = mmwFoodStats;
        }
      }
    }
    // Fatigue/Exhaustion
    if (optionsSpecial.getToggleValue("Exhaustion")) {
      mmwFoodStats.setFoodExhaustionLevel(0.0F);
    }
    // Hunger
    mmwFoodStats.hunger = optionsSpecial.getToggleValue("Hunger");
    // Healing
    mmwFoodStats.healing = optionsSpecial.getToggleValue("Auto Healing");
  }

  public static void processRecipes() {
    // Diamond Tools
    if (!optionsRecipes.getToggleValue("Diamond Tools")) {
      if (!removeDiamondTools) {
        removeDiamondTools = true;
        removeRecipe(new ItemStack(Item.swordDiamond, 1), new Object[] {"D", "D", "S", 'D', Item.diamond, 'S', Item.stick}); 
        removeRecipe(new ItemStack(Item.shovelDiamond, 1), new Object[] {"D", "S", "S", 'D', Item.diamond, 'S', Item.stick}); 
        removeRecipe(new ItemStack(Item.pickaxeDiamond, 1), new Object[] {"DDD", " S ", " S ", 'D', Item.diamond, 'S', Item.stick}); 
        removeRecipe(new ItemStack(Item.axeDiamond, 1), new Object[] {"DD", "DS", " S", 'D', Item.diamond, 'S', Item.stick}); 
        removeRecipe(new ItemStack(Item.hoeDiamond, 1), new Object[] {"DD", " S", " S", 'D', Item.diamond, 'S', Item.stick}); 
      }
    } else if (removeDiamondTools) {
      removeDiamondTools = false;
      ModLoader.addRecipe(new ItemStack(Item.swordDiamond, 1), new Object[] {"D", "D", "S", 'D', Item.diamond, 'S', Item.stick}); 
      ModLoader.addRecipe(new ItemStack(Item.shovelDiamond, 1), new Object[] {"D", "S", "S", 'D', Item.diamond, 'S', Item.stick}); 
      ModLoader.addRecipe(new ItemStack(Item.pickaxeDiamond, 1), new Object[] {"DDD", " S ", " S ", 'D', Item.diamond, 'S', Item.stick}); 
      ModLoader.addRecipe(new ItemStack(Item.axeDiamond, 1), new Object[] {"DD", "DS", " S", 'D', Item.diamond, 'S', Item.stick}); 
      ModLoader.addRecipe(new ItemStack(Item.hoeDiamond, 1), new Object[] {"DD", " S", " S", 'D', Item.diamond, 'S', Item.stick}); 
    }
    // Diamond Armor
    if (!optionsRecipes.getToggleValue("Diamond Armor")) {
      if (!removeDiamondArmor) {
        removeDiamondArmor = true;
        removeRecipe(new ItemStack(Item.helmetDiamond, 1), new Object[] {"DDD", "D D", 'D', Item.diamond}); 
        removeRecipe(new ItemStack(Item.plateDiamond, 1), new Object[] {"D D", "DDD", "DDD", 'D', Item.diamond}); 
        removeRecipe(new ItemStack(Item.legsDiamond, 1), new Object[] {"DDD", "D D", "D D", 'D', Item.diamond}); 
        removeRecipe(new ItemStack(Item.bootsDiamond, 1), new Object[] {"D D", "D D", 'D', Item.diamond}); 
      }
    } else if (removeDiamondArmor) {
      removeDiamondArmor = false;
      ModLoader.addRecipe(new ItemStack(Item.helmetDiamond, 1), new Object[] {"DDD", "D D", 'D', Item.diamond}); 
      ModLoader.addRecipe(new ItemStack(Item.plateDiamond, 1), new Object[] {"D D", "DDD", "DDD", 'D', Item.diamond}); 
      ModLoader.addRecipe(new ItemStack(Item.legsDiamond, 1), new Object[] {"DDD", "D D", "D D", 'D', Item.diamond}); 
      ModLoader.addRecipe(new ItemStack(Item.bootsDiamond, 1), new Object[] {"D D", "D D", 'D', Item.diamond}); 
    }
    // Wood Axes
    if (!optionsRecipes.getToggleValue("Wood Axes")) {
      if (!removeWoodAxes) {
        removeWoodAxes = true;
        removeRecipe(new ItemStack(Item.pickaxeWood, 1), new Object[] {"WWW", " S ", " S ", 'W', Block.planks, 'S', Item.stick}); 
        removeRecipe(new ItemStack(Item.axeWood, 1), new Object[] {"WW", "WS", " S", 'W', Block.planks, 'S', Item.stick}); 
      }
    } else if (removeWoodAxes) {
      removeWoodAxes = false;
      ModLoader.addRecipe(new ItemStack(Item.pickaxeWood, 1), new Object[] {"WWW", " S ", " S ", 'W', Block.planks, 'S', Item.stick}); 
      ModLoader.addRecipe(new ItemStack(Item.axeWood, 1), new Object[] {"WW", "WS", " S", 'W', Block.planks, 'S', Item.stick}); 
    }
    // Cobblestone Tools
    if (!optionsRecipes.getToggleValue("Cobblestone Tools")) {
      if (!removeCobblestoneTools) {
        removeCobblestoneTools = true;
        removeRecipe(new ItemStack(Item.swordStone, 1), new Object[] {"C", "C", "S", 'C', Block.cobblestone, 'S', Item.stick}); 
        removeRecipe(new ItemStack(Item.shovelStone, 1), new Object[] {"C", "S", "S", 'C', Block.cobblestone, 'S', Item.stick}); 
        removeRecipe(new ItemStack(Item.pickaxeStone, 1), new Object[] {"CCC", " S ", " S ", 'C', Block.cobblestone, 'S', Item.stick}); 
        removeRecipe(new ItemStack(Item.axeStone, 1), new Object[] {"CC", "CS", " S", 'C', Block.cobblestone, 'S', Item.stick}); 
        removeRecipe(new ItemStack(Item.hoeStone, 1), new Object[] {"CC", " S", " S", 'C', Block.cobblestone, 'S', Item.stick}); 
      }
    } else if (removeCobblestoneTools) {
      removeCobblestoneTools = false;
      ModLoader.addRecipe(new ItemStack(Item.swordStone, 1), new Object[] {"C", "C", "S", 'C', Block.cobblestone, 'S', Item.stick}); 
      ModLoader.addRecipe(new ItemStack(Item.shovelStone, 1), new Object[] {"C", "S", "S", 'C', Block.cobblestone, 'S', Item.stick}); 
      ModLoader.addRecipe(new ItemStack(Item.pickaxeStone, 1), new Object[] {"CCC", " S ", " S ", 'C', Block.cobblestone, 'S', Item.stick}); 
      ModLoader.addRecipe(new ItemStack(Item.axeStone, 1), new Object[] {"CC", "CS", " S", 'C', Block.cobblestone, 'S', Item.stick}); 
      ModLoader.addRecipe(new ItemStack(Item.hoeStone, 1), new Object[] {"CC", " S", " S", 'C', Block.cobblestone, 'S', Item.stick}); 
    }
    // TNT
    if (optionsRecipes.getToggleValue("TNT")) {
      if (!removeTNT) {
        removeTNT = true;
        removeRecipe(new ItemStack(Block.tnt, 1), new Object[] {"X#X", "#X#", "X#X", 'X', Item.gunpowder, '#', Block.sand});
      }
    } else if (removeTNT) {
      removeTNT = false;
      ModLoader.addRecipe(new ItemStack(Block.tnt, 1), new Object[] {"X#X", "#X#", "X#X", 'X', Item.gunpowder, '#', Block.sand});
    }
    // Craft Flint
    if (optionsRecipes.getToggleValue("Craft Flint")) {
      if (!craftFlint) {
        craftFlint = true;
        ModLoader.addRecipe(new ItemStack(Item.flint, 1), new Object[] {"#", '#', Block.gravel});
      }
    } else if (craftFlint) {
      craftFlint = false;
      removeRecipe(new ItemStack(Item.flint, 1), new Object[] {"#", '#', Block.gravel});
    }
    // Craft Cobblestone
    if (optionsRecipes.getToggleValue("Craft Cobblestone")) {
      if (!craftCobblestone) {
        craftCobblestone = true;
        ModLoader.addRecipe(new ItemStack(Block.cobblestone, 2), new Object[] {"#c", "c#", '#', Block.gravel, 'c', Item.clay});
      }
    } else if (craftCobblestone) {
      craftCobblestone = false;
      removeRecipe(new ItemStack(Block.cobblestone, 2), new Object[] {"#c", "c#", '#', Block.gravel, 'c', Item.clay});
    }
    // Flint Tools
    if (optionsRecipes.getToggleValue("Flint Tools")) {
      if (!flintTools) {
        flintTools = true;
        ModLoader.addRecipe(new ItemStack(spearFlint, 1), new Object[] {"F", "S", "S", 'F', Item.flint, 'S', Item.stick}); 
        ModLoader.addRecipe(new ItemStack(pickaxeFlint, 1), new Object[] {"FFF", " S ", " S ", 'F', Item.flint, 'S', Item.stick}); 
        ModLoader.addRecipe(new ItemStack(axeFlint, 1), new Object[] {"FF", "FS", " S", 'F', Item.flint, 'S', Item.stick}); 
        ModLoader.addRecipe(new ItemStack(hoeFlint, 1), new Object[] {"FF", " S", " S", 'F', Item.flint, 'S', Item.stick});
      }
    } else if (flintTools) {
      flintTools = false;
      removeRecipe(new ItemStack(spearFlint, 1), new Object[] {"F", "S", "S", 'F', Item.flint, 'S', Item.stick}); 
      removeRecipe(new ItemStack(pickaxeFlint, 1), new Object[] {"FFF", " S ", " S ", 'F', Item.flint, 'S', Item.stick}); 
      removeRecipe(new ItemStack(axeFlint, 1), new Object[] {"FF", "FS", " S", 'F', Item.flint, 'S', Item.stick}); 
      removeRecipe(new ItemStack(hoeFlint, 1), new Object[] {"FF", " S", " S", 'F', Item.flint, 'S', Item.stick});
    }
    // Extended 2x2 Crafting
	  if (optionsRecipes.getToggleValue("Extended 2x2 Crafting")) {
      if (!extendedCrafting) {
        extendedCrafting = true;
        ModLoader.addRecipe(new ItemStack(Item.stick, 2), new Object[] {"#", "#", '#', Block.sapling}); 
        ModLoader.addRecipe(new ItemStack(Item.stick, 2), new Object[] {"#", "#", '#', Block.cactus}); 
        ModLoader.addRecipe(new ItemStack(Item.stick, 2), new Object[] {"/", "/", '/', Item.bone});
        ModLoader.addRecipe(new ItemStack(Item.silk, 2), new Object[] {"#", '#', Block.cloth});
        if (optionsRecipes.getToggleValue("Flint Tools")) {
          ModLoader.addRecipe(new ItemStack(hatchetFlint, 1), new Object[] {"FS", "S ", 'F', Item.flint, 'S', Item.stick});
        }
    	  if (optionsRecipes.getToggleValue("Wood Axes")) {
          ModLoader.addRecipe(new ItemStack(hatchetWood, 1), new Object[] {"WS", "S ", 'W', Block.planks, 'S', Item.stick}); 
        } 
    	  if (optionsRecipes.getToggleValue("Cobblestone Tools")) {
          ModLoader.addRecipe(new ItemStack(hatchetStone, 1), new Object[] {"CS", "S ", 'C', Block.cobblestone, 'S', Item.stick}); 
        } 
        ModLoader.addRecipe(new ItemStack(hatchetIron, 1), new Object[] {"IS", "S ", 'I', Item.ingotIron, 'S', Item.stick}); 
    	  if (optionsRecipes.getToggleValue("Diamond Tools")) {
          ModLoader.addRecipe(new ItemStack(hatchetDiamond, 1), new Object[] {"DS", "S ", 'D', Item.diamond, 'S', Item.stick}); 
        } 
        ModLoader.addRecipe(new ItemStack(hatchetGold, 1), new Object[] {"GS", "S ", 'G', Item.ingotGold, 'S', Item.stick}); 
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
      removeRecipe(new ItemStack(Item.stick, 1), new Object[] {"#", "#", '#', Block.sapling}); 
      removeRecipe(new ItemStack(Item.stick, 2), new Object[] {"#", "#", '#', Block.cactus}); 
      removeRecipe(new ItemStack(Item.stick, 2), new Object[] {"/", "/", '/', Item.bone});
      removeRecipe(new ItemStack(Item.silk, 2), new Object[] {"#", '#', Block.cloth});
      removeRecipe(new ItemStack(hatchetFlint, 1), new Object[] {"FS", "S ", 'F', Item.flint, 'S', Item.stick});
      removeRecipe(new ItemStack(hatchetWood, 1), new Object[] {"WS", "S ", 'W', Block.planks, 'S', Item.stick}); 
      removeRecipe(new ItemStack(hatchetStone, 1), new Object[] {"CS", "S ", 'C', Block.cobblestone, 'S', Item.stick}); 
      removeRecipe(new ItemStack(hatchetIron, 1), new Object[] {"IS", "S ", 'I', Item.ingotIron, 'S', Item.stick}); 
      removeRecipe(new ItemStack(hatchetDiamond, 1), new Object[] {"DS", "S ", 'D', Item.diamond, 'S', Item.stick}); 
      removeRecipe(new ItemStack(hatchetGold, 1), new Object[] {"GS", "S ", 'G', Item.ingotGold, 'S', Item.stick}); 
    }
  }  
  
  public static void processSpawns() {
    boolean allowHostile = optionsHostileSpawns.getToggleValue("Allow Hostile Spawns");
    boolean allowBlaze = optionsHostileSpawns.getToggleValue("Blaze") && allowHostile;  
    boolean allowCaveSpider = optionsHostileSpawns.getToggleValue("Cave Spider") && allowHostile;  
    boolean allowCreeper = optionsHostileSpawns.getToggleValue("Creeper") && allowHostile;  
    boolean allowDragon = optionsHostileSpawns.getToggleValue("Dragon") && allowHostile;  
    boolean allowEnderman = optionsHostileSpawns.getToggleValue("Enderman") && allowHostile;  
    boolean allowGhast = optionsHostileSpawns.getToggleValue("Ghast") && allowHostile;  
    boolean allowGiantZombie = optionsHostileSpawns.getToggleValue("Giant Zombie") && allowHostile;  
    boolean allowMagmaCube = optionsHostileSpawns.getToggleValue("Magma Cube") && allowHostile;  
    boolean allowPigZombie = optionsHostileSpawns.getToggleValue("Pig Zombie") && allowHostile;  
    boolean allowSilverfish = optionsHostileSpawns.getToggleValue("Silverfish") && allowHostile;  
    boolean allowSkeleton = optionsHostileSpawns.getToggleValue("Skeleton") && allowHostile;  
    boolean allowSlime = optionsHostileSpawns.getToggleValue("Slime") && allowHostile;  
    boolean allowSpider = optionsHostileSpawns.getToggleValue("Spider") && allowHostile;  
    boolean allowWitch = optionsHostileSpawns.getToggleValue("Witch") && allowHostile;  
    boolean allowWither = optionsHostileSpawns.getToggleValue("Wither") && allowHostile;  
    boolean allowZombie = optionsHostileSpawns.getToggleValue("Zombie") && allowHostile;  
    boolean allowPeaceful = optionsPeacefulSpawns.getToggleValue("Allow Peaceful Spawns"); 
    boolean allowBats = optionsPeacefulSpawns.getToggleValue("Bats") && allowPeaceful; 
    boolean allowChicken = optionsPeacefulSpawns.getToggleValue("Chicken") && allowPeaceful; 
    boolean allowCow = optionsPeacefulSpawns.getToggleValue("Cow") && allowPeaceful; 
    boolean allowIronGolem = optionsPeacefulSpawns.getToggleValue("Iron Golem") && allowPeaceful; 
    boolean allowMooshroom = optionsPeacefulSpawns.getToggleValue("Mooshroom") && allowPeaceful; 
    boolean allowOcelot = optionsPeacefulSpawns.getToggleValue("Ocelot/Cat") && allowPeaceful; 
    boolean allowPig = optionsPeacefulSpawns.getToggleValue("Pig") && allowPeaceful; 
    boolean allowSheep = optionsPeacefulSpawns.getToggleValue("Sheep") && allowPeaceful; 
    boolean allowSnowman = optionsPeacefulSpawns.getToggleValue("Snowman") && allowPeaceful; 
    boolean allowWolf = optionsPeacefulSpawns.getToggleValue("Wolf") && allowPeaceful; 
    boolean allowSquid = optionsPeacefulSpawns.getToggleValue("Squid") && allowPeaceful; 
    boolean allowNPCs = optionsPeacefulSpawns.getToggleValue("NPCs") && allowPeaceful;
      
    // XP Orbs
    if (!optionsSpecial.getToggleValue("XP")) {
      for (int idx=0; idx<mc.theWorld.loadedEntityList.size(); idx++) {
        Entity ent = (Entity)mc.theWorld.loadedEntityList.get(idx);
        if ((ent!=null) && (ent instanceof EntityXPOrb)) {
          ((EntityXPOrb)ent).setDead();
        }
      }
      mc.thePlayer.experience = 0;
      mc.thePlayer.experienceLevel = 0;
      if (mc.thePlayer.worldObj.isRemote) {
        for (int idx=0; idx<mc.getIntegratedServer().getConfigurationManager().playerEntityList.size(); idx++) {
          ((EntityPlayerMP)mc.getIntegratedServer().getConfigurationManager().playerEntityList.get(idx)).experience = 0;
          ((EntityPlayerMP)mc.getIntegratedServer().getConfigurationManager().playerEntityList.get(idx)).experienceLevel = 0;
        }
      }
    }
    // Hostile & Peaceful Spawn overrides
    mc.theWorld.setAllowedSpawnTypes(allowHostile, allowPeaceful);
    if (!allowHostile) {
      killAll(EntityBlaze.class, EnumCreatureType.monster);
      killAll(EntityCaveSpider.class, EnumCreatureType.monster);
      killAll(EntityCreeper.class, EnumCreatureType.monster);
      killAll(EntityDragon.class, EnumCreatureType.monster);
      killAll(EntityEnderman.class, EnumCreatureType.monster);
      killAll(EntityGhast.class, EnumCreatureType.monster);
      killAll(EntityGiantZombie.class, EnumCreatureType.monster);
      killAll(EntityMagmaCube.class, EnumCreatureType.monster);
      killAll(EntityPigZombie.class, EnumCreatureType.monster);
      killAll(EntitySilverfish.class, EnumCreatureType.monster);
      killAll(EntitySkeleton.class, EnumCreatureType.monster);
      killAll(EntitySlime.class, EnumCreatureType.monster);
      killAll(EntitySpider.class, EnumCreatureType.monster);
      killAll(EntityWitch.class, EnumCreatureType.monster);
      killAll(EntityWither.class, EnumCreatureType.monster);
      killAll(EntityZombie.class, EnumCreatureType.monster);
    }    
    if (!allowPeaceful) {
      killAll(EntityVillager.class, EnumCreatureType.creature); // Enum is a dummy value in this case.
      killAll(EntityBat.class, EnumCreatureType.ambient);
      killAll(EntityChicken.class, EnumCreatureType.creature);
      killAll(EntityCow.class, EnumCreatureType.creature);
      killAll(EntityIronGolem.class, EnumCreatureType.creature);
      killAll(EntityMooshroom.class, EnumCreatureType.creature);
      killAll(EntityOcelot.class, EnumCreatureType.creature);
      killAll(EntityPig.class, EnumCreatureType.creature);
      killAll(EntitySheep.class, EnumCreatureType.creature);
      killAll(EntitySnowman.class, EnumCreatureType.creature);
      killAll(EntityWolf.class, EnumCreatureType.creature);
      killAll(EntitySquid.class, EnumCreatureType.waterCreature);
    }
    // NPCs
    mc.getIntegratedServer().setCanSpawnNPCs(allowNPCs);
    if (!allowNPCs) { 
      killAll(EntityVillager.class, EnumCreatureType.creature); // Enum is a dummy value in this case.
    }
    if (!allowHostile && !allowPeaceful) {
      return;
    }
    // Set peaceful mob adjustments
    peacefulDropBones = optionsPeacefulSpawns.getToggleValue("Drop bones");
    peacefulAdjustDrops = optionsPeacefulSpawns.getToggleValue("Adjust drops");
    // Set peaceful mob spawns
    if (allowPeaceful) {
      if (allowBats) {
        ModLoader.addSpawn(EntityBat.class, 10, 8, 8, EnumCreatureType.ambient);
      } else {
        killAll(EntityBat.class, EnumCreatureType.ambient);
      }
      if (allowChicken) {
        ModLoader.addSpawn(EntityChicken.class, 10, 4, 4, EnumCreatureType.creature);
      } else {
        killAll(EntityChicken.class, EnumCreatureType.creature);
      }
      if (allowCow) {
        ModLoader.addSpawn(MMWEntityCow.class, 8, 4, 4, EnumCreatureType.creature);
      } else {
        killAll(EntityCow.class, EnumCreatureType.creature);
        killAll(MMWEntityCow.class, EnumCreatureType.creature);
      }
      if (!allowIronGolem) {
        killAll(EntityIronGolem.class, EnumCreatureType.creature);
      }
      if (allowMooshroom) {
        ModLoader.addSpawn(EntityMooshroom.class, 8, 4, 8, EnumCreatureType.creature, new BiomeGenBase[] {BiomeGenBase.mushroomIsland});
      } else {
        killAll(EntityMooshroom.class, EnumCreatureType.creature);
      }
      if (allowOcelot) {
        ModLoader.addSpawn(EntityOcelot.class, 2, 1, 1, EnumCreatureType.creature, new BiomeGenBase[] {BiomeGenBase.jungle});
      } else {
        killAll(EntityOcelot.class, EnumCreatureType.creature);
      }
      if (allowPig) {
        ModLoader.addSpawn(MMWEntityPig.class, 10, 4, 4, EnumCreatureType.creature);
      } else {
        killAll(EntityPig.class, EnumCreatureType.creature);
        killAll(MMWEntityPig.class, EnumCreatureType.creature);
      } 
      if (allowSheep) {
        ModLoader.addSpawn(MMWEntitySheep.class, 12, 4, 4, EnumCreatureType.creature);
      } else {
        killAll(EntitySheep.class, EnumCreatureType.creature);
        killAll(MMWEntitySheep.class, EnumCreatureType.creature);
      }
      if (!allowSnowman) {
        killAll(EntitySnowman.class, EnumCreatureType.creature);
      }
      if (allowWolf) {
        ModLoader.addSpawn(EntityWolf.class, 5, 4, 4, EnumCreatureType.creature, new BiomeGenBase[] {BiomeGenBase.forest});
        ModLoader.addSpawn(EntityWolf.class, 8, 4, 4, EnumCreatureType.creature, new BiomeGenBase[] {BiomeGenBase.taiga});
      } else {
        killAll(EntityWolf.class, EnumCreatureType.creature);
      }
      if (allowSquid) {
        ModLoader.addSpawn(EntitySquid.class, 10, 4, 4, EnumCreatureType.waterCreature);
      } else {
        killAll(EntitySquid.class, EnumCreatureType.waterCreature);
      }
    }
    if (allowHostile) {
      if (!allowBlaze) {
        killAll(EntityBlaze.class, EnumCreatureType.monster);
      }
      if (!allowCaveSpider) {
        killAll(EntityCaveSpider.class, EnumCreatureType.monster);
      }
      if (allowCreeper) {
        ModLoader.addSpawn(EntityCreeper.class, 10, 4, 4, EnumCreatureType.monster);
      } else {
        killAll(EntityCreeper.class, EnumCreatureType.monster);
      }
      if (!allowDragon) {
        killAll(EntityDragon.class, EnumCreatureType.monster);
      }
      if (allowEnderman) {
        ModLoader.addSpawn(EntityEnderman.class, 1, 1, 4, EnumCreatureType.monster);
      } else {
        killAll(EntityEnderman.class, EnumCreatureType.monster);
      }
      if (allowGhast) {
        ModLoader.addSpawn(EntityGhast.class, 50, 4, 4, EnumCreatureType.monster, new BiomeGenBase[] {BiomeGenBase.hell});
      } else {
        killAll(EntityGhast.class, EnumCreatureType.monster);
      }
      if (!allowGiantZombie) {
        killAll(EntityGiantZombie.class, EnumCreatureType.monster);
      }
      if (allowMagmaCube) {
        ModLoader.addSpawn(EntityMagmaCube.class, 1, 4, 4, EnumCreatureType.monster, new BiomeGenBase[] {BiomeGenBase.hell});
      } else {
        killAll(EntityMagmaCube.class, EnumCreatureType.monster);
      }
      if (allowPigZombie) {
        ModLoader.addSpawn(EntityPigZombie.class, 100, 4, 4, EnumCreatureType.monster, new BiomeGenBase[] {BiomeGenBase.hell});
      } else {
        killAll(EntityPigZombie.class, EnumCreatureType.monster);
      }
      if (!allowSilverfish) {
        killAll(EntitySilverfish.class, EnumCreatureType.monster);
      }
      if (allowSkeleton) {
        ModLoader.addSpawn(EntitySkeleton.class, 10, 4, 4, EnumCreatureType.monster);
      } else {
        killAll(EntitySkeleton.class, EnumCreatureType.monster);
      }
      if (allowSlime) {
        ModLoader.addSpawn(EntitySlime.class, 10, 4, 4, EnumCreatureType.monster);
      } else {
        killAll(EntitySlime.class, EnumCreatureType.monster);
      }
      if (allowSpider) {
        ModLoader.addSpawn(EntitySpider.class, 10, 4, 4, EnumCreatureType.monster);
      } else {
        killAll(EntitySpider.class, EnumCreatureType.monster);
      }
      if (!allowWitch) {
        killAll(EntityWitch.class, EnumCreatureType.monster);
      }
      if (!allowWither) {
        killAll(EntityWither.class, EnumCreatureType.monster);
      }
      if (allowZombie) {
        ModLoader.addSpawn(EntityZombie.class, 10, 4, 4, EnumCreatureType.monster);
      } else {
        killAll(EntityZombie.class, EnumCreatureType.monster);
      }
    }
  }

  public static void killAll(Class entityType, EnumCreatureType creatureType) {
    ModLoader.removeSpawn(entityType, creatureType);
    for (int idx=0; idx<mc.theWorld.loadedEntityList.size(); idx++) {
      Entity ent = (Entity)mc.theWorld.loadedEntityList.get(idx);
      if ((ent!=null) && (entityType.isInstance(ent))) {
        ((EntityLiving)ent).setDead();
      }
    }
  }
  
	public static void removeRecipe(int item) {
		Iterator<?> itr = CraftingManager.getInstance().getRecipeList().iterator();
		
		while (itr.hasNext()) {
			Object o = itr.next();
			if (o instanceof ShapedRecipes) {
				if (((ShapedRecipes) o).recipeOutputItemID == item) {
					itr.remove();
				}
			} else if (o instanceof ShapelessRecipes) {
				if (((ShapelessRecipes) o).getRecipeOutput().itemID == item) {
					itr.remove();
				}
			}
		}
	}

  public static void removeRecipe(ItemStack par1ItemStack, Object ... par2ArrayOfObj) {
    ShapedRecipes checkRecipe;
		Iterator<?> itr = CraftingManager.getInstance().getRecipeList().iterator();

    // Set recipe to check against
    String recipeString = "";
    int itemIndex = 0;
    int width = 0;
    int height = 0;
    int numRows;
    HashMap recipeTokens;

    // Build recipe string
    if (par2ArrayOfObj[itemIndex] instanceof String[]) {
      String[] recipeRows = (String[])((String[])par2ArrayOfObj[itemIndex++]);
      numRows = recipeRows.length;

      for (int idx=0; idx<numRows; idx++) {
        String row = recipeRows[idx];
        height++;
        width = row.length();
        recipeString = recipeString + row;
      }
    } else {
      while (par2ArrayOfObj[itemIndex] instanceof String) {
        String row = (String)par2ArrayOfObj[itemIndex++];
        height++;
        width = row.length();
        recipeString = recipeString + row;
      }
    }
    // Build hash of token to replace in recipe string
    for (recipeTokens = new HashMap(); itemIndex < par2ArrayOfObj.length; itemIndex += 2) {
      Character token = (Character)par2ArrayOfObj[itemIndex];
      ItemStack tokenItem = null;

      if (par2ArrayOfObj[itemIndex + 1] instanceof Item) {
        tokenItem = new ItemStack((Item)par2ArrayOfObj[itemIndex + 1]);
      } else if (par2ArrayOfObj[itemIndex + 1] instanceof Block) {
        tokenItem = new ItemStack((Block)par2ArrayOfObj[itemIndex + 1], 1, -1);
      } else if (par2ArrayOfObj[itemIndex + 1] instanceof ItemStack) {
        tokenItem = (ItemStack)par2ArrayOfObj[itemIndex + 1];
      }
      recipeTokens.put(token, tokenItem);
    }
    // Build recipe array of ItemStacks
    ItemStack[] recipeStacks = new ItemStack[width * height];

    for (int idx=0; idx<width * height; idx++) {
      char token = recipeString.charAt(idx);
      if (recipeTokens.containsKey(Character.valueOf(token))) {
        recipeStacks[idx] = ((ItemStack)recipeTokens.get(Character.valueOf(token))).copy();
      } else {
        recipeStacks[idx] = null;
      }
    }
    checkRecipe = new ShapedRecipes(width, height, recipeStacks, par1ItemStack);
		// Look through list for recipe and remove if found
		while (itr.hasNext()) {
			Object o = itr.next();
			if (o instanceof ShapedRecipes) {
        if (recipesEqual((ShapedRecipes)o, checkRecipe)) {
					itr.remove();
        }
			}
		}
  }

  public static void removeShapelessRecipe(ItemStack par1ItemStack, Object ... par2ArrayOfObj) {
    ShapelessRecipes checkRecipe;
		Iterator<?> itr = CraftingManager.getInstance().getRecipeList().iterator();
		
    // Set recipe to check against
    ArrayList recipeItems = new ArrayList();
    Object[] items = par2ArrayOfObj;

    for (int idx=0; idx<par2ArrayOfObj.length; idx++) {
      if (items[idx] instanceof ItemStack) {
        recipeItems.add(((ItemStack)items[idx]).copy());
      } else if (items[idx] instanceof Item) {
        recipeItems.add(new ItemStack((Item)items[idx]));
      } else if (items[idx] instanceof Block) {
        recipeItems.add(new ItemStack((Block)items[idx]));
      }
    }
    checkRecipe = new ShapelessRecipes(par1ItemStack, recipeItems);
		// Look through list for recipe and remove if found
		while (itr.hasNext()) {
			Object o = itr.next();
			if (o instanceof ShapelessRecipes) {
        if (recipesEqual((ShapelessRecipes)o, checkRecipe)) {
					itr.remove();
        }
			}
		}
    
  }

  public static boolean recipesEqual(ShapedRecipes r1, ShapedRecipes r2) {
    // Different output, quick exit
    if (!ItemStack.areItemStacksEqual(r1.getRecipeOutput(), r2.getRecipeOutput())) {
      return false;
    }
    try {
      ItemStack[] recipe1Items = (ItemStack[])ModLoader.getPrivateValue(ShapedRecipes.class, r1, (isObfuscated) ? "d" : "recipeItems");
      ItemStack[] recipe2Items = (ItemStack[])ModLoader.getPrivateValue(ShapedRecipes.class, r2, (isObfuscated) ? "d" : "recipeItems");
      if (recipe1Items.length != recipe2Items.length) {
        return false;
      }
      for (int idx=0; idx<recipe1Items.length; idx++) {
        if (!ItemStack.areItemStacksEqual(recipe1Items[idx], recipe2Items[idx])) {
          return false;
        }
      }
    } catch (Exception error) {
      return false;
    }
    return true;
  }
  
  public static boolean recipesEqual(ShapelessRecipes r1, ShapelessRecipes r2) {
    // Different output, quick exit
    if (!ItemStack.areItemStacksEqual(r1.getRecipeOutput(), r2.getRecipeOutput())) {
      return false;
    }
    try {
      List recipe1Items = (List)ModLoader.getPrivateValue(ShapelessRecipes.class, r1, (isObfuscated) ? "b" : "recipeItems");
      List recipe2Items = (List)ModLoader.getPrivateValue(ShapelessRecipes.class, r2, (isObfuscated) ? "b" : "recipeItems");
      if (recipe1Items.size() != recipe2Items.size()) {
        return false;
      }
      for (int idx=0; idx<recipe1Items.size(); idx++) {
        if (!ItemStack.areItemStacksEqual((ItemStack)recipe1Items.get(idx), (ItemStack)recipe2Items.get(idx))) {
          return false;
        }
      }
    } catch (Exception error) {
      return false;
    }
    return true;
  }

  private void initModOptionsAPI() {
    // Create option screens
    optionsMain = new ModOptions("Minecraft My Way");
    ModOptionsAPI.addMod(optionsMain);
    optionsRecipes = new ModOptions("Minecraft My Way - Recipes");
    optionsMain.addSubOptions(optionsRecipes);
    optionsHostileSpawns = new ModOptions("Minecraft My Way - Hostile Spawns");
    optionsMain.addSubOptions(optionsHostileSpawns);
    optionsPeacefulSpawns = new ModOptions("Minecraft My Way - Peaceful Spawns");
    optionsMain.addSubOptions(optionsPeacefulSpawns);
    optionsSpecial = new ModOptions("Minecraft My Way - Special");
    optionsMain.addSubOptions(optionsSpecial);
    optionsWorldGen = new ModOptions("Minecraft My Way - Default World Gen");
    optionsMain.addSubOptions(optionsWorldGen);
    // Add options to Hostile Spawn screen
	  optionsHostileSpawns.addToggle("Zombie").setValue(true);
	  optionsHostileSpawns.addToggle("Wither").setValue(true);
	  optionsHostileSpawns.addToggle("Witch").setValue(true);
	  optionsHostileSpawns.addToggle("Spider").setValue(true);
	  optionsHostileSpawns.addToggle("Slime").setValue(true);
	  optionsHostileSpawns.addToggle("Skeleton").setValue(true);
	  optionsHostileSpawns.addToggle("Silverfish").setValue(true);
	  optionsHostileSpawns.addToggle("Pig Zombie").setValue(true);
	  optionsHostileSpawns.addToggle("Magma Cube").setValue(true);
	  optionsHostileSpawns.addToggle("Giant Zombie").setValue(true);
	  optionsHostileSpawns.addToggle("Ghast").setValue(true);
	  optionsHostileSpawns.addToggle("Enderman").setValue(true);
	  optionsHostileSpawns.addToggle("Dragon").setValue(true);
	  optionsHostileSpawns.addToggle("Creeper").setValue(true);
	  optionsHostileSpawns.addToggle("Cave Spider").setValue(true);
	  optionsHostileSpawns.addToggle("Blaze").setValue(true);
	  optionsHostileSpawns.addToggle("Allow Hostile Spawns").setValue(true);
    // Add options to Peaceful Spawn screen
	  optionsPeacefulSpawns.addToggle("Wolf").setValue(true);
	  optionsPeacefulSpawns.addToggle("Squid").setValue(true);
	  optionsPeacefulSpawns.addToggle("Snowman").setValue(true);
	  optionsPeacefulSpawns.addToggle("Sheep").setValue(true);
	  optionsPeacefulSpawns.addToggle("Pig").setValue(true);
	  optionsPeacefulSpawns.addToggle("Ocelot/Cat").setValue(true);
	  optionsPeacefulSpawns.addToggle("NPCs").setValue(true);
	  optionsPeacefulSpawns.addToggle("Mooshroom").setValue(true);
	  optionsPeacefulSpawns.addToggle("Iron Golem").setValue(true);
	  optionsPeacefulSpawns.addToggle("Cow").setValue(true);
	  optionsPeacefulSpawns.addToggle("Chicken").setValue(true);
	  optionsPeacefulSpawns.addToggle("Bats").setValue(true);
    optionsPeacefulSpawns.addToggle("Drop bones").setValue(false);
    optionsPeacefulSpawns.addToggle("Adjust drops").setValue(false);
	  optionsPeacefulSpawns.addToggle("Allow Peaceful Spawns").setValue(true);
    // Add options to Recipe screen
	  optionsRecipes.addToggle("Wood Axes").setValue(true);
	  optionsRecipes.addToggle("TNT").setValue(true);
	  optionsRecipes.addToggle("Flint Tools").setValue(false);
	  optionsRecipes.addToggle("Extended 2x2 Crafting").setValue(false);
	  optionsRecipes.addToggle("Diamond Armor").setValue(true);
	  optionsRecipes.addToggle("Craft Flint").setValue(false);
	  optionsRecipes.addToggle("Craft Cobblestone").setValue(false);
	  optionsRecipes.addToggle("Cobblestone Tools").setValue(true);
    optionsRecipes.addToggle("Diamond Tools").setValue(true);
    optionsRecipes.addTextOption("Base Item ID", "12120", 5).setWide(true);
    // Add options to Special screen
	  optionsSpecial.addToggle("XP").setValue(true);
    optionsSpecial.addMultiOption("Time Flow", new String[]{"Normal", "Always Day", "Always Night", "Rapid", "Slow", "Single Day"});
	  optionsSpecial.addToggle("Stone Drops Gravel").setValue(false);
    optionsSpecial.addToggle("Sprinting").setValue(true);
    optionsSpecial.addToggle("Hunger").setValue(true);
	  optionsSpecial.addToggle("Gravel/Sand Gravity").setValue(true);
    optionsSpecial.addToggle("Exhaustion").setValue(true);
	  optionsSpecial.addToggle("Bonemeal Instagrowth").setValue(true);
    optionsSpecial.addToggle("Auto Healing").setValue(true);
    // Add options to World Gen screen
	  optionsWorldGen.addToggle("Villages").setValue(true);
	  optionsWorldGen.addToggle("Strongholds").setValue(true);
	  optionsWorldGen.addToggle("Scattered Features").setValue(true);
	  optionsWorldGen.addToggle("Ravines").setValue(true);
	  optionsWorldGen.addToggle("Mineshafts").setValue(true);
	  optionsWorldGen.addToggle("Caves").setValue(true);
	  // Load saved values over defaults (if any)
    optionsMain.loadValues();
    // Save current values
    optionsMain.save();
  }

  public String getName() {
    return Name;
  }
	
  public String getVersion() {
    return Version;
  }

  public mod_MyWay() {
    isObfuscated = false;
    try {
      Class.forName("net.minecraft.src.MathHelper", false, this.getClass().getClassLoader());
    } catch (ClassNotFoundException e) {
      isObfuscated = true;
    }  
  }
}
