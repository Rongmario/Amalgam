package amalgam.client.renderers;

import amalgam.common.Config;
import amalgam.common.block.BlockCastingTable;
import amalgam.common.tile.TileCastingTable;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

public class CastingTableRenderer extends TileEntitySpecialRenderer implements ISimpleBlockRenderingHandler
{
  public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer)
  {
    block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    renderer.setRenderBoundsFromBlock(block);
    

    BlockCastingTable table = (BlockCastingTable)block;
    IIcon top = table.getBlockTextureFromSide(1);
    IIcon side = table.getBlockTextureFromSide(2);
    IIcon base = table.getBlockTextureFromSide(0);
    IIcon baseSide = table.getBlockTextureFromSide(7);
    IIcon neck = table.getBlockTextureFromSide(6);
    
    Tessellator t = Tessellator.instance;
    GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
    


    t.startDrawingQuads();
    t.setNormal(0.0F, -1.0F, 0.0F);
    renderer.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, base);
    renderer.renderFaceYNeg(block, 0.0D, 0.625D, 0.0D, top);
    
    t.setNormal(0.0F, 1.0F, 0.0F);
    renderer.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, top);
    renderer.renderFaceYPos(block, 0.0D, -0.75D, 0.0D, base);
    
    t.setNormal(0.0F, 0.0F, 1.0F);
    renderer.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, side);
    renderer.renderFaceXNeg(block, 0.125D, 0.0D, 0.0D, baseSide);
    renderer.renderFaceXNeg(block, 0.3125D, 0.0D, 0.0D, neck);
    
    t.setNormal(0.0F, 0.0F, -1.0F);
    renderer.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, side);
    renderer.renderFaceXPos(block, -0.125D, 0.0D, 0.0D, baseSide);
    renderer.renderFaceXPos(block, -0.3125D, 0.0D, 0.0D, neck);
    
    t.setNormal(1.0F, 0.0F, 0.0F);
    renderer.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, side);
    renderer.renderFaceZNeg(block, 0.0D, 0.0D, 0.125D, baseSide);
    renderer.renderFaceZNeg(block, 0.0D, 0.0D, 0.3125D, neck);
    
    t.setNormal(-1.0F, 0.0F, 0.0F);
    renderer.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, side);
    renderer.renderFaceZPos(block, 0.0D, 0.0D, -0.125D, baseSide);
    renderer.renderFaceZPos(block, 0.0D, 0.0D, -0.3125D, neck);
    
    t.draw();
    
    GL11.glTranslatef(0.5F, 0.5F, 0.5F);
  }
  
  public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer)
  {
    block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    
    renderer.setRenderBoundsFromBlock(block);
    renderer.renderStandardBlock(block, x, y, z);
    
    BlockCastingTable table = (BlockCastingTable)block;
    IIcon top = table.getBlockTextureFromSide(1);
    IIcon base = table.getBlockTextureFromSide(0);
    IIcon baseSide = table.getBlockTextureFromSide(7);
    IIcon neck = table.getBlockTextureFromSide(6);
    



    renderer.renderFaceYNeg(block, x, y + 1.0F - 0.375F, z, top);
    renderer.renderFaceYPos(block, x, y - 1.0F + 0.25F, z, base);
    
    renderer.renderFaceXPos(block, x - 1.0F + 0.875D, y, z, baseSide);
    renderer.renderFaceXNeg(block, x + 1.0F - 0.875D, y, z, baseSide);
    renderer.renderFaceZPos(block, x, y, z - 1.0F + 0.875D, baseSide);
    renderer.renderFaceZNeg(block, x, y, z + 1.0F - 0.875D, baseSide);
    
    renderer.renderFaceXPos(block, x - 1.0F + 0.6875D, y, z, neck);
    renderer.renderFaceXNeg(block, x + 1.0F - 0.6875D, y, z, neck);
    renderer.renderFaceZPos(block, x, y, z - 1.0F + 0.6875D, neck);
    renderer.renderFaceZNeg(block, x, y, z + 1.0F - 0.6875D, neck);
    
    renderer.clearOverrideBlockTexture();
    renderer.setRenderBoundsFromBlock(block);
    renderer.renderStandardBlock(block, x, y, z);
    return true;
  }
  
  public boolean shouldRender3DInInventory(int modelId)
  {
    return true;
  }
  
  public int getRenderId()
  {
    return Config.castingTableRID;
  }
  
  public void renderTileEntityAt(TileEntity te, double x, double y, double z, float f)
  {
    if (!Minecraft.getMinecraft().gameSettings.fancyGraphics) {
      return;
    }
    
    TileCastingTable table = (TileCastingTable)te;
    ItemStack stack = table.getStackInSlot(9);
    
    if (stack == null) {
      return;
    }
    
    GL11.glPushMatrix();
    
    if (Config.floatingCastResult) {
      long time = te.getWorldObj().getWorldTime();
      GL11.glTranslated(x + 0.5D, y + 1.1D, z + 0.5D);
      GL11.glRotatef((float)time * 1.5F, 0.0F, 90.0F, 0.0F);
    } else {
      GL11.glTranslated(x + 0.5D, y + 1.05D, z + 0.25D);
      GL11.glRotatef(90.0F, 90.0F, 0.0F, 0.0F);
    }
    
    GL11.glScalef(1.25F, 1.25F, 1.25F);
    
    if (table.getEmptySpace() != 0) {
      GL11.glEnable(3042);
      GL11.glBlendFunc(1, 772);
    }
    
    if (Block.getBlockFromItem(stack.getItem()) == net.minecraft.init.Blocks.air) {
      ItemStack temp = stack.copy();
      temp.stackSize = 1;
      EntityItem itemEntity = new EntityItem(te.getWorldObj(), te.xCoord, te.yCoord + 1.2D, te.zCoord, temp);
      itemEntity.hoverStart = 0.0F;
      RenderManager.instance.renderEntityWithPosYaw(itemEntity, 0.0D, 0.0D, 0.0D, 0.0F, 0.0F);
    } else {
      GL11.glTranslated(0.0D, 0.5D, 0.0D);
      Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);
      RenderBlocks.getInstance().renderBlockAsItem(Block.getBlockFromItem(stack.getItem()), 0, 1.0F);
    }
    
    GL11.glDisable(3042);
    GL11.glPopMatrix();
  }
}
