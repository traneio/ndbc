package io.trane.ndbc.mysql.netty4;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.function.Supplier;

import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.DefaultThreadFactory;
import io.trane.future.Future;
import io.trane.ndbc.Config;
import io.trane.ndbc.DataSource;
import io.trane.ndbc.datasource.Connection;
import io.trane.ndbc.datasource.LockFreePool;
import io.trane.ndbc.datasource.Pool;
import io.trane.ndbc.datasource.PooledDataSource;
import io.trane.ndbc.mysql.proto.ExtendedExecuteExchange;
import io.trane.ndbc.mysql.proto.ExtendedQueryExchange;
import io.trane.ndbc.mysql.proto.SimpleExecuteExchange;
import io.trane.ndbc.mysql.proto.SimpleQueryExchange;
import io.trane.ndbc.mysql.proto.StartupExchange;
import io.trane.ndbc.mysql.proto.marshaller.MysqlMarshaller;
import io.trane.ndbc.mysql.proto.unmarshaller.MysqlUnmarshaller;
import io.trane.ndbc.netty4.ChannelSupplier;
import io.trane.ndbc.netty4.NettyChannel;

public final class DataSourceSupplier implements Supplier<DataSource> {

	private final Config config;
	private final Supplier<Future<NettyChannel>> channelSupplier;
	private final StartupExchange startup = new StartupExchange();

	public DataSourceSupplier(final Config config) {
		this.config = config;
		channelSupplier = new ChannelSupplier(config.charset(), new MysqlMarshaller(), new MysqlUnmarshaller(),
				new NioEventLoopGroup(config.nioThreads().orElse(0), new DefaultThreadFactory("ndbc-netty4", true)),
				config.host(), config.port());
	}

	@Override
	public final DataSource get() {
		return new PooledDataSource(createPool());
	}

	private final Pool<Connection> createPool() {
		return LockFreePool.apply(createConnection(), config.poolMaxSize(), config.poolMaxWaiters(),
				config.poolValidationInterval(),
				new ScheduledThreadPoolExecutor(1, new DefaultThreadFactory("ndbc-pool-scheduler", true)));
	}

	private final Supplier<Future<Connection>> createConnection() {
		return () -> channelSupplier.get()
				.flatMap(channel -> startup.apply(config.user(), config.password(), config.database(), "utf8")
						.run(channel)
						.map(backendKeyData -> new io.trane.ndbc.mysql.Connection(channel, channelSupplier,
								new SimpleQueryExchange(), new SimpleExecuteExchange(), new ExtendedQueryExchange(),
								new ExtendedExecuteExchange())));

	}
}
