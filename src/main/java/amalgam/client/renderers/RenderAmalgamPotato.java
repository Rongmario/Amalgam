package amalgam.client.renderers;

import amalgam.common.Config;
import amalgam.common.entity.EntityAmalgamPotato;
import amalgam.common.item.ItemAmalgamPotato;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;







public class RenderAmalgamPotato
  extends Render
{
  public void doRender(Entity entity, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_)
  {
    IIcon base = ((ItemAmalgamPotato)Config.amalgamPotato).getIcon(null, 0);
    IIcon overlay = ((ItemAmalgamPotato)Config.amalgamPotato).getIcon(null, 1);
    
    int color = ((EntityAmalgamPotato)entity).getColor();
    
    if ((base != null) && (overlay != null)) {
      GL11.glPushMatrix();
      GL11.glTranslatef((float)p_76986_2_, (float)p_76986_4_, (float)p_76986_6_);
      GL11.glEnable(32826);
      GL11.glScalef(0.5F, 0.5F, 0.5F);
      bindEntityTexture(entity);
      Tessellator tessellator = Tessellator.instance;
      
      float f2 = (color >> 16 & 0xFF) / 255.0F;
      float f3 = (color >> 8 & 0xFF) / 255.0F;
      float f4 = (color & 0xFF) / 255.0F;
      
      GL11.glColor3f(f2, f3, f4);
      func_77026_a(tessellator, overlay, true);
      GL11.glColor3f(1.0F, 1.0F, 1.0F);
      
      func_77026_a(tessellator, base, false);
      
      GL11.glDisable(32826);
      GL11.glPopMatrix();
    }
  }
  


  protected ResourceLocation getEntityTexture(Entity p_110775_1_)
  {
    return TextureMap.locationItemsTexture;
  }
  
  private void func_77026_a(Tessellator p_77026_1_, IIcon p_77026_2_, boolean rotate) {
    float f = p_77026_2_.getMinU();
    float f1 = p_77026_2_.getMaxU();
    float f2 = p_77026_2_.getMinV();
    float f3 = p_77026_2_.getMaxV();
    float f4 = 1.0F;
    float f5 = 0.5F;
    float f6 = 0.25F;
    if (rotate) {
      GL11.glRotatef(180.0F - this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
      GL11.glRotatef(-this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
    }
    p_77026_1_.startDrawingQuads();
    p_77026_1_.setNormal(0.0F, 1.0F, 0.0F);
    p_77026_1_.addVertexWithUV(0.0F - f5, 0.0F - f6, 0.0D, f, f3);
    p_77026_1_.addVertexWithUV(f4 - f5, 0.0F - f6, 0.0D, f1, f3);
    p_77026_1_.addVertexWithUV(f4 - f5, f4 - f6, 0.0D, f1, f2);
    p_77026_1_.addVertexWithUV(0.0F - f5, f4 - f6, 0.0D, f, f2);
    p_77026_1_.draw();
  }
}
