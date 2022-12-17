package com.lolo.exceptionHandler;
import com.lolo.common.ResultEnum;
import com.lolo.exception.NotEnoughException;
import com.lolo.util.Result;
import com.lolo.util.ResultGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;


import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;


@ControllerAdvice
public class NotEnoughExceptionHandler {
    private Logger logger = LoggerFactory.getLogger(NotEnoughExceptionHandler.class);
    @ExceptionHandler(value = NotEnoughException.class)
    @ResponseBody
    public Result notEnoughExceptionHandler(NotEnoughException e){
        logger.error("",e);
        return ResultGenerator.fail(ResultEnum.CAR_MORE_ERROR.getCode(),
                ResultEnum.CAR_MORE_ERROR.getMes());
    }

}
