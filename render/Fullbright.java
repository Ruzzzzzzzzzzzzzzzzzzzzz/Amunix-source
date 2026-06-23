package missu.amunix.modules.render;
import missu.amunix.core.*;
import missu.amunix.settings.*;
import net.minecraft.client.MinecraftClient;
public class Fullbright extends BaseModule {
    private final BooleanSetting toggle = new BooleanSetting("Enabled", true);
    private final ModeSetting mode = new ModeSetting("Mode",new String[]{"Gamma","NightVision","Both"},"Gamma");
    private double prevGamma;
    public Fullbright(){super("Fullbright","Removes darkness effect.",ModuleCategory.RENDER);addSettings(toggle,mode);}
    @Override public void onEnable(){MinecraftClient mc=MinecraftClient.getInstance();prevGamma=mc.options.getGamma().getValue();mc.options.getGamma().setValue(100.0);}
    @Override public void onDisable(){MinecraftClient.getInstance().options.getGamma().setValue(prevGamma);}
    @Override public void onTick(){
        if(!toggle.getValue())return;
        MinecraftClient mc=MinecraftClient.getInstance();
        if(mode.getValue().equals("NightVision")&&mc.player!=null&&!mc.player.hasStatusEffect(net.minecraft.entity.effect.StatusEffects.NIGHT_VISION)){
        }
    }
}
