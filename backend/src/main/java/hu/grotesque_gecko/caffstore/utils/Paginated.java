package hu.grotesque_gecko.caffstore.utils;

import java.util.List;

public class Paginated<T> {
    private final List<T> entities;
    private final long totalCount;

    public Paginated(final List<T> entities, final long totalCount) {
        this.entities = entities;
        this.totalCount = totalCount;
    }

    public List<T> getEntities() {
        return entities;
    }

    public long getTotalCount() {
        return totalCount;
    }
}
