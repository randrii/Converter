package com.rybka.dao;

import com.rybka.config.QueryConstants;
import com.rybka.exception.DBConnectionException;
import com.rybka.model.CurrencyHistory;
import lombok.extern.log4j.Log4j;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

@Log4j
public class CurrencyHistoryDAO {
    private Transaction transaction;

    public void save(CurrencyHistory currencyHistory) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(currencyHistory);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            log.error("Unable to save object. Reason: " + e.getMessage());
        }
    }

    public List<CurrencyHistory> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(QueryConstants.CURRENCY_HISTORY_QUERY, CurrencyHistory.class).list();
        } catch (Exception e) {
            transaction.rollback();
            log.error("Error while retrieving data from DB. Reason: " + e.getMessage());
            throw new DBConnectionException("Unable to access to DB. Reason: " + e.getMessage());
        }
    }

    public List<CurrencyHistory> findAllLimit(int limit) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            var query = session.createQuery(QueryConstants.CURRENCY_HISTORY_QUERY + QueryConstants.LIMIT_QUERY,
                    CurrencyHistory.class);
            query.setMaxResults(limit);
            return query.list();
        } catch (Exception e) {
            transaction.rollback();
            log.error("Error while retrieving data from DB. Reason: " + e.getMessage());
            throw new DBConnectionException("Unable to access to DB. Reason: " + e.getMessage());
        }
    }
}
