package jcache.L2C;

import com.hazelcast.core.Hazelcast;
import jcache.L2C.entity.Item;
import jcache.L2C.entity.SubItem;
import jcache.L2C.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class CollectionCache {

    public static void main(String[] args){

        initializeDatabase();

        Session session1, session2;
        Item item;


        // Get Item from DB.
        // Collection cache miss & put are expected
        session1 = HibernateUtil.getSession();
        session1.beginTransaction();
        item = session1.get(Item.class, 1);

        // Since fetch type for the collection is Lazy, force it to be fetched.
        for(SubItem i : item.getSubItems()){
            System.out.println(i);
        };
        session1.close();
        HibernateUtil.printCollectionCacheStats();

        //Get Department again. Collection cache hit is expected.
        session2 = HibernateUtil.getSession();
        session2.beginTransaction();
        item = session2.get(Item.class, 1);

        // Since fetch type for the collection is Lazy, force it to be fetched.
        for(SubItem i : item.getSubItems()){
            System.out.println(i);
        };
        session2.close();
        HibernateUtil.printCollectionCacheStats();

        HibernateUtil.closeFactory();
        Hazelcast.shutdownAll();

    }

    private static void initializeDatabase(){

        Session session = HibernateUtil.getSession();

        Item item1 = new Item("item-1",1);


        SubItem subItem1 = new SubItem(1,"subitem-1",item1);
        SubItem subItem2 = new SubItem(2,"subitem-2",item1);

        item1.addSubItem(subItem1).addSubItem(subItem2);

        Transaction tx = session.beginTransaction();

        session.save(item1);

        tx.commit();
        session.close();
        HibernateUtil.evictAllRegions();
    }
}
