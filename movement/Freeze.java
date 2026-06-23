package missu.amunix.modules.movement;
import missu.amunix.core.*;
import missu.amunix.settings.*;
import net.minecraft.client.MinecraftClient;
public class Freeze extends BaseModule {
    private final BooleanSetting toggle = new BooleanSetting("Enabled", true);
    private double freezeX, freezeY, freezeZ; private float freezeYaw, freezePitch;
    public Freeze(){super("Freeze","Freezes your movement.",ModuleCategory.MOVEMENT);addSetting(toggle);}
    @Override public void onEnable(){MinecraftClient mc=MinecraftClient.getInstance();if(mc.player!=null){freezeX=mc.player.getX();freezeY=mc.player.getY();freezeZ=mc.player.getZ();freezeYaw=mc.player.getYaw();freezePitch=mc.player.getPitch();}}
    @Override public void onTick(){
        if(!toggle.getValue())return;
        MinecraftClient mc=MinecraftClient.getInstance();
        if(mc.player!=null){mc.player.setPos(freezeX,freezeY,freezeZ);mc.player.setYaw(freezeYaw);mc.player.setPitch(freezePitch);mc.player.setVelocity(0,0,0);}
    }
}
