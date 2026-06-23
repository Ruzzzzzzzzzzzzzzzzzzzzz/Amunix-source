package missu.amunix.modules.combat;
import missu.amunix.core.*;
import missu.amunix.settings.*;
import missu.amunix.utils.InventoryUtil;
import net.minecraft.client.MinecraftClient;
public class AutoWeapon extends BaseModule {
    private final BooleanSetting toggle = new BooleanSetting("Enabled", true);
    private final ModeSetting weapon = new ModeSetting("Weapon",new String[]{"Sword","Axe","Best","Any"},"Best");
    private final BooleanSetting switchBack = new BooleanSetting("Switch Back",true);
    private int prevSlot = -1;
    public AutoWeapon(){super("AutoWeapon","Selects the weapon to use in combat.",ModuleCategory.COMBAT);addSettings(toggle,weapon,switchBack);}
    @Override public void onTick(){
        if(!toggle.getValue())return;
        MinecraftClient mc=MinecraftClient.getInstance();
        if(mc.player==null||mc.interactionManager==null)return;
        if(mc.player.getAttackCooldownProgress(0.5f)==1.0f&&mc.options.attackKey.isPressed()){
            int best=InventoryUtil.findBestWeapon();
            if(best>=0){prevSlot=mc.player.getInventory().selectedSlot;mc.player.getInventory().selectedSlot=best;}
        }
        if(switchBack.getValue()&&prevSlot>=0&&mc.player.getAttackCooldownProgress(0.5f)<0.5f){mc.player.getInventory().selectedSlot=prevSlot;prevSlot=-1;}
    }
}
