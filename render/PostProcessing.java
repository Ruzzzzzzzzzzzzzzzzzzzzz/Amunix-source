package missu.amunix.modules.render;
import missu.amunix.core.*;
import missu.amunix.settings.*;
public class PostProcessing extends BaseModule {
    private final BooleanSetting toggle = new BooleanSetting("Enabled", true);
    private final ModeSetting shader = new ModeSetting("Shader",new String[]{"None","Blur","Bloom","Outline","MotionBlur"},"None");
    public PostProcessing(){super("PostProcessing","Applies post-processing shader effects.",ModuleCategory.RENDER);addSettings(toggle,shader);}
}
