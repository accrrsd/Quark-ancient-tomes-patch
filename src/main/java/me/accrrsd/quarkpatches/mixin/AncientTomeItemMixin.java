package me.accrrsd.quarkpatches.mixin;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.enchantment.Enchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.violetmoon.quark.content.tools.item.AncientTomeItem;

@Mixin(AncientTomeItem.class)
public class AncientTomeItemMixin {

    @Inject(
            method = "getFullTooltipText",
            at = @At("HEAD"),
            cancellable = true,
            remap = false
    )
    private static void patchTooltipText(Enchantment ench, CallbackInfoReturnable<Component> cir) {
        int maxOverleveledValue = getConfigValue();
        Component tooltip = Component.translatable("quark.misc.ancient_tome_tooltip", Component.translatable(ench.getDescriptionId()), Component.translatable("enchantment.level." + (ench.getMaxLevel() + maxOverleveledValue))).withStyle(ChatFormatting.GRAY);
        cir.setReturnValue(tooltip);
    }
    @Unique
    private static int getConfigValue() {
        try {
            Class<?> configClass = Class.forName("me.accrrsd.quarkpatches.QuarkPatchesConfig");
            java.lang.reflect.Field field = configClass.getField("MAX_OVERLEVELED_VALUE");
            Object configValue = field.get(null);

            if (configValue instanceof net.minecraftforge.common.ForgeConfigSpec.IntValue) {
                return ((net.minecraftforge.common.ForgeConfigSpec.IntValue) configValue).get();
            }
        } catch (Exception e) {
        }
        return 3;
    }
}