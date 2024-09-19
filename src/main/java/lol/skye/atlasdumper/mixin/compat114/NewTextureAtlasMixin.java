package lol.skye.atlasdumper.mixin.compat114;

import lol.skye.atlasdumper.util.TextureDumper;
import net.minecraft.client.render.texture.TextureAtlas;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TextureAtlas.class)
public class NewTextureAtlasMixin {

	@Shadow
	private int maxMipLevel;

	@Final
	@Shadow
	private String path;

	@Inject(method = "m_3299210", at = @At("TAIL"))
	private void loadAndStitch(TextureAtlas.C_5897828 c_5897828, CallbackInfo ci) {
		TextureAtlas atlas = (TextureAtlas) (Object) this;
		int width = c_5897828.f_7364706;
		int height = c_5897828.f_2608719;

		TextureDumper.dumpTexture(
				atlas.getGlId(),
				path,
				maxMipLevel,
				width,
				height);
	}
}
