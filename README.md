# Amunix Client v1.7.0

Minecraft 1.21.4 Fabric Hypixel PvP client. 60 modules across 6 categories.

## Requirements

- JDK 21
- Minecraft 1.21.4
- Fabric Loader 0.17.2+
- Fabric API

## Build

```bash
git clone https://github.com/YOUR_USER/amunix.git
cd amunix
./gradlew build
```

Output: `build/libs/amunix-1.7.0.jar`

## Install

Copy `build/libs/amunix-1.7.0.jar` to `%APPDATA%/.minecraft/mods/`

## Modules (60)

### Combat (9)
| Module | Description |
|--------|-------------|
| KillAura | Auto-attack with rotation, CPS, priority, FOV, auto block |
| ProjectileAura | Throw snowballs/eggs/rods at targets |
| FireballsAura | Hit/deflect incoming fireballs |
| Velocity | Reduce horizontal/vertical knockback (JumpReset/Hypixel) |
| AutoWeapon | Switch to best weapon on attack |
| NoAttackDelay | Remove 1.9+ attack cooldown (1.8 PvP) |
| AntiTeam | Prevent attacking teammates by color/armor |
| TargetConfig | Configure targeting settings |
| FakeLag | Simulate network latency on attack |

### Player (11)
| Module | Description |
|--------|-------------|
| Blink | Buffer outgoing packets, release on command |
| KillInsult | Auto-insult after kills (random/sequential/custom) |
| AutoGG | Send GG/GF at game end |
| AutoPlay | Auto-join next BedWars/SkyWars game |
| AutoMLG | Water bucket/hay bale clutch |
| AntiVoid | Freeze/boost/fly/pearl when falling into void |
| Disabler | Bypass anti-cheat (Hypixel/NCP/Vulcan) |
| Streamer | Obfuscate name and skin |
| SilentDisconnect | Instant disconnect without screen |
| AutoMessage | Periodic chat messages |
| FakeLag2 | Delay opponent position updates |

### Movement (7)
| Module | Description |
|--------|-------------|
| Speed | Vanilla/NCP/Hypixel/Ground/Legit speed modes |
| AutoSprint | Always sprint |
| NoSlow | No slowdown while eating/drinking/blocking/bow |
| NoJumpDelay | Remove jump delay |
| InvMove | Move while in inventory/chest screens |
| Freeze | Freeze in place |
| NoFluid | Normal movement in water/lava |

### Render (19)
| Module | Description |
|--------|-------------|
| ESP | Box/Outline/2D/Shader entity ESP with health bar |
| ChestESP | Chest entity outlines |
| StorageESP | Container outlines (chests/shulkers/furnaces) |
| Chams | Player model xray |
| AntiBlind | Remove blindness/nausea effects |
| Fullbright | Gamma/NightVision/Both |
| MurderMystery | Murder Mystery detective helper |
| Animations | Custom 1st person animations (1.7/Slide/Swing) |
| HUD | Client HUD overlay |
| ArrayList | Active module list on screen |
| Nametags | Enhanced player nametags with health/distance |
| NoHurtCam | Remove hurt camera shake |
| NoCameraClip | Third person camera doesn't clip through walls |
| NoChatBackground | Transparent chat background |
| Trajectories | Projectile trajectory lines |
| Particles | Control particle rendering |
| PostProcessing | Blur/Bloom/Outline/MotionBlur shaders |
| Watermark | Client watermark display |

### World (8)
| Module | Description |
|--------|-------------|
| Scaffold | Auto bridge/Tower/GodBridge (Normal/Tower/GodBridge/Legit) |
| Ambience | Change world time (Day/Night/Sunset/Custom) and weather |
| AutoTool | Auto-select best tool for mining |
| FastPlace | Remove block placement delay |
| LegacyCombat | Restore 1.8 combat mechanics |
| BedESP | BedWars bed ESP |
| ChestAura | Auto-open nearby chests |
| BedNuker | Auto-break beds in BedWars |

### Misc (7)
| Module | Description |
|--------|-------------|
| AntiBot | Filter NPCs/bots from target selection |
| GhostHand | Interact with blocks through walls |
| InvClicker | Inventory autoclicker |
| InvManager | Auto armor, drop trash, sort, refill hotbar |
| ClickGUI | Module settings GUI (Right Shift) |
| Stealer | Auto-loot chest contents |
| Notification | Module toggle notifications |

## Commands

All commands start with `.` in chat:

| Command | Description |
|---------|-------------|
| `.toggle <module>` | Toggle a module on/off |
| `.bind <module> <key>` | Set keybind for module |
| `.list` | List all enabled modules |
| `.help` | Show all commands |
| `.save` | Save config to disk |
| `.load` | Load config from disk |

## Config

Config stored in `config/amunix/`:
- `modules.json` - module enabled states
- `binds.json` - keybinds

## Project Structure

```
src/main/java/missu/amunix/
├── AmunixInitializer.java    Client entry point
├── ModLoader.java            Server entry point
├── core/                     Framework (8 files)
│   ├── ModuleManager.java    Singleton, registers all 60 modules
│   ├── BaseModule.java       Module base with lifecycle hooks
│   ├── ModuleCategory.java   COMBAT/PLAYER/MOVEMENT/RENDER/WORLD
│   ├── EventBus.java         Event pub/sub system
│   ├── HudRenderer.java      HUD rendering
│   ├── ConfigManager.java    JSON config save/load
│   ├── CommandSystem.java    Chat command handler
│   └── PacketFlow.java       SEND/RECEIVE enum
├── modules/
│   ├── combat/   9 modules
│   ├── player/   11 modules
│   ├── movement/ 7 modules
│   ├── render/   19 modules
│   ├── world/    8 modules
│   └── misc/     7 modules
├── settings/                 Setting types (4 files)
├── utils/                    Utilities (9 files)
└── mixins/                   Minecraft mixins (10 files)
```

## License

MIT
