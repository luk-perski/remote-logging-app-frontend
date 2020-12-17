package utils.repository;

import io.ebean.Finder;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.List;

public class FinderUtils {
    public static <T> List<T> getObjects(Finder<Long, T> finder, Class<T> clazz, Object value, String columnName) {
        return (value == null) ? null :
                finder.query()
                        .where()
                        .eq(columnName, value).findList();
    }

    public static <T> List<T> getObjectsByIds(Finder<Long, T> finder, Class<T> clazz, List<Long> ids) {
        //todo like https://stackoverflow.com/a/18987480
        throw new NotImplementedException();
    }
}
