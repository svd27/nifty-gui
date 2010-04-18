package de.lessvoid.nifty.java2d.renderer;

import java.awt.Canvas;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.net.URL;

import de.lessvoid.nifty.render.BlendMode;
import de.lessvoid.nifty.spi.render.RenderDevice;
import de.lessvoid.nifty.spi.render.RenderFont;
import de.lessvoid.nifty.spi.render.RenderImage;
import de.lessvoid.nifty.tools.Color;

public class RenderDeviceJava2dImpl implements RenderDevice {

	Graphics graphics;

	private final Canvas canvas;

	private BufferedImage offscreenImage;

	public RenderDeviceJava2dImpl(Canvas canvas) {
		this.canvas = canvas;
		offscreenImage = (BufferedImage) canvas.createImage(getWidth(),
				getHeight());
	}

	@Override
	public void beginFrame() {

		if (screenSizeHasChanged())
			offscreenImage = (BufferedImage) canvas.createImage(getWidth(),
					getHeight());
		graphics = offscreenImage.getGraphics();
	}

	private boolean screenSizeHasChanged() {
		return (offscreenImage.getWidth() != getWidth() || offscreenImage
				.getHeight() != getHeight());
	}

	@Override
	public void endFrame() {
		canvas.getGraphics().drawImage(offscreenImage, 0, 0, null);
	}

	@Override
	public void clear() {
		graphics.clearRect(0, 0, getWidth(), getHeight());
	}

	@Override
	public RenderFont createFont(String filename) {
		// try {
		java.awt.Font font = new Font("arial", Font.BOLD, 17);
		// java.awt.Font font = Font.createFont(Font.TYPE1_FONT, Thread
		// .currentThread().getContextClassLoader().getResource(
		// filename).openStream());
		return new RenderFontJava2dImpl(this, font);
		// } catch (FontFormatException e) {
		// throw new RuntimeException("", e);
		// } catch (IOException e) {
		// throw new RuntimeException("", e);
		// }
	}

	@Override
	public RenderImage createImage(String filename, boolean filterLinear) {
		URL imageUrl = Thread.currentThread().getContextClassLoader()
				.getResource(filename);
		Toolkit tk = Toolkit.getDefaultToolkit();
		Image image = tk.getImage(imageUrl);
		return new RenderImageJava2dImpl(image);
	}

	@Override
	public void disableClip() {

	}

	@Override
	public void enableClip(int x0, int y0, int x1, int y1) {

	}

	@Override
	public int getHeight() {
		return canvas.getSize().height;
	}

	@Override
	public int getWidth() {
		return canvas.getSize().width;
	}

	@Override
	public void renderImage(RenderImage image, int x, int y, int width,
			int height, Color color, float imageScale) {

		if (!(image instanceof RenderImageJava2dImpl))
			return;

		RenderImageJava2dImpl renderImage = (RenderImageJava2dImpl) image;
		// graphics.drawImage(renderImage.image, x, y, null);
		graphics.drawImage(renderImage.image, x, y, x + width, y + height, 0,
				0, renderImage.getWidth(), renderImage.getHeight(), null);
	}

	@Override
	public void renderImage(RenderImage image, int x, int y, int w, int h,
			int srcX, int srcY, int srcW, int srcH, Color color, float scale,
			int centerX, int centerY) {

		if (!(image instanceof RenderImageJava2dImpl))
			return;

		RenderImageJava2dImpl renderImage = (RenderImageJava2dImpl) image;
		graphics.drawImage(renderImage.image, x, y, x + w, y + h, srcX, srcY,
				srcX + srcW, srcY + srcH, null);

	}

	public java.awt.Color convertNiftyColor(Color color) {
		return new java.awt.Color(color.getRed(), color.getGreen(), color
				.getBlue(), color.getAlpha());
	}

	@Override
	public void renderQuad(int x, int y, int width, int height, Color color) {
		graphics.setColor(convertNiftyColor(color));
		graphics.fillRect(x, y, width, height);
	}

	@Override
	public void renderQuad(int x, int y, int width, int height, Color topLeft, Color topRight, Color bottomRight, Color bottomLeft) {
    graphics.setColor(convertNiftyColor(topLeft));
    graphics.fillRect(x, y, width, height);
	}

	@Override
	public void setBlendMode(BlendMode renderMode) {

	}

	@Override
	public void renderFont(RenderFont font, String text, int x, int y,
			Color fontColor, float size) {

		if (!(font instanceof RenderFontJava2dImpl))
			return;

		RenderFontJava2dImpl renderFont = (RenderFontJava2dImpl) font;

		graphics.setFont(renderFont.getFont());
		graphics.setColor(convertNiftyColor(fontColor));
		graphics.drawString(text, x, y);
	}

}