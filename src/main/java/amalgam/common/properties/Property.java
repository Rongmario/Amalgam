package amalgam.common.properties;

import java.util.Map;

public class Property
{
  public static enum ComboType
  {
    MAX,  MIN,  AVERAGE,  QUADAVERAGE,  COLOR;
    
    private ComboType() {} }
  private static Map<String, Property> properties = new java.util.HashMap();
  private final ComboType comboType;
  private final float defaultValue;
  private final String name;
  
  public Property(String name)
  {
    this.name = name;
    this.comboType = ComboType.QUADAVERAGE;
    this.defaultValue = 0.0F;
    properties.put(this.name, this);
  }
  
  public Property(String name, float dValue) {
    this.name = name;
    this.defaultValue = dValue;
    this.comboType = ComboType.QUADAVERAGE;
    properties.put(this.name, this);
  }
  
  public Property(String name, float dValue, ComboType cType) {
    this.name = name;
    this.defaultValue = dValue;
    this.comboType = cType;
    properties.put(this.name, this);
  }
  
  public static Property getProperty(String name) {
    return (Property)properties.get(name);
  }
  
  public float getDefaultValue() {
    return this.defaultValue;
  }
  
  public String getName() {
    return this.name;
  }
  
  public ComboType getComboType() {
    return this.comboType;
  }
  
  public static Property[] getAll() {
    return (Property[])properties.values().toArray(new Property[0]);
  }
}
