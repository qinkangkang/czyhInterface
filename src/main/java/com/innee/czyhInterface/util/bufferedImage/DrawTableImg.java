package com.innee.czyhInterface.util.bufferedImage;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.OutputStream;

import javax.imageio.ImageIO;

/**
 * 使用bufferedImage类画出表格
 * 
 * @author jinsey
 *
 */
public class DrawTableImg {

	/**
	 * 二位数组方式画出图片表格
	 * 
	 * @param cellsValue
	 * @param path
	 */
	public void myGraphicsGeneration(String cellsValue[][], String path) {
		// 字体大小
		int fontTitileSize = 14;
		// 横线的行数
		int totalrow = cellsValue.length + 1;
		// 竖线的行数
		int totalcol = 0;
		if (cellsValue[0] != null) {
			totalcol = cellsValue[0].length;
		}
		// 图片宽度
		int imageWidth = 375;
		// 行高
		int rowheight = 40;
		// 图片高度
		int imageHeight = totalrow * rowheight + 50;
		// 起始高度
		int startHeight = -40;
		// 起始宽度
		int startWidth = 10;
		// 单元格宽度
		int colwidth = (int) ((imageWidth - 20) / totalcol);
		BufferedImage image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics = image.createGraphics();
		graphics.setColor(Color.WHITE);
		graphics.fillRect(0, 0, imageWidth, imageHeight);
		graphics.setColor(new Color(220, 240, 240));

		// 画横线
		for (int j = 0; j < totalrow; j++) {
			graphics.setColor(Color.LIGHT_GRAY);
			graphics.drawLine(startWidth, startHeight + (j + 1) * rowheight, startWidth + colwidth * totalcol,
					startHeight + (j + 1) * rowheight);
		}
		// // 画竖线
		// for (int k = 0; k < totalcol + 1; k++) {
		// graphics.setColor(Color.black);
		// graphics.drawLine(startWidth + k * colwidth, startHeight + rowheight,
		// startWidth + k * colwidth,
		// startHeight + rowheight * totalrow);
		// }
		// 设置字体
		Font font = new Font("微软雅黑", Font.PLAIN, fontTitileSize);
		graphics.setFont(font);
		// 写标题
		// String title = "【指标完成进度】";
		// graphics.drawString(title, startWidth, startHeight + rowheight - 10);
		// 写入内容
		for (int n = 0; n < cellsValue.length; n++) {
			for (int l = 0; l < cellsValue[n].length; l++) {
				// if (n == 0) {
				// font = new Font("微软雅黑", Font.BOLD, fontTitileSize);
				// graphics.setFont(font);
				// } else if (n > 0 && l > 0) {
				// font = new Font("微软雅黑", Font.PLAIN, fontTitileSize);
				// graphics.setFont(font);
				// graphics.setColor(Color.RED);
				// } else {
				font = new Font("微软雅黑", Font.PLAIN, fontTitileSize);
				graphics.setFont(font);
				graphics.setColor(Color.DARK_GRAY);
				// }
				graphics.drawString(cellsValue[n][l].toString(), startWidth + colwidth * l + 5,
						startHeight + rowheight * (n + 2) - 10);
			}
		}
		// 保存图片
		createImage(image, path);
	}

	/**
	 * 将图片保存到相对路径
	 * 
	 * @param image
	 * @param fileLocation
	 */
	public void createImage(BufferedImage image, String fileLocation) {
		try {
			OutputStream outImage = new FileOutputStream(fileLocation);
			ImageIO.write(image, "png", outImage);
			outImage.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		DrawTableImg cg = new DrawTableImg();
		try {
			String tableData1[][] = { { "商品规格", "商品参数", }, { "掌厅客户端（户）", "469281", }, { "掌厅客户端（户）", "469281", } };

			String[][] tableData2 = { { "8月31日（户）", "新增用户数", "日访问量", "累计用户数", "环比上月" },
					{ "合肥和巢湖", "469281", "1500000", "31.2%", "33.6%" }, { "芜湖", "469281", "1500000", "31.2%", "33.6%" },
					{ "蚌埠", "469281", "1500000", "31.2%", "33.6%" }, { "淮南", "469281", "1500000", "31.2%", "33.6%" },
					{ "马鞍山", "469281", "1500000", "31.2%", "33.6%" }, { "淮北", "469281", "1500000", "31.2%", "33.6%" } };
			cg.myGraphicsGeneration(tableData1, "e:\\myPic.jpg");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
