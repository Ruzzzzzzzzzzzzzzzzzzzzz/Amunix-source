package missu.amunix.modules.misc;
import missu.amunix.core.*;
import missu.amunix.settings.*;
import missu.amunix.utils.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.*;
import net.minecraft.screen.slot.SlotActionType;
import java.util.*;
public class InvManager extends BaseModule {
    private final BooleanSetting toggle = new BooleanSetting("Enabled", true);
    private final NumberSetting delay = new NumberSetting("Delay ms", 100, 0, 500, 10);
    private final BooleanSetting autoArmor = new BooleanSetting("Auto Armor", true);
    private final BooleanSetting dropTrash = new BooleanSetting("Drop Trash", true);
    private final BooleanSetting sortInv = new BooleanSetting("Sort", true);
    private final BooleanSetting autoRefill = new BooleanSetting("Auto Refill", true);
    private final NumberSetting refillThreshold = new NumberSetting("Refill At", 16, 1, 64, 1);
    private final BooleanSetting hotbarOnly = new BooleanSetting("Hotbar Only", false);
    private final BooleanSetting closeAfter = new BooleanSetting("Close After", true);
    private long lastAction;
    private final Random random = new Random();
    public InvManager(){super("InvManager","Manages and cleans inventory.",ModuleCategory.RENDER);addSettings(toggle,delay,autoArmor,dropTrash,sortInv,autoRefill,refillThreshold,hotbarOnly,closeAfter);}
    @Override public void onTick() {
        if(!toggle.getValue())return;
        MinecraftClient mc=MinecraftClient.getInstance();
        if(mc.player==null||mc.interactionManager==null)return;
        if(System.currentTimeMillis()-lastAction<delay.getValue())return;
        if(autoArmor.getValue())equipBestArmor(mc);
        if(dropTrash.getValue())dropTrashItems(mc);
        if(sortInv.getValue())sortInventory(mc);
        if(autoRefill.getValue())refillHotbar(mc);
        lastAction=System.currentTimeMillis()+random.nextInt(50);
    }
    private void equipBestArmor(MinecraftClient mc){
        for(int slot=0;slot<4;slot++){
            int best=-1;float bestProt=0;
            for(int i=9;i<45;i++){
                var s=mc.player.getInventory().getStack(i);
                if(s.isEmpty())continue;
                if(s.getItem() instanceof ArmorItem armor){
                    if(armor.getSlotType().getEntitySlotId()!=slot)continue;
                    float prot=armor.getProtection();
                    if(prot>bestProt){bestProt=prot;best=i;}
                }
            }
            if(best>=0){clickSlot(mc,best,slot);}
        }
    }
    private void dropTrashItems(MinecraftClient mc){
        Set<Item> trash=Set.of(Items.ROTTEN_FLESH,Items.POISONOUS_POTATO,Items.SPIDER_EYE,Items.PUFFERFISH,Items.POISONOUS_POTATO);
        for(int i=9;i<45;i++){
            var s=mc.player.getInventory().getStack(i);
            if(!s.isEmpty()&&trash.contains(s.getItem())){clickSlot(mc,i,999);}
        }
    }
    private void sortInventory(MinecraftClient mc){
        List<Integer> slots=new ArrayList<>();
        for(int i=9;i<45;i++)slots.add(i);
        slots.sort((a,b)->{
            var sa=mc.player.getInventory().getStack(a);var sb=mc.player.getInventory().getStack(b);
            int pa=getPriority(sa),pb=getPriority(sb);
            return Integer.compare(pb,pa);
        });
        for(int i=0;i<slots.size();i++){if(slots.get(i)!=i+9)clickSlot(mc,slots.get(i),i+9);}
    }
    private void refillHotbar(MinecraftClient mc){
        for(int i=0;i<9;i++){
            var s=mc.player.getInventory().getStack(i);
            if(s.isEmpty()||(s.getCount()<refillThreshold.getValue()&&s.getMaxCount()>1)){
                for(int j=9;j<45;j++){
                    var t=mc.player.getInventory().getStack(j);
                    if(!t.isEmpty()&&(s.isEmpty()||t.getItem()==s.getItem())){
                        clickSlot(mc,j,i);break;
                    }
                }
            }
        }
    }
    private int getPriority(ItemStack s){if(s.isEmpty())return 0;Item i=s.getItem();if(i instanceof SwordItem||i instanceof PickaxeItem)return 5;if(i instanceof BowItem||i instanceof CrossbowItem)return 4;if(i instanceof BlockItem)return 3;if(i.getFoodComponent()!=null)return 2;if(i instanceof ArmorItem)return 1;return 0;}
    private void clickSlot(MinecraftClient mc,int from,int to){if(mc.interactionManager!=null)mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId,from<9?from+36:from,to,SlotActionType.SWAP,mc.player);lastAction=System.currentTimeMillis()+random.nextInt(50);}
}
