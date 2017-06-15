package amalgam.common.properties;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class PropertyList
{
  private final Map<Property, Float> properties = new HashMap();
  
  public PropertyList() {}
  
  public PropertyList(ItemStack stack)
  {
    PropertyList temp = PropertyManager.getProperties(stack);
    
    if (temp != null) {
      for (Property a : temp.getProperties()) {
        add(a, temp.getValue(a));
      }
    }
  }
  
  public Property[] getProperties() {
    Property[] q = new Property[1];
    return (Property[])this.properties.keySet().toArray(q);
  }
  
  public float getValue(Property a) {
    if ((this.properties.get(a) == null) || (((Float)this.properties.get(a)).isNaN())) {
      return a.getDefaultValue();
    }
    
    return ((Float)this.properties.get(a)).floatValue();
  }
  
  public PropertyList add(Property a, float value) {
    this.properties.put(a, Float.valueOf(value));
    
    return this;
  }
  
  public PropertyList remove(Property a) {
    this.properties.remove(a);
    
    return this;
  }
  
  public void readFromNBT(NBTTagCompound nbtComp) {
    this.properties.clear();
    NBTTagList list = nbtComp.getTagList("AmalgamProperties", 10);
    
    for (int i = 0; i < list.tagCount(); i++) {
      NBTTagCompound attr = list.getCompoundTagAt(i);
      
      if (attr.hasKey("name")) {
        add(Property.getProperty(attr.getString("name")), attr.getFloat("value"));
      }
    }
  }
  
  public void writeToNBT(NBTTagCompound nbtComp) {
    NBTTagList list = new NBTTagList();
    nbtComp.setTag("AmalgamProperties", list);
    
    for (Property attr : getProperties()) {
      if (attr != null) {
        NBTTagCompound attrComp = new NBTTagCompound();
        attrComp.setString("name", attr.getName());
        attrComp.setFloat("value", getValue(attr));
        list.appendTag(attrComp);
      }
    }
  }
  
  public String toString() {
    StringBuffer returnString = new StringBuffer();
    
    for (Property attr : getProperties()) {
      if (attr != null) {
        returnString.append(attr.getName()).append(": ").append(getValue(attr)).append('\n');
      }
    }
    
    return returnString.toString();
  }
}
