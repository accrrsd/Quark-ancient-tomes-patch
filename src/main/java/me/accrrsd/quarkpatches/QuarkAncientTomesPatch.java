package me.accrrsd.quarkpatches;

import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

@Mod("quark_ancient_tomes_patch")
public class QuarkAncientTomesPatch {
    public QuarkAncientTomesPatch() {
        ModLoadingContext.get().registerConfig(
                ModConfig.Type.COMMON,
                QuarkPatchesConfig.SPEC,
                "quark-ancient-tomes-patch.toml"
        );
    }
}