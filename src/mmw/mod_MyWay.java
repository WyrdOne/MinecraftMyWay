package net.minecraft.src;

import java.util.Iterator;
import net.minecraft.client.Minecraft;
import moapi.ModOptions;
import moapi.ModOptionsAPI;
import moapi.gui.StdFormatters;
import java.io.PrintWriter;

public class mod_MyWay extends BaseMod {
  // Copyright/license info
  private static final String Name = "Minecraft My Way";
  private static final String Version = "0.1 (For use with Minecraft 1.2.5)";
	private static final String Copyright = "All original code and images (C) 2011-2012, Jonathan \"Wyrd\" Brazell";
	private static final String License = "This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 3.0 Unported License.";
  private static final String ModLoaderVersion = "ModLoader 1.2.5";
  // Options
  private static ModOptions optionsMain;
  private static ModOptions optionsSpawns;
  private static ModOptions optionsRecipes;
  private static ModOptions optionsSpecial;
	public static boolean removeDiamondTools = false;
	public static boolean removeDiamondArmor = false;
	public static boolean removeWoodAxes = false;
	public static boolean removeTNT = false;
  // Other variables
  public static Item saveItemDye = Item.dyePowder;
  private static int tickCounter = 0;
      	
  public void load() {
    // Check proper version of ModLoader to stop version mismatch errors
    if (ModLoader.VERSION != ModLoaderVersion) {
      System.out.println("MinecraftMyWay/ModLoader version mismatch.");
      return;
    }
    // Set up options menu for mod options
    initModOptionsAPI();
 
    // Replace dye to allow bonemeal to be disabled.
    Item.itemsList[Item.dyePowder.shiftedIndex] = null;
    Item.dyePowder = (new MMWItemDye(95)).setIconCoord(14, 4).setItemName("dyePowder");

    // Check for option changes every tick
    ModLoader.setInGameHook(this, true, true);
	}

  public void unload() {
    ModLoader.setInGameHook(this, false, false);
    Item.itemsList[Item.dyePowder.shiftedIndex] = null;
    Item.dyePowder = saveItemDye;
    ModLoader.addRecipe(new ItemStack(Item.swordDiamond, 1), new Object[] {"D", "D", "S", 'D', Item.diamond, 'S', Item.stick}); 
    ModLoader.addRecipe(new ItemStack(Item.shovelDiamond, 1), new Object[] {"D", "S", "S", 'D', Item.diamond, 'S', Item.stick}); 
    ModLoader.addRecipe(new ItemStack(Item.pickaxeDiamond, 1), new Object[] {"DDD", " S ", " S ", 'D', Item.diamond, 'S', Item.stick}); 
    ModLoader.addRecipe(new ItemStack(Item.axeDiamond, 1), new Object[] {"DD", "DS", " S", 'D', Item.diamond, 'S', Item.stick}); 
    ModLoader.addRecipe(new ItemStack(Item.hoeDiamond, 1), new Object[] {"DD", " S", " S", 'D', Item.diamond, 'S', Item.stick}); 
    ModLoader.addRecipe(new ItemStack(Item.helmetDiamond, 1), new Object[] {"DDD", "D D", 'D', Item.diamond}); 
    ModLoader.addRecipe(new ItemStack(Item.plateDiamond, 1), new Object[] {"D D", "DDD", "DDD", 'D', Item.diamond}); 
    ModLoader.addRecipe(new ItemStack(Item.legsDiamond, 1), new Object[] {"DDD", "D D", "D D", 'D', Item.diamond}); 
    ModLoader.addRecipe(new ItemStack(Item.bootsDiamond, 1), new Object[] {"D D", "D D", 'D', Item.diamond}); 
    ModLoader.addRecipe(new ItemStack(Item.pickaxeWood, 1), new Object[] {"WWW", " S ", " S ", 'W', Block.planks, 'S', Item.stick}); 
    ModLoader.addRecipe(new ItemStack(Item.axeWood, 1), new Object[] {"WW", "WS", " S", 'W', Block.planks, 'S', Item.stick}); 
    ModLoader.addRecipe(new ItemStack(Block.tnt, 1), new Object[] {"X#X", "#X#", "X#X", 'X', Item.gunpowder, '#', Block.sand});
    ModLoader.addSpawn(EntityChicken.class, 10, 4, 4, EnumCreatureType.creature);
    ModLoader.addSpawn(EntityCow.class, 8, 4, 4, EnumCreatureType.creature);
    ModLoader.addSpawn(EntityMooshroom.class, 8, 4, 8, EnumCreatureType.creature, new BiomeGenBase[] {BiomeGenBase.mushroomIsland});
    ModLoader.addSpawn(EntityOcelot.class, 2, 1, 1, EnumCreatureType.creature, new BiomeGenBase[] {BiomeGenBase.jungle});
    ModLoader.addSpawn(EntityPig.class, 10, 4, 4, EnumCreatureType.creature);
    ModLoader.addSpawn(EntitySheep.class, 12, 4, 4, EnumCreatureType.creature);
    ModLoader.addSpawn(EntityWolf.class, 5, 4, 4, EnumCreatureType.creature, new BiomeGenBase[] {BiomeGenBase.forest});
    ModLoader.addSpawn(EntityWolf.class, 8, 4, 4, EnumCreatureType.creature, new BiomeGenBase[] {BiomeGenBase.taiga});
    ModLoader.addSpawn(EntitySquid.class, 10, 4, 4, EnumCreatureType.waterCreature);
    ModLoader.addSpawn(EntityCreeper.class, 10, 4, 4, EnumCreatureType.monster);
    ModLoader.addSpawn(EntityEnderman.class, 1, 1, 4, EnumCreatureType.monster);
    ModLoader.addSpawn(EntityGhast.class, 50, 4, 4, EnumCreatureType.monster, new BiomeGenBase[] {BiomeGenBase.hell});
    ModLoader.addSpawn(EntityMagmaCube.class, 1, 4, 4, EnumCreatureType.monster, new BiomeGenBase[] {BiomeGenBase.hell});
    ModLoader.addSpawn(EntityPigZombie.class, 100, 4, 4, EnumCreatureType.monster, new BiomeGenBase[] {BiomeGenBase.hell});
    ModLoader.addSpawn(EntitySkeleton.class, 10, 4, 4, EnumCreatureType.monster);
    ModLoader.addSpawn(EntitySlime.class, 10, 4, 4, EnumCreatureType.monster);
    ModLoader.addSpawn(EntitySpider.class, 10, 4, 4, EnumCreatureType.monster);
    ModLoader.addSpawn(EntityZombie.class, 10, 4, 4, EnumCreatureType.monster);
  }

