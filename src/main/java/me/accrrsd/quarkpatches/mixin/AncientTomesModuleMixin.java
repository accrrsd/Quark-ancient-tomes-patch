package me.accrrsd.quarkpatches.mixin;

import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.violetmoon.quark.content.tools.module.AncientTomesModule;
import org.violetmoon.zeta.event.play.ZAnvilUpdate;

import java.util.Map;

@Mixin(AncientTomesModule.class)
public class AncientTomesModuleMixin {

    @Inject(
            method = "onAnvilUpdate(Lorg/violetmoon/zeta/event/play/ZAnvilUpdate$Highest;)V",
            at = @At("HEAD"),
            cancellable = true,
            remap = false)
    private void patchedOnAnvilUpdate(ZAnvilUpdate.Highest event, CallbackInfo ci) {
        ItemStack left = event.getLeft();
        ItemStack right = event.getRight();
        String name = event.getName();

        if (!left.isEmpty() && !right.isEmpty() && left.getCount() == 1 && right.getCount() == 1) {

            if (right.is(AncientTomesModule.ancient_tome)) {
                if (!AncientTomesModule.combineWithBooks && left.is(Items.ENCHANTED_BOOK))
                    return;

                Enchantment ench = AncientTomesModule.getTomeEnchantment(right);
                Map<Enchantment, Integer> enchants = EnchantmentHelper.getEnchantments(left);

                if (ench != null && enchants.containsKey(ench)) {
                    int currentLevel = enchants.get(ench);
                    int maxLevel = ench.getMaxLevel();

                    int maxOverleveledValue = getConfigValue();

                    if (currentLevel >= maxLevel + maxOverleveledValue) {
                        return;
                    }

                    int newLevel = currentLevel + 1;
                    enchants.put(ench, newLevel);

                    ItemStack out = left.copy();
                    EnchantmentHelper.setEnchantments(enchants, out);
                    int cost = newLevel > maxLevel ? AncientTomesModule.limitBreakUpgradeCost : AncientTomesModule.normalUpgradeCost;

                    if (name != null && !name.isEmpty() && (!out.hasCustomHoverName() || !out.getHoverName().getString().equals(name))) {
                        out.setHoverName(Component.literal(name));
                        cost++;
                    }

                    event.setOutput(out);
                    event.setCost(cost);
                    ci.cancel();
                }
            }
        }
    }
    @Unique
    private int getConfigValue() {
        try {
            Class<?> configClass = Class.forName("me.accrrsd.quarkpatches.QuarkPatchesConfig");
            java.lang.reflect.Field field = configClass.getField("MAX_OVERLEVELED_VALUE");
            Object configValue = field.get(null);

            if (configValue instanceof net.minecraftforge.common.ForgeConfigSpec.IntValue) {
                return ((net.minecraftforge.common.ForgeConfigSpec.IntValue) configValue).get();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 3;
    }
}