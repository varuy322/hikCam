package HikJavaCVCamShift;

import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.OpenCVFrameGrabber;

import javax.swing.JFrame;

import org.bytedeco.javacv.FrameGrabber.Exception;
import org.bytedeco.javacv.OpenCVFrameConverter;

public class cameraTest {
	public static void main(String[] args) throws Exception, InterruptedException {
		String rtspPath = "rtsp://admin:Aa11111111@192.168.1.65:554";
		@SuppressWarnings("resource")
		//OpenCVFrameGrabber grabber = new OpenCVFrameGrabber(0);
		FFmpegFrameGrabber grabber = FFmpegFrameGrabber.createDefault(rtspPath);
		int width = 640, height = 480;
		grabber.setOption("rtsp_transport", "tcp"); // 使用tcp的方式
		grabber.setImageWidth(width);
		grabber.setImageHeight(height);
		
		System.out.println("grabber start");
		grabber.start(); // 开始获取摄像头数据
		CanvasFrame canvas = new CanvasFrame("cam");// 新建一个窗口
		canvas.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		canvas.setAlwaysOnTop(true);

		while (true) {
			if (!canvas.isDisplayable()) {// 窗口是否关闭
				grabber.stop();// 停止抓取
				System.exit(2);// 退出
			}
			canvas.showImage(grabber.grab());// 获取摄像头图像并放到窗口上显示， 这里的Frame
												// frame=grabber.grab();
												// frame是一帧视频图像

			Thread.sleep(50);// 50毫秒刷新一次图像
		}
	}
}
