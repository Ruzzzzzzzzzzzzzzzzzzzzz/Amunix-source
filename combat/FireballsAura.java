package missu.amunix.modules.combat;
import missu.amunix.core.*;
import missu.amunix.settings.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.util.Hand;
public class FireballsAura extends BaseModule {
    private final BooleanSetting toggle = new BooleanSetting("Enabled", true);
    private final NumberSetting range = new NumberSetting("Range",6,1,20,0.5);
    private final NumberSetting delay = new NumberSetting("Delay ms",200,50,1000,50);
    private final ModeSetting mode = new ModeSetting("Mode",new String[]{"Hit","Deflect","Destroy"},"Hit");
    private long lastHit;
    public FireballsAura(){super("FireballsAura","Blocks incoming fireballs.",ModuleCategory.COMBAT);addSettings(toggle,range,delay,mode);}
    @Override public void onTick(){
        if(!toggle.getValue())return;
        MinecraftClient mc=MinecraftClient.getInstance();
        if(mc.player==null||mc.world==null)return;
        long now=System.currentTimeMillis();if(now-lastHit<delay.getValue())return;
        for(Entity e:mc.world.getEntities()){
            if(!(e instanceof FireballEntity fb)||mc.player.distanceTo(e)>range.getValue())continue;
            float[] angles=missu.amunix.utils.RotationUtil.calculateAngles(mc.player,fb);
            mc.player.setYaw(angles[0]);mc.player.setPitch(angles[1]);
            if(mode.getValue().equals("Hit")){mc.interactionManager.attackEntity(mc.player,fb);mc.player.swingHand(Hand.MAIN_HAND);}
            else if(mode.getValue().equals("Deflect")){}
            lastHit=now;break;
        }
    }
}
