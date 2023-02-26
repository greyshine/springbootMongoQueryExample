package de.greyshine.test.mongodb;

import java.util.Comparator;

public class ExampleComparatorItems implements Comparator<Entity> {

    public static final ExampleComparatorItems INSTANCE =  new ExampleComparatorItems();

    public static ExampleComparatorItems getSingleton() {
        return INSTANCE;
    }

    /**
     * The more items an {@link Entity} has the higher the order. <br/>
     * On equally sized item-arrays the alphabetical order of the each items decides on the order.
     */
    @Override
    public int compare(Entity e1, Entity e2) {

        final var items1 = e1.getItems();
        final var items2 = e2.getItems();

        // longest comes first
        var result = items2.length - items1.length;
        if ( result != 0 ) { return result; }

        // length is equal
        // compare each entry and let each entry decide of the order
        for(int i=0, l=items1.length; i<l; i++) {

            final var entry1 = e1.getItems()[i];
            final var entry2 = e2.getItems()[i];
            final var cmp = entry1.compareTo(entry2);

            if ( cmp != 0 ) { return cmp; }
        }

        return 0;
    }
}
