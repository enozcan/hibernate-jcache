package jcache.sample.server;

import jcache.sample.server.util.HibernateUtil;
import jcache.sample.server.entity.Item;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.List;

import static java.lang.System.exit;

// TODO: Cache region names
public class QueryCache {

    private static final String QUERY_STRING = "select i from Item i where i.id < 102";

    public static void main(String[] args){

        Session session1, session2, session3;
        Transaction tx1;
        List<Item> queryResults;

        // Store Items with id = 100,101 and 102
        // L2C miss and put are expected
        session1 = HibernateUtil.getSession();
        tx1 = session1.getTransaction();
        tx1.begin();
        session1.save(new Item(100,"New Hazelcast Item-0",12345L));
        session1.save(new Item(101,"New Hazelcast Item-1",12345L));
        session1.save(new Item(102,"New Hazelcast Item-2",12345L));
        tx1.commit();
        System.out.print("SESSION 1, Factory L2C stats:" + HibernateUtil.getSecondLevelCacheStats());
        System.out.println("SESSION 1, Factory Query Cache stats:" + HibernateUtil.getQueryCacheStats());
        session1.close();

        // Get recently stored values via query.
        // Data is expected to be fetched from database, not from L2C.
        // QueryCache miss is expected
        session2 = HibernateUtil.getSession();
        queryResults = executeQuery(QUERY_STRING, session2);
        printList(queryResults);
        System.out.print("SESSION 2, Factory L2C stats:" + HibernateUtil.getSecondLevelCacheStats());
        System.out.println("SESSION 2, Factory Query Cache stats:" + HibernateUtil.getQueryCacheStats());
        session2.close();

        // Execute last executed query again from another session.
        // Data is expected to be fetched from query Cache, not from database.
        // QueryCache hit is expected
        session3 = HibernateUtil.getSession();
        queryResults = executeQuery(QUERY_STRING, session3);
        printList(queryResults);
        System.out.print("SESSION 3, Factory L2C stats:" + HibernateUtil.getSecondLevelCacheStats());
        System.out.println("SESSION 3, Factory Query Cache stats:" + HibernateUtil.getQueryCacheStats());
        session3.close();

        HibernateUtil.closeFactory();
        exit(0);

    }

    private static void printList(List<Item> list){
        System.out.println("Items fetched: ");
        for(Item i : list) {
            System.out.println(i);
        }
    }

    private static List<Item> executeQuery(String query, Session session){
        Transaction tx;
        tx = session.getTransaction();
        tx.begin();
        return (List<Item>) session.createQuery(QUERY_STRING)
                .setCacheable(true)
                .setCacheRegion("hz-query-region")
                .list();
    }

}
