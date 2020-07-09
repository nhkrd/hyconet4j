package jp.or.nhk.rd.hyconet4j;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshakerFactory;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketVersion;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;

import io.netty.handler.codec.http.websocketx.WebSocketHandshakeException;

import java.net.InetSocketAddress;
import java.net.URI;
import java.lang.NullPointerException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.json.JSONjava.JSONObject;
import org.json.JSONjava.JSONException;

public final class WebSocketClient {
	private static _Logger logger = null;

	private final Bootstrap bootstrap = new Bootstrap();
	private Channel ch;
	private final URI uri;
	private final WebSocketVersion version;
	private volatile boolean closed;
	private String subprotocol = "Hybridcast";
	private static TVRCDevinfo devinfo;

	private static boolean connect_flag = false;
	private static String  fail_reason = "";

	private static final AtomicInteger count = new AtomicInteger();

	public WebSocketClient(WebSocketVersion version, URI uri, TVRCDevinfo devinfo) {
		logger = new _Logger("WebSocketClient");

		this.uri = uri;
		this.version = version;
		this.devinfo = devinfo;
	}

	/**
	 * Connect the WebSocket client
	 * @param listener HybridcastListener
	 * @throws Exception コネクト失敗した際の捕捉
	 * @return websocketConnectionClinet
	 */
	public WebSocketClient connect( final HCListener listener ) throws Exception {
		String protocol = uri.getScheme();
		if (!"ws".equals(protocol)) {
			throw new IllegalArgumentException("Unsupported protocol: " + protocol);
		}

		try {
			HttpHeaders  header = new DefaultHttpHeaders();
			final WebSocketClientHandshaker handshaker = WebSocketClientHandshakerFactory.newHandshaker( uri, version, subprotocol, false, header );
			EventLoopGroup group = new NioEventLoopGroup();
			final CountDownLatch handshakeLatch = new CountDownLatch(1);
			bootstrap.group(group)
				.channel(NioSocketChannel.class)
				.handler(new ChannelInitializer() {
					@Override
					protected void initChannel(Channel channel) throws Exception {
						ChannelPipeline p = channel.pipeline();
						p.addLast("codec", new HttpClientCodec())
							.addLast("aggregator", new HttpObjectAggregator(8192))
							.addLast("initialListener", new WSClientHandler(handshaker, handshakeLatch, listener));
					}
				});

			// Connect
			connect_flag = true;

			ChannelFuture future = bootstrap.connect( new InetSocketAddress(uri.getHost(), uri.getPort()));
			future.syncUninterruptibly();

			ch = future.channel();

			handshaker.handshake(ch).syncUninterruptibly();
			handshakeLatch.await();

		}
		catch(Exception e) {
			connect_flag = false;
			fail_reason = "connect: " + TVRCStatus.Status.DenyInternalProcessing.code() + " " + e.getMessage();
			logger.error("WebSocketClient connect: " + e);
		}

		if( connect_flag == true ) {
	        return this;
		}
		else {
			throw new Exception( fail_reason );
		}
    }


