package jcache.L2C.test;

import jcache.L2C.test.entity.Item;
import jcache.L2C.test.entity.SubItem;
import org.hibernate.Session;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 *  Tests if the entities are created on DB as expected.
 */
public class DatabaseEntitiesTest extends HibernateTestBase {

    public DatabaseEntitiesTest() {
        super(false);
    }

    @Test
    public void testItemsOnDatabaseStartUp(){
        Session session = sfUtil.getSession();

        List<Item> items = session.createQuery("SELECT i from Item i ").getResultList();

        Assert.assertEquals(4,items.size());

        for (Item i : items) {
            Assert.assertTrue(i.getName().equals("item-"+i.getId()));
        }

        System.out.println(session.getSessionFactory().getStatistics().getSecondLevelCacheHitCount());
        System.out.println(session.getSessionFactory().getStatistics().getSecondLevelCacheMissCount());
        System.out.println(session.getSessionFactory().getStatistics().getSecondLevelCachePutCount());
    }

    @Test
    public void testSubItemsOnDatabaseStartUp(){

        Session session = sfUtil.getSession();

        List<SubItem> subItems = session.createQuery("SELECT si from SubItem si ").getResultList();

        Assert.assertEquals(10,subItems.size());

        for(SubItem si : subItems) {
            Assert.assertTrue(si.getName().equals("subitem-"+si.getId()));
        }

    }


    @Test
    public void testSubItemRelationsOfItems(){
        Session session = sfUtil.getSession();

        System.out.println(session.getSessionFactory().getStatistics().getSecondLevelCacheHitCount());
        System.out.println(session.getSessionFactory().getStatistics().getSecondLevelCacheMissCount());
        System.out.println(session.getSessionFactory().getStatistics().getSecondLevelCachePutCount());

        Item item1 = session.get(Item.class,1);
        Item item2 = session.get(Item.class,2);
        Item item3 = session.get(Item.class,3);
        Item item4 = session.get(Item.class,4);

        Assert.assertNotNull(item1);
        Assert.assertNotNull(item2);
        Assert.assertNotNull(item3);
        Assert.assertNotNull(item4);

        Assert.assertEquals(1,item1.getSubItems().size());
        Assert.assertEquals(2,item2.getSubItems().size());
        Assert.assertEquals(3,item3.getSubItems().size());
        Assert.assertEquals(4,item4.getSubItems().size());


        System.out.println(session.getSessionFactory().getStatistics().getSecondLevelCacheHitCount());
        System.out.println(session.getSessionFactory().getStatistics().getSecondLevelCacheMissCount());
        System.out.println(session.getSessionFactory().getStatistics().getSecondLevelCachePutCount());
    }
}


