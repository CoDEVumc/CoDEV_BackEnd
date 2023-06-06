package com.codevumc.codev_backend.chat;

import com.codevumc.codev_backend.jwt.JwtTokenProvider;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

@Component
public class StompHandler implements ChannelInterceptor {

    private final JwtTokenProvider jwtTokenProvider;

    public StompHandler(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        return message;
    }

    /*@Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        try {
            if(accessor.getCommand() == StompCommand.CONNECT) {
                if (!jwtTokenProvider.validateToken(accessor.getFirstNativeHeader("CoDev_Authorization"))) {
                    throw new IllegalArgumentException();

                }
            }
        } catch (SignatureException | MalformedJwtException | ExpiredJwtException | UnsupportedJwtException | IllegalArgumentException e) {
            e.printStackTrace();
        }
        return message;
    }*/
}
