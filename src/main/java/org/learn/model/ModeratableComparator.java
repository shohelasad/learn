package org.learn.model;

import java.util.Comparator;

import org.learn.model.interfaces.Moderatable;

public class ModeratableComparator implements Comparator<Moderatable> {

    @Override
    public int compare(Moderatable moderatable1, Moderatable moderatable2) {
        if (moderatable1.getId().equals(moderatable2.getId()))
            return 0;
        return (Long) moderatable1.getId() - (Long) moderatable2.getId() < 0l ? -1 : 1;
    }

}
