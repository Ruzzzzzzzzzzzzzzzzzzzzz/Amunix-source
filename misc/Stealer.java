package missu.amunix.modules.misc;
import missu.amunix.core.*;
import missu.amunix.settings.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.item.ItemStack;

public class Stealer extends BaseModule {
    private final BooleanSetting toggle = new BooleanSetting("Enabled", true);
    private final NumberSetting delay = new NumberSetting("Delay ms",100,0,500,10);
    private final BooleanSetting smart = new BooleanSetting("Smart", true);
    private final BooleanSetting autoClose = new BooleanSetting("Auto Close", true);
    private long lastSteal;

    public Stealer(){super("Stealer","Steals items from chests.",ModuleCategory.RENDER);addSettings(toggle,delay,smart,autoClose);}

    @Override public void onTick(){
        if(!toggle.getValue())return;
        MinecraftClient mc=MinecraftClient.getInstance();
        if(mc.player==null||mc.interactionManager==null)return;
        if(mc.player.currentScreenHandler==mc.player.playerScreenHandler)return;
        if(System.currentTimeMillis()-lastSteal<delay.getValue())return;
        for(int i=0;i<mc.player.currentScreenHandler.slots.size()-36;i++){
            ItemStack stack=mc.player.currentScreenHandler.getSlot(i).getStack();
            if(!stack.isEmpty()){
                mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId,i,0,SlotActionType.QUICK_MOVE,mc.player);
                lastSteal=System.currentTimeMillis();return;
            }
        }
        if(autoClose.getValue())mc.player.closeHandledScreen();
    }
}
