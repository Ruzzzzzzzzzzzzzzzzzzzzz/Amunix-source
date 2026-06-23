package missu.amunix.modules.combat;
import missu.amunix.core.*;
import missu.amunix.settings.*;
import net.minecraft.client.MinecraftClient;
public class NoAttackDelay extends BaseModule {
    private final BooleanSetting toggle = new BooleanSetting("Enabled", true);
    private final BooleanSetting c03Spoof = new BooleanSetting("C03 Spoof",false);
    public NoAttackDelay(){super("NoAttackDelay","Removes attack cooldowns.",ModuleCategory.COMBAT);addSettings(toggle,c03Spoof);}
    @Override public void onTick(){
        if(!toggle.getValue())return;
        MinecraftClient mc=MinecraftClient.getInstance();
        if(mc.player!=null&&mc.interactionManager!=null){
            mc.player.resetLastAttackedTicks();
        }
    }
}
