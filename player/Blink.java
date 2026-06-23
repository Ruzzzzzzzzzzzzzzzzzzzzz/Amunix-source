package missu.amunix.modules.player;
import missu.amunix.core.*;
import missu.amunix.settings.*;
import missu.amunix.utils.PacketUtil;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.client.MinecraftClient;
import java.util.concurrent.LinkedBlockingDeque;
public class Blink extends BaseModule {
    private final BooleanSetting toggle = new BooleanSetting("Enabled", true);
    private final NumberSetting maxPackets = new NumberSetting("Max Packets", 50, 5, 500, 5);
    private final ModeSetting releaseMode = new ModeSetting("Release", new String[]{"Manual","Auto","Pulse"},"Manual");
    private final NumberSetting pulseDelay = new NumberSetting("Pulse ms", 1000, 100, 5000, 100);
    private final BooleanSetting c2sOnly = new BooleanSetting("C2S Only", true);
    private final BooleanSetting hidePlayer = new BooleanSetting("Hide Player", true);
    private final LinkedBlockingDeque<Packet<?>> queue = new LinkedBlockingDeque<>();
    private long lastPulse;
    private boolean wasBlinking;
    private double startX, startY, startZ;
    public Blink(){super("Blink","Suspends and buffers outgoing packets.",ModuleCategory.PLAYER);addSettings(toggle,maxPackets,releaseMode,pulseDelay,c2sOnly,hidePlayer);}
    @Override public void onEnable(){queue.clear();MinecraftClient mc=MinecraftClient.getInstance();if(mc.player!=null){startX=mc.player.getX();startY=mc.player.getY();startZ=mc.player.getZ();}PacketUtil.startBlink();}
    @Override public void onDisable(){releasePackets();PacketUtil.stopBlink();}
    @Override public void onTick(){
        if(!toggle.getValue())return;
        if(releaseMode.getValue().equals("Pulse")){
            if(System.currentTimeMillis()-lastPulse>=pulseDelay.getValue()){releasePackets();lastPulse=System.currentTimeMillis();}
        }
        if(releaseMode.getValue().equals("Auto")&&queue.size()>=maxPackets.getValue()){releasePackets();}
    }
    public boolean shouldQueue(Packet<?> p){
        if(!toggle.getValue()||!enabled)return false;
        if(c2sOnly.getValue()&&!(p instanceof net.minecraft.network.packet.c2s.common.CommonPackets))return false;
        if(p instanceof PlayerMoveC2SPacket)return true;
        if(queue.size()>=maxPackets.getValue()&&releaseMode.getValue().equals("Auto")){releasePackets();}
        queue.add(p);
        return true;
    }
    private void releasePackets(){while(!queue.isEmpty()){Packet<?> p=queue.poll();PacketUtil.sendPacket(p);}queue.clear();}
}
