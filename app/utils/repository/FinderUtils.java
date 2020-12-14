package utils.repository;

import io.ebean.Finder;

import java.util.List;

public class FinderUtils {
    public static <T> List<T> getObjects(Finder<Long, T> finder,Class<T> clazz, Long id, String columnName){
        return (id == null) ? null :
                finder.query()
                        .where()
                        .eq(columnName, id).findList();
    }
}