	/**
	 * add Listener on WebSocket session
	 * @param listenerName String
	 * @param listener HCListener
	 * @throws Exception コネクト失敗した際の捕捉
	 * @return websocketConnectionClient
	 */
	public WebSocketClient addWSListener( String listenerName, final FrameListener listener ) throws Exception {
		// hookListener as listenerName
		ChannelPipeline cpl = ch.pipeline().addLast(listenerName , new SimpleChannelInboundHandler<Object>() {
			@Override
			protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
				if (msg instanceof CloseWebSocketFrame) {
					closed = true;
					connect_flag = false;
				}
				listener.onFrame( (WebSocketFrame) msg);
				//ctx.pipeline().remove(this);  // 一回限りのlistener
			}

			@Override
			public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
				cause.printStackTrace();
				//listener.onError(cause);
				//ctx.pipeline().remove(this);
			}
		});
		if(cpl != null){ return this;
		}else{return null; }
	}

	/**
	 * remove Listener on WebSocket session
	 * @param listenerName listenerName
	 * @throws Exception コネクト失敗した際の捕捉
	 * @return websocketConnectionClient
	 */
	public WebSocketClient removeWSListener( String listenerName ) throws Exception {
		try{
				ChannelHandler chh = ch.pipeline().remove(listenerName);
				if(chh != null){ return this;
				}else{return null; }
		}catch(NoSuchElementException e){
			return null;
		}catch(NullPointerException e){
			return null;
		}
	}

	/**
	 * List Of Listener on WebSocket session
	 * @throws Exception コネクト失敗した際の補足
	 * @return List Of Listener on WebSocket session
	 */
	public List<String> getListenerList() throws Exception {
		try{
				List<String> handlerLists = ch.pipeline().names();
				return handlerLists;
		}catch(Exception e){
			return null;
		}
	}


	/**
	 * Send the WebSocketFrame
	 * @param frame websocketFrame
	 * @param listener websocketListener
	 * @return WebsocketClinetObject
	 */
	public WebSocketClient send(WebSocketFrame frame, final FrameListener listener) {
		ch.pipeline().addLast("responseHandler" + count.incrementAndGet(), new SimpleChannelInboundHandler<Object>() {
			@Override
			protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
				if (msg instanceof CloseWebSocketFrame) {
					closed = true;
				}
				listener.onFrame((WebSocketFrame) msg);
				ctx.pipeline().remove(this);
			}

			@Override
			public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
				cause.printStackTrace();
				listener.onError(cause);
				ctx.pipeline().remove(this);
			}
		});

		ChannelFuture cf = ch.writeAndFlush(frame).syncUninterruptibly();
		if (!cf.isSuccess()) {
			listener.onError(cf.cause());
		}

		return this;
	}

	/**
	 * Destroy the client and also close open connections if any exist
	 */
	public void destroy() {
		if (!closed) {
			final CountDownLatch latch = new CountDownLatch(1);
			send(new CloseWebSocketFrame(), new FrameListener() {
				@Override
				public void onFrame(WebSocketFrame frame) {
					latch.countDown();
				}

				@Override
				public void onError(Throwable t) {
					latch.countDown();
				}
			});
			closed = true;
			connect_flag = false;
			try {
				latch.await(10, TimeUnit.SECONDS);
			}
			catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		}

		//bootstrap.releaseExternalResources();
		if(ch != null) {
			ch.close().syncUninterruptibly();
		}

//		bootstrap.group().shutdownGracefully(); 	//Deprecated
		bootstrap.config().group().shutdownGracefully();
	}

	public interface FrameListener {
		/**
		 * Is called if an WebSocketFrame was received
		 * @param frame websocketFrame
		 */
		void onFrame(WebSocketFrame frame);

		/**
		 * Is called if an error occurred
		 * @param t ErrorObject
		 */
		void onError(Throwable t);
	}

	private static final class WSClientHandler extends SimpleChannelInboundHandler<Object> {
		private final WebSocketClientHandshaker handshaker;
		private final CountDownLatch handshakeLatch;
		private final HCListener hclistener;

		public WSClientHandler(WebSocketClientHandshaker handshaker, CountDownLatch handshakeLatch, HCListener hclistener) {
			super(false);
			this.handshaker = handshaker;
			this.handshakeLatch = handshakeLatch;
			this.hclistener = hclistener;
		}

		@Override
		protected void channelRead0(ChannelHandlerContext ctx, Object o) throws Exception {
			Channel ch = ctx.channel();

			if (!handshaker.isHandshakeComplete()) {
				try {
					// the handshake response was processed upgrade is complete
					handshaker.finishHandshake(ch, (FullHttpResponse) o);
				}catch( WebSocketHandshakeException e) {
//System.out.println("channelRead0: " + e);
					//throw new Exception("WebSocketHandshakeException");
					connect_flag = false;
					fail_reason = e.getMessage() ;
				}finally{
					FullHttpResponse response = (FullHttpResponse) o;
					logger.debug("HttpResponse (status=" + response.status() + ", content=" + response.content().toString(CharsetUtil.UTF_8) + ')');
					handshakeLatch.countDown();
					ReferenceCountUtil.release(o);
				}
			}
			else {
				if (o instanceof TextWebSocketFrame) {
					WebSocketFrame frame = (WebSocketFrame)o;
					// Send the uppercase string back.
					String recvdata = ((TextWebSocketFrame)frame).text();
					logger.debug("WebSocketClient::WSClientHandler::channelRead0(): " + recvdata);

					//callback wsDataReceived listener
					hclistener.wsDataReceived( recvdata );

					//parse and callback
					try {
						JSONObject recvObj = new JSONObject(recvdata);
						if(recvObj.has("control")) {
							JSONObject ctrlObj = (JSONObject)recvObj.get("control");
							if (ctrlObj.has("setURLForCompanionDevice")) {
								hclistener.setUrlReceived( new TVRCSetURL(recvObj) );
							}
						}
						else if(recvObj.has("message")) {
							JSONObject msgObj = (JSONObject)recvObj.get("message");
							if (msgObj.has("sendTextToCompanionDevice")) {
								JSONObject textObj = (JSONObject)msgObj.get("sendTextToCompanionDevice");
								hclistener.sendTextReceived( textObj.getString("text") );
							}
						}
					}
					catch (JSONException e) {
						logger.error("WebSocketClient::WSClientHandler json parse error: " + e);
					}
				}

				if (o instanceof FullHttpResponse) {
					FullHttpResponse response = (FullHttpResponse) o;
					ReferenceCountUtil.release(o);
//					throw new Exception("Unexpected HttpResponse (status=" + response.getStatus() + ", content=" + response.content().toString(CharsetUtil.UTF_8) + ')');  	//Deprecated
					connect_flag = false;
					throw new Exception("Unexpected HttpResponse (status=" + response.status() + ", content=" + response.content().toString(CharsetUtil.UTF_8) + ')');
				}

				ctx.fireChannelRead(o);
			}
		}
	}
}
