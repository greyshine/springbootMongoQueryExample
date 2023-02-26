package de.greyshine.test.mongodb;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface EntityRepository extends MongoRepository<Entity,String> {

}
