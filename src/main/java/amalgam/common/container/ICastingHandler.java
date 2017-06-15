package amalgam.common.container;

public abstract interface ICastingHandler
{
  public abstract void updateTankCapacity(InventoryCasting paramInventoryCasting);
  
  public abstract boolean isCastComplete();
  
  public abstract void onCastPickup();
  
  public abstract void onCastMatrixChanged(InventoryCasting paramInventoryCasting);
}
