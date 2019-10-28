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
        Session session = sfr.getSession();


        List<Item> items = session.createQuery("SELECT i from Item i ").getResultList();

        Assert.assertEquals(4,items.size());

        for (Item i : items) {
            Assert.assertTrue(i.getName().equals("item-"+i.getId()));
        }
    }

    @Test
    public void testSubItemsOnDatabaseStartUp(){
        Session session = sfr.getSession();

        List<SubItem> subItems = session.createQuery("SELECT si from SubItem si ").getResultList();

        Assert.assertEquals(10,subItems.size());
    }


    @Test
    public void testSubItemRelationsOfItems(){
        Session session = sfr.getSession();

        Item item3 = session.get(Item.class,31);
        List<SubItem> subItems = item3.getSubItems();

        for (SubItem s : subItems) {
            System.out.println(s);
        }



        // TODO: getSubItems returns a bag containing the one sublist only.
        // all the subitems have to be fetched here.
    }
}


