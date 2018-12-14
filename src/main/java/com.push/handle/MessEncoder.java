package com.push.handle;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.apache.mina.filter.codec.demux.MessageEncoder;

/**
 * 编码器
 * @author zf
 */
public class MessEncoder implements MessageEncoder<String> {

	/**
	 * 编码器未做任何处理
	 * @param session
	 * @param msg
	 * @param out
	 * @throws Exception
	 * String  转     数组
	 */
	public void encode(IoSession session, String msg, ProtocolEncoderOutput out)
			throws Exception {
		//分配字节缓冲区
		IoBuffer buf = IoBuffer.allocate(msg.getBytes().length);
		//16进制转byte数组
		byte[] bytes = hex2byte(msg);  
		buf.put(bytes);
		buf.flip();
		out.write(buf);
	}
	
	/**
	 * 十六进制字符串转化为byte数组
	 * @return the array of byte
	 */
	public static final byte[] hex2byte(String hex) throws IllegalArgumentException {
		if (hex.length() % 2 != 0) {
			throw new IllegalArgumentException();
		}
		char[] arr = hex.toCharArray();
		byte[] b = new byte[hex.length() / 2];
		for (int i = 0, j = 0, l = hex.length(); i < l; i++, j++) {
			String swap = "" + arr[i++] + arr[i];
			int byteint = Integer.parseInt(swap, 16) & 0xFF;
			b[j] = new Integer(byteint).byteValue();
		}
		return b;
	}
	
}
