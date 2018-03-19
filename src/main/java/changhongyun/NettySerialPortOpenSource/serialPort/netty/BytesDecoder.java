package changhongyun.NettySerialPortOpenSource.serialPort.netty;

import java.util.List;

import org.apache.log4j.Logger;

import changhongyun.NettySerialPortOpenSource.util.Convert;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

public class BytesDecoder extends MessageToMessageDecoder<ByteBuf> {

	private static final Logger logger = Logger.getLogger(BytesDecoder.class.getName());


	private StringBuffer recevieData = new StringBuffer();


	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
		// copy the ByteBuf content to a byte array
		byte[] array = new byte[msg.readableBytes()];
		msg.getBytes(0, array);
		logger.info("decode message:" + Convert.ByteArrayToHexString(array));
		// 得到数据后去按照具体的格式去解析。
	}
	
}
