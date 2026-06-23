package missu.amunix.modules.combat;
import missu.amunix.core.*;
import missu.amunix.settings.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
public class FakeLag extends BaseModule {
    private final BooleanSetting toggle = new BooleanSetting("Enabled", true);
    private final NumberSetting lagDelay = new NumberSetting("Lag ms",200,50,2000,50);
    private final NumberSetting lagChance = new NumberSetting("Chance %",100,10,100,10);
    private final BooleanSetting onAttack = new BooleanSetting("On Attack Only",true);
    private final BooleanSetting onTarget = new BooleanSetting("On Target",false);
    private long lagEnd; private boolean lagging; private final java.util.Random r=new java.util.Random();
    public FakeLag(){super("FakeLag","Simulates network latency.",ModuleCategory.COMBAT);addSettings(toggle,lagDelay,lagChance,onAttack,onTarget);}
    @Override public void onTick(){
        if(!toggle.getValue())return;
        MinecraftClient mc=MinecraftClient.getInstance();
        if(mc.player==null)return;
        if(lagging&&System.currentTimeMillis()>=lagEnd){lagging=false;}
        if(!lagging&&r.nextInt(100)<lagChance.getValue()){
            lagging=true;lagEnd=System.currentTimeMillis()+(long)lagDelay.getValue();
        }
    }
}
