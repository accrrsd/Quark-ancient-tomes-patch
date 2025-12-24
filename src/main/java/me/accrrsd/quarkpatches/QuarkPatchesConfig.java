package me.accrrsd.quarkpatches;

import net.minecraftforge.common.ForgeConfigSpec;

public class QuarkPatchesConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.IntValue MAX_OVERLEVELED_VALUE;

    static {
        BUILDER.push("Ancient Tomes Patch Settings");

        MAX_OVERLEVELED_VALUE = BUILDER
                .comment("How many levels above max an enchantment can be upgraded using Ancient Tomes.",
                        "Set to 1 for original Quark behavior (+1 only),",
                        "3 is default, you can use up to 100")
                .defineInRange("maxOverleveledValue", 3, 0, 100);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}