package org.example;
import org.example.com.Key;
import org.example.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import static java.lang.Integer.parseInt;

public class Update extends Thread {

    @Override
    public void run() {

        super.run();
        while (true) {
            try {
                update();
//                download();
                sleep(50000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void update() {

        SessionFactory sessionFactory = null;
        try {
            sessionFactory = new Configuration().addAnnotatedClass(User.class).
                    //addAnnotatedClass(Item.class).
                    //addAnnotatedClass(Year.class).
                            setProperty("hibernate.driver_class", Settings.getProperties("hibernate.driver_class")).
                            setProperty("hibernate.connection.url", Settings.getProperties("hibernate.connection.url")).
                            setProperty("hibernate.connection.username", Settings.getProperties("hibernate.connection.username")).
                            setProperty("hibernate.connection.password", Settings.getProperties("hibernate.connection.password")).
                            setProperty("hibernate.dialect", Settings.getProperties("hibernate.dialect")).
                            setProperty("hibernate.current_session_context_class", Settings.getProperties("hibernate.current_session_context_class")).
                            setProperty("hibernate.show_sql", Settings.getProperties("hibernate.show_sql")).
                            buildSessionFactory();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Session session = sessionFactory.getCurrentSession();

        try {
            session.beginTransaction();
            URL generetedURL = null;
            String response = null;
            List<User> users = session.createQuery("FROM User").getResultList();

            ArrayList<Key> keyArrayList = new ArrayList<>();
            keyArrayList.add(new Key("locale", "ru"));
            keyArrayList.add(new Key("beginTime", URLRequestResponse.getDate(-7)));
            keyArrayList.add(new Key("endTime", URLRequestResponse.getDateCurrent()));
            keyArrayList.add(new Key("sort", "date"));
            keyArrayList.add(new Key("order", "desc"));
//            keyArrayList.add(new Key("category", ""));
//            keyArrayList.add(new Key("serviceName", ""));

            for (User user : users) {
                if (user.getNameShopOzon() != null) {
                    if (user.getTokenClientOzon() != null) {
                        generetedURL = URLRequestResponse.generateURL("wb", "getDocumentsList", user.getTokenClientOzon(), keyArrayList);
                        try {
                            response = URLRequestResponse.getResponseFromURL(generetedURL, user.getTokenStatisticOzon());
                            System.out.println(response);
//                            JSONObject jsonObject = new JSONObject(response);
//                            JSONObject jsonObject1 = (JSONObject) jsonObject.get("data");
//                            byte[] encodedString = Base64.getDecoder().decode(jsonObject1.get("document").toString());
//                            Files.write(Paths.get("D:\\image.zip"), encodedString);
                        } catch (IOException e) {
                            e.printStackTrace();
                            e.getMessage();
                        }
                    }
                }
            }
            session.getTransaction().commit();
        } finally {
            sessionFactory.close();
        }
    }



    private void download() {

        SessionFactory sessionFactory = null;
        try {
            sessionFactory = new Configuration().addAnnotatedClass(User.class).
                    //addAnnotatedClass(Item.class).
                    //addAnnotatedClass(Year.class).
                    setProperty("hibernate.driver_class", Settings.getProperties("hibernate.driver_class")).
                    setProperty("hibernate.connection.url", Settings.getProperties("hibernate.connection.url")).
                    setProperty("hibernate.connection.username", Settings.getProperties("hibernate.connection.username")).
                    setProperty("hibernate.connection.password", Settings.getProperties("hibernate.connection.password")).
                    setProperty("hibernate.dialect", Settings.getProperties("hibernate.dialect")).
                    setProperty("hibernate.current_session_context_class", Settings.getProperties("hibernate.current_session_context_class")).
                    setProperty("hibernate.show_sql", Settings.getProperties("hibernate.show_sql")).
                    buildSessionFactory();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Session session = sessionFactory.getCurrentSession();

        try {
            session.beginTransaction();
            URL generetedURL = null;
            String response = null;
            List<User> users = session.createQuery("FROM User").getResultList();

//            ArrayList<Key> keyArrayList = new ArrayList<>();
//            keyArrayList.add(new Key("locale", "ru"));
//            keyArrayList.add(new Key("beginTime", URLRequestResponse.getDate(-7)));
//            keyArrayList.add(new Key("endTime", URLRequestResponse.getDateCurrent()));
//            keyArrayList.add(new Key("sort", "date"));
//            keyArrayList.add(new Key("order", "desc"));
//            keyArrayList.add(new Key("category", ""));
//            keyArrayList.add(new Key("serviceName", ""));

            ArrayList<Key> keyArrayList = new ArrayList<>();
//            keyArrayList.add(new Key("serviceName", "redeem-notification-upd-198040528"));
            keyArrayList.add(new Key("serviceName", "upd-198040528"));
            keyArrayList.add(new Key("extension", "zip"));

            for (User user : users) {
                if (user.getNameShopOzon() != null) {
                    if (user.getTokenClientOzon() != null) {
                        generetedURL = URLRequestResponse.generateURL("wb", "getDocument", user.getTokenClientOzon(), keyArrayList);
                        try {
                            response = URLRequestResponse.getResponseFromURL(generetedURL, user.getTokenStatisticOzon());
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject jsonObject1 = (JSONObject) jsonObject.get("data");
                            byte[] encodedString = Base64.getDecoder().decode(jsonObject1.get("document").toString());
                            Files.write(Paths.get("D:\\image.zip"), encodedString);
                        } catch (IOException e) {
                            e.printStackTrace();
                            e.getMessage();
                        }
                    }
                }
            }
            session.getTransaction().commit();
        } finally {
            sessionFactory.close();
        }
    }
}
