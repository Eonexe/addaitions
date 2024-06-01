package dev.eonexe.additions;

import dev.eonexe.additions.commands.CommandExample;
import dev.eonexe.additions.hud.HudExample;
import dev.eonexe.additions.modules.ModuleExample;
import com.mojang.logging.LogUtils;
import meteordevelopment.meteorclient.addons.GithubRepo;
import meteordevelopment.meteorclient.addons.MeteorAddon;
import meteordevelopment.meteorclient.commands.Commands;
import meteordevelopment.meteorclient.systems.hud.Hud;
import meteordevelopment.meteorclient.systems.hud.HudGroup;
import meteordevelopment.meteorclient.systems.modules.Category;
import meteordevelopment.meteorclient.systems.modules.Modules;
import org.slf4j.Logger;

public class Additions extends MeteorAddon {
    public static final Logger LOG = LogUtils.getLogger();
    public static final Category CATEGORY = new Category("Additions");
    public static final HudGroup HUD_GROUP = new HudGroup("Additions");

    @Override
    public void onInitialize() {
        LOG.info("Initializing Eonexe's Meteor Additions");

        Modules register = Modules.get();
        Hud hud = Hud.get();

        // Modules
        register.add(new ModuleExample());

        // Commands
        Commands.add(new CommandExample());

        // HUD
        hud.register(HudExample.INFO);
    }

    @Override
    public void onRegisterCategories() {
        Modules.registerCategory(CATEGORY);
    }

    @Override
    public String getPackage() {
        return "dev.eonexe.additions";
    }

    @Override
    public GithubRepo getRepo() {
        return new GithubRepo("Eonexe", "additions");
    }
}
