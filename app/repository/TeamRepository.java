package repository;

import io.ebean.Finder;
import models.db.remote.logging.Team;

import javax.inject.Singleton;
import java.util.List;

@Singleton
public class TeamRepository implements ITeamRepository {

    Finder<Integer, Team> finder = new Finder<Integer, Team>(Team.class);

    @Override
    public List<Team> getAll() {
        return finder.all();
    }

    @Override
    public Team add(Team team) {
        team.save();
        team.refresh();
        return team;
    }

    @Override
    public Team update(Team team) {
        team.update();
        team.refresh();
        return team;
    }
}
