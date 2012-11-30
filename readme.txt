///////////////////////////////////////////////////////////////////////////////
// Minecraft My Way - Mod for Mincraft
//    by Jonathan "Wyrd" Brazell
//    Version: 0.72
//    Minecraft Version: 1.4.5
///////////////////////////////////////////////////////////////////////////////

////////////////////////////////////OVERVIEW///////////////////////////////////
// This mod gives you the option of removing certain aspects of the current
// Minecraft game to customize the play experience to what you want to do.
///////////////////////////////////////////////////////////////////////////////

//////////////////////////////////INSTALLATION/////////////////////////////////
// 1. Go to the minecraft bin directory and backup minecraft.jar
// 2. Using 7zip or equally capable ZIP viewer open minecraft.jar
// 3. Delete the META-INF folder
// 4. Install ModLoader (http://www.minecraftforum.net/topic/75440-/).
// 5. Install ModOptionsAPI (http://www.minecraftforum.net/topic/1163014-/)
// 6. Copy mod_MyWay.zip  to your mods folder
// 7. Start up minecraft and play.
///////////////////////////////////////////////////////////////////////////////

/////////////////////////////////VERSION HISTORY///////////////////////////////
// 0.72 - Bug fix.  Dirt gravity works right now.
//     *  Hunger will work now, option for exhaustion removed for now.
// 0.71 - Bug fix, monsters were spawning on the internal server, but not the
//        client, making them invisible, but able to attack.
// 0.7 - Reorganized code to reduce lag
//     * Fixed bug where sand/gravel did not drop
//     * Updated HUD to eliminate unused xp or hunger indicators
//     * Added options to allow blocks to be broken in Adventure mode
//     * Added option to have Adventure mode as game type when creating world
//     * Added option to make dirt drop when placed if not supported
//     * Split sand/gravel gravity into two options
// 0.6 - Updated to Minecraft 1.4.4 & MOAPI 1.0.0
//     * Added option for cows, pigs & sheep to drop bones
//     * Added option to increase drops from cows, pigs & sheep.
//     * Added option for base item ID to allow items to be moved if conflicting
//     * Added option to disable auto healing
//     * Added option to disable hunger
//     * Added option to disable sprinting
//     * Added option to disable exhaustion
//     * Added time flow options for always day, always night, rapid, slow, & single day
//     * Added option for flint tools (uses stone tools images)
//     * Added 2x2 crafting options for:
//             2 saplings -> 2 sticks
//             2 cactuses -> 2 sticks
//             2 bones -> 2 sticks
//             1 cloth -> 2 strings
//             hatchets (top row material and stick, bottom row stick and empty)
// 0.5 - Now kills any existing creatures of type not allowed to spawn.
//      * Added option to kill Blazes
//      * Added option to kill Cave Spiders
//      * Added option to kill Dragon
//      * Added option to kill Giant Zombies
//      * Added option to kill Iron Golems
//      * Added option to kill Silverfish
//      * Added option to kill Snowman
//      * Added option to kill Witch
//      * Added option to kill Wither
// 0.4 - Added World Gen option for Caves
//      * Added World Gen option for Strongholds
//      * Added World Gen option for Villages
//      * Added World Gen option for Mineshafts
//      * Added World Gen option for Scattered Features (Pyramids/Swamp Huts)
//      * Added World Gen option for Ravines
// 0.3 - Updated to minecraft 1.4.2
//      * Added option to remove Bat spawn
// 0.2 - Updated to minecraft 1.3.1/1.3.2
//      * Added option to disable ALL hostile spawns
//      * Added option to disable ALL peaceful spawns
//      * Added option to disable NPC spawns
//      * Added option to craft stone tools from flint
//      * Added option to craft cobblestone from gravel and clay
//      * Added option to craft flint from gravel
//      * Added option to disable cobblestone tools
//      * Added option to stop gravel/sand from falling
//      * Added option to make stone drop gravel when harvested
// 0.1 - Initial release
//      * Added option to remove Diamond tool recipes  
//      * Added option to remove Diamond armor recipes 
//      * Added option to remove Wooden axe/pickaxe recipes 
//      * Added option to remove TNT recipe
//      * Added option to remove Chicken spawn
//      * Added option to remove Cow spawn
//      * Added option to remove Mooshroom spawn
//      * Added option to remove Ocelot spawn
//      * Added option to remove Pig spawn
//      * Added option to remove Sheep spawn
//      * Added option to remove Wolf spawn
//      * Added option to remove Squid spawn
//      * Added option to remove Creeper spawn
//      * Added option to remove Enderman spawn
//      * Added option to remove Ghast spawn
//      * Added option to remove MagmaCube spawn
//      * Added option to remove PigZombie spawn
//      * Added option to remove Skeleton spawn
//      * Added option to remove Slime spawn
//      * Added option to remove Spider spawn
//      * Added option to remove Zombie spawn
//      * Added option to remove BoneMeal instant growth 
///////////////////////////////////////////////////////////////////////////////

////////////////////////////////////LICENSE////////////////////////////////////
// This software is Copyright © 2012 Jonathan "Wyrd" Brazell and is licensed
// under a Creative Commons Attribution-NonCommercial-ShareAlike 3.0 Unported
// License. (http://creativecommons.org/licenses/by-nc-sa/3.0/)
///////////////////////////////////////////////////////////////////////////////
