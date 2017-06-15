package amalgam.common.fluid;

import amalgam.common.Config;
import amalgam.common.properties.Property;
import amalgam.common.properties.PropertyList;
import java.awt.Color;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;

public class AmalgamStack extends FluidStack
{
  private static final String PROPERTY_KEY = "Tag";
  private static final String AMOUNT_KEY = "Amount";
  
  public AmalgamStack(int amount, PropertyList pList)
  {
    super(Config.fluidAmalgam.getID(), amount, null);
    this.tag = new NBTTagCompound();
    
    if (pList == null) {
      new PropertyList().writeToNBT(this.tag);
    } else {
      pList.writeToNBT(this.tag);
    }
  }
  
  public AmalgamStack(AmalgamStack resource, int amount) {
    super(Config.fluidAmalgam.getID(), amount, resource.tag);
  }
  
  public static AmalgamStack combine(AmalgamStack stackA, AmalgamStack stackB) {
    Property[] allProperties = Property.getAll();
    PropertyList aList = stackA.getProperties();
    PropertyList bList = stackB.getProperties();
    PropertyList newList = new PropertyList();
    
    for (Property p : allProperties) {
      float aValue = aList.getValue(p);
      float bValue = bList.getValue(p);
      float result = 0.0F;
      
      switch (p.getComboType()) {
      case AVERAGE: 
        result = (aValue * stackA.amount + bValue * stackB.amount) / (stackA.amount + stackB.amount);
        break;
      case QUADAVERAGE: 
        result = (float)Math.sqrt((aValue * aValue * stackA.amount + bValue * bValue * stackB.amount) / (stackA.amount + stackB.amount));
        break;
      case MAX: 
        result = Math.max(aValue, bValue);
        break;
      case MIN: 
        result = Math.min(aValue, bValue);
        break;
      
      case COLOR: 
    	  Color aColor = new Color((int)aValue);
          Color bColor = new Color((int)bValue);
          float[] aHSV = new float[3];
          float[] bHSV = new float[3];
          Color.RGBtoHSB(aColor.getRed(), aColor.getGreen(), aColor.getBlue(), aHSV);
          Color.RGBtoHSB(bColor.getRed(), bColor.getGreen(), bColor.getBlue(), bHSV);
          float[] rHSV = new float[3];
          if(Math.abs(aHSV[0] - bHSV[0]) > 0.5F) {
             if(aHSV[0] < bHSV[0]) {
                rHSV[0] = (aHSV[0] * (float)stackA.amount - (1.0F - bHSV[0]) * (float)stackB.amount) / (float)(stackA.amount + stackB.amount);
             } else {
                rHSV[0] = (bHSV[0] * (float)stackB.amount - (1.0F - aHSV[0]) * (float)stackA.amount) / (float)(stackA.amount + stackB.amount);
             }

             if(rHSV[0] < 0.0F) {
                rHSV[0] = (float)((double)rHSV[0] + 1.0D);
             }
          } else {
             rHSV[0] = (aHSV[0] * (float)stackA.amount + bHSV[0] * (float)stackB.amount) / (float)(stackA.amount + stackB.amount);
          }

          for(int i = 1; i < 3; ++i) {
             rHSV[i] = (aHSV[i] * (float)stackA.amount + bHSV[i] * (float)stackB.amount) / (float)(stackA.amount + stackB.amount);
          }

          result = (float)Color.HSBtoRGB(rHSV[0], rHSV[1], rHSV[2]);
          break;
       default:
          result = (aValue * (float)stackA.amount + bValue * (float)stackB.amount) / (float)(stackA.amount + stackB.amount);
       }

       newList.add(p, result);
    }

    return new AmalgamStack(stackA.amount + stackB.amount, newList);
 }
  
  public PropertyList getProperties() {
    PropertyList pList = new PropertyList();
    pList.readFromNBT(this.tag);
    
    return pList;
  }
  
  public static AmalgamStack loadAmalgamStackFromNBT(NBTTagCompound nbtComp) {
    int amount = nbtComp.getInteger("Amount");
    PropertyList pList = new PropertyList();
    pList.readFromNBT(nbtComp.getCompoundTag("Tag"));
    
    return new AmalgamStack(amount, pList);
  }
}
