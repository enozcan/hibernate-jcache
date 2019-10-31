package jcache.L2C.test;


import com.hazelcast.core.Hazelcast;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class SessionFactoryUtil {

    private SessionFactory sessionFactory;
    private Transaction transaction;
    private Session session;
    private boolean useHazelcastClient;

    public SessionFactoryUtil(boolean useHazelcastClient) {
        this.useHazelcastClient = useHazelcastClient;
        sessionFactory = createSessionFactory();
        createSession();
        beginTransaction();
    }

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

    private SessionFactory createSessionFactory() {
        Configuration configuration = new Configuration();
        String hazelcastConfigURL = useHazelcastClient ? "client-hibernate.cfg.xml" : "server-hibernate.cfg.xml";
        configuration.configure(hazelcastConfigURL);
        SessionFactory sessionFactory = configuration.buildSessionFactory();
        return sessionFactory;
    }
    public Session createSession() {
        if (session.isOpen()) session.close();
        session = sessionFactory.openSession();
        beginTransaction();
        return session;
    }
    public void commit() {
        transaction.commit();
    }

    public void beginTransaction() {
        transaction = session.beginTransaction();
    }

    public Session getSession() {
        return session.isOpen() ? session : createSession();
    }

    public Session changeSession(){
        if(session.isOpen()) session.close();
        session = createSession();
        beginTransaction();
        return session;
    }

    public void resetCache(){
        changeSession(); // evict first level cache as well
        sessionFactory.getCache().evictAllRegions();
        sessionFactory.getStatistics().clear();
    }

}
