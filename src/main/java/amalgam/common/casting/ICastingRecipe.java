package amalgam.common.casting;

import amalgam.common.container.InventoryCasting;
import amalgam.common.properties.PropertyList;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public abstract interface ICastingRecipe
{
  public abstract boolean matches(InventoryCasting paramInventoryCasting, World paramWorld);
  
  public abstract ItemStack getCastingResult(InventoryCasting paramInventoryCasting, PropertyList paramPropertyList);
  
  public abstract int getRecipeSize();
  
  public abstract ItemStack getRecipeOutput();
}
