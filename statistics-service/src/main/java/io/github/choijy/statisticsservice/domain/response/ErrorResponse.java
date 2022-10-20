package io.github.choijy.statisticsservice.domain.response;

import org.springframework.http.HttpStatus;

import lombok.Getter;

/**
 * Description : Error Response class.
 * <p>
 * Created by jychoi on 2022/09/18.
 */
@Getter
public class ErrorResponse extends Response {

    private final String message;

    public ErrorResponse(HttpStatus httpStatus, String message) {
        super(httpStatus.value());
        this.message = message;
    }
}
