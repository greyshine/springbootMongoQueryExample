package de.greyshine.test.mongodb;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
@Slf4j
public class EntityService {

    private final MongoRepository mongoRepository;
    private final MongoTemplate mongoTemplate;

    public EntityService(final MongoRepository mongoRepository, MongoTemplate mongoTemplate) {
        this.mongoRepository = mongoRepository;
        this.mongoTemplate = mongoTemplate;
    }

    public String save(String name, String... tags) {

        final var entity = (Entity)mongoRepository.save( Entity.builder()
                .name( name )
                .items( tags )
                .build() );

        return entity.getId();
    }

    /**
     * All items will be contained in the resulting entities
     */
    public List<Entity> queryAll(String... items) {

        final var andCriterias = new ArrayList<>();

        for(String item : items) {
            andCriterias.add( Criteria.where("items").in( item ) );
        }

        final var criteria = new Criteria().andOperator(andCriterias.toArray(new Criteria[andCriterias.size()]));

        final var results = new ArrayList<Entity>();

        mongoTemplate.executeQuery(
                new Query( criteria ),
                "TestEntities",
                // Is there a faster way to transform/map a org.bson.Document
                // into the Object in context (here it is the Entity.java)?
                document -> results.add( Entity.build(document) )
                );

        return results;
    }

    /**
     * At least one item will be contained in the resulting entities
     */
    public List<Entity> queryAny(String... items) {

        final var orCriterias = new ArrayList<>();

        for(String item : items) {
            orCriterias.add( Criteria.where("items").in( item ) );
        }

        final var criteria = new Criteria().orOperator(orCriterias.toArray(new Criteria[orCriterias.size()]));

        final var results = new ArrayList<Entity>();

        mongoTemplate.executeQuery(
                new Query( criteria ),
                "TestEntities",
                // Is there a faster way to transform/map a org.bson.Document
                // into the Object in context (here it is the Entity.java)?
                document -> results.add( Entity.build(document) )
        );

        return results;
    }

}
