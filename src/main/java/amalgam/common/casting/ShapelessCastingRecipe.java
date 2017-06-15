package amalgam.common.casting;

import amalgam.common.container.InventoryCasting;
import amalgam.common.properties.PropertyList;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ShapelessCastingRecipe implements ICastingRecipe
{
  private final ICastItem recipeOutput;
  private final int amount;
  public final List<ItemStack> recipeItems;
  
  public ShapelessCastingRecipe(ICastItem item, int amount, List<ItemStack> list)
  {
    this.recipeOutput = item;
    this.amount = amount;
    this.recipeItems = list;
  }
  
  public ItemStack getRecipeOutput() {
    return this.recipeOutput.generateStackWithProperties(null, null, this.amount);
  }
  
  public boolean matches(InventoryCasting inv, World world)
  {
    ArrayList<ItemStack> arraylist = new ArrayList(this.recipeItems);
    
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 3; j++) {
        ItemStack itemstack = inv.getStackInRowAndColumn(j, i);
        
        if ((itemstack == null) && (inv.getCastState(j + i * 3) != 0)) {
          itemstack = new ItemStack(Blocks.fire);
        }
        
        if (itemstack != null) {
          boolean flag = false;
          Iterator<ItemStack> iterator = arraylist.iterator();
          
          while (iterator.hasNext()) {
            ItemStack itemstack1 = (ItemStack)iterator.next();
            
            if ((itemstack.getItem() == itemstack1.getItem()) && ((itemstack1.getItemDamage() == 32767) || (itemstack.getItemDamage() == itemstack1.getItemDamage())))
            {
              flag = true;
              arraylist.remove(itemstack1);
              break;
            }
          }
          
          if (!flag) {
            return false;
          }
        }
      }
    }
    
    return arraylist.isEmpty();
  }
  
  public int getRecipeSize() {
    return this.recipeItems.size();
  }
  
  public int getRecipeAmount() {
    return this.amount;
  }
  
  public ItemStack getCastingResult(InventoryCasting inv, PropertyList list)
  {
    ItemStack[] temp = new ItemStack[this.recipeItems.size()];
    int currentIndex = 0;
    
    for (int i = 0; i < inv.getSizeInventory(); i++) {
      ItemStack s = inv.getStackInSlot(i);
      if (s != null) {
        temp[currentIndex] = s;
        currentIndex++;
      }
    }
    
    return this.recipeOutput.generateStackWithProperties(list, temp, this.amount);
  }
}
