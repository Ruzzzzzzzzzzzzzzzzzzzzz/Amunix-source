package missu.amunix.modules.world;
import missu.amunix.core.*;
import missu.amunix.settings.*;
import net.minecraft.client.MinecraftClient;
public class Ambience extends BaseModule {
    private final BooleanSetting toggle = new BooleanSetting("Enabled", true);
    private final ModeSetting timeMode = new ModeSetting("Time", new String[]{"Day","Night","Sunset","Custom"},"Day");
    private final NumberSetting customTime = new NumberSetting("Custom Time", 6000, 0, 24000, 100);
    private final ModeSetting weather = new ModeSetting("Weather", new String[]{"Clear","Rain","Thunder","None"},"None");
    private final NumberSetting timeSpeed = new NumberSetting("Speed", 1, 0, 100, 1);
    private long currentTime;
    private boolean wasRaining;
    public Ambience(){super("Ambience","Changes world time and weather.",ModuleCategory.WORLD);addSettings(toggle,timeMode,customTime,weather,timeSpeed);}
    @Override public void onTick(){
        if(!toggle.getValue())return;
        MinecraftClient mc=MinecraftClient.getInstance();
        if(mc.world==null)return;
        long t=switch(timeMode.getValue()){
            case "Day"->1000;case "Night"->13000;case "Sunset"->12000;case "Custom"->(long)customTime.getValue();default->mc.world.getTimeOfDay();
        };
        if(timeMode.getValue().equals("None"))t=mc.world.getTimeOfDay()+1;
        mc.world.setTimeOfDay(t);
    }
}
