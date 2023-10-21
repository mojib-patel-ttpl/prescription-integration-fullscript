package com.fullscriptintegration.fullscript.integration.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fullscriptintegration.fullscript.integration.util.Response;
import com.fullscriptintegration.fullscript.integration.util.ResponseCode;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.nio.file.AccessDeniedException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@ControllerAdvice
@Slf4j
@Component
@Configuration
public class AppExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(FullscriptThinkitiveException.class)
    protected ResponseEntity<Object> handleCustomException(FullscriptThinkitiveException exception, WebRequest request) {

        if (exception.getErrorCode().equals(ResponseCode.UNSUPPORTED_MEDIA_TYPE)){
            return handleExceptionInternal(exception, buildResponse(exception.getErrorCode(), exception.getMessage(), request), new HttpHeaders(),
                    exception.getErrorCode() == ResponseCode.UNSUPPORTED_MEDIA_TYPE ? HttpStatus.UNSUPPORTED_MEDIA_TYPE : HttpStatus.UNSUPPORTED_MEDIA_TYPE, request);
        } else if (exception.getErrorCode().equals(ResponseCode.NOT_FOUND)) {
            return handleExceptionInternal(exception, buildResponse(exception.getErrorCode(), exception.getMessage(), request), new HttpHeaders(),
                    exception.getErrorCode() == ResponseCode.NOT_FOUND ? HttpStatus.NOT_FOUND : HttpStatus.NOT_FOUND, request);
        }

        return handleExceptionInternal(exception, buildResponse(exception.getErrorCode(), exception.getMessage(), request), new HttpHeaders(),
                exception.getErrorCode() == ResponseCode.INTERNAL_ERROR ? HttpStatus.INTERNAL_SERVER_ERROR : HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException exception, WebRequest request){
        Map<Integer, List<String>> violationsByRow = new HashMap<>();

        exception.getConstraintViolations().forEach(violation -> {
            Matcher matcher = Pattern.compile("\\[(\\d+)\\]").matcher(violation.getPropertyPath().toString());
            if (matcher.find()) {
                int rowIndex = Integer.parseInt(matcher.group(1));
                String message = "row " + (rowIndex + 1) + ": " + violation.getMessage();
                violationsByRow.computeIfAbsent(rowIndex, k -> new ArrayList<>()).add(message);
            }
        });

        List<String> violations = violationsByRow.entrySet().stream()
                .flatMap(entry -> entry.getValue().stream())
                .collect(Collectors.toList());

        String message = "Constraint violation: " + String.join("; ", violations);

        return handleExceptionInternal(exception, buildResponse(ResponseCode.BAD_REQUEST, message, request), new HttpHeaders(),
                HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> handleDaoException(DataIntegrityViolationException exception, WebRequest request) {
        log.error("Database Exception", exception);
        return handleExceptionInternal(exception, buildResponse(ResponseCode.DB_ERROR,
                NestedExceptionUtils.getMostSpecificCause(exception).getMessage(), request),
                new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        log.error("Method Args Exception", exception);
        String message;
        try {
            message = exception.getBindingResult().getFieldError().getDefaultMessage();
        }catch (Exception e) {
            message = exception.getBindingResult().toString();
        }
        return handleExceptionInternal(exception, buildResponse(ResponseCode.BAD_REQUEST, message, request),
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException exception,
                                                               HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        log.error("HttpMessageNotReadable Exception", exception);
        String message = null;
        if (exception.getCause() instanceof InvalidFormatException) {
            InvalidFormatException ifx = (InvalidFormatException) exception.getCause();
            if (ifx.getTargetType() != null && ifx.getTargetType().isEnum()) {
                message = String.format("Invalid enum value for the field: '%s'. The value must be one of: %s.",
                         ifx.getPath().get(ifx.getPath().size() - 1).getFieldName(), Arrays.toString(ifx.getTargetType().getEnumConstants()));
            } else {
                message = exception.getMessage();
            }
        }
        return handleExceptionInternal(exception, buildResponse(ResponseCode.BAD_REQUEST, message, request), headers, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handle(Exception exception, WebRequest request) {
        log.error("Generic Exception", exception);
        return handleExceptionInternal(exception, buildResponse(ResponseCode.INTERNAL_ERROR, exception.getMessage(), request),
                new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException exception, WebRequest request, HttpServletResponse response) {

        try {
            response.setStatus(403);
            return handleExceptionInternal(exception, buildResponse(ResponseCode.ACCESS_DENIED, "Access denied", request),
                    new HttpHeaders(), HttpStatus.FORBIDDEN, request);
        } catch (Exception e) {
            return handleExceptionInternal(exception, buildResponse(ResponseCode.SERVICE_UNAVAILABLE, exception.getMessage(), request),
                    new HttpHeaders(), HttpStatus.SERVICE_UNAVAILABLE, request);
        }

    }


    @ExceptionHandler(HttpClientErrorException.UnsupportedMediaType.class)
    public ResponseEntity<Object> unsupportedMediaTypeException(HttpClientErrorException.UnsupportedMediaType exception, WebRequest request) {
        Response response = buildResponse(ResponseCode.UNSUPPORTED_MEDIA_TYPE, "Unsupported media type", request);
        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(response);
    }


    private Response buildResponse(ResponseCode code, String message, WebRequest request) {
        return Response.builder()
                .code(code)
                .message(message)
                .path(request.getContextPath())
                .requestId(UUID.randomUUID().toString())
                .errors(null)
                .version("1.0")
                .build();
    }
}
