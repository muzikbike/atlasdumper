package lol.skye.atlasdumper.mixin;

import lol.skye.atlasdumper.AtlasDumper;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.SemanticVersion;
import net.fabricmc.loader.impl.FabricLoaderImpl;
import net.fabricmc.loader.impl.game.minecraft.MinecraftGameProvider;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public class MixinPlugin implements IMixinConfigPlugin {
	private int major, minor, patch;
	private int year = -1, week = -1;

	@Override
	public void onLoad(String mixinPackage) {
		MinecraftGameProvider mc = (MinecraftGameProvider)
				FabricLoaderImpl.INSTANCE.getGameProvider();
		ModContainer mcMod = FabricLoader.getInstance().getModContainer("minecraft")
				.orElseThrow(() -> new RuntimeException("no minecraft mod container"));

		SemanticVersion version = (SemanticVersion) mcMod.getMetadata().getVersion();
		major = version.getVersionComponent(0);
		minor = version.getVersionComponent(1);
		patch = version.getVersionComponent(2);
		if (patch == 0) {
			AtlasDumper.LOGGER.info("detected minecraft version {}.{}", major, minor);
		} else {
			AtlasDumper.LOGGER.info("detected minecraft version {}.{}.{}", major, minor, patch);
		}

		try {
			String versionString = mc.getNormalizedGameVersion();
			if (versionString.contains("-alpha")) {
				String[] parts = versionString.split("\\.");
				year = Integer.parseInt(parts[parts.length - 3]);
				week = Integer.parseInt(parts[parts.length - 2]);
				AtlasDumper.LOGGER.info("detected snapshot version year {} week {}", year, week);
			}
		} catch (NumberFormatException ignored) {
		}
	}

	@Override
	public String getRefMapperConfig() {
		return null;
	}

	@Override
	public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
		if (mixinClassName.contains("compat114")) {
			// this mixin should only load on >= 1.14.1
			// if you're on a 1.14 snapshot, it should only load >= 19w06a
			boolean is114 = major == 1 && minor == 14;
			boolean validSnapshot = year == 19 && week >= 6;
			return is114 && (patch >= 1 || validSnapshot);
		}

		if (mixinClassName.contains("pre1141")) {
			// not 1.14, or is 18w43a-19w05a
			boolean isPreChange = year == 18 || (year == 19 && week < 6);
			return (major == 1 && minor < 14)
					|| (major == 1 && minor == 14 && isPreChange);
		}

		// ?
		return true;
	}

	@Override
	public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {

	}

	@Override
	public List<String> getMixins() {
		return null;
	}

	@Override
	public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

	}

	@Override
	public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

	}
}
