package jcache.L2C.test;

import com.fasterxml.classmate.AnnotationConfiguration;
import com.hazelcast.core.Hazelcast;
import jcache.L2C.test.entity.Item;
import jcache.L2C.test.entity.SubItem;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.junit.rules.MethodRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

public class SessionFactoryRule {


    private SessionFactory sessionFactory;
    private Transaction transaction;
    private Session session;
    private boolean useHazelcastClient;

    public SessionFactoryRule(boolean useHazelcastClient) {
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
        session = sessionFactory.openSession();
        return session;
    }
    public void commit() {
        transaction.commit();
    }

    public void beginTransaction() {
        transaction = session.beginTransaction();
    }

    public Session getSession() {
        return session;
    }

    public Session changeSession(){
        if(session.isOpen()) session.close();
        session = createSession();
        beginTransaction();
        return session;
    }

    public void evictCache(){
        sessionFactory.getCache().evictAllRegions();
    }

}
