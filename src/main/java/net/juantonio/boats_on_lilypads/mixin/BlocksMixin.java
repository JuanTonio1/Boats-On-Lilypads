package net.juantonio.boats_on_lilypads.mixin;

import net.juantonio.boats_on_lilypads.CustomLilyPadBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.LilyPadBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Blocks.class)
public class BlocksMixin {
    @Redirect(method = "<clinit>", at = @At(value = "NEW", target = "net/minecraft/block/LilyPadBlock"))
    private static LilyPadBlock redirectLilyPadBlock(Block.Settings settings) {
        return new CustomLilyPadBlock(settings);
    }
}
