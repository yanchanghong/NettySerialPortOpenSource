package changhongyun.NettySerialPortOpenSource.serialPort.netty;

import java.util.List;

import org.apache.log4j.Logger;

import changhongyun.NettySerialPortOpenSource.util.Convert;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

public class BytesEncoder extends MessageToMessageEncoder<byte[]>{
	private static final Logger logger = Logger.getLogger(BytesEncoder.class.getName());
	
	/**
	 * The starting bytes of a message. This is fixed, defined in the documentation.
	 */
	public static final int[] PREAMBLE_DLMTR = {0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0x86};
	
	
	 @Override
	 protected void encode(ChannelHandlerContext ctx, byte[] msg, List<Object> out) throws Exception {
		 //需要对msg加密的操作
		 logger.info("msg:" + Convert.ByteArrayToHexString(msg));
		/* byte[] payload = constructSerialMessage(1, msg);//sendId 现在是模拟的
		 out.add(Unpooled.wrappedBuffer(payload));*/
	 }
	
}
