package lol.skye.atlasdumper.util;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.IntBuffer;

public class TextureDumper {

	public static void dumpTexture(int glId, String path, int maxMipLevel, int width, int height) {
		GL11.glBindTexture(3553, glId);
		GL11.glPixelStorei(GL11.GL_PACK_ALIGNMENT, 1);
		GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
		for (int mip = 0; mip <= Math.max(1, maxMipLevel); mip++) {
			int stitchWidth = width >> mip;
			int stitchHeight = height >> mip;
			int capacity = stitchWidth * stitchHeight;

			IntBuffer buffer = BufferUtils.createIntBuffer(capacity);
			int[] arr = new int[capacity];
			GL11.glGetTexImage(GL11.GL_TEXTURE_2D, mip, 32993, 33639, buffer);
			buffer.get(arr);

			BufferedImage image = new BufferedImage(stitchWidth, stitchHeight, BufferedImage.TYPE_INT_ARGB);
			image.setRGB(0, 0, stitchWidth, stitchHeight, arr, 0, stitchWidth);
			try {
				String fileName = path.replace("/", "_") + "_" + mip + ".png";
				ImageIO.write(image, "png", new File(fileName));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
