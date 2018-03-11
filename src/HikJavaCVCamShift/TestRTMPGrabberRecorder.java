package HikJavaCVCamShift;

import org.bytedeco.javacpp.avcodec;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.OpenCVFrameConverter;

public class TestRTMPGrabberRecorder {
	static boolean exit = false;

	public static void main(String[] args) throws Exception {
		System.out.println("start...");
		//String rtmpPath = "rtmp://casic207-pc1/live360p/ss1";
		// String rtspPath = "rtmp://live.hkstv.hk.lxdns.com/live/hks"; // �������
		// "rtsp://184.72.239.149/vod/mp4://BigBuckBunny_175k.mov";
		String rtspPath = "rtsp://admin:Aa11111111@192.168.1.65:554/h264/ch1/main/av_stream";
		// ffmpeg -f rtsp -rtsp_transport tcp -i		
		// rtmp://casic207-pc1/live360p/ss1
		// ffmpeg -i
		// rtsp://admin:123123@192.168.1.64:554/h264/ch1/main/av_stream -vcodec
		// copy -acodec copy -f flv rtmp://casic207-pc1/live360p/ss1
		int audioRecord = 0; // 0 = ��¼�ƣ�1=¼��
		boolean saveVideo = false;
		test(rtspPath, audioRecord, saveVideo);
		System.out.println("end...");
	}

	public static void test(String rtspPath, int audioRecord,
			boolean saveVideo) throws Exception {
		//FrameGrabber grabber = FrameGrabber.createDefault(0); // ��������ͷ Ĭ��
		// ʹ��rtsp��ʱ����Ҫʹ�� FFmpegFrameGrabber���������� FrameGrabber
		int width = 640, height = 480;
		FFmpegFrameGrabber grabber = FFmpegFrameGrabber.createDefault(rtspPath);
		grabber.setOption("rtsp_transport", "tcp"); // ʹ��tcp�ķ�ʽ����Ȼ�ᶪ��������
		// һֱ�����ԭ�򣡣���������Ϊ�� 2560 * 1440��̫���ˡ���
		grabber.setImageWidth(width);
		grabber.setImageHeight(height);
		System.out.println("grabber start");
		grabber.start();
		// FrameRecorder recorder = FrameRecorder.createDefault(rtmpPath,
		// 640,480,0);
		// ��ý�������ַ���ֱ��ʣ������ߣ����Ƿ�¼����Ƶ��0:��¼��/1:¼�ƣ�
		FFmpegFrameRecorder recorder = new FFmpegFrameRecorder(rtspPath, width,
				height, audioRecord);
		recorder.setInterleaved(true);
		recorder.setVideoOption("crf", "28");
		recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264); // 28
		//recorder.setFormat("flv"); // rtmp������
		recorder.setFrameRate(25);
		recorder.setPixelFormat(0); // yuv420p
		System.out.println("recorder start");
		recorder.start();
		//
		OpenCVFrameConverter.ToIplImage conveter = new OpenCVFrameConverter.ToIplImage();
		System.out.println("all start!!");
		int count = 0;
		while (!exit) {
			count++;
			Frame frame = ((FFmpegFrameGrabber) grabber).grabImage();
			if (frame == null) {
				continue;
			}
			if (count % 100 == 0) {
				System.out.println("count=" + count);
			}
			recorder.record(frame);
		}
		grabber.stop();
		grabber.release();
		recorder.stop();
		recorder.release();
	}
}