package jcache.L2C.test.util;


import com.hazelcast.core.Hazelcast;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.stat.Statistics;

public class SessionFactoryUtil {

    private SessionFactory sessionFactory;

    private boolean useHazelcastClient;

    public SessionFactoryUtil(boolean useHazelcastClient) {
        this.useHazelcastClient = useHazelcastClient;
        sessionFactory = createSessionFactory();
    }


    private SessionFactory createSessionFactory() {
        Configuration configuration = new Configuration();
        String hibernateConfigURL;

        Hazelcast.shutdownAll();

        if (useHazelcastClient) {
             hibernateConfigURL = "client-hibernate.cfg.xml";
             Hazelcast.newHazelcastInstance();
        } else {
            hibernateConfigURL = "server-hibernate.cfg.xml";
        }

        configuration.configure(hibernateConfigURL);
        SessionFactory sessionFactory = configuration.buildSessionFactory();
        return sessionFactory;
    }

    public Statistics getStats(){
        return sessionFactory.getStatistics();
    }

    public void resetCache(){
        sessionFactory.getCache().evictAllRegions();
        sessionFactory.getStatistics().clear();
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    /*

    private void shutdown() {
        try {
            //transaction.rollback();
            session.close();
            sessionFactory.close();
            Hazelcast.shutdownAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public Session getActiveSession(){
        return this.session.isOpen() ? this.session : null;
    }

    public Session createNewSession(){
        //if (session != null && session.isOpen()) session.close();
        session = sessionFactory.openSession();
        return session;
    }

    public void commit() {
        if(!session.isOpen()) return;
        Transaction tx = session.beginTransaction();
        tx.commit();
    }

    public void closeSession(){
        if (!session.isOpen()) session.close();
    }*/


}
