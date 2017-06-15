package amalgam.common.item;

import amalgam.common.casting.ICastItem;
import com.google.common.collect.Sets;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Set;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;


public class ItemAmalgamAxe
  extends ItemAmalgamTool
  implements ICastItem
{
  private static final Set<Block> USABLE_BLOCKS = Sets.newHashSet(new Block[] { Blocks.planks, Blocks.bookshelf, Blocks.log, Blocks.log2, Blocks.chest, Blocks.pumpkin, Blocks.lit_pumpkin });
  
  public ItemAmalgamAxe()
  {
    super(3.0F, "axe", USABLE_BLOCKS);
    setHarvestLevel("axe", 0);
  }
  
  @SideOnly(Side.CLIENT)
  public void registerIcons(IIconRegister iconRegister) {
    this.itemIcon = iconRegister.registerIcon("amalgam:amalgamAxeBlade");
    this.hilt = iconRegister.registerIcon("amalgam:amalgamAxeHilt");
  }
  
  public float func_150893_a(ItemStack stack, Block block)
  {
    return (block.getMaterial() != Material.wood) && (block.getMaterial() != Material.plants) && (block.getMaterial() != Material.vine) ? super.func_150893_a(stack, block) : getEfficiency(stack);
  }
}
