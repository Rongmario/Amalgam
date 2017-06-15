package amalgam.client.gui;

import amalgam.common.Config;
import cpw.mods.fml.client.config.GuiConfig;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;

public class AmalgamConfigGui extends GuiConfig
{
  public AmalgamConfigGui(net.minecraft.client.gui.GuiScreen parent)
  {
    super(parent, new ConfigElement(Config.configFile.getCategory("general")).getChildElements(), "amalgam", false, false, GuiConfig.getAbridgedConfigPath(Config.configFile.toString()));
  }
}
