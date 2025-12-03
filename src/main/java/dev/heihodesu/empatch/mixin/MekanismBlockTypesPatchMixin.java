package dev.heihodesu.empatch.mixin;

import fr.iglee42.evolvedmekanism.registries.EMBlocks;
import mekanism.common.MekanismLang;
import mekanism.common.block.attribute.AttributeParticleFX;
import mekanism.common.block.attribute.AttributeStateActive;
import mekanism.common.block.attribute.AttributeStateFacing;
import mekanism.common.block.attribute.AttributeTier;
import mekanism.common.block.attribute.AttributeUpgradeSupport;
import mekanism.common.block.attribute.AttributeUpgradeable;
import mekanism.common.content.blocktype.BlockShapes;
import mekanism.common.content.blocktype.Machine;
import mekanism.common.lib.transmitter.TransmissionType;
import mekanism.common.registration.impl.BlockRegistryObject;
import mekanism.common.registration.impl.TileEntityTypeRegistryObject;
import mekanism.common.registries.MekanismBlockTypes;
import mekanism.common.registries.MekanismContainerTypes;
import mekanism.common.tier.ChemicalTankTier;
import mekanism.common.tier.EnergyCubeTier;
import mekanism.common.tile.TileEntityBin;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Supplier;

/**
 * This class includes code modified from 'Evolved Mekanism' by iglee42.
 * Original Source: https://github.com/iglee42/EvolvedMekanism
 * Licensed under the MIT License.
 */

@Mixin(value = MekanismBlockTypes.class, remap = false, priority = 500)
public class MekanismBlockTypesPatchMixin {
    @Inject(method = "createEnergyCube", at = @At("HEAD"), cancellable = true)
    private static <TILE extends TileEntityBin> void patchEnergyCube(EnergyCubeTier tier, Supplier<TileEntityTypeRegistryObject<TILE>> tile, Supplier<BlockRegistryObject<?, ?>> upgradeBlock, CallbackInfoReturnable<Machine<TILE>> cir) {
        if (tier.equals(EnergyCubeTier.ULTIMATE)) {
            cir.setReturnValue(Machine.MachineBuilder.createMachine(tile, MekanismLang.DESCRIPTION_ENERGY_CUBE)
                    .withGui(() -> MekanismContainerTypes.ENERGY_CUBE)
                    .withEnergyConfig(tier::getMaxEnergy)
                    .with(new AttributeTier<>(tier), new AttributeUpgradeable(() -> EMBlocks.OVERCLOCKED_ENERGY_CUBE), new AttributeStateFacing(BlockStateProperties.FACING))
                    .withSideConfig(TransmissionType.ENERGY, TransmissionType.ITEM)
                    .without(AttributeParticleFX.class, AttributeStateActive.class, AttributeUpgradeSupport.class)
                    .withComputerSupport(tier, "EnergyCube")
                    .build());
        }
    }
    @Inject(method = "createChemicalTank", at = @At("HEAD"), cancellable = true)
    private static <TILE extends TileEntityBin> void patchChemicalTank(ChemicalTankTier tier, Supplier<TileEntityTypeRegistryObject<TILE>> tile, Supplier<BlockRegistryObject<?, ?>> upgradeBlock, CallbackInfoReturnable<Machine<TILE>> cir) {
        if (tier.equals(ChemicalTankTier.ULTIMATE)) {
            cir.setReturnValue(Machine.MachineBuilder.createMachine(tile, MekanismLang.DESCRIPTION_CHEMICAL_TANK)
                    .withGui(() -> MekanismContainerTypes.CHEMICAL_TANK)
                    .withCustomShape(BlockShapes.CHEMICAL_TANK)
                    .with(new AttributeTier<>(tier), new AttributeUpgradeable(() -> EMBlocks.OVERCLOCKED_CHEMICAL_TANK))
                    .withSideConfig(TransmissionType.CHEMICAL, TransmissionType.ITEM)
                    .without(AttributeParticleFX.class, AttributeStateActive.class, AttributeUpgradeSupport.class)
                    .withComputerSupport(tier, "ChemicalTank")
                    .build());
        }
    }
}