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
		grabber.setOption("rtsp_transport", "tcp"); // ʹ��tcp�ķ�ʽ
		grabber.setImageWidth(width);
		grabber.setImageHeight(height);
		
		System.out.println("grabber start");
		grabber.start(); // ��ʼ��ȡ����ͷ����
		CanvasFrame canvas = new CanvasFrame("cam");// �½�һ������
		canvas.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		canvas.setAlwaysOnTop(true);

		while (true) {
			if (!canvas.isDisplayable()) {// �����Ƿ�ر�
				grabber.stop();// ֹͣץȡ
				System.exit(2);// �˳�
			}
			canvas.showImage(grabber.grab());// ��ȡ����ͷͼ�񲢷ŵ���������ʾ�� �����Frame
												// frame=grabber.grab();
												// frame��һ֡��Ƶͼ��

			Thread.sleep(50);// 50����ˢ��һ��ͼ��
		}
	}
}
