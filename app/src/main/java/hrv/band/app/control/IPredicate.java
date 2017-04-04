package hrv.band.app.control;

/**
 * Copyright (c) 2017
 * Created by Julian on 04.04.2017.
 */

public interface IPredicate<T> {
    boolean apply(T type);
}
