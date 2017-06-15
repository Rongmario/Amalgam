package amalgam.client.gui;

import cpw.mods.fml.client.IModGuiFactory;
import cpw.mods.fml.client.IModGuiFactory.RuntimeOptionCategoryElement;
import cpw.mods.fml.client.IModGuiFactory.RuntimeOptionGuiHandler;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

public class AmalgamGuiFactory
  implements IModGuiFactory
{
  public void initialize(Minecraft minecraftInstance) {}
  
  public Class<? extends GuiScreen> mainConfigGuiClass()
  {
    return AmalgamConfigGui.class;
  }
  
  public Set<IModGuiFactory.RuntimeOptionCategoryElement> runtimeGuiCategories()
  {
    return null;
  }
  
  public IModGuiFactory.RuntimeOptionGuiHandler getHandlerFor(IModGuiFactory.RuntimeOptionCategoryElement element)
  {
    return null;
  }
}
