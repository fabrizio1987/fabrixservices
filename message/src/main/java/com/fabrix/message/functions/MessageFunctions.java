package com.fabrix.message.functions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fabrix.message.dto.InfoMsgDto;

import java.util.function.Function;

@Configuration
public class MessageFunctions {

    private static final Logger log = LoggerFactory.getLogger(MessageFunctions.class);

    @Bean
    public Function<InfoMsgDto,InfoMsgDto> email() {
        return infoMsgDto -> {
            log.info("Sending email with the details : " +  infoMsgDto.toString());
            return infoMsgDto;
        };
    }

    @Bean
    public Function<InfoMsgDto,Integer> sms() {
        return infoMsgDto -> {
            log.info("Sending sms with the details : " +  infoMsgDto.toString());
            return infoMsgDto.customerId();
        };
    }

}
