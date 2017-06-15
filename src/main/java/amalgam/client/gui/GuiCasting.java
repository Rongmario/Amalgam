package amalgam.client.gui;

import amalgam.common.container.ContainerCasting;
import amalgam.common.container.SlotCasting;
import amalgam.common.properties.PropertyList;
import amalgam.common.properties.PropertyManager;
import amalgam.common.tile.TileCastingTable;
import java.awt.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class GuiCasting
  extends GuiContainer
{
  private static final ResourceLocation CRAFTING_GUI_TEXTURES = new ResourceLocation("amalgam", "textures/gui/CastingGui.png");
  
  public GuiCasting(Container container) {
    super(container);
  }
  
  protected void drawGuiContainerBackgroundLayer(float floatParam, int intParam1, int intParam2)
  {
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    

    this.mc.getTextureManager().bindTexture(CRAFTING_GUI_TEXTURES);
    int xPos = (this.width - this.xSize) / 2;
    int yPos = (this.height - this.ySize) / 2;
    drawTexturedModalRect(xPos, yPos, 0, 0, this.xSize, this.ySize);
    
    ContainerCasting table = (ContainerCasting)this.inventorySlots;
    int color = (int)table.castingTable.getAmalgamPropertyList().getValue(PropertyManager.COLOR);
    
    Color c = new Color(color);
    



    for (int i = 0; i < 9; i++) {
      SlotCasting testSlot = (SlotCasting)table.getSlot(i);
      int state = testSlot.getCastState();
      
      if (state != 0) {
        int rowNum = i / 3;
        int colNum = i % 3;
        

        drawTexturedModalRect(xPos + 30 + 18 * colNum, yPos + 17 + 18 * rowNum, 160 + 17 * state, 1, 16, 16);
        

        GL11.glColor3f(c.getRed() / 255.0F, c.getGreen() / 255.0F, c.getBlue() / 255.0F);
        drawTexturedModalRect(xPos + 30 + 18 * colNum, yPos + 17 + 16 - (int)(16.0D * testSlot.getFillLevel()) + 18 * rowNum, 160 + 17 * state, 35 - (int)(16.0D * testSlot.getFillLevel()) + 16, 16, (int)(16.0D * testSlot.getFillLevel()));
        
        GL11.glColor3f(1.0F, 1.0F, 1.0F);
      }
    }
    
    TileCastingTable castingTable = table.castingTable;
    

    if ((castingTable.getEmptySpace() != 0) && (castingTable.getStackInSlot(9) != null)) {
      float red = (float)Math.pow(Math.sin(Minecraft.getMinecraft().theWorld.getWorldTime() * 0.1D), 2.0D);
      GL11.glEnable(3042);
      GL11.glColor4f(red, 0.0F, 0.0F, 0.3F);
      drawTexturedModalRect(xPos + 120, yPos + 31, 178, 52, 24, 24);
      GL11.glDisable(3042);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }
  }
}
