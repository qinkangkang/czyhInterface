package com.innee.czyhInterface.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;

public class ImageUtil {

	public static final String ThumbnailFileFlag = "_thumbnail_";

	public static void thumbnail(File sourcePath, File targetPath, int width, int height, boolean ratio)
			throws IOException {
		Thumbnails.of(sourcePath).size(width, height).keepAspectRatio(ratio).toFile(targetPath);
	}

	public static void thumbnailAndWatermark(File sourcePath, File targetPath, int width, int height, boolean ratio)
			throws IOException {
		Thumbnails.of(sourcePath).size(width, height).keepAspectRatio(ratio)
				.watermark(Positions.BOTTOM_LEFT, Constant.watermarkFile, 0.6f).toFile(targetPath);
	}

	public static void addWatermark(File sourcePath, File targetPath, boolean ratio) throws IOException {
		BufferedImage sourceImage = ImageIO.read(sourcePath);
		int width = sourceImage.getWidth();
		int height = sourceImage.getHeight();
		Thumbnails.of(sourcePath).size(width, height).keepAspectRatio(ratio)
				.watermark(Positions.BOTTOM_LEFT, Constant.watermarkFile, 0.6f).toFile(targetPath);
	}

	public static void thumbnail(File sourcePath, File targetPath, int x, int y, int width, int height, int targetWidth,
			int targetHeight) throws IOException {
		Thumbnails.of(sourcePath).sourceRegion(x, y, width, height).size(targetWidth, targetHeight)
				.keepAspectRatio(false).toFile(targetPath);
	}

	public static void square(File sourcePath, File targetPath, int SquareSideLength) throws IOException {
		BufferedImage sourceImage = ImageIO.read(sourcePath);
		int width = sourceImage.getWidth();
		int height = sourceImage.getHeight();
		int temp = SquareSideLength;
		if (width != -1 && height != -1) {
			temp = width < height ? width : height;
		}
		Thumbnails.of(sourceImage).sourceRegion(Positions.CENTER, temp, temp).size(SquareSideLength, SquareSideLength)
				.keepAspectRatio(false).toFile(targetPath);
	}

	public static void squareAndWatermark(File sourcePath, File targetPath, int SquareSideLength) throws IOException {
		BufferedImage sourceImage = ImageIO.read(sourcePath);
		int width = sourceImage.getWidth();
		int height = sourceImage.getHeight();
		int temp = SquareSideLength;
		if (width != -1 && height != -1) {
			temp = width < height ? width : height;
		}
		Thumbnails.of(sourceImage).sourceRegion(Positions.CENTER, temp, temp).size(SquareSideLength, SquareSideLength)
				.keepAspectRatio(false).watermark(Positions.BOTTOM_LEFT, Constant.watermarkSmFile, 0.6f)
				.toFile(targetPath);
	}

	public static void toJpg(File sourceFile, File targetPath) throws IOException {
		BufferedImage sourceImage = ImageIO.read(sourceFile);
		int width = sourceImage.getWidth();
		int height = sourceImage.getHeight();
		Thumbnails.of(sourceFile).size(width, height).outputFormat("jpg").toFile(targetPath);
	}

	public static void main(String[] args) {
		// try {
		// BufferedImage sourceImage = ImageIO.read(new File("d:/90.gif"));
		// int width = sourceImage.getWidth();
		// int height = sourceImage.getHeight();
		// int temp =
		// Integer.valueOf(PropertiesUtil.getProperty("productSquareSideLength"));
		// if (width != -1 && height != -1) {
		// temp = width > height ? width : height;
		// }
		// Thumbnails.of(sourceImage).size(Integer.valueOf(PropertiesUtil.getProperty("logoSquareWidth")),
		// Integer.valueOf(PropertiesUtil.getProperty("logoSquareHeight"))).toFile("d:/999.gif");
		// } catch (IOException e) {
		// logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(e)));
		// }
	}
}