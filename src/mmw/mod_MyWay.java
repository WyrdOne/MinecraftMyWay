package net.minecraft.src;

import java.util.Iterator;
import net.minecraft.client.Minecraft;
import moapi.ModOptions;
import moapi.ModOptionsAPI;
import java.io.PrintWriter;

public class mod_MyWay extends BaseMod {
  // Copyright/license info
  private static final String Name = "Minecraft My Way";
  private static final String Version = "0.2 (For use with Minecraft 1.3.2)";
	private static final String Copyright = "All original code and images (C) 2011-2012, Jonathan \"Wyrd\" Brazell";
	private static final String License = "This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 3.0 Unported License.";
  private static final String ModLoaderVersion = "ModLoader 1.3.2";
  // Options
  private static ModOptions optionsMain;
  private static ModOptions optionsHostileSpawns;
  private static ModOptions optionsPeacefulSpawns;
  private static ModOptions optionsRecipes;
  private static ModOptions optionsSpecial;
  // Save states
	private static boolean removeDiamondTools = false;
	private static boolean removeDiamondArmor = false;
	private static boolean removeWoodAxes = false;
	private static boolean removeCobblestoneTools = false;
	private static boolean removeTNT = false;
	private static boolean craftFlint = false;
	private static boolean craftCobblestone = false;
	private static boolean flintTools = false;
  // Other variables
  private static Minecraft mc = ModLoader.getMinecraftInstance();
  private static Block saveStone = Block.stone;
  private static Block mmwStone;
  private static Item saveItemDye = Item.dyePowder;
  private static int tickCounter = 0;
      	
  public void load() {
    // Check proper version of ModLoader to stop version mismatch errors
    if (ModLoader.VERSION != ModLoaderVersion) {
      System.out.println("MinecraftMyWay/ModLoader version mismatch.");
      return;
    }
    // Set up options menu for mod options
    initModOptionsAPI();

    // Replace Stone
    Block.blocksList[Block.stone.blockID] = null;
    mmwStone = (new MMWBlockStone(1, 1)).setHardness(1.5F).setResistance(10.0F).setStepSound(Block.soundStoneFootstep).setBlockName("stone");
    ModLoader.registerBlock(mmwStone);

    // Replace dye to allow bonemeal to be disabled.
    Item.itemsList[Item.dyePowder.shiftedIndex] = null;
    Item.dyePowder = (new MMWItemDye(95)).setIconCoord(14, 4).setItemName("dyePowder");

    // Check for option changes every tick
    ModLoader.setInGameHook(this, true, true);
	}

