package amalgam.common;

import amalgam.common.block.BlockCastingTable;
import amalgam.common.block.BlockStoneCrucible;
import amalgam.common.casting.CastingManager;
import amalgam.common.casting.ICastItem;
import amalgam.common.entity.EntityAmalgamPotato;
import amalgam.common.item.ItemAmalgamArmor;
import amalgam.common.item.ItemAmalgamAxe;
import amalgam.common.item.ItemAmalgamBlob;
import amalgam.common.item.ItemAmalgamHoe;
import amalgam.common.item.ItemAmalgamPick;
import amalgam.common.item.ItemAmalgamPotato;
import amalgam.common.item.ItemAmalgamShovel;
import amalgam.common.item.ItemAmalgamShroom;
import amalgam.common.item.ItemAmalgamSword;
import amalgam.common.item.ItemStoneTongs;
import amalgam.common.properties.PropertyList;
import amalgam.common.properties.PropertyManager;
import amalgam.common.tile.TileCastingTable;
import amalgam.common.tile.TileStoneCrucible;
import com.google.common.collect.Sets;

import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;






public class Config
{
  public static Config instance = new Config();
  
  public static Configuration configFile;
  
  public static final Logger LOG = LogManager.getLogger("amalgam");
  
  public static final int CASTING_GUI_ID = 1;
  public static boolean moreMaterials = true;
  public static boolean modMaterials = true;
  public static boolean floatingCastResult = true;
  public static boolean coloredAmalgam = true;
  public static boolean allowFunItems = true;
  public static boolean disableVanillaRecipes = true;
  
  public static final int BASE_AMOUNT = 1;
  
  public static final int INGOT_AMOUNT = 9;
  
  public static final int BLOCK_AMOUNT = 81;
  
  public static Fluid fluidAmalgam;
  
  public static Block stoneCrucible;
  
  public static Block castingTable;
  
  public static Item stoneTongs;
  
  public static Item amalgamBlob;
  
  public static Item amalgamSword;
  public static Item amalgamPick;
  public static Item amalgamAxe;
  public static Item amalgamShovel;
  public static Item amalgamHoe;
  public static Item amalgamPotato;
  public static Item amalgamShroom;
  public static Item amalgamHelmet;
  public static Item amalgamChest;
  public static Item amalgamLegs;
  public static Item amalgamBoots;
  public static CreativeTabs tab;
  public static int castingTableRID = -1;
  public static int crucibleRID = -1;
  
  public static void init(FMLPreInitializationEvent event) {
    tab = new CreativeTabs("Amalgam") {
      @SideOnly(Side.CLIENT)
      public Item getTabIconItem() {
        return Config.amalgamBlob;
      }
      
    };
    configFile = new Configuration(event.getSuggestedConfigurationFile());
    
    syncConfig();
  }
  
  public static void syncConfig()
  {
    moreMaterials = configFile.getBoolean("Allow extra vanilla materials to be used", "general", moreMaterials, "Allow obsidian, blaze rods, emeralds and nether quartz to be used in amalgam in addition to iron, gold and diamond");
    

    modMaterials = configFile.getBoolean("Allow common mod ores to be used", "general", modMaterials, "Allows copper, tin, silver, and lead to be used in amalgam");
    

    floatingCastResult = configFile.getBoolean("Floating Cast Result", "general", floatingCastResult, "Render the casting table output above the casting table.");
    

    coloredAmalgam = configFile.getBoolean("Allow colored amalgam", "general", coloredAmalgam, "Allow amalgam color to be determined by material properties");
    

    allowFunItems = configFile.getBoolean("Allow fun items", "general", allowFunItems, "Allow fun items like the AmalgaShroom and Unstable Amalgamized Potato");
    

    disableVanillaRecipes = configFile.getBoolean("Disable vanilla recipes", "general", disableVanillaRecipes, "Removes vanilla recipes for swords, tools and armor");
    

    if (configFile.hasChanged()) {
      configFile.save();
    }
  }
  
