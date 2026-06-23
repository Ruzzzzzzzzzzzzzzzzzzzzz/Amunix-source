package missu.amunix.modules.misc;
import missu.amunix.core.*;
import missu.amunix.settings.*;
public class Notification extends BaseModule {
    private final BooleanSetting toggle = new BooleanSetting("Enabled", true);
    private final NumberSetting duration = new NumberSetting("Duration s",3.0,1.0,10.0,0.5);
    private final ModeSetting position = new ModeSetting("Position",new String[]{"TopRight","TopLeft","BottomRight","BottomLeft"},"TopRight");
    public Notification(){super("Notification","Displays module toggle notifications.",ModuleCategory.RENDER);addSettings(toggle,duration,position);}
}
