package amalgam.common.casting;

import amalgam.common.container.InventoryCasting;
import java.util.ArrayList;
import java.util.HashMap;

import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;


public final class CastingManager
{
  private static final CastingManager INSTANCE = new CastingManager();
  private static List<ICastingRecipe> recipes = new ArrayList();
  


  public static final Block NUGGET_PLACEHOLDER = Blocks.water;
  public static final Block INGOT_PLACEHOLDER = Blocks.fire;
  public static final Block BLOCK_PLACEHOLDER = Blocks.lava;
  
  public static final char NUGGET_CHAR = '.';
  
  public static final char INGOT_CHAR = '@';
  
  public static final char BLOCK_CHAR = '#';
  
  public static CastingManager getInstance()
  {
    return INSTANCE;
  }
  


  public static ShapedCastingRecipe addRecipe(ICastItem output, int amount, Object... inputList)
  {
    StringBuffer s = new StringBuffer("");
    int i = 0;
    int j = 0;
    int k = 0;
    
    if ((inputList[i] instanceof String[])) {
      String[] astring = (String[])(String[])inputList[(i++)];
      
      for (int l = 0; l < astring.length; l++) {
        String s1 = astring[l];
        k++;
        j = s1.length();
        s = s.append(s1);
      }
    } else {
      while ((i < inputList.length) && ((inputList[i] instanceof String))) {
        String s2 = (String)inputList[(i++)];
        k++;
        j = s2.length();
        s = s.append(s2);
      }
    }
    
    HashMap hashmap;
    
    for (/*HashMap<Character, ItemStack>*/ hashmap = new HashMap(); i < inputList.length; i += 2) {
    	Character character = (Character)inputList[i];
    	ItemStack itemstack1 = null;
      
    	if ((inputList[(i + 1)] instanceof Item)) {
    		itemstack1 = new ItemStack((Item)inputList[(i + 1)]);
    	} else if ((inputList[(i + 1)] instanceof Block)) {
    		itemstack1 = new ItemStack((Block)inputList[(i + 1)], 1, 32767);
    	} else if ((inputList[(i + 1)] instanceof ItemStack)) {
    		itemstack1 = (ItemStack)inputList[(i + 1)];
    	}
      
    	hashmap.put(character, itemstack1);
    }
    
    ItemStack[] aitemstack = new ItemStack[j * k];
    
    for (int i1 = 0; i1 < j * k; i1++) {
      char c0 = s.charAt(i1);
      
      if (hashmap.containsKey(Character.valueOf(c0))) {
          aitemstack[i1] = ((ItemStack)hashmap.get(Character.valueOf(c0))).copy();
        } else if (c0 == '.') {
          aitemstack[i1] = new ItemStack(NUGGET_PLACEHOLDER);
        } else if (c0 == '@') {
          aitemstack[i1] = new ItemStack(INGOT_PLACEHOLDER);
        } else if (c0 == '#') {
          aitemstack[i1] = new ItemStack(BLOCK_PLACEHOLDER);
        } else {
          aitemstack[i1] = null;
        }
    }
    
    ShapedCastingRecipe shapedrecipe = new ShapedCastingRecipe(j, k, aitemstack, output, amount);
    recipes.add(shapedrecipe);
    
    return shapedrecipe;
  }
  
  public static void addShapelessRecipe(ICastItem output, int amount, Object... recipeInput) {
    ArrayList<ItemStack> arraylist = new ArrayList();
    Object[] recipeList = recipeInput;
    int numRecipeItems = recipeList.length;
    
    for (int itemIndex = 0; itemIndex < numRecipeItems; itemIndex++) {
      Object component = recipeList[itemIndex];
      
      if ((component instanceof ItemStack)) {
        arraylist.add(((ItemStack)component).copy());
      } else if ((component instanceof Item)) {
        arraylist.add(new ItemStack((Item)component));
      } else if ((component instanceof String)) {
        if (((String)component).equalsIgnoreCase(String.valueOf('.'))) {
          arraylist.add(new ItemStack(NUGGET_PLACEHOLDER));
        } else if (((String)component).equalsIgnoreCase(String.valueOf('@'))) {
          arraylist.add(new ItemStack(INGOT_PLACEHOLDER));
        } else if (((String)component).equalsIgnoreCase(String.valueOf('#'))) {
          arraylist.add(new ItemStack(BLOCK_PLACEHOLDER));
        }
      } else {
        if (!(component instanceof Block)) {
          throw new RuntimeException("Invalid shapeless recipy!");
        }
        
        arraylist.add(new ItemStack((Block)component));
      }
    }
    
    recipes.add(new ShapelessCastingRecipe(output, amount, arraylist));
  }
  
  public static ICastingRecipe findMatchingRecipe(InventoryCasting inv, World world) {
    for (int recipeIndex = 0; recipeIndex < recipes.size(); recipeIndex++) {
      ICastingRecipe irecipe = (ICastingRecipe)recipes.get(recipeIndex);
      
      if (irecipe.matches(inv, world)) {
        return irecipe;
      }
    }
    
    return null;
  }
  
  public static List<ICastingRecipe> getRecipeList() {
    return recipes;
  }
}
