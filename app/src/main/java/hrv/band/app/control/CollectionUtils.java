package hrv.band.app.control;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Copyright (c) 2017
 * Created by Julian on 04.04.2017.
 */

public class CollectionUtils {

    private CollectionUtils() {}

    public static <T> Collection<T> filter(Collection<T> coll, IPredicate<T> predicate) {
        Collection<T> newCollection = new ArrayList<>();

        for (T element : coll) {
            if(predicate.apply(element)) {
                newCollection.add(element);
            }
        }

        return newCollection;
    }
}
