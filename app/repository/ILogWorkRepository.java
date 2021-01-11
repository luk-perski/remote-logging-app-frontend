package repository;

import models.db.remote.logging.LogWork;

import java.util.List;

public interface ILogWorkRepository {
    LogWork add(LogWork logWork);

    List<LogWork> getAll();

}
