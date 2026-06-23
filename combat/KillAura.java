package missu.amunix.modules.combat;
import missu.amunix.core.*;
import missu.amunix.settings.*;
import missu.amunix.utils.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.util.Hand;
import java.util.*;
public class KillAura extends BaseModule {
    private final BooleanSetting toggle = new BooleanSetting("Enabled", true);
    private final NumberSetting range = new NumberSetting("Range", 4.2, 1.0, 8.0, 0.1);
    private final NumberSetting maxCps = new NumberSetting("Max CPS", 12, 1, 20, 1);
    private final NumberSetting minCps = new NumberSetting("Min CPS", 8, 1, 20, 1);
    private final ModeSetting rotationMode = new ModeSetting("Rotation", new String[]{"None","Smooth","Snap","Legit"}, "Smooth");
    private final NumberSetting rotationSpeed = new NumberSetting("Rot Speed", 30, 5, 90, 1);
    private final ModeSetting priority = new ModeSetting("Priority", new String[]{"Distance","Health","FOV","Angle","Armor"}, "Distance");
    private final ModeSetting targetMode = new ModeSetting("Target", new String[]{"Single","Multi","Switch"}, "Single");
    private final NumberSetting switchDelay = new NumberSetting("Switch Delay", 500, 0, 2000, 50);
    private final BooleanSetting players = new BooleanSetting("Players", true);
    private final BooleanSetting monsters = new BooleanSetting("Monsters", false);
    private final BooleanSetting animals = new BooleanSetting("Animals", false);
    private final BooleanSetting invisibles = new BooleanSetting("Invisibles", false);
    private final BooleanSetting throughWalls = new BooleanSetting("Through Walls", true);
    private final BooleanSetting autoBlock = new BooleanSetting("Auto Block", false);
    private final ModeSetting blockMode = new ModeSetting("Block Mode", new String[]{"Vanilla","Hypixel","NCP"}, "Vanilla");
    private final BooleanSetting rayTrace = new BooleanSetting("Ray Trace", true);
    private final BooleanSetting requireSwing = new BooleanSetting("Require Swing", false);
    private final NumberSetting fov = new NumberSetting("FOV", 360, 30, 360, 10);
    private final ModeSetting renderMode = new ModeSetting("Render", new String[]{"None","Box","Circle","Shader"}, "Box");
    private final NumberSetting red = new NumberSetting("Red", 255, 0, 255, 1);
    private final NumberSetting green = new NumberSetting("Green", 0, 0, 255, 1);
    private final NumberSetting blue = new NumberSetting("Blue", 0, 0, 255, 1);
    private final NumberSetting alpha = new NumberSetting("Alpha", 100, 0, 255, 1);
    private LivingEntity target;
    private final List<LivingEntity> targets = new ArrayList<>();
    private long lastAttackTime;
    private float serverYaw, serverPitch;
    private boolean blocking;
    private final Random random = new Random();
    public KillAura() {
        super("KillAura", "Attacks entities within reach.", ModuleCategory.COMBAT);
        addSettings(toggle, range, minCps, maxCps, rotationMode, rotationSpeed,
            priority, targetMode, switchDelay, players, monsters, animals,
            invisibles, throughWalls, autoBlock, blockMode, rayTrace,
            requireSwing, fov, renderMode, red, green, blue, alpha);
    }
    @Override public void onEnable() { target = null; targets.clear(); }
    @Override public void onDisable() {
        target = null; targets.clear();
        if (blocking && MinecraftClient.getInstance().player != null) {
            KeyBindingUtil.stopUse();
            blocking = false;
        }
    }
    @Override public void onTick() {
        if (!toggle.getValue()) return;
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null || mc.world == null || mc.player.isDead()) return;
        findTargets();
        selectTarget();
        if (target != null) {
            rotateToTarget(mc);
            attackTarget(mc);
        } else {
            if (blocking) { KeyBindingUtil.stopUse(); blocking = false; }
        }
    }
    private void findTargets() {
        targets.clear();
        MinecraftClient mc = MinecraftClient.getInstance();
        double r = range.getValue();
        float fovVal = fov.getValue();
        for (Entity entity : mc.world.getEntities()) {
            if (!(entity instanceof LivingEntity e)) continue;
            if (e == mc.player || !e.isAlive() || e.isDead()) continue;
            if (mc.player.distanceTo(e) > r) continue;
            if (e.isInvisible() && !invisibles.getValue()) continue;
            if (e instanceof PlayerEntity && !players.getValue()) continue;
            if (e instanceof Monster && !monsters.getValue()) continue;
            if (e instanceof AnimalEntity && !animals.getValue()) continue;
            if (!throughWalls.getValue() && !mc.player.canSee(e)) continue;
            if (rayTrace.getValue() && !RayTraceUtil.canHit(e)) continue;
            if (fovVal < 360) {
                float[] angles = RotationUtil.calculateAngles(mc.player, e);
                float yawDiff = RotationUtil.angleDifference(mc.player.getYaw(), angles[0]);
                float pitchDiff = Math.abs(mc.player.getPitch() - angles[1]);
                if (yawDiff > fovVal / 2 || pitchDiff > fovVal / 2) continue;
            }
            if (AntiBotUtil.isBot(e)) continue;
            targets.add(e);
        }
    }
    private void selectTarget() {
        if (targets.isEmpty()) { target = null; return; }
        LivingEntity old = target;
        switch (priority.getValue()) {
            case "Distance" -> targets.sort(Comparator.comparingDouble(e -> MinecraftClient.getInstance().player.distanceTo(e)));
            case "Health" -> targets.sort(Comparator.comparingDouble(LivingEntity::getHealth));
            case "FOV" -> targets.sort(Comparator.comparingDouble(this::fovAngle));
            case "Angle" -> targets.sort(Comparator.comparingDouble(e -> {
                float[] a = RotationUtil.calculateAngles(MinecraftClient.getInstance().player, e);
                return Math.abs(RotationUtil.angleDifference(MinecraftClient.getInstance().player.getYaw(), a[0]));
            }));
            case "Armor" -> targets.sort((a,b) -> Integer.compare(b.getArmor(), a.getArmor()));
        }
        LivingEntity best = targets.get(0);
        if (old != null && old.isAlive() && targets.contains(old) && targetMode.getValue().equals("Single")) {
            if (System.currentTimeMillis() - lastAttackTime < switchDelay.getValue()) return;
        }
        target = best;
    }
    private void rotateToTarget(MinecraftClient mc) {
        if (rotationMode.getValue().equals("None") || target == null) return;
        float[] angles = RotationUtil.calculateAngles(mc.player, target);
        float speed = rotationSpeed.getValue();
        switch (rotationMode.getValue()) {
            case "Smooth" -> {
                serverYaw = RotationUtil.smoothAngle(serverYaw, angles[0], speed);
                serverPitch = RotationUtil.smoothAngle(serverPitch, angles[1], speed / 2);
            }
            case "Snap" -> { serverYaw = angles[0]; serverPitch = angles[1]; }
            case "Legit" -> {
                float yawDist = RotationUtil.angleDifference(mc.player.getYaw(), angles[0]);
                if (Math.abs(yawDist) > 60) {
                    int dir = yawDist > 0 ? 1 : -1;
                    serverYaw += dir * Math.min(speed * 2, Math.abs(yawDist));
                }
                serverPitch = RotationUtil.smoothAngle(mc.player.getPitch(), angles[1], speed / 3);
            }
        }
        serverPitch = Math.max(-90, Math.min(90, serverPitch));
        mc.player.setYaw(serverYaw);
        mc.player.setPitch(serverPitch);
    }
    private void attackTarget(MinecraftClient mc) {
        if (target == null || mc.interactionManager == null) return;
        long now = System.currentTimeMillis();
        double cps = minCps.getValue() + random.nextDouble() * (maxCps.getValue() - minCps.getValue());
        long delay = (long)(1000.0 / Math.max(1, cps));
        if (now - lastAttackTime < delay) return;
        if (mc.player.getAttackCooldownProgress(0.5f) < 1.0f && !ModuleManager.getInstance().getModule(NoAttackDelay.class).enabled) return;
        if (requireSwing.getValue() && !mc.player.handSwinging) return;
        if (autoBlock.getValue()) {
            switch (blockMode.getValue()) {
                case "Vanilla" -> { KeyBindingUtil.pressUse(); blocking = true; }
                case "Hypixel" -> {
                    if (!blocking) { KeyBindingUtil.pressUse(); blocking = true; }
                }
                case "NCP" -> {
                    KeyBindingUtil.pressUse();
                    new Thread(() -> { try { Thread.sleep(50); } catch (Exception e) {} KeyBindingUtil.stopUse(); }).start();
                }
            }
        }
        mc.interactionManager.attackEntity(mc.player, target);
        mc.player.swingHand(Hand.MAIN_HAND);
        lastAttackTime = now;
        if (blocking && !autoBlock.getValue()) { KeyBindingUtil.stopUse(); blocking = false; }
    }
    private double fovAngle(LivingEntity e) {
        MinecraftClient mc = MinecraftClient.getInstance();
        float[] angles = RotationUtil.calculateAngles(mc.player, e);
        return Math.hypot(RotationUtil.angleDifference(mc.player.getYaw(), angles[0]), mc.player.getPitch() - angles[1]);
    }
}
