package changhongyun.NettySerialPortOpenSource.serialPort.netty;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;

import changhongyun.NettySerialPortOpenSource.util.Convert;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ChannelFactory;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.oio.OioEventLoopGroup;
import io.netty.channel.rxtx.RxtxChannel;
import io.netty.channel.rxtx.RxtxDeviceAddress;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

public class RxtxClient extends ChannelInboundHandlerAdapter implements ApPortConnection {

	private static final Logger logger = Logger.getLogger(RxtxClient.class.getName());

	private static String serialport = "/dev/ttymxc2";
	static {
		logger.info("serial port:" + serialport);
	}
	public static final int DEFAULT_QUEUE_SIZE = 100;
	public static final int AP_MSG_MAX_LENGTH = 262;// before is 256 262

	private BlockingQueue<byte[]> receviveMsgQueue = new LinkedBlockingQueue<byte[]>(DEFAULT_QUEUE_SIZE);

	private BlockingQueue<byte[]> sendBoundQueue = new LinkedBlockingQueue<byte[]>(DEFAULT_QUEUE_SIZE);

	private RxtxChannel channel; // 也用到基础包，RXTXcomm.jar
	private ChannelFuture future;
	private EventLoopGroup group;

	private RxtxClient() {
	}

	private static final RxtxClient rxtxClient = new RxtxClient();

	/*
	 * LengthFieldBasedFrameDecoder 定长解码器
	 */
	@SuppressWarnings("deprecation")
	public void initialize() throws Exception {
		// Get the port to listen from.

		group = new OioEventLoopGroup();
		try {
			Bootstrap b = new Bootstrap();
			b.group(group).channelFactory(new ChannelFactory<RxtxChannel>() {
				public RxtxChannel newChannel() {
					return channel;
				}
			}).handler(new ChannelInitializer<RxtxChannel>() {
				@Override
				public void initChannel(RxtxChannel ch) throws Exception {
					ch.pipeline().addLast(
							// outbound handler
							new BytesEncoder(),
							// inbound handler
							// new SerialDebugIncoming(),
							new LengthFieldBasedFrameDecoder(AP_MSG_MAX_LENGTH, 6, 1, 0, 0, true), new BytesDecoder(),
							rxtxClient);
				}
			});

			channel = new RxtxChannel();
			channel.config().setBaudrate(57600);// 配置波特率
			future = b.connect(new RxtxDeviceAddress(serialport)).sync();

		} catch (Exception e) {
			logger.error("netty connect resial error", e);
		}
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) {
		// 程序运行起来第一次下发的命令
		// ctx.writeAndFlush(Object msg);
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		byte[] payload = (byte[]) msg;
		logger.debug("receiving data:" + Convert.ByteArrayToHexString(payload));
		// 处理从串口返回来的数据
	}

	public void launch() {
		ExecutorService executor = Executors.newFixedThreadPool(4);// 初始化四个线程池
		// start to execute the incoming queue
		executor.execute(new Runnable() {
			public void run() {
				while (!Thread.currentThread().isInterrupted()) {
					try {
						byte[] message = receviveMsgQueue.take();
						logger.debug("recevive:" + Convert.ByteArrayToHexString(message));
					} catch (InterruptedException e) {
						logger.error("parse message frame error:", e);
						Thread.currentThread().interrupt();
					}
				}
			}
		});

		// start to execute the outcming queue
		executor.execute(new Runnable() {
			public void run() {
				while (!Thread.currentThread().isInterrupted()) {
					try {
						byte[] frame = sendBoundQueue.take();
						channel.writeAndFlush(frame).sync();
					} catch (InterruptedException e) {
						logger.error("write mesage to node error", e);
					}
				}
			}
		});
	}

	public void readMessage(byte[] message) {
		receviveMsgQueue.add(message);
		if (receviveMsgQueue.size() == DEFAULT_QUEUE_SIZE) {
			receviveMsgQueue.clear();
			logger.error("over inboundQueue size");
		}
		// 记录下上条数据和当前时间对比，判断

	}

	public void sendMessage(byte[] data) throws IOException {
		logger.debug("add message:" + Convert.ByteArrayToHexString(data));
		if (sendBoundQueue.offer(data) == false) {
			logger.info("add message to ap frame queue fail");
		}
	}

	public void close() throws Exception {
		if (future != null) {
			try {
				logger.info("wating for");
				channel.close();
				future.channel().closeFuture().sync();
				logger.info("stop");
			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			}
		}
		if (group != null)
			group.shutdownGracefully();
	}

	public static final RxtxClient newRxtxClient() {
		return rxtxClient;
	}

}
