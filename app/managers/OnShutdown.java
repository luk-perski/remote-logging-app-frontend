package managers;

import java.util.concurrent.CompletableFuture;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import play.inject.ApplicationLifecycle;

@Singleton
public class OnShutdown {

	private static final Logger log = LoggerFactory.getLogger(OnStartup.class);

	@Inject
	public OnShutdown(ApplicationLifecycle lifecycle) {
		lifecycle.addStopHook(() -> {
			log.info("Application stopping...");
			// ApplicationLog.log(new Date(), this.getClass(), new ApplicationGeneralAction("Application shutting down"));
			return CompletableFuture.completedFuture(null);
		});
	}
}
