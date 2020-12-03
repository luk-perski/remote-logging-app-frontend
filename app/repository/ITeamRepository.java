package repository;

import models.db.remote.logging.Team;

import java.util.List;

public interface ITeamRepository {
    List<Team> getAll();

    Team add(Team team);

    Team update(Team team);
}
