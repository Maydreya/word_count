package com.example.word_count;

import com.mysql.cj.protocol.Resultset;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

@WebServlet("/")
public class WordCount extends HttpServlet {

    static final String jdbcdriver = "org.mysql.Driver";
    static final String url = "jdbc:mysql://localhost:3306/word_count";
    static final String user = "root";
    static final String password = "Sqteam453515291";
    static final Boolean checking = false;

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        getServletContext().getRequestDispatcher("/create.jsp").forward(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String word = request.getParameter("word");
        String getIdWordRequest = "SELECT keys_id FROM word_count.keys WHERE keys_value = ?";
        String req = "SELECT word_count.files.files_name, keys.keys_value, count FROM word_count.connection" +
                " INNER JOIN word_count.keys ON word_count.connection.keys_id_con = keys.keys_id" +
                " INNER JOIN word_count.files ON word_count.connection.files_id_con = files.files_id" +
                " WHERE keys_id_con = ? ORDER BY word_count.connection.count DESC";
        CheckingFiles checkingFiles = new CheckingFiles();
        try {
            Properties p=new Properties();
            p.setProperty("user", user);
            p.setProperty("password", password);
            p.setProperty("useUnicode", "true");
            p.setProperty("characterEncoding", "cp1251");
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection(url, p);
            if (checking){
                String SQL = "DELETE FROM word_count.connection";
                String SQL2 = "DELETE FROM word_count.keys";
                String SQL3 = "DELETE FROM word_count.files";
                Statement sm = connection.createStatement();
                sm.executeUpdate(SQL);
                sm.executeUpdate(SQL2);
                sm.executeUpdate(SQL3);
                String path = "d:/work/word_count/data/";
                Map<String, Filesmap> maps = checkingFiles.countWords(path);
                File[] listOfFiles = new File(path).listFiles();
                for(File file : listOfFiles){
                    String query = "INSERT INTO word_count.files (files_name) VALUES (?)";
                    PreparedStatement statement = connection.prepareStatement(query);
                    statement.setString(1, file.toString());
                    statement.executeUpdate();
                }
                for(Map.Entry<String, Filesmap> pair : maps.entrySet()){
                    Filesmap value = pair.getValue();
                    String key = pair.getKey();
                    String query = "INSERT INTO word_count.keys (keys_value) VALUES (?)";
                    PreparedStatement statement = connection.prepareStatement(query);
                    statement.setString(1, key);
                    statement.executeUpdate();
                    for(int i = 0; i < value.size(); i++){
                        String file_name = value.keySet().toArray()[i].toString();
                        Integer count = value.get(file_name);
                        String query2 = "INSERT INTO word_count.connection (files_id_con, keys_id_con, count) VALUES ((SELECT files_id FROM word_count.files WHERE files_name = ?), (SELECT keys_id FROM word_count.keys WHERE keys_value = ?), ?)";
                        statement = connection.prepareStatement(query2);
                        statement.setString(1, file_name);
                        statement.setString(2, key);
                        statement.setInt(3, count);
                        statement.executeUpdate();
                    }
                }


            }
            PreparedStatement st = connection.prepareStatement(getIdWordRequest);
            st.setString(1, word);
            ResultSet rs = st.executeQuery();
            if(rs.next()){
                int word_id = rs.getInt(1);
                st = connection.prepareStatement(req);
                st.setInt(1, word_id);
                rs = st.executeQuery();
                List<Words> list = new ArrayList<>();
                while (rs.next()){
                    Words word_c = new Words();
                    word_c.setFiles(rs.getString(1));
                    word_c.setKeys(rs.getString(2));
                    word_c.setCount(rs.getString(3));
                    list.add(word_c);
                }
                request.setAttribute("words", list);
                request.getServletContext().getRequestDispatcher("/word_count.jsp").forward(request, response);
            }
            response.sendRedirect(request.getContextPath()+"/word_count");

        } catch (SQLException | ServletException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
    }

}
