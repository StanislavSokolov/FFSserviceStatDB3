package org.example;
import org.example.com.Key;
import org.example.model.Documents;
import org.example.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.json.JSONArray;
import org.json.JSONException;
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
                if (!download()) update();
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
                    addAnnotatedClass(Documents.class).
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
                            if (!response.equals("{\"errors\":[\"(api-new) too many requests\"]}")) {
                                JSONObject jsonObject1 = new JSONObject(response);
                                JSONObject jsonObject = jsonObject1.getJSONObject("data");
                                for (int i = 0; i < jsonObject.getJSONArray("documents").length(); i++) {
                                    List<Documents> documents = user.getDocuments();
                                    if (documents.isEmpty()) {
                                        Documents document = new Documents(jsonObject.getJSONArray("documents").getJSONObject(i).get("serviceName").toString(),
                                                jsonObject.getJSONArray("documents").getJSONObject(i).get("name").toString(),
                                                jsonObject.getJSONArray("documents").getJSONObject(i).get("category").toString(),
                                                jsonObject.getJSONArray("documents").getJSONObject(i).get("extensions").toString(),
                                                jsonObject.getJSONArray("documents").getJSONObject(i).get("creationTime").toString(),
                                                jsonObject.getJSONArray("documents").getJSONObject(i).get("viewed").toString(),
                                                "false", user);
                                        session.save(document);
                                    } else {
                                        boolean coincidence = false;
                                        for (Documents d : documents) {
                                            if (d.getServiceName().equals(jsonObject.getJSONArray("documents").getJSONObject(i).get("serviceName").toString())) {
                                                coincidence = true;
                                            }
                                        }
                                        if (!coincidence) {
                                            Documents document = new Documents(jsonObject.getJSONArray("documents").getJSONObject(i).get("serviceName").toString(),
                                                    jsonObject.getJSONArray("documents").getJSONObject(i).get("name").toString(),
                                                    jsonObject.getJSONArray("documents").getJSONObject(i).get("category").toString(),
                                                    jsonObject.getJSONArray("documents").getJSONObject(i).get("extensions").toString(),
                                                    jsonObject.getJSONArray("documents").getJSONObject(i).get("creationTime").toString(),
                                                    jsonObject.getJSONArray("documents").getJSONObject(i).get("viewed").toString(),
                                                    "false", user);
                                            session.save(document);
                                        }
                                    }
                                }
                            }
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



    private boolean download() {

        SessionFactory sessionFactory = null;
        try {
            sessionFactory = new Configuration().addAnnotatedClass(User.class).
                    addAnnotatedClass(Documents.class).
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
            List<User> users = session.createQuery("FROM User").getResultList();
            for (User user : users) {
                if (user.getNameShopOzon() != null) {
                    if (user.getTokenClientOzon() != null) {
                        List<Documents> documents = user.getDocuments();
                        if (documents.isEmpty()) {
                            return false;
                        } else {
                            for (Documents d : documents) {
                                if (d.getDownload().equals("false")) {

                                    URL generetedURL = null;
                                    String response = null;
                                    ArrayList<Key> keyArrayList = new ArrayList<>();
                                    keyArrayList.add(new Key("serviceName", d.getServiceName()));
                                    keyArrayList.add(new Key("extension", "zip"));

                                    generetedURL = URLRequestResponse.generateURL("wb", "getDocument", user.getTokenClientOzon(), keyArrayList);
                                    try {
                                        response = URLRequestResponse.getResponseFromURL(generetedURL, user.getTokenStatisticOzon());
                                        JSONObject jsonObject = new JSONObject(response);
                                        JSONObject jsonObject1 = (JSONObject) jsonObject.get("data");
                                        byte[] encodedString = Base64.getDecoder().decode(jsonObject1.get("document").toString());
                                        Files.write(Paths.get("D:\\" + d.getName() + ".zip"), encodedString);
                                        session.createQuery("update Documents set download = 'true' WHERE serviceName = '" + d.getServiceName() + "'").executeUpdate();
                                        session.getTransaction().commit();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                        e.getMessage();
                                    }
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
            session.getTransaction().commit();
        } finally {
            sessionFactory.close();
        }
        return false;
    }
}
