package missu.amunix.modules.player;
import missu.amunix.core.*;
import missu.amunix.settings.*;
import missu.amunix.utils.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.*;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.block.Blocks;
public class AutoMLG extends BaseModule {
    private final BooleanSetting toggle = new BooleanSetting("Enabled", true);
    private final ModeSetting mode = new ModeSetting("Mode", new String[]{"WaterBucket","HayBale","Slime","Cobweb"},"WaterBucket");
    private final NumberSetting fallDistance = new NumberSetting("Fall Distance", 5, 2, 20, 0.5);
    private final NumberSetting pitch = new NumberSetting("Look Pitch", 90, 45, 90, 5);
    private final BooleanSetting autoSwitch = new BooleanSetting("Auto Switch", true);
    private final BooleanSetting swingBack = new BooleanSetting("Swing Back", true);
    private int prevSlot = -1;
    private boolean mlging;
    public AutoMLG(){super("AutoMLG","Places water to prevent fall damage.",ModuleCategory.PLAYER);addSettings(toggle,mode,fallDistance,pitch,autoSwitch,swingBack);}
    @Override public void onTick() {
        if(!toggle.getValue())return;
        MinecraftClient mc=MinecraftClient.getInstance();
        if(mc.player==null||mc.player.isOnGround()||mc.player.isCreative()||mc.player.isSpectator()){mlging=false;return;}
        if(mc.player.fallDistance<fallDistance.getValue()||mlging)return;
        int slot=findBucket();
        if(slot<0)return;
        mlging=true;
        if(autoSwitch.getValue()){prevSlot=mc.player.getInventory().selectedSlot;mc.player.getInventory().selectedSlot=slot;}
        mc.player.setPitch(Math.min((float)pitch.getValue(),90));
        mc.interactionManager.interactItem(mc.player,Hand.MAIN_HAND);
        if(swingBack.getValue()&&prevSlot>=0){new Thread(()->{try{Thread.sleep(100);}catch(Exception e){}mc.player.getInventory().selectedSlot=prevSlot;prevSlot=-1;}).start();}
        new Thread(()->{try{Thread.sleep(500);mlging=false;}catch(Exception e){}}).start();
    }
    private int findBucket(){
        MinecraftClient mc=MinecraftClient.getInstance();
        String m=mode.getValue();
        for(int i=0;i<9;i++){
            var s=mc.player.getInventory().getStack(i);
            if(s.isEmpty())continue;
            if(m.equals("WaterBucket")&&s.getItem()==Items.WATER_BUCKET)return i;
            if(m.equals("HayBale")&&s.getItem()==Items.HAY_BLOCK)return i;
            if(m.equals("Slime")&&s.getItem()==Items.SLIME_BLOCK)return i;
            if(m.equals("Cobweb")&&s.getItem()==Items.COBWEB)return i;
        }
        return -1;
    }
}
