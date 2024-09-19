package lol.skye.atlasdumper.mixin.pre1141;

import com.llamalad7.mixinextras.sugar.Local;
import lol.skye.atlasdumper.util.TextureDumper;
import net.minecraft.client.render.texture.TextureAtlas;
import net.minecraft.client.render.texture.TextureStitcher;
import net.minecraft.client.resource.manager.ResourceManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TextureAtlas.class)
public class StandardTextureAtlasMixin {

	@Shadow
	private int maxMipLevel;

	@Final
	@Shadow
	private String path;

	@Inject(method = "m_8596643", at = @At("TAIL"), remap = false)
	private void loadAndStitch(ResourceManager resourceManager, CallbackInfo ci,
							   @Local TextureStitcher textureStitcher) {
		TextureAtlas atlas = (TextureAtlas) (Object) this;
		int width = textureStitcher.getWidth();
		int height = textureStitcher.getHeight();

		TextureDumper.dumpTexture(
				atlas.getGlId(),
				path,
				maxMipLevel,
				width,
				height);
	}

}
