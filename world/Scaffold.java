package missu.amunix.modules.world;
import missu.amunix.core.*;
import missu.amunix.settings.*;
import missu.amunix.utils.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.item.BlockItem;
import net.minecraft.util.Hand;

public class Scaffold extends BaseModule {
    private final BooleanSetting toggle = new BooleanSetting("Enabled", true);
    private final ModeSetting mode = new ModeSetting("Mode",new String[]{"Normal","Tower","GodBridge","Legit"},"Normal");
    private final NumberSetting delay = new NumberSetting("Place Delay",1,0,4,1);
    private final BooleanSetting autoSwitch = new BooleanSetting("Auto Switch",true);
    private final BooleanSetting sameY = new BooleanSetting("Same Y",false);
    private final BooleanSetting rotate = new BooleanSetting("Rotate",true);
    private final BooleanSetting swing = new BooleanSetting("Swing",true);
    private final NumberSetting towerHeight = new NumberSetting("Tower Height",5,1,20,1);
    private int slot; private long lastPlace;

    public Scaffold(){super("Scaffold","Automatically places blocks under you.",ModuleCategory.WORLD);addSettings(toggle,mode,delay,autoSwitch,sameY,rotate,swing,towerHeight);}

    @Override public void onTick(){
        if(!toggle.getValue())return;
        MinecraftClient mc=MinecraftClient.getInstance();
        if(mc.player==null||mc.world==null||mc.interactionManager==null)return;
        if(System.currentTimeMillis()-lastPlace<delay.getValue()*50)return;
        if(mode.getValue().equals("Tower")){doTower(mc);return;}
        BlockPos pos=findPlacePos(mc);if(pos==null)return;
        int best=findBlock(mc);if(best<0)return;
        if(autoSwitch.getValue()){slot=mc.player.getInventory().selectedSlot;mc.player.getInventory().selectedSlot=best;}
        if(rotate.getValue()){float[]a=RotationUtil.calculateAngles(mc.player,mc.player);mc.player.setYaw(a[0]);mc.player.setPitch(85f);}
        mc.interactionManager.interactBlock(mc.player,Hand.MAIN_HAND,new net.minecraft.util.hit.BlockHitResult(pos.toCenterPos(),Direction.UP,pos,false));
        if(swing.getValue())mc.player.swingHand(Hand.MAIN_HAND);
        if(autoSwitch.getValue()&&slot>=0){mc.player.getInventory().selectedSlot=slot;}
        lastPlace=System.currentTimeMillis();
    }

    private void doTower(MinecraftClient mc){
        if(!mc.player.isOnGround()){mc.player.setVelocity(0,0.42,0);return;}
        BlockPos under=mc.player.getBlockPos().down();
        int best=findBlock(mc);if(best<0)return;
        if(autoSwitch.getValue())mc.player.getInventory().selectedSlot=best;
        mc.interactionManager.interactBlock(mc.player,Hand.MAIN_HAND,new net.minecraft.util.hit.BlockHitResult(under.toCenterPos(),Direction.UP,under,false));
        mc.player.jump();
        lastPlace=System.currentTimeMillis();
    }

    private BlockPos findPlacePos(MinecraftClient mc){
        BlockPos under=mc.player.getBlockPos().down();
        if(mc.world.getBlockState(under).isAir())return under;
        if(sameY.getValue())for(int x=-1;x<=1;x++)for(int z=-1;z<=1;z++){
            BlockPos p=under.add(x,0,z);if(mc.world.getBlockState(p).isAir())return p;
        }
        return null;
    }

    private int findBlock(MinecraftClient mc){
        for(int i=0;i<9;i++){var s=mc.player.getInventory().getStack(i);if(!s.isEmpty()&&s.getItem() instanceof BlockItem)return i;}
        return mc.player.getInventory().selectedSlot;
    }
}
