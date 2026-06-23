package missu.amunix.modules.player;
import missu.amunix.core.*;
import missu.amunix.settings.*;
import missu.amunix.utils.ChatUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import java.util.*;
public class KillInsult extends BaseModule {
    private final BooleanSetting toggle = new BooleanSetting("Enabled", true);
    private final ModeSetting mode = new ModeSetting("Mode",new String[]{"Random","Sequential","Custom"},"Random");
    private final NumberSetting delay = new NumberSetting("Delay ms",500,0,3000,100);
    private final BooleanSetting whisper = new BooleanSetting("Whisper",false);
    private final List<String> insults = new ArrayList<>(List.of("ez","L","skill issue","get good","so bad","ur trash","free","LMAO","nice try","sit down"));
    private final Queue<String> queue = new LinkedList<>();
    private String lastKilled;
    private long lastKill;
    public KillInsult(){super("KillInsult","Ragebates your enemy after you killed them.",ModuleCategory.PLAYER);addSettings(toggle,mode,delay,whisper);}
    @Override public void onTick(){
        if(!toggle.getValue())return;
        if(lastKilled!=null&&System.currentTimeMillis()-lastKill>=delay.getValue()){
            String msg=getInsult();
            if(msg!=null){
                String cmd=whisper.getValue()?"/msg "+lastKilled+" "+msg:msg;
                MinecraftClient mc=MinecraftClient.getInstance();
                if(mc.getNetworkHandler()!=null)mc.getNetworkHandler().sendChatMessage(cmd);
            }
            lastKilled=null;
        }
    }
    public void onKill(PlayerEntity victim){
        if(!toggle.getValue()||victim==null)return;
        lastKilled=victim.getGameProfile().getName();
        lastKill=System.currentTimeMillis();
    }
    private String getInsult(){
        if(insults.isEmpty())return null;
        return switch(mode.getValue()){
            case "Random"->insults.get(new Random().nextInt(insults.size()));
            case "Sequential"->{String m=queue.poll();queue.offer(m);yield m;}
            case "Custom"->insults.get(0);
            default->"ez";
        };
    }
    @Override public void onEnable(){queue.clear();queue.addAll(insults);}
}