  public boolean onTickInGame(float f, Minecraft paramMinecraft) {
    // Checking options once a second is enough.
    tickCounter++;
    if ((tickCounter % 20)!=1)
      return true;
    if (tickCounter==20)
      tickCounter = 0;
    // Bonemeal
    ((MMWItemDye)Item.dyePowder).disableBonemeal = !optionsSpecial.getToggleValue("Bonemeal Instagrowth");
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
    // Chicken
    if (optionsSpawns.getToggleValue("Chicken")) {
      ModLoader.addSpawn(EntityChicken.class, 10, 4, 4, EnumCreatureType.creature);
    } else {
      ModLoader.removeSpawn(EntityChicken.class, EnumCreatureType.creature);
    }
    // Cow    
    if (optionsSpawns.getToggleValue("Cow")) {
      ModLoader.addSpawn(EntityCow.class, 8, 4, 4, EnumCreatureType.creature);
    } else {
      ModLoader.removeSpawn(EntityCow.class, EnumCreatureType.creature);
    }
    // Mooshroom
    if (optionsSpawns.getToggleValue("Mooshroom")) {
      ModLoader.addSpawn(EntityMooshroom.class, 8, 4, 8, EnumCreatureType.creature, new BiomeGenBase[] {BiomeGenBase.mushroomIsland});
    } else {
      ModLoader.removeSpawn(EntityMooshroom.class, EnumCreatureType.creature);
    }
    // Ocelot
    if (optionsSpawns.getToggleValue("Ocelot/Cat")) {
      ModLoader.addSpawn(EntityOcelot.class, 2, 1, 1, EnumCreatureType.creature, new BiomeGenBase[] {BiomeGenBase.jungle});
    } else {
      ModLoader.removeSpawn(EntityOcelot.class, EnumCreatureType.creature);
    }
    // Pig    
    if (optionsSpawns.getToggleValue("Pig")) {
      ModLoader.addSpawn(EntityPig.class, 10, 4, 4, EnumCreatureType.creature);
    } else {
      ModLoader.removeSpawn(EntityPig.class, EnumCreatureType.creature);
    } 
    // Sheep
    if (optionsSpawns.getToggleValue("Sheep")) {
      ModLoader.addSpawn(EntitySheep.class, 12, 4, 4, EnumCreatureType.creature);
    } else {
      ModLoader.removeSpawn(EntitySheep.class, EnumCreatureType.creature);
    }
    // Wolf
    if (optionsSpawns.getToggleValue("Wolf")) {
      ModLoader.addSpawn(EntityWolf.class, 5, 4, 4, EnumCreatureType.creature, new BiomeGenBase[] {BiomeGenBase.forest});
      ModLoader.addSpawn(EntityWolf.class, 8, 4, 4, EnumCreatureType.creature, new BiomeGenBase[] {BiomeGenBase.taiga});
    } else {
      ModLoader.removeSpawn(EntityWolf.class, EnumCreatureType.creature);
    }
    // Squid    
    if (optionsSpawns.getToggleValue("Squid")) {
      ModLoader.addSpawn(EntitySquid.class, 10, 4, 4, EnumCreatureType.waterCreature);
    } else {
      ModLoader.removeSpawn(EntitySquid.class, EnumCreatureType.waterCreature);
    }
    // Creeper
    if (optionsSpawns.getToggleValue("Creeper")) {
      ModLoader.addSpawn(EntityCreeper.class, 10, 4, 4, EnumCreatureType.monster);
    } else {
      ModLoader.removeSpawn(EntityCreeper.class, EnumCreatureType.monster);
    }
    // Enderman
    if (optionsSpawns.getToggleValue("Enderman")) {
      ModLoader.addSpawn(EntityEnderman.class, 1, 1, 4, EnumCreatureType.monster);
    } else {
      ModLoader.removeSpawn(EntityEnderman.class, EnumCreatureType.monster);
    }
    // Ghast
    if (optionsSpawns.getToggleValue("Ghast")) {
      ModLoader.addSpawn(EntityGhast.class, 50, 4, 4, EnumCreatureType.monster, new BiomeGenBase[] {BiomeGenBase.hell});
    } else {
      ModLoader.removeSpawn(EntityGhast.class, EnumCreatureType.monster);
    }
    // MagmaCube
    if (optionsSpawns.getToggleValue("Magma Cube")) {
      ModLoader.addSpawn(EntityMagmaCube.class, 1, 4, 4, EnumCreatureType.monster, new BiomeGenBase[] {BiomeGenBase.hell});
    } else {
      ModLoader.removeSpawn(EntityMagmaCube.class, EnumCreatureType.monster);
    }
    // PigZombie
    if (optionsSpawns.getToggleValue("Pig Zombie")) {
      ModLoader.addSpawn(EntityPigZombie.class, 100, 4, 4, EnumCreatureType.monster, new BiomeGenBase[] {BiomeGenBase.hell});
    } else {
      ModLoader.removeSpawn(EntityPigZombie.class, EnumCreatureType.monster);
    }
    // Skeleton    
    if (optionsSpawns.getToggleValue("Skeleton")) {
      ModLoader.addSpawn(EntitySkeleton.class, 10, 4, 4, EnumCreatureType.monster);
    } else {
      ModLoader.removeSpawn(EntitySkeleton.class, EnumCreatureType.monster);
    }
    // Slime    
    if (optionsSpawns.getToggleValue("Slime")) {
      ModLoader.addSpawn(EntitySlime.class, 10, 4, 4, EnumCreatureType.monster);
    } else {
      ModLoader.removeSpawn(EntitySlime.class, EnumCreatureType.monster);
    }
    // Spider    
    if (optionsSpawns.getToggleValue("Spider")) {
      ModLoader.addSpawn(EntitySpider.class, 10, 4, 4, EnumCreatureType.monster);
    } else {
      ModLoader.removeSpawn(EntitySpider.class, EnumCreatureType.monster);
    }
    // Zombie    
    if (optionsSpawns.getToggleValue("Zombie")) {
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
    optionsSpawns = new ModOptions("Minecraft My Way - Spawns");
    optionsMain.addSubOptions(optionsSpawns);
    optionsSpecial = new ModOptions("Minecraft My Way - Special");
    optionsMain.addSubOptions(optionsSpecial);
    // Add options to Spawn screen
	  optionsSpawns.addToggle("Zombie").setValue(true);
	  optionsSpawns.addToggle("Spider").setValue(true);
	  optionsSpawns.addToggle("Slime").setValue(true);
	  optionsSpawns.addToggle("Skeleton").setValue(true);
	  optionsSpawns.addToggle("Pig Zombie").setValue(true);
	  optionsSpawns.addToggle("Magma Cube").setValue(true);
	  optionsSpawns.addToggle("Ghast").setValue(true);
	  optionsSpawns.addToggle("Enderman").setValue(true);
	  optionsSpawns.addToggle("Creeper").setValue(true);
	  optionsSpawns.addToggle("Wolf").setValue(true);
	  optionsSpawns.addToggle("Squid").setValue(true);
	  optionsSpawns.addToggle("Sheep").setValue(true);
	  optionsSpawns.addToggle("Pig").setValue(true);
	  optionsSpawns.addToggle("Ocelot/Cat").setValue(true);
	  optionsSpawns.addToggle("Mooshroom").setValue(true);
	  optionsSpawns.addToggle("Cow").setValue(true);
	  optionsSpawns.addToggle("Chicken").setValue(true);
    // Add options to Recipe screen
	  optionsRecipes.addToggle("Flint Tools").setValue(false);
	  optionsRecipes.addToggle("Gravel to Cobblestone").setValue(false);
	  optionsRecipes.addToggle("Gravel to Flint").setValue(false);
	  optionsRecipes.addToggle("Wood Axes").setValue(true);
	  optionsRecipes.addToggle("TNT").setValue(true);
    optionsRecipes.addToggle("Diamond Tools").setValue(true);
	  optionsRecipes.addToggle("Diamond Armor").setValue(true);
    // Add options to Special screen
	  optionsSpecial.addToggle("Bonemeal Instagrowth").setValue(true);
	  optionsSpecial.addToggle("Stone Drops Gravel").setValue(false);
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