  public boolean onTickInGame(float f, Minecraft paramMinecraft) {
    // Don't need to check the options every tick.
    tickCounter++;
    if ((tickCounter & 0x001f)!=0x0001)
      return true;
      
    // Bonemeal
    ((MMWItemDye)Item.dyePowder).disableBonemeal = !optionsSpecial.getToggleValue("Bonemeal Instagrowth");
    // Gravel/Sand
    ((BlockSand)Block.sand).gravityWorks = optionsSpecial.getToggleValue("Gravel/Sand Gravity"); 
    // Stone
    ((MMWBlockStone)Block.blocksList[Block.stone.blockID]).drop = optionsSpecial.getToggleValue("Stone Drops Gravel") ? Block.gravel.blockID : Block.cobblestone.blockID;
   
    // Diamond Tools
    if (!optionsRecipes.getToggleValue("Diamond Tools")) {
      if (!removeDiamondTools) {
        removeDiamondTools = true;
        removeRecipe(Item.swordDiamond.shiftedIndex);
        removeRecipe(Item.shovelDiamond.shiftedIndex);
        removeRecipe(Item.pickaxeDiamond.shiftedIndex);
        removeRecipe(Item.axeDiamond.shiftedIndex);
        removeRecipe(Item.hoeDiamond.shiftedIndex);
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
        removeRecipe(Item.helmetDiamond.shiftedIndex);
        removeRecipe(Item.plateDiamond.shiftedIndex);
        removeRecipe(Item.legsDiamond.shiftedIndex);
        removeRecipe(Item.bootsDiamond.shiftedIndex); 
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
        removeRecipe(Item.pickaxeWood.shiftedIndex);
        removeRecipe(Item.axeWood.shiftedIndex);
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
        removeRecipe(Item.swordStone.shiftedIndex);
        removeRecipe(Item.shovelStone.shiftedIndex);
        removeRecipe(Item.pickaxeStone.shiftedIndex);
        removeRecipe(Item.axeStone.shiftedIndex);
        removeRecipe(Item.hoeStone.shiftedIndex);
      }
    } else if (removeCobblestoneTools) {
      removeCobblestoneTools = false;
      ModLoader.addRecipe(new ItemStack(Item.swordStone, 1), new Object[] {"C", "C", "C", 'C', Block.cobblestone, 'S', Item.stick}); 
      ModLoader.addRecipe(new ItemStack(Item.shovelStone, 1), new Object[] {"C", "S", "S", 'C', Block.cobblestone, 'S', Item.stick}); 
      ModLoader.addRecipe(new ItemStack(Item.pickaxeStone, 1), new Object[] {"CCC", " S ", " S ", 'C', Block.cobblestone, 'S', Item.stick}); 
      ModLoader.addRecipe(new ItemStack(Item.axeStone, 1), new Object[] {"CC", "CS", " S", 'C', Block.cobblestone, 'S', Item.stick}); 
      ModLoader.addRecipe(new ItemStack(Item.hoeStone, 1), new Object[] {"CC", " S", " S", 'C', Block.cobblestone, 'S', Item.stick}); 
    }
    // TNT
    if (optionsRecipes.getToggleValue("TNT")) {
      if (!removeTNT) {
        removeTNT = true;
        removeRecipe(Block.tnt.blockID);
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
      removeRecipe(Item.flint.shiftedIndex);
    }
    // Craft Cobblestone
    if (optionsRecipes.getToggleValue("Craft Cobblestone")) {
      if (!craftCobblestone) {
        craftCobblestone = true;
        ModLoader.addRecipe(new ItemStack(Block.cobblestone, 2), new Object[] {"#c", "c#", '#', Block.gravel, 'c', Item.clay});
      }
    } else if (craftCobblestone) {
      craftCobblestone = false;
      removeRecipe(Block.cobblestone.blockID);
    }
    // Flint Tools
    if (optionsRecipes.getToggleValue("Flint Tools")) {
      if (!flintTools) {
        flintTools = true;
        ModLoader.addRecipe(new ItemStack(Item.swordStone, 1), new Object[] {"C", "C", "C", 'C', Item.flint, 'S', Item.stick}); 
        ModLoader.addRecipe(new ItemStack(Item.shovelStone, 1), new Object[] {"C", "S", "S", 'C', Item.flint, 'S', Item.stick}); 
        ModLoader.addRecipe(new ItemStack(Item.pickaxeStone, 1), new Object[] {"CCC", " S ", " S ", 'C', Item.flint, 'S', Item.stick}); 
        ModLoader.addRecipe(new ItemStack(Item.axeStone, 1), new Object[] {"CC", "CS", " S", 'C', Item.flint, 'S', Item.stick}); 
        ModLoader.addRecipe(new ItemStack(Item.hoeStone, 1), new Object[] {"CC", " S", " S", 'C', Item.flint, 'S', Item.stick}); 
      }
    } else if (flintTools) {
      flintTools = false;
      removeRecipe(Item.swordStone.shiftedIndex);
      removeRecipe(Item.shovelStone.shiftedIndex);
      removeRecipe(Item.pickaxeStone.shiftedIndex);
      removeRecipe(Item.axeStone.shiftedIndex);
      removeRecipe(Item.hoeStone.shiftedIndex);
      if (!removeCobblestoneTools) {
        ModLoader.addRecipe(new ItemStack(Item.swordStone, 1), new Object[] {"C", "C", "C", 'C', Block.cobblestone, 'S', Item.stick}); 
        ModLoader.addRecipe(new ItemStack(Item.shovelStone, 1), new Object[] {"C", "S", "S", 'C', Block.cobblestone, 'S', Item.stick}); 
        ModLoader.addRecipe(new ItemStack(Item.pickaxeStone, 1), new Object[] {"CCC", " S ", " S ", 'C', Block.cobblestone, 'S', Item.stick}); 
        ModLoader.addRecipe(new ItemStack(Item.axeStone, 1), new Object[] {"CC", "CS", " S", 'C', Block.cobblestone, 'S', Item.stick}); 
        ModLoader.addRecipe(new ItemStack(Item.hoeStone, 1), new Object[] {"CC", " S", " S", 'C', Block.cobblestone, 'S', Item.stick}); 
      }
    }
    // Hostile & Peaceful Spawn overrides
    mc.theWorld.setAllowedSpawnTypes(optionsHostileSpawns.getToggleValue("Allow Hostile Spawns"), optionsPeacefulSpawns.getToggleValue("Allow Peaceful Spawns"));
    // NPCs 
    mc.getIntegratedServer().setSpawnNpcs(optionsPeacefulSpawns.getToggleValue("Allow Peaceful Spawns") && optionsPeacefulSpawns.getToggleValue("NPCs"));
    // Chicken
    if (optionsPeacefulSpawns.getToggleValue("Chicken")) {
      ModLoader.addSpawn(EntityChicken.class, 10, 4, 4, EnumCreatureType.creature);
    } else {
      ModLoader.removeSpawn(EntityChicken.class, EnumCreatureType.creature);
    }
    // Cow    
    if (optionsPeacefulSpawns.getToggleValue("Cow")) {
      ModLoader.addSpawn(EntityCow.class, 8, 4, 4, EnumCreatureType.creature);
    } else {
      ModLoader.removeSpawn(EntityCow.class, EnumCreatureType.creature);
    }
    // Mooshroom
    if (optionsPeacefulSpawns.getToggleValue("Mooshroom")) {
      ModLoader.addSpawn(EntityMooshroom.class, 8, 4, 8, EnumCreatureType.creature, new BiomeGenBase[] {BiomeGenBase.mushroomIsland});
    } else {
      ModLoader.removeSpawn(EntityMooshroom.class, EnumCreatureType.creature);
    }
    // Ocelot
    if (optionsPeacefulSpawns.getToggleValue("Ocelot/Cat")) {
      ModLoader.addSpawn(EntityOcelot.class, 2, 1, 1, EnumCreatureType.creature, new BiomeGenBase[] {BiomeGenBase.jungle});
    } else {
      ModLoader.removeSpawn(EntityOcelot.class, EnumCreatureType.creature);
    }
    // Pig    
    if (optionsPeacefulSpawns.getToggleValue("Pig")) {
      ModLoader.addSpawn(EntityPig.class, 10, 4, 4, EnumCreatureType.creature);
    } else {
      ModLoader.removeSpawn(EntityPig.class, EnumCreatureType.creature);
    } 
    // Sheep
    if (optionsPeacefulSpawns.getToggleValue("Sheep")) {
      ModLoader.addSpawn(EntitySheep.class, 12, 4, 4, EnumCreatureType.creature);
    } else {
      ModLoader.removeSpawn(EntitySheep.class, EnumCreatureType.creature);
    }
    // Wolf
    if (optionsPeacefulSpawns.getToggleValue("Wolf")) {
      ModLoader.addSpawn(EntityWolf.class, 5, 4, 4, EnumCreatureType.creature, new BiomeGenBase[] {BiomeGenBase.forest});
      ModLoader.addSpawn(EntityWolf.class, 8, 4, 4, EnumCreatureType.creature, new BiomeGenBase[] {BiomeGenBase.taiga});
    } else {
      ModLoader.removeSpawn(EntityWolf.class, EnumCreatureType.creature);
    }
    // Squid    
    if (optionsPeacefulSpawns.getToggleValue("Squid")) {
      ModLoader.addSpawn(EntitySquid.class, 10, 4, 4, EnumCreatureType.waterCreature);
    } else {
      ModLoader.removeSpawn(EntitySquid.class, EnumCreatureType.waterCreature);
    }
    // Creeper
    if (optionsHostileSpawns.getToggleValue("Creeper")) {
      ModLoader.addSpawn(EntityCreeper.class, 10, 4, 4, EnumCreatureType.monster);
    } else {
      ModLoader.removeSpawn(EntityCreeper.class, EnumCreatureType.monster);
    }
    // Enderman
    if (optionsHostileSpawns.getToggleValue("Enderman")) {
      ModLoader.addSpawn(EntityEnderman.class, 1, 1, 4, EnumCreatureType.monster);
    } else {
      ModLoader.removeSpawn(EntityEnderman.class, EnumCreatureType.monster);
    }
    // Ghast
    if (optionsHostileSpawns.getToggleValue("Ghast")) {
      ModLoader.addSpawn(EntityGhast.class, 50, 4, 4, EnumCreatureType.monster, new BiomeGenBase[] {BiomeGenBase.hell});
    } else {
      ModLoader.removeSpawn(EntityGhast.class, EnumCreatureType.monster);
    }
    // MagmaCube
    if (optionsHostileSpawns.getToggleValue("Magma Cube")) {
      ModLoader.addSpawn(EntityMagmaCube.class, 1, 4, 4, EnumCreatureType.monster, new BiomeGenBase[] {BiomeGenBase.hell});
    } else {
      ModLoader.removeSpawn(EntityMagmaCube.class, EnumCreatureType.monster);
    }
    // PigZombie
    if (optionsHostileSpawns.getToggleValue("Pig Zombie")) {
      ModLoader.addSpawn(EntityPigZombie.class, 100, 4, 4, EnumCreatureType.monster, new BiomeGenBase[] {BiomeGenBase.hell});
    } else {
      ModLoader.removeSpawn(EntityPigZombie.class, EnumCreatureType.monster);
    }
    // Skeleton    
    if (optionsHostileSpawns.getToggleValue("Skeleton")) {
      ModLoader.addSpawn(EntitySkeleton.class, 10, 4, 4, EnumCreatureType.monster);
    } else {
      ModLoader.removeSpawn(EntitySkeleton.class, EnumCreatureType.monster);
    }
    // Slime    
    if (optionsHostileSpawns.getToggleValue("Slime")) {
      ModLoader.addSpawn(EntitySlime.class, 10, 4, 4, EnumCreatureType.monster);
    } else {
      ModLoader.removeSpawn(EntitySlime.class, EnumCreatureType.monster);
    }
    // Spider    
    if (optionsHostileSpawns.getToggleValue("Spider")) {
      ModLoader.addSpawn(EntitySpider.class, 10, 4, 4, EnumCreatureType.monster);
    } else {
      ModLoader.removeSpawn(EntitySpider.class, EnumCreatureType.monster);
    }
    // Zombie    
    if (optionsHostileSpawns.getToggleValue("Zombie")) {
      ModLoader.addSpawn(EntityZombie.class, 10, 4, 4, EnumCreatureType.monster);
    } else {
      ModLoader.removeSpawn(EntityZombie.class, EnumCreatureType.monster);
    }
    return true;
  }
  
	public void removeRecipe(int item) {
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
    // Add options to Hostile Spawn screen
	  optionsHostileSpawns.addToggle("Zombie").setValue(true);
	  optionsHostileSpawns.addToggle("Spider").setValue(true);
	  optionsHostileSpawns.addToggle("Slime").setValue(true);
	  optionsHostileSpawns.addToggle("Skeleton").setValue(true);
	  optionsHostileSpawns.addToggle("Pig Zombie").setValue(true);
	  optionsHostileSpawns.addToggle("Magma Cube").setValue(true);
	  optionsHostileSpawns.addToggle("Ghast").setValue(true);
	  optionsHostileSpawns.addToggle("Enderman").setValue(true);
	  optionsHostileSpawns.addToggle("Creeper").setValue(true);
	  optionsHostileSpawns.addToggle("Allow Hostile Spawns").setValue(true);
    // Add options to Peaceful Spawn screen
	  optionsPeacefulSpawns.addToggle("Wolf").setValue(true);
	  optionsPeacefulSpawns.addToggle("Squid").setValue(true);
	  optionsPeacefulSpawns.addToggle("Sheep").setValue(true);
	  optionsPeacefulSpawns.addToggle("Pig").setValue(true);
	  optionsPeacefulSpawns.addToggle("Ocelot/Cat").setValue(true);
	  optionsPeacefulSpawns.addToggle("NPCs").setValue(true);
	  optionsPeacefulSpawns.addToggle("Mooshroom").setValue(true);
	  optionsPeacefulSpawns.addToggle("Cow").setValue(true);
	  optionsPeacefulSpawns.addToggle("Chicken").setValue(true);
	  optionsPeacefulSpawns.addToggle("Allow Peaceful Spawns").setValue(true);
    // Add options to Recipe screen
	  optionsRecipes.addToggle("Flint Tools").setValue(false);
	  optionsRecipes.addToggle("Craft Cobblestone").setValue(false);
	  optionsRecipes.addToggle("Craft Flint").setValue(false);
	  optionsRecipes.addToggle("Cobblestone Tools").setValue(true);
	  optionsRecipes.addToggle("Wood Axes").setValue(true);
	  optionsRecipes.addToggle("TNT").setValue(true);
    optionsRecipes.addToggle("Diamond Tools").setValue(true);
	  optionsRecipes.addToggle("Diamond Armor").setValue(true);
    // Add options to Special screen
	  optionsSpecial.addToggle("Gravel/Sand Gravity").setValue(true);
	  optionsSpecial.addToggle("Stone Drops Gravel").setValue(false);
	  optionsSpecial.addToggle("Bonemeal Instagrowth").setValue(true);
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
}
