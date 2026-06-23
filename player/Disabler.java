package missu.amunix.modules.player;
import missu.amunix.core.*;
import missu.amunix.settings.*;
import net.minecraft.network.packet.c2s.play.*;
import net.minecraft.client.MinecraftClient;
public class Disabler extends BaseModule {
    private final BooleanSetting toggle = new BooleanSetting("Enabled", true);
    private final ModeSetting mode = new ModeSetting("Mode",new String[]{"Hypixel","NCP","AAC","Verus","Vulcan"},"Hypixel");
    private final BooleanSetting cancelC03 = new BooleanSetting("Cancel C03",false);
    private final BooleanSetting spoofGround = new BooleanSetting("Spoof Ground",true);
    private final BooleanSetting transaction = new BooleanSetting("Transaction",true);
    private int tickCounter;
    public Disabler(){super("Disabler","Disables server anti-cheat checks.",ModuleCategory.PLAYER);addSettings(toggle,mode,cancelC03,spoofGround,transaction);}
    @Override public void onTick(){
        if(!toggle.getValue())return;
        tickCounter++;
        MinecraftClient mc=MinecraftClient.getInstance();
        if(mc.player==null||mc.getNetworkHandler()==null)return;
        switch(mode.getValue()){
            case "Hypixel"->{if(tickCounter%20==0&&transaction.getValue());}
            case "Vulcan"->{if(cancelC03.getValue());}
            case "NCP"->{if(spoofGround.getValue());}
        }
    }
}
