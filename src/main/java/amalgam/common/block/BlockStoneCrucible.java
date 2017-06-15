package amalgam.common.block;

import amalgam.common.Config;
import amalgam.common.fluid.AmalgamStack;
import amalgam.common.fluid.IAmalgamContainerItem;
import amalgam.common.properties.PropertyList;
import amalgam.common.properties.PropertyManager;
import amalgam.common.tile.TileStoneCrucible;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import org.apache.logging.log4j.Logger;

public class BlockStoneCrucible extends BlockAmalgamContainer implements ITileEntityProvider
{
  public static final float EMPTY_LEVEL = 0.3F;
  @SideOnly(Side.CLIENT)
  private IIcon iconInner;
  @SideOnly(Side.CLIENT)
  private IIcon iconTop;
  @SideOnly(Side.CLIENT)
  private IIcon iconBottom;
  @SideOnly(Side.CLIENT)
  public IIcon liquidAmalgam;
  @SideOnly(Side.CLIENT)
  public IIcon solidAmalgam;
  
  public BlockStoneCrucible()
  {
    super(Material.iron);
    setHardness(3.0F);
    setResistance(5.0F);
    setStepSound(soundTypeStone);
    setCreativeTab(Config.tab);
  }
  
  @SideOnly(Side.CLIENT)
  public IIcon getIcon(int side, int meta)
  {
    return side == 6 ? this.iconInner : side == 0 ? this.iconBottom : side == 1 ? this.iconTop : this.blockIcon;
  }
  
  @SideOnly(Side.CLIENT)
  public void registerBlockIcons(IIconRegister iconRegister)
  {
    this.iconInner = iconRegister.registerIcon("amalgam:stoneCrucibleInner");
    this.iconTop = iconRegister.registerIcon("amalgam:stoneCrucibleTop");
    this.iconBottom = iconRegister.registerIcon("amalgam:stoneCrucibleBottom");
    this.blockIcon = iconRegister.registerIcon("amalgam:stoneCrucibleSide");
    this.liquidAmalgam = iconRegister.registerIcon("amalgam:amalgamStill");
    this.solidAmalgam = iconRegister.registerIcon("amalgam:amalgamSolid");
  }
  
  public boolean isOpaqueCube()
  {
    return false;
  }
  
  public boolean renderAsNormalBlock()
  {
    return false;
  }
  
  public int getRenderType()
  {
    return Config.crucibleRID;
  }
  
  @SideOnly(Side.CLIENT)
  public static IIcon getAmalgamIcon(boolean isSolid) {
    if (isSolid) {
      return ((BlockStoneCrucible)Config.stoneCrucible).solidAmalgam;
    }
    
    return ((BlockStoneCrucible)Config.stoneCrucible).liquidAmalgam;
  }
  
  public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB aabb, List list, Entity entity)
  {
    setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.3125F, 1.0F);
    super.addCollisionBoxesToList(world, x, y, z, aabb, list, entity);
    float f = 0.125F;
    setBlockBounds(0.0F, 0.0F, 0.0F, f, 1.0F, 1.0F);
    super.addCollisionBoxesToList(world, x, y, z, aabb, list, entity);
    setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, f);
    super.addCollisionBoxesToList(world, x, y, z, aabb, list, entity);
    setBlockBounds(1.0F - f, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    super.addCollisionBoxesToList(world, x, y, z, aabb, list, entity);
    setBlockBounds(0.0F, 0.0F, 1.0F - f, 1.0F, 1.0F, 1.0F);
    super.addCollisionBoxesToList(world, x, y, z, aabb, list, entity);
    
    setBlockBoundsForItemRender();
  }
  
  public void setBlockBoundsForItemRender()
  {
    setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
  }
  
  public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity)
  {
    TileEntity te = world.getTileEntity(x, y, z);
    
    if ((te instanceof TileStoneCrucible)) {
      TileStoneCrucible cruc = (TileStoneCrucible)te;
      
      if (cruc.isHot())
      {
        if ((entity instanceof EntityItem)) {
          ItemStack stack = ((EntityItem)entity).getEntityItem();
          if (PropertyManager.itemIsAmalgable(stack)) {
            interactWithAmalgableItem(cruc, stack);
            return;
          }
        }
        
        if (cruc.getRenderLiquidLevel() > 0.3F) {
          entity.setFire(3);
        }
      }
    }
  }
  
  public TileEntity createNewTileEntity(World world, int metaData)
  {
    return new TileStoneCrucible();
  }
  
  public boolean isToolEffective(String type, int metadata)
  {
    if ("pickaxe".equals(type)) {
      return true;
    }
    
    return false;
  }
  


  protected void interactWithAmalgableItem(TileEntity te, ItemStack stack)
  {
    if (te.getWorldObj().isRemote) {
      return;
    }
    
    if (!(te instanceof TileStoneCrucible)) {
      return;
    }
    
    TileStoneCrucible crucible = (TileStoneCrucible)te;
    
    if (!crucible.isHot()) {
      return;
    }
    
    int amount = PropertyManager.getVolume(stack);
    
    if ((amount > 0) && (amount <= crucible.getEmptySpace())) {
      PropertyList amalgProperties = PropertyManager.getProperties(stack);
      AmalgamStack amalg = new AmalgamStack(amount, amalgProperties);
      
      if (amalgProperties == null) {
        Config.LOG.error("No properties!!!!!");
      }
      
      crucible.fill(ForgeDirection.UNKNOWN, amalg, true);
      stack.stackSize -= 1;
      
      te.getWorldObj().notifyBlocksOfNeighborChange(te.xCoord, te.yCoord, te.zCoord, this);
      
      return;
    }
  }
  
  protected void interactWithAmalgamContainerItem(TileEntity te, IAmalgamContainerItem container, ItemStack stack, EntityPlayer player)
  {
    if (((te instanceof TileStoneCrucible)) && (((TileStoneCrucible)te).isHot())) {
      super.interactWithAmalgamContainerItem(te, container, stack, player);
      te.getWorldObj().notifyBlocksOfNeighborChange(te.xCoord, te.yCoord, te.zCoord, this);
    }
  }
}
