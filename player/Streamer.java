package missu.amunix.modules.player;
import missu.amunix.core.*;
import missu.amunix.settings.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
public class Streamer extends BaseModule {
    private final BooleanSetting toggle = new BooleanSetting("Enabled", true);
    private final BooleanSetting hideName = new BooleanSetting("Hide Name",true);
    private final BooleanSetting hideSkin = new BooleanSetting("Hide Skin",false);
    private final BooleanSetting randomName = new BooleanSetting("Random Name",true);
    private final String fakeName = "Player"+(int)(Math.random()*1000);
    public Streamer(){super("Streamer","Obfuscates names and skins.",ModuleCategory.PLAYER);addSettings(toggle,hideName,hideSkin,randomName);}
    @Override public void onTick(){
        if(!toggle.getValue())return;
        MinecraftClient mc=MinecraftClient.getInstance();
        if(mc.player!=null&&hideName.getValue()){
            mc.player.setCustomName(Text.literal(randomName.getValue()?fakeName:"Player"));
        }
    }
}
