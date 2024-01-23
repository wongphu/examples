import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.SslHandler;

import javax.net.ssl.SSLEngine;

public class HttpClientInitializer extends ChannelInitializer<SocketChannel> {
    private final String host;

    public HttpClientInitializer(String host) {
        this.host = host;
    }

    protected void initChannel(SocketChannel channel) throws Exception {
        SslContext sslContext = SslContextBuilder.forClient().build();
        SSLEngine sslEngine = sslContext.newEngine(channel.alloc(), host, 443);
        ChannelPipeline pipeline = channel.pipeline();
        pipeline.addLast("ssl", new SslHandler(sslEngine, false));
        pipeline.addLast("log", new LoggingHandler(LogLevel.DEBUG));
        pipeline.addLast("codec", new HttpClientCodec());
        pipeline.addLast("joke", new HttpClientHandler());
    }
}
