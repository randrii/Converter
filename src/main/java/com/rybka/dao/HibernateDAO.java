package com.rybka.dao;

import com.rybka.model.Currency;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class HibernateDAO {

    private Transaction transaction;

    public void save(Currency currency) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(currency);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public void showTableRow() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Currency> currencies = session.createQuery("from Currency", Currency.class).list();
            currencies.forEach(c -> System.out.println(c.toString()));
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }
}
