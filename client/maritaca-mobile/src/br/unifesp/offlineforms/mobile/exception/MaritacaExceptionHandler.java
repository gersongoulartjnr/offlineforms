package br.unifesp.offlineforms.mobile.exception;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.Thread.UncaughtExceptionHandler;

import android.content.Context;
import android.content.Intent;
import br.unifesp.offlineforms.mobile.activities.ErrorReportActivity;
import br.unifesp.offlineforms.mobile.util.Constants;

/**
 * 
 * @author Jimmy Valverde S&aacute;nchez
 * @version 0.1.6
 *
 */
public class MaritacaExceptionHandler implements UncaughtExceptionHandler {
	
	private final UncaughtExceptionHandler defaultExceptionHandler;
	private final Context currentContext;
	private final String TAG = "UNHANDLED";
	
	public MaritacaExceptionHandler(final Context context) {
		this.defaultExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
		this.currentContext = context;
	}

	@Override
	public void uncaughtException(final Thread thread, final Throwable throwable) {
		try {
			maritacaException(this.currentContext, throwable, TAG);
		}
		catch(Throwable t) {
 			defaultExceptionHandler.uncaughtException(thread, throwable);
		}		
	}
	
	public void maritacaException(final Context context, final Throwable throwable, final String tag) {			
		final StringWriter stringWriter = new StringWriter();
		throwable.printStackTrace(new PrintWriter(stringWriter));
		Intent intent = new Intent(currentContext, ErrorReportActivity.class);
		intent.putExtra(Constants.UNCAUGHT_EXCEPTION_CODE, Constants.UNCAUGHT_EXCEPTION_CODE);
		intent.putExtra(Constants.UNCAUGHT_EXCEPTION_ERROR, throwable.getMessage());
		currentContext.startActivity(intent);
	}
}