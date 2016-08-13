package com.ccthanking.framework.message.comet;

import org.springframework.context.ApplicationEvent;

public class InfoEvent extends ApplicationEvent {
	public InfoEvent(Object source) {
		super(source);
	}
}
