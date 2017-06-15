package amalgam.common.item;

import com.google.common.collect.Sets;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Set;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;



public class ItemAmalgamPick
  extends ItemAmalgamTool
{
  private static final Set<Block> USABLE_BLOCKS = Sets.newHashSet(new Block[] { Blocks.cobblestone, Blocks.double_stone_slab, Blocks.stone_slab, Blocks.stone, Blocks.sandstone, Blocks.mossy_cobblestone, Blocks.iron_ore, Blocks.iron_block, Blocks.coal_ore, Blocks.gold_block, Blocks.gold_ore, Blocks.diamond_ore, Blocks.diamond_block, Blocks.ice, Blocks.netherrack, Blocks.lapis_ore, Blocks.lapis_block, Blocks.redstone_ore, Blocks.lit_redstone_ore, Blocks.rail, Blocks.detector_rail, Blocks.golden_rail, Blocks.activator_rail });
  


  public ItemAmalgamPick()
  {
    super(2.0F, "pickaxe", USABLE_BLOCKS);
    setHarvestLevel("pickaxe", 0);
  }
  
  @SideOnly(Side.CLIENT)
  public void registerIcons(IIconRegister iconRegister) {
    this.itemIcon = iconRegister.registerIcon("amalgam:amalgamPickBlade");
    this.hilt = iconRegister.registerIcon("amalgam:amalgamPickHilt");
  }
  
  public boolean canHarvestBlock(Block block, ItemStack stack)
  {
    float harvestLevel = getHarvestLevel(stack, block.getHarvestTool(0));
    return harvestLevel >= 3.0F;
  }
  











  public float func_150893_a(ItemStack stack, Block block)
  {
    return (block.getMaterial() != Material.iron) && (block.getMaterial() != Material.anvil) && (block.getMaterial() != Material.rock) ? super.func_150893_a(stack, block) : getEfficiency(stack);
  }
}
