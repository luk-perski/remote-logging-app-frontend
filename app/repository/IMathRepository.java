package repository;

import models.db.math.Sum;

import java.util.List;

public interface IMathRepository {

    List<Sum> getAllSums();
}
