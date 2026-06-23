package missu.amunix.modules.player;
import missu.amunix.core.*;
import missu.amunix.settings.*;
import net.minecraft.client.MinecraftClient;
public class AutoGG extends BaseModule {
    private final BooleanSetting toggle = new BooleanSetting("Enabled", true);
    private final ModeSetting message = new ModeSetting("Message", new String[]{"GG","gg","Good Game!","gf","Good Fight!"},"GG");
    private final NumberSetting delay = new NumberSetting("Delay ms", 1500, 0, 5000, 100);
    private final BooleanSetting hypixelOnly = new BooleanSetting("Hypixel Only", true);
    private final BooleanSetting bedwars = new BooleanSetting("BedWars", true);
    private final BooleanSetting skywars = new BooleanSetting("SkyWars", true);
    private final BooleanSetting duels = new BooleanSetting("Duels", true);
    private boolean sent;
    private long gameEndTime;
    public AutoGG(){super("AutoGG","Sends a chat message at game end.",ModuleCategory.PLAYER);addSettings(toggle,message,delay,hypixelOnly,bedwars,skywars,duels);}
    @Override public void onEnable(){sent=false;gameEndTime=0;}
    @Override public void onTick(){
        if(!toggle.getValue()||sent)return;
        MinecraftClient mc=MinecraftClient.getInstance();
        if(mc.player==null||mc.getNetworkHandler()==null)return;
        if(gameEndTime>0&&System.currentTimeMillis()-gameEndTime>=delay.getValue()){
            mc.getNetworkHandler().sendChatMessage(message.getValue());
            sent=true;
        }
    }
    public void onChatMessage(String msg){
        if(!toggle.getValue()||sent)return;
        String lower=msg.toLowerCase();
        if(lower.contains("you won")||lower.contains("victory")||lower.contains("game over")||lower.contains("1st place")){gameEndTime=System.currentTimeMillis();}
    }
}
