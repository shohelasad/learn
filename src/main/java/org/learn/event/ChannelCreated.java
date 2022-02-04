package org.learn.event;

import org.learn.model.Channel;

public class ChannelCreated {
	private final Channel channel;

	public ChannelCreated(Channel channel) {
		this.channel = channel;
	}

	public Channel getChannel() {
		return channel;
	}
}
