package repository;

import io.ebean.Finder;
import models.db.remote.logging.LogWork;

import javax.inject.Singleton;
import java.util.List;

@Singleton
public class LogWorkRepository implements ILogWorkRepository {

    Finder<Long, LogWork> finder = new Finder<>(LogWork.class);

    @Override
    public LogWork add(LogWork logWork) {
        logWork.save();
        logWork.refresh();
        return logWork;
    }

    @Override
    public List<LogWork> getAll() {
        return finder.all();
    }
}
