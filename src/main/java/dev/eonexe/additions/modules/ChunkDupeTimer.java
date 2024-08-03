package dev.eonexe.additions.modules;

import dev.eonexe.additions.Additions;
import meteordevelopment.meteorclient.settings.DoubleSetting;
import meteordevelopment.meteorclient.settings.IntSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Module;

public class ChunkDupeTimer extends Module {
    private final SettingGroup sgGeneral = this.settings.getDefaultGroup();
    private final SettingGroup sgRender = this.settings.createGroup("Render");

    public final Setting<Double> delay = sgGeneral.add(new DoubleSetting.Builder()
        .name("delay")
        .description("Delay displayed in seconds.")
        .defaultValue(30.0d)
        .range(1.0d, 1000.0d)
        .build()
    );

    public ChunkDupeTimer() {
        super(Additions.CATEGORY, "chunk-dupe-timer", "Timer in the multiplayer screen to tell you when to relog.");
    }

}
