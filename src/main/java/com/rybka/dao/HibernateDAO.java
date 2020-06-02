package com.rybka.dao;

import com.rybka.model.ExchangeHistory;
import lombok.extern.log4j.Log4j;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

@Log4j
public class HibernateDAO {

    private Transaction transaction;

    public void save(ExchangeHistory exchangeHistory) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(exchangeHistory);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            log.error("Unable to save object. Reason: " + e.getMessage());
        }
    }

    public void showTableRow() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<ExchangeHistory> currencies = session.createQuery("from ExchangeHistory", ExchangeHistory.class).list();
            currencies.forEach(c -> log.info(c.toString()));
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            log.error("Error while retrieving data from DB. Reason: " + e.getMessage());
        }
    }
}
