import de.greyshine.test.mongodb.Application;
import de.greyshine.test.mongodb.EntityRepository;
import de.greyshine.test.mongodb.EntityService;
import de.greyshine.test.mongodb.ExampleComparatorItems;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class ApplicationTest {

    @Autowired
    private EntityService entityService;

    @Autowired
    private EntityRepository entityRepository;

    @BeforeEach
    public void beforeEach() {

        entityRepository.deleteAll();
        entityService.save("First: one", "one");
        entityService.save("Second: two", "two");
        entityService.save("Third: three", "three");
        entityService.save("Fourth: one,two", "two", "one");
        entityService.save("Fifth: three,two", "two", "three");
        entityService.save("Sixth: four,one,two", "one", "two", "four");
        entityService.save("Seventh: one,three,two", "one", "two", "three");
    }

    @Test
    public void testQueryAll() {

        final var queryAllItems = entityService.queryAll("two", "one");
        Assertions.assertEquals(3, queryAllItems.size());

        // more items first, on equal items order alphabetical
        Assertions.assertEquals("Sixth: four,one,two", queryAllItems.get(0).getName());
        Assertions.assertEquals("Seventh: one,three,two", queryAllItems.get(1).getName());
        Assertions.assertEquals("Fourth: one,two", queryAllItems.get(2).getName());
    }

    @Test
    public void testQueryAny() {

        final var queryAnyItems = entityService.queryAny("two", "one");
        Assertions.assertEquals(6, queryAnyItems.size());

        Assertions.assertEquals("Sixth: four,one,two", queryAnyItems.get(0).getName()); // one,two,four
        Assertions.assertEquals("Seventh: one,three,two", queryAnyItems.get(1).getName());
        Assertions.assertEquals("Fourth: one,two", queryAnyItems.get(2).getName()); // one,two
        Assertions.assertEquals("Fifth: three,two", queryAnyItems.get(3).getName()); // two,three
        Assertions.assertEquals("First: one", queryAnyItems.get(4).getName()); // one
        Assertions.assertEquals("Second: two", queryAnyItems.get(5).getName()); // two
    }

    @Test
    public void showCorrectOrderQueryAll() {

        final var queryAllItems = entityService.queryAll("two", "one");

        queryAllItems.sort(ExampleComparatorItems.getSingleton());

        Assertions.assertEquals(3, queryAllItems.size());
        Assertions.assertEquals("Sixth: four,one,two", queryAllItems.get(0).getName());
        Assertions.assertEquals("Seventh: one,three,two", queryAllItems.get(1).getName());
        Assertions.assertEquals("Fourth: one,two", queryAllItems.get(2).getName());
    }

    @Test
    public void showCorrectOrderQueryAny() {

        final var queryAnyItems = entityService.queryAny("two", "one");

        queryAnyItems.sort(ExampleComparatorItems.getSingleton());

        Assertions.assertEquals(6, queryAnyItems.size());
        Assertions.assertEquals("Sixth: four,one,two", queryAnyItems.get(0).getName());
        Assertions.assertEquals("Seventh: one,three,two", queryAnyItems.get(1).getName());
        Assertions.assertEquals("Fourth: one,two", queryAnyItems.get(2).getName());
        Assertions.assertEquals("Fifth: three,two", queryAnyItems.get(3).getName());
        Assertions.assertEquals("First: one", queryAnyItems.get(4).getName());
        Assertions.assertEquals("Second: two", queryAnyItems.get(5).getName());
    }
}
