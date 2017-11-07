package com.innee.czyhInterface.exception;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;
import org.springside.modules.utils.Exceptions;

public class FxlMappingExceptionResolver extends SimpleMappingExceptionResolver {

	@Override
	protected void logException(Exception ex, HttpServletRequest request) {
		super.logger.error(Exceptions.getStackTraceAsString(Exceptions.getRootCause(ex)));
	}

}