  @SubscribeEvent
  public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent eventArgs) {
    if (eventArgs.modID.equals("amalgam")) {
      syncConfig();
    }
  }
  
  public static void registerFluids()
  {
    fluidAmalgam = new Fluid("Amalgam");
    FluidRegistry.registerFluid(fluidAmalgam);
  }
  
  public static void registerItems()
  {
    stoneTongs = new ItemStoneTongs().setUnlocalizedName("stoneTongs");
    amalgamBlob = new ItemAmalgamBlob().setUnlocalizedName("amalgamBlob");
    amalgamSword = new ItemAmalgamSword().setUnlocalizedName("amalgamSword");
    amalgamPick = new ItemAmalgamPick().setUnlocalizedName("amalgamPick");
    amalgamAxe = new ItemAmalgamAxe().setUnlocalizedName("amalgamAxe");
    amalgamShovel = new ItemAmalgamShovel().setUnlocalizedName("amalgamShovel");
    amalgamHoe = new ItemAmalgamHoe().setUnlocalizedName("amalgamHoe");
    
    amalgamShroom = new ItemAmalgamShroom().setUnlocalizedName("amalgamShroom");
    amalgamPotato = new ItemAmalgamPotato().setUnlocalizedName("amalgamPotato");
    
    amalgamHelmet = new ItemAmalgamArmor(ArmorMaterial.CLOTH, 0, 0).setUnlocalizedName("amalgamHelmet");
    amalgamChest = new ItemAmalgamArmor(ArmorMaterial.CLOTH, 0, 1).setUnlocalizedName("amalgamChest");
    amalgamLegs = new ItemAmalgamArmor(ArmorMaterial.CLOTH, 0, 2).setUnlocalizedName("amalgamLegs");
    amalgamBoots = new ItemAmalgamArmor(ArmorMaterial.CLOTH, 0, 3).setUnlocalizedName("amalgamBoots");
    
    GameRegistry.registerItem(stoneTongs, "stoneTongs");
    GameRegistry.registerItem(amalgamBlob, "amalgamBlob");
    GameRegistry.registerItem(amalgamSword, "amalgamSword");
    GameRegistry.registerItem(amalgamPick, "amalgamPick");
    GameRegistry.registerItem(amalgamAxe, "amalgamAxe");
    GameRegistry.registerItem(amalgamShovel, "amalgamShovel");
    GameRegistry.registerItem(amalgamHoe, "amalgamHoe");
    GameRegistry.registerItem(amalgamHelmet, "amalgamHelmet");
    GameRegistry.registerItem(amalgamChest, "amalgamChest");
    GameRegistry.registerItem(amalgamLegs, "amalgamLegs");
    GameRegistry.registerItem(amalgamBoots, "amalgamBoots");
    GameRegistry.registerItem(amalgamShroom, "amalgamShroom");
    GameRegistry.registerItem(amalgamPotato, "amalgamPotato");
  }
  
  public static void registerBlocks()
  {
    stoneCrucible = new BlockStoneCrucible().setBlockName("stoneCrucible");
    castingTable = new BlockCastingTable().setBlockName("castingTable");
    
    GameRegistry.registerBlock(stoneCrucible, "StoneCrucible");
    GameRegistry.registerBlock(castingTable, "CastingTable");
    
    GameRegistry.registerTileEntity(TileStoneCrucible.class, "stoneCrucible");
    GameRegistry.registerTileEntity(TileCastingTable.class, "castingTable");
  }
  



  public static void registerAmalgamProperties()
  {
    PropertyList ironProp = new PropertyList().add(PropertyManager.DENSITY, 2.1F).add(PropertyManager.HARDNESS, 2.5F).add(PropertyManager.LUSTER, 2.2F).add(PropertyManager.MALIABILITY, 6.1F);
    

    PropertyList goldProp = new PropertyList().add(PropertyManager.DENSITY, 0.5F).add(PropertyManager.HARDNESS, 0.8F).add(PropertyManager.LUSTER, 9.1F).add(PropertyManager.MALIABILITY, 1.9F);
    

    PropertyList diamondProp = new PropertyList().add(PropertyManager.DENSITY, 6.2F).add(PropertyManager.HARDNESS, 5.7F).add(PropertyManager.LUSTER, 1.5F).add(PropertyManager.MALIABILITY, 2.2F);
    

    ironProp.add(PropertyManager.COLOR, 1.2303291E7F);
    goldProp.add(PropertyManager.COLOR, 1.5396439E7F);
    diamondProp.add(PropertyManager.COLOR, 3402699.0F);
    
    PropertyManager.registerOreDictProperties("nuggetIron", ironProp, 1);
    PropertyManager.registerOreDictProperties("nuggetGold", goldProp, 1);
    PropertyManager.registerOreDictProperties("nuggetDiamond", diamondProp, 1);
    PropertyManager.registerOreDictProperties("ingotIron", ironProp, 9);
    PropertyManager.registerOreDictProperties("ingotGold", goldProp, 9);
    PropertyManager.registerOreDictProperties("gemDiamond", diamondProp, 9);
    PropertyManager.registerOreDictProperties("blockIron", ironProp, 81);
    PropertyManager.registerOreDictProperties("blockGold", goldProp, 81);
    PropertyManager.registerOreDictProperties("blockDiamond", diamondProp, 81);
    
    PropertyManager.registerItemProperties(new ItemStack(amalgamBlob), null, 0);
    
    if (moreMaterials) {
      PropertyList emeraldProp = new PropertyList().add(PropertyManager.DENSITY, 3.1F).add(PropertyManager.HARDNESS, 3.7F).add(PropertyManager.LUSTER, 3.4F).add(PropertyManager.MALIABILITY, 3.2F).add(PropertyManager.COLOR, 4322180.0F);
      
      PropertyList quartzProp = new PropertyList().add(PropertyManager.DENSITY, 3.2F).add(PropertyManager.HARDNESS, 2.9F).add(PropertyManager.LUSTER, 5.1F).add(PropertyManager.MALIABILITY, 3.3F).add(PropertyManager.COLOR, 1.6777215E7F);
      
      PropertyList obsidianProp = new PropertyList().add(PropertyManager.DENSITY, 6.5F).add(PropertyManager.HARDNESS, 9.5F).add(PropertyManager.LUSTER, 1.1F).add(PropertyManager.MALIABILITY, 0.2F).add(PropertyManager.COLOR, 1378587.0F);
      
      PropertyList blazeRodProp = new PropertyList().add(PropertyManager.DENSITY, 0.8F).add(PropertyManager.HARDNESS, 0.5F).add(PropertyManager.LUSTER, 2.0F).add(PropertyManager.MALIABILITY, 8.5F).add(PropertyManager.COLOR, 1.6763648E7F);
      

      PropertyManager.registerOreDictProperties("gemQuartz", quartzProp, 2);
      PropertyManager.registerOreDictProperties("gemEmerald", emeraldProp, 9);
      
      PropertyManager.registerOreDictProperties("blockQuartz", quartzProp, 18);
      PropertyManager.registerOreDictProperties("blockEmerald", emeraldProp, 81);
      
      PropertyManager.registerItemProperties(new ItemStack(Items.blaze_powder), blazeRodProp, 1);
      PropertyManager.registerItemProperties(new ItemStack(Items.blaze_rod), blazeRodProp, 2);
      PropertyManager.registerItemProperties(new ItemStack(Blocks.obsidian), obsidianProp, 3);
    }
    
    if (modMaterials) {
      PropertyList copperProp = new PropertyList().add(PropertyManager.DENSITY, 2.1F).add(PropertyManager.HARDNESS, 1.7F).add(PropertyManager.LUSTER, 4.4F).add(PropertyManager.MALIABILITY, 4.2F).add(PropertyManager.COLOR, 1.2088115E7F);
      
      PropertyList tinProp = new PropertyList().add(PropertyManager.DENSITY, 3.2F).add(PropertyManager.HARDNESS, 2.9F).add(PropertyManager.LUSTER, 1.1F).add(PropertyManager.MALIABILITY, 2.3F).add(PropertyManager.COLOR, 1.2307643E7F);
      
      PropertyList silverProp = new PropertyList().add(PropertyManager.DENSITY, 1.5F).add(PropertyManager.HARDNESS, 3.5F).add(PropertyManager.LUSTER, 6.5F).add(PropertyManager.MALIABILITY, 4.3F).add(PropertyManager.COLOR, 1.3421772E7F);
      
      PropertyList leadProp = new PropertyList().add(PropertyManager.DENSITY, 7.8F).add(PropertyManager.HARDNESS, 6.5F).add(PropertyManager.LUSTER, 1.2F).add(PropertyManager.MALIABILITY, 2.2F).add(PropertyManager.COLOR, 7833753.0F);
      

      PropertyManager.registerOreDictProperties("nuggetCopper", copperProp, 1);
      PropertyManager.registerOreDictProperties("nuggetTin", tinProp, 1);
      PropertyManager.registerOreDictProperties("nuggetSilver", silverProp, 1);
      PropertyManager.registerOreDictProperties("nuggetLead", leadProp, 1);
      
      PropertyManager.registerOreDictProperties("ingotCopper", copperProp, 9);
      PropertyManager.registerOreDictProperties("ingotTin", tinProp, 9);
      PropertyManager.registerOreDictProperties("ingotSilver", silverProp, 9);
      PropertyManager.registerOreDictProperties("ingotLead", leadProp, 9);
      
      PropertyManager.registerOreDictProperties("blockCopper", copperProp, 81);
      PropertyManager.registerOreDictProperties("blockTin", tinProp, 81);
      PropertyManager.registerOreDictProperties("blockSilver", silverProp, 81);
      PropertyManager.registerOreDictProperties("iblockLead", leadProp, 81);
    }
  }
  
  public static void registerRecipes() {
    String n = String.valueOf('.');
    String i = String.valueOf('@');
    String b = String.valueOf('#');
    
    CastingManager.addShapelessRecipe((ICastItem)amalgamSword, 1, new Object[] { "Amalgam", Blocks.stone });
    CastingManager.addRecipe((ICastItem)amalgamSword, 1, new Object[] { i, i, "s", Character.valueOf('s'), Items.stick });
    CastingManager.addRecipe((ICastItem)amalgamPick, 1, new Object[] { i + i + i, " s ", " s ", Character.valueOf('s'), Items.stick });
    CastingManager.addRecipe((ICastItem)amalgamAxe, 1, new Object[] { i + i, i + "s", " s", Character.valueOf('s'), Items.stick });
    CastingManager.addRecipe((ICastItem)amalgamAxe, 1, new Object[] { i + i, "s" + i, "s ", Character.valueOf('s'), Items.stick });
    CastingManager.addRecipe((ICastItem)amalgamShovel, 1, new Object[] { i, "s", "s", Character.valueOf('s'), Items.stick });
    CastingManager.addRecipe((ICastItem)amalgamHoe, 1, new Object[] { i + i, " s", " s", Character.valueOf('s'), Items.stick });
    CastingManager.addRecipe((ICastItem)amalgamHoe, 1, new Object[] { i + i, "s ", "s ", Character.valueOf('s'), Items.stick });
    
    CastingManager.addRecipe((ICastItem)amalgamHelmet, 1, new Object[] { i + i + i, i + " " + i });
    CastingManager.addRecipe((ICastItem)amalgamChest, 1, new Object[] { i + " " + i, i + i + i, i + i + i });
    CastingManager.addRecipe((ICastItem)amalgamLegs, 1, new Object[] { i + i + i, i + " " + i, i + " " + i });
    CastingManager.addRecipe((ICastItem)amalgamBoots, 1, new Object[] { i + " " + i, i + " " + i });
    
    CastingManager.addRecipe((ICastItem)amalgamShroom, 1, new Object[] { n + n + n, n + "a" + n, n + n + n, Character.valueOf('a'), Blocks.red_mushroom });
    CastingManager.addRecipe((ICastItem)amalgamPotato, 4, new Object[] { "gpg", "p" + b + "p", "gpg", Character.valueOf('g'), Items.gunpowder, Character.valueOf('p'), Items.potato });
    
    GameRegistry.addRecipe(new ItemStack(stoneTongs), new Object[] { "s s", " s ", " s ", Character.valueOf('s'), Blocks.cobblestone });
    GameRegistry.addRecipe(new ItemStack(stoneCrucible), new Object[] { "s s", "s s", "sss", Character.valueOf('s'), Blocks.cobblestone });
    GameRegistry.addRecipe(new ItemStack(castingTable), new Object[] { "sss", "sws", "sss", Character.valueOf('s'), Blocks.cobblestone, Character.valueOf('w'), Blocks.crafting_table });
  }
  
  public static void removeVanillaRecipes() {
    if (disableVanillaRecipes) {
      Iterator<IRecipe> recipes = CraftingManager.getInstance().getRecipeList().iterator();
      
      Set<Item> REMOVE_ITEMS = Sets.newHashSet(new Item[] { Items.diamond_axe, Items.diamond_boots, Items.diamond_chestplate, Items.diamond_helmet, Items.diamond_hoe, Items.diamond_leggings, Items.diamond_pickaxe, Items.diamond_shovel, Items.diamond_sword, Items.iron_axe, Items.iron_boots, Items.iron_chestplate, Items.iron_helmet, Items.iron_hoe, Items.iron_leggings, Items.iron_pickaxe, Items.iron_shovel, Items.iron_sword, Items.golden_axe, Items.golden_boots, Items.golden_chestplate, Items.golden_helmet, Items.golden_hoe, Items.golden_leggings, Items.golden_pickaxe, Items.golden_shovel, Items.golden_sword });
      





      while (recipes.hasNext()) {
        IRecipe testRecipe = (IRecipe)recipes.next();
        if ((testRecipe.getRecipeOutput() != null) && (REMOVE_ITEMS.contains(testRecipe.getRecipeOutput().getItem()))) {
          recipes.remove();
        }
      }
    }
  }
  
  public static void registerEntities() {
    int entityID = 0;
    EntityRegistry.registerModEntity(EntityAmalgamPotato.class, "Unstable Amalgamized Potato", entityID++, Amalgam.instance, 64, 10, true);
  }
}
