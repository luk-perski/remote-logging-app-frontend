package repository;

import io.ebean.Finder;
import models.db.math.Sum;

import java.util.List;

public class MathRepository implements IMathRepository {
    @Override
    public List<Sum> getAllSums() {
        Finder<Integer, Sum> finder = new Finder<Integer, Sum>(Sum.class);
        return finder.all();
    }
}
