package missu.amunix.modules.player;
import missu.amunix.core.*;
import missu.amunix.settings.*;
import net.minecraft.client.MinecraftClient;
public class AutoPlay extends BaseModule {
    private final BooleanSetting toggle = new BooleanSetting("Enabled", true);
    private final ModeSetting mode = new ModeSetting("Mode",new String[]{"Solo","Doubles","3v3v3v3","4v4v4v4","Any"},"Any");
    private final NumberSetting delay = new NumberSetting("Join Delay ms",3000,500,10000,100);
    private final BooleanSetting bedwars = new BooleanSetting("BedWars",true);
    private final BooleanSetting skywars = new BooleanSetting("SkyWars",true);
    private boolean joining; private long endTime;
    public AutoPlay(){super("AutoPlay","Automatically joins the next game.",ModuleCategory.PLAYER);addSettings(toggle,mode,delay,bedwars,skywars);}
    @Override public void onTick(){
        if(!toggle.getValue()||joining)return;
        MinecraftClient mc=MinecraftClient.getInstance();
        if(mc.player==null||mc.getNetworkHandler()==null)return;
        if(endTime>0&&System.currentTimeMillis()-endTime>=delay.getValue()){
            String cmd="/play ";
            if(bedwars.getValue())cmd+=mode.getValue().equals("Any")?"bedwars":"play bedwars_"+mode.getValue().toLowerCase();
            else if(skywars.getValue())cmd+="play skywars_"+mode.getValue().toLowerCase();
            mc.getNetworkHandler().sendCommand(cmd.substring(1));
            joining=true;endTime=0;
        }
    }
    public void onGameEnd(){if(toggle.getValue()){endTime=System.currentTimeMillis();joining=false;}}
    @Override public void onEnable(){joining=false;endTime=0;}
}
