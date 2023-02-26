package de.greyshine.test.mongodb;

import lombok.Builder;
import lombok.Data;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.List;

@Document("TestEntities")
@Data
@Builder
public class Entity {

    @MongoId
    public String id;
    public String name;
    public String[] items;

    public void setItems(String[] items) {
        sortItems(items);
        this.items = items;
    }

    private static void sortItems(String[] items) {
        if ( items!=null ) {

            for(String item : items) {
                if ( item == null ) { throw new IllegalStateException("no null items allowed"); }
            }

            Arrays.sort( items );
        }
    }

    /**
     * Override functionalities of lombok's @Builder
     */
    public static class EntityBuilder {

        private String[] items;

        public EntityBuilder items(String[] items) {
            sortItems(items);
            this.items = items;
            return this;
        }
    }

    /**
     * Convert bson document to an entity.
     * @param document document to be converted to an {@link Entity}
     * @return the converte {@link Entity}
     */
    public static Entity build(org.bson.Document document) {
        Assert.notNull(document,"document must not be null");
        return Entity.builder()
                .id( document.getString("_id") )
                .name( document.getString("name") )
                .items( ((List<String>)document.get("items")).toArray(new String[0]) )
                .build();
    }
}
