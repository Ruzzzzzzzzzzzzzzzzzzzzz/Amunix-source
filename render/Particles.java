package missu.amunix.modules.render;
import missu.amunix.core.*;
import missu.amunix.settings.*;
public class Particles extends BaseModule {
    private final BooleanSetting toggle = new BooleanSetting("Enabled", true);
    private final ModeSetting mode = new ModeSetting("Mode",new String[]{"None","Minimal","All"},"Minimal");
    public Particles(){super("Particles","Controls particle rendering.",ModuleCategory.RENDER);addSettings(toggle,mode);}
}
