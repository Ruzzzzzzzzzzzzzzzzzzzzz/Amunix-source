package missu.amunix.modules.world;
import missu.amunix.core.*;
import missu.amunix.settings.*;
import missu.amunix.utils.InventoryUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.hit.HitResult;
public class AutoTool extends BaseModule {
    private final BooleanSetting toggle = new BooleanSetting("Enabled", true);
    private final BooleanSetting switchBack = new BooleanSetting("Switch Back", true);
    private final BooleanSetting onAttack = new BooleanSetting("On Attack", false);
    private int prevSlot = -1;
    public AutoTool(){super("AutoTool","Selects the best tool to mine blocks.",ModuleCategory.WORLD);addSettings(toggle,switchBack,onAttack);}
    @Override public void onTick(){
        if(!toggle.getValue()||onAttack.getValue())return;
        MinecraftClient mc=MinecraftClient.getInstance();
        if(mc.player==null||mc.crosshairTarget==null||mc.crosshairTarget.getType()!= HitResult.Type.BLOCK||!mc.options.attackKey.isPressed()){if(prevSlot>=0){mc.player.getInventory().selectedSlot=prevSlot;prevSlot=-1;}return;}
        int best=InventoryUtil.findBestTool();
        if(best>=0&&best!=mc.player.getInventory().selectedSlot){if(prevSlot<0)prevSlot=mc.player.getInventory().selectedSlot;mc.player.getInventory().selectedSlot=best;}
    }
    public void onStartBreaking(){if(onAttack.getValue()&&toggle.getValue()){MinecraftClient mc=MinecraftClient.getInstance();int best=InventoryUtil.findBestTool();if(best>=0){prevSlot=mc.player.getInventory().selectedSlot;mc.player.getInventory().selectedSlot=best;}}}
    public void onStopBreaking(){if(switchBack.getValue()&&prevSlot>=0){MinecraftClient.getInstance().player.getInventory().selectedSlot=prevSlot;prevSlot=-1;}}
}